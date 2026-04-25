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
        while (remaining != null) {
            Matcher m = pattern.matcher(remaining);
            if (!m.find()) {
                remaining = null;
                return false;
            }

            String matched = m.group(0);

            int localStart = remaining.indexOf(matched);
            if (localStart < 0) {
                offset += 1;
                remaining = remaining.substring(1);
                continue;
            }
            int localEnd = localStart + matched.length();

            storeMatch(matched, m, offset + localStart, offset + localEnd);

            int advance = localEnd > localStart ? localEnd : localStart + 1;
            offset += advance;
            remaining = advance >= remaining.length() ? null : remaining.substring(advance);
            return true;
        }
        return false;
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

    private void storeMatch(String matched, Matcher m, int absStart, int absEnd) {
        matchedGroup0 = matched;
        matchStart    = absStart;
        matchEnd      = absEnd;

        captureGroups = new ArrayList<String>();
        for (int i = 1; ; i++) {
            try {
                captureGroups.add(m.group(i));
            } catch (IndexOutOfBoundsException e) {
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