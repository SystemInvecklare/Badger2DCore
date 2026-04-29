package com.github.systeminvecklare.badger.core.pattern;

public interface IPatternMatch {
	/**
	 * Returns the name of the pattern that matched
	 */
	String getName();
	
	/**
	 * Returns a capture group by index.
	 * 
	 * @param groupIndex
	 * @return The captured group
	 */
	String get(int groupIndex);
	
	/**
	 * Returns a named capture group.
	 * 
	 * @param groupName The name of the group (must not be <code>null</code> nor empty String)
	 * @return The captured group
	 */
	String get(String groupName);
}
