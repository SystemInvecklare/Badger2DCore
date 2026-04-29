package com.github.systeminvecklare.badger.core.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Fast and GWT-compatible pattern matcher.
 * </p>
 * 
 * <p>
 * Not thread safe. Tip: Use {@link #copy()} and {@code ThreadLocal<IPatternMatcher>} for thread safe usage.
 * </p>
 * @author Mattias Selin
 */
public class PatternMatcher implements IPatternMatcher {
	private static final PatternMatcher META_PATTERN = PatternMatcher.builder().beginPattern(null).text("${").group(null).text("}").allowReset().endPattern().build();
	
	public static class PatternMatcherBuilder {
		private PatternMatcher metaPattern = null;
		
		public class PatternBuilder {
			private final String name;
			private boolean expectingText = true;
			private final List<String> textParts = new ArrayList<String>();
			private final List<String> captureNames = new ArrayList<String>();
			private boolean allowReset = false;

			private PatternBuilder(String name) {
				this.name = name;
			}
			
			public PatternBuilder allowReset() {
				this.allowReset = true;
				return this;
			}
			
			public PatternBuilder text(String text) throws PatternCompilationException {
				if(!expectingText) {
					throw new PatternCompilationException("Expected group(String name) to be called! Calls must alternate text(...) -> group(...) -> text(...)");
				}
				if(text == null) {
					throw new NullPointerException("text is null");
				}
				if("".equals(text)) {
					throw new PatternCompilationException("Text may not be empty");
				}
				textParts.add(text);
				expectingText = false;
				return this;
			}
			
			/**
			 * A group can be named or anonymous. Named groups must have unique names.
			 * 
			 * @param name May be <code>null</code>
			 * @return
			 * @throws PatternCompilationException
			 */
			public PatternBuilder group(String name) throws PatternCompilationException {
				if(expectingText) {
					if(textParts.isEmpty()) {
						throw new PatternCompilationException("First call must be text(String text)");
					} else {
						throw new PatternCompilationException("Expected text(String text) to be called! Calls must alternate text(...) -> group(...) -> text(...)");
					}
				}
				if(name != null && !"".equals(name) && captureNames.contains(name)) {
					throw new PatternCompilationException("Group with name \""+name+"\" already added");
				}
				captureNames.add(name);
				expectingText = true;
				return this;
			}

			public PatternMatcherBuilder endPattern() throws PatternCompilationException {
				if(textParts.isEmpty()) {
					throw new PatternCompilationException("Patterns must have at least 1 text part");
				}
				if(textParts.size() != captureNames.size() + 1) {
					throw new PatternCompilationException("Last call must be text(String text)");
				}
				
				Capture[] captureParts = new Capture[captureNames.size()];
				for(int i = 0; i < captureParts.length; ++i) {
					captureParts[i] = new Capture(captureNames.get(i));
				}
				matchers.add(new Matcher(name, allowReset, textParts.toArray(new String[textParts.size()]), captureParts));
				return PatternMatcherBuilder.this;
			}

			private IPatternMatchHandler asHandler() {
				return new IPatternMatchHandler() {
					@Override
					public void onText(String text) {
						PatternBuilder.this.text(text);
					}
					
					@Override
					public void onPattern(IPatternMatch patternMatch) {
						PatternBuilder.this.group(patternMatch.get(0));
					}
				};
			}
		}
		
		private boolean outputEmptyStrings = false;
		private final List<Matcher> matchers = new ArrayList<Matcher>();
		
		private PatternMatcherBuilder() {}
		
		public PatternMatcherBuilder outputEmptyStrings() {
			this.outputEmptyStrings = true;
			return this;
		}
		
		public PatternBuilder beginPattern(String name) {
			return new PatternBuilder(name);
		}
		
		/**
		 * Adds a pattern by specifying the pattern in the following format:
		 * <ul>
		 * <li>Consists of interleaving text parts and capture groups.</li>
		 * <li>Text parts are just written as text, and are always non-empty. (i.e. <code>""</code> is not considered a text part.)</li>
		 * <li>Named capture groups are written as <code>${groupName}</code>. Names must be unique.</li>
		 * <li>Anonymous capture groups are written as <code>${}</code>.</li>
		 * <li>Capture groups must be separated by text parts.</li>
		 * <li>Capture groups are parsed as &quot;inner first&quot;. For example, <code>${${var}}</code> is a valid pattern consisting of text part <code>"${"</code>, capture group named <code>"var"</code> and text part <code>"}"</code>.</li>
		 * <li>Pattern must start with text part.</li>
		 * <li>Pattern must end with text part.</li>
		 * </ul>
		 *
		 * Example:
		 * <p>
		 * {@code addPattern("myPattern", "[${first},${},${last}]")}
		 * </p>
		 * <p>And then later,</p>
		 * <blockquote><pre>
		 * patternMatcher.match("Let's check [if,this,works]!", new IPatternMatchHandler() &#123;
		 *     &#64;Override
		 *     public void onText(String text) &#123;
		 *         System.out.println("Got text: "+text+"?");
		 *     &#125;
		 *
		 *     &#64;Override
		 *     public void onPattern(IPatternMatch patternMatch) &#123;
		 *         System.out.println("Got match on pattern "+patternMatch.getName()+": "+patternMatch.get("first")+"->"+patternMatch.get(1)+"->"+patternMatch.get("last"));
		 *     &#125;
		 * &#125;);
		 * </pre></blockquote>
		 *
		 * Prints:
		 * <blockquote><pre>
		 * Got text: Let's check ?
		 * Got match on pattern myPattern: if->this->works
		 * Got text: !?
		 * </pre></blockquote>
		 *
		 * @param name The name of the pattern. Can be {@code null}.
		 * @param pattern
		 * @return
		 * @throws PatternCompilationException
		 */
		public PatternMatcherBuilder addPattern(String name, String pattern) throws PatternCompilationException {
			return addPattern(name, pattern, false);
		}
		
		/**
		 * <p>Exactly like {@link #addPattern(String, String)}, but with an option to allow pattern matchers to reset.</p>
		 * <p>
		 * Allowing pattern matcher to reset restart the matching logic if the first character of the text part appears while parsing. 
		 * </p>
		 * 
		 * @see #addPattern(String, String)
		 * 
		 * @param name
		 * @param pattern
		 * @param allowReset
		 * @return
		 * @throws PatternCompilationException
		 */
		public PatternMatcherBuilder addPattern(String name, String pattern, boolean allowReset) throws PatternCompilationException {
			if(metaPattern == null) {
				metaPattern = META_PATTERN.copy();
			}
			try {
				PatternBuilder patternBuilder = new PatternBuilder(name);
				metaPattern.match(pattern, patternBuilder.asHandler());
				if(allowReset) {
					patternBuilder.allowReset();
				}
				patternBuilder.endPattern();
			} catch(PatternCompilationException e) {
				throw new PatternCompilationException("Invalid pattern: "+pattern, e);
			}
			return this;
		}
		
		public PatternMatcher build() {
			return new PatternMatcher(outputEmptyStrings, matchers.toArray(new Matcher[matchers.size()]));
		}
	}
	
	public static PatternMatcherBuilder builder() {
		return new PatternMatcherBuilder();
	}
	
	public static PatternMatcher simple(String pattern) throws PatternCompilationException {
		return builder().addPattern(null, pattern).build();
	}
	
	private final boolean outputEmptyStrings;
	private final Matcher[] matchers;
	private final PatternMatch patternMatch = new PatternMatch();
	
	public PatternMatcher(boolean outputEmptyStrings, Matcher[] matchers) {
		this.outputEmptyStrings = outputEmptyStrings;
		this.matchers = matchers;
	}
	
	public PatternMatcher copy() {
		Matcher[] copiedMatchers = new Matcher[matchers.length];
		for(int i = 0; i < matchers.length; ++i) {
			copiedMatchers[i] = matchers[i].copy();
		}
		return new PatternMatcher(outputEmptyStrings, copiedMatchers);
	}

	@Override
	public boolean match(String string, IPatternMatchHandler handler) throws PatternMatchingException {
		if(string == null) {
			throw new NullPointerException("string was null");
		}
		try {
			patternMatch.inputString = string;
			
			final int length = string.length();
			int lastEnd = 0;
			int fullyMatchingMatcher;
			for(int i = 0; i < length; ++i) {
				char c = string.charAt(i);
				
				fullyMatchingMatcher = -1;
				for(int m = 0; m < matchers.length; ++m) {
					if(matchers[m].feed(c)) {
						if(fullyMatchingMatcher != -1) {
							int matchAStart = i + 1 - matchers[fullyMatchingMatcher].matchLength;
							int matchBStart = i + 1 - matchers[m].matchLength;
							throw new PatternMatchingException("Multiple patterns matched at the same time: "+matchers[fullyMatchingMatcher].name+" (index "+fullyMatchingMatcher+") on \""+string.substring(matchAStart, i+1)+"\" and "+matchers[m].name+" (index "+m+") on \""+string.substring(matchBStart, i+1)+"\"");
						}
						fullyMatchingMatcher = m;
					}
				}
				if(fullyMatchingMatcher != -1) {
					Matcher matcher = matchers[fullyMatchingMatcher];
					int matchStart = i + 1 - matcher.matchLength;
					if(outputEmptyStrings || matchStart - lastEnd > 0) {
						handler.onText(string.substring(lastEnd, matchStart));
					}
					handler.onPattern(patternMatch.reset(matcher, matchStart));
					
					lastEnd = i + 1;
					
					// Reset matchers
					for(Matcher m : matchers) {
						m.matchingInARow = 0;
						m.matchLength = 0;
						m.partIndex = 0;
					}
				}
			}
			
			if(lastEnd <= length) {
				if(outputEmptyStrings || length - lastEnd > 0) {
					handler.onText(string.substring(lastEnd));
				}
			}
			
			return lastEnd > 0;
		} finally {
			// Clear inputString so we don't hold on to big memory
			patternMatch.inputString = null;
			
			// Reset matchers
			for(Matcher matcher : matchers) {
				matcher.matchingInARow = 0;
				matcher.matchLength = 0;
				matcher.partIndex = 0;
			}
		}
	}
	
	private static class PatternMatch implements IPatternMatch {
		private String inputString;
		private Matcher matcher;
		private int matchStart;
		
		public PatternMatch reset(Matcher matcher, int matchStart) {
			this.matcher = matcher;
			this.matchStart = matchStart;
			return this;
		}
		
		@Override
		public String getName() {
			return matcher.name;
		}
		
		@Override
		public String get(String groupName) {
			if(groupName == null || "".equals(groupName)) {
				throw new IllegalArgumentException("Only groups with non-empty non-null names may be retrieved with get(String groupName)");
			}
			for(Capture capture : matcher.captureParts) {
				if(groupName.equals(capture.name)) {
					return inputString.substring(matchStart + capture.captureStart, matchStart + capture.captureEnd);
				}
			}
			throw new PatternMatchingException("Pattern named "+matcher.name+" does not have a group by the name "+groupName+".");
		}

		@Override
		public String get(int groupIndex) {
			Capture capture = matcher.captureParts[groupIndex];
			return inputString.substring(matchStart + capture.captureStart, matchStart + capture.captureEnd);
		}
	}
	
	private static class Capture {
		private final String name;
		// Relative to matchStart.
		public int captureStart;
		public int captureEnd;
		
		public Capture(String name) {
			this.name = name;
		}
		
		public Capture copy() {
			return new Capture(name);
		}
	}
	
	/**
	 *  Patterns can't be "${part}${nextPart}" -> can't know when to stop!
	 *  So we can assume matcherParts are interleaved. text->name->text
	 *  We can also assume we start with a text-part and end with a text-part.
	 */
	private static class Matcher {
		private final String name;
		private final boolean allowReset;
		
		private final String[] textParts;
		private final Capture[] captureParts;
		private int matchingInARow = 0;
		private int partIndex = 0;
		private int matchLength = 0;
		private boolean resetMatchLengthNextFeed = false;

		
		public Matcher(String name, boolean allowReset, String[] textParts, Capture[] captureParts) {
			this.name = name;
			this.allowReset = allowReset;
			if(textParts.length < 1) {
				throw new IllegalArgumentException("Expected at least 1 textPart");
			}
			if(textParts.length != captureParts.length + 1) {
				throw new IllegalArgumentException("textParts.length == captureParts.length + 1");
			}
			this.textParts = textParts;
			this.captureParts = captureParts;
		}

		public Matcher copy() {
			Capture[] copiedCaptureParts = new Capture[captureParts.length];
			for(int i = 0; i < captureParts.length; ++i) {
				copiedCaptureParts[i] = captureParts[i].copy();
			}
			return new Matcher(name, allowReset, textParts, copiedCaptureParts);
		}

		public boolean feed(char c) {
			if(matchLength > 0 && allowReset) {
				if(textParts[0].charAt(0) == c) {
					partIndex = 0;
					matchLength = 0;
					matchingInARow = 0;
				}
			}
			if(resetMatchLengthNextFeed) {
				resetMatchLengthNextFeed = false;
				matchLength = 0;
			}
			if(partIndex%2 == 0) { // Text part (part index even)
				if(textParts[partIndex/2].charAt(matchingInARow) == c) {
					matchingInARow++;
					matchLength++;
					if(matchingInARow >= textParts[partIndex/2].length()) {
						matchingInARow = 0;
						partIndex++;
						if((partIndex-1)/2 >= captureParts.length) { // Check if partIndex outside bound
							// Reset and return
							partIndex = 0;
							// matchingInARow = 0 // Already 0 at this point
							resetMatchLengthNextFeed = true;
							return true;
						} else {
							// Reset upcoming capturePart
							captureParts[(partIndex-1)/2].captureStart = matchLength;
							captureParts[(partIndex-1)/2].captureEnd = matchLength;
						}
					}
				} else if(partIndex == 0) {
					matchingInARow = 0;
					partIndex = 0;
				}
			} else { // Capture part (part index odd)
				matchLength++;
				if(textParts[(partIndex+1)/2].charAt(matchingInARow) == c) {
					matchingInARow++;
					if(matchingInARow >= textParts[(partIndex + 1)/2].length()) {
						matchingInARow = 0;
						if((partIndex+1)/2 >= captureParts.length) { // Check if next partIndex outside bound
							// Reset and return
							partIndex = 0;
							// matchingInARow = 0 // Already 0 at this point
							resetMatchLengthNextFeed = true;
							return true;
						} else {
							partIndex += 2;
							// Reset upcoming capturePart
							captureParts[(partIndex-1)/2].captureStart = matchLength;
							captureParts[(partIndex-1)/2].captureEnd = matchLength;
						}
					}
				} else {
					captureParts[(partIndex-1)/2].captureEnd += (matchingInARow + 1);
					matchingInARow = 0;
				}
			}
			return false;
		}
	}
}
