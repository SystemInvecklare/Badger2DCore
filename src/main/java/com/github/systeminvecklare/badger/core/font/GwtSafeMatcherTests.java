package com.github.systeminvecklare.badger.core.font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GwtSafeMatcherTests {
	
	public static void main(String[] args) {
		runTests();
	}

    public static void runTests() {
        testSimpleFind();
        testMultipleFinds();
        testStartEnd();
        testMatches();
        testGroupZeroOnly();
        testHarder();
        System.out.println("All GwtSafeMatcher tests passed.");
    }

    private static void testHarder() {
        List<String> words = new ArrayList<String>();

        String input =
            "This is my ${code_injected} string that ${replaces} many ${cool} things. ${cool}, huh? Very ${code_injected}";

        Pattern pat = Pattern.compile("\\$\\{([^}]+)\\}");

        IMatcher matcher = createMatcher(pat, input);

        int i = 0;

        while (matcher.find()) {
            String g0 = matcher.group(0);
            String g1 = matcher.group(1);
            int start = matcher.start();
            int end = matcher.end();

            System.out.println("MATCH " + (i++));
            System.out.println("  full   = " + g0);
            System.out.println("  group1 = " + g1);
            System.out.println("  range  = [" + start + "," + end + "]");
            System.out.println();

            words.add(g1);
        }

        System.out.println("FINAL WORDS: " + words);

        List<String> expected = Arrays.asList(
            "code_injected",
            "replaces",
            "cool",
            "cool",
            "code_injected"
        );

        if (!words.equals(expected)) {
            throw new AssertionError("Fail!");
        }
    }

	// -------------------------
    // TEST 1: basic find
    // -------------------------
    private static void testSimpleFind() {
    	IMatcher m = createMatcher(Pattern.compile("a"), "abc");

        if (!m.find()) {
            throw new AssertionError("Expected first match to succeed");
        }

        if (!"a".equals(m.group())) {
            throw new AssertionError("Expected group() == 'a'");
        }

        if (m.start() != 0 || m.end() != 1) {
            throw new AssertionError("Incorrect start/end for first match");
        }
    }

    // -------------------------
    // TEST 2: multiple finds
    // -------------------------
    private static void testMultipleFinds() {
    	IMatcher m = createMatcher(Pattern.compile("a"), "ababa");

        int count = 0;

        while (m.find()) {
            count++;
        }

        if (count != 3) {
            throw new AssertionError("Expected 3 matches, got " + count);
        }
    }

    // -------------------------
    // TEST 3: start/end tracking
    // -------------------------
    private static void testStartEnd() {
    	IMatcher m = createMatcher(Pattern.compile("b"), "ababa");

        m.find(); // first 'b'

        if (m.start() != 1) {
            throw new AssertionError("Expected start 1, got " + m.start());
        }

        if (m.end() != 2) {
            throw new AssertionError("Expected end 2, got " + m.end());
        }
    }

    // -------------------------
    // TEST 4: matches()
    // -------------------------
    private static void testMatches() {
        IMatcher m = createMatcher(Pattern.compile("abc"), "abc");

        if (!m.matches()) {
            throw new AssertionError("Expected full match");
        }

        if (!"abc".equals(m.group())) {
            throw new AssertionError("Expected group() == 'abc'");
        }

        if (m.start() != 0 || m.end() != 3) {
            throw new AssertionError("Incorrect full match bounds");
        }
    }

    // -------------------------
    // TEST 5: group() behavior
    // -------------------------
    private static void testGroupZeroOnly() {
        IMatcher m = createMatcher(Pattern.compile("(a)(b)"), "ab");

        
        m.find();

        if (!"ab".equals(m.group())) {
            throw new AssertionError("Expected group() == 'ab'");
        }

        if (!"ab".equals(m.group(0))) {
            throw new AssertionError("Expected group(0) == 'ab'");
        }
        
        if (!"a".equals(m.group(1))) {
            throw new AssertionError("Expected group(1) == 'a' ");
        }
        
        if (!"b".equals(m.group(2))) {
            throw new AssertionError("Expected group(2) == 'b' ");
        }
    }
    
    public static IMatcher createMatcher(Pattern pattern, String text) {
//    	final Matcher matcher = pattern.matcher(text);
//		return new IMatcher() {
//		
//			@Override
//			public boolean find() {
//				return matcher.find();
//			}
//		
//			@Override
//			public String group(int i) {
//				return matcher.group(i);
//			}
//		
//			@Override
//			public String group() {
//				return matcher.group();
//			}
//		
//			@Override
//			public int end() {
//				return matcher.end();
//			}
//		
//			@Override
//			public int start() {
//				return matcher.start();
//			}
//		
//			@Override
//			public boolean matches() {
//				return matcher.matches();
//			}
//		};
    	
    	final GwtSafeMatcher matcher = GwtSafeMatcher.matcher(pattern, text);
		return new IMatcher() {
		
			@Override
			public boolean find() {
				return matcher.find();
			}
		
			@Override
			public String group(int i) {
				return matcher.group(i);
			}
		
			@Override
			public String group() {
				return matcher.group();
			}
		
			@Override
			public int end() {
				return matcher.end();
			}
		
			@Override
			public int start() {
				return matcher.start();
			}
		
			@Override
			public boolean matches() {
				return matcher.matches();
			}
		};
    }

    

	private interface IMatcher {
		boolean find();
		int end();
		int start();
		boolean matches();
		String group(int i);
		String group();
    }
}