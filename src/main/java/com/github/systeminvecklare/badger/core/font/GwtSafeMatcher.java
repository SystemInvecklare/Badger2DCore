package com.github.systeminvecklare.badger.core.font;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GwtSafeMatcher {
    private final Pattern pattern;
    private final String text;

    private String remaining;
    private int offset;

    private String matchedGroup0;
    private List<String> captureGroups;
    private int matchStart;
    private int matchEnd;
    private boolean hasMatch;

    private GwtSafeMatcher(Pattern pattern, String text) {
        this.pattern = pattern;
        this.text = text;
        this.remaining = text;
        this.offset = 0;
        this.hasMatch = false;
        this.captureGroups = new ArrayList<String>();
    }

    public static GwtSafeMatcher matcher(Pattern pattern, String text) {
        return new GwtSafeMatcher(pattern, text);
    }

    public boolean find() {
        hasMatch = false;
        if (remaining == null) return false;

        Matcher m = pattern.matcher(remaining);
        if (!m.find()) {
            remaining = null;
            return false;
        }

        String matched = m.group(0);
        // Split on the matched string to get the text before it
        String[] parts = remaining.split(quoteRegex(matched), 2);
        int localStart = parts[0].length();
        int localEnd = localStart + matched.length();

        storeMatch(matched, m, offset + localStart, offset + localEnd);

        offset += localEnd;
        remaining = localEnd >= remaining.length() ? null : remaining.substring(localEnd);
        return true;
    }

    public boolean matches() {
        hasMatch = false;
        Matcher m = pattern.matcher(text);
        if (!m.matches()) {
            return false;
        }
        storeMatch(m.group(0), m, 0, text.length());
        return true;
    }

    public String group() {
        checkMatch();
        return matchedGroup0;
    }

    public String group(int group) {
        checkMatch();
        if (group == 0) {
            return matchedGroup0;
        }
        int idx = group - 1;
        if (idx < 0 || idx >= captureGroups.size()) {
            throw new IndexOutOfBoundsException(
                    "No group " + group + " (found " + captureGroups.size() + " capture group(s))");
        }
        return captureGroups.get(idx);
    }

    public int start() {
        checkMatch();
        return matchStart;
    }

    public int end() {
        checkMatch();
        return matchEnd;
    }
    
    private static String quoteRegex(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\': case '.': case '+': case '*': case '?':
                case '^': case '$': case '{': case '}': case '[':
                case ']': case '(': case ')': case '|':
                    sb.append('\\');
                    break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private void storeMatch(String matched, Matcher m, int absStart, int absEnd) {
        matchedGroup0 = matched;
        matchStart    = absStart;
        matchEnd      = absEnd;

        captureGroups = new ArrayList<String>();
        for (int i = 1; ; i++) {
            try {
                captureGroups.add(m.group(i));
            } catch (Exception e) {
                break;
            }
        }

        hasMatch = true;
    }

    private void checkMatch() {
        if (!hasMatch) {
            throw new IllegalStateException("No match available - call find() or matches() first");
        }
    }
}