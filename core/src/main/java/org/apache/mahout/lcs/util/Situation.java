package org.apache.mahout.lcs.util;

public interface Situation extends Cloneable {
	/**
	 * Retuns a copy(clone) of the situation as a new object.
	 * @return Copy of the Situation
	 */
	public Object clone();

	/**
	 * Returns the size of the situation, can be dynamic if situation is a set.
	 * @return Size of the situation as int.
	 */
	public int size();

	/**
	 * Returns the size of the situation, for sets this should be the maximum
	 * size / size of the defined basic set.
	 * @return Length of the situation as int.
	 */
	public int getLength();

	/**
	 * getLanguageReturns the Language defined for the Situation.
	 * @return Language of the Situation
	 */
	public Language getLanguage();

	/**
	 * Sets the Language to be used for the Situation.
	 * @param lang Language to be set
	 */
	public void setLanguage(Language lang);

	/**
	 * Method checks compatitbility with another situation, e.g. if Languages
	 * match.
	 * @param sit Situation to compare with
	 * @return true, if compatible, else false
	 */
	public boolean compatibility(Situation sit);

	/**
	 * Method for crossover at given position, returns new Situation after
	 * crossover.
	 * @param sit Situation to cross with
	 * @param Position Position for crossover
	 * @return new Situation after crossover
	 */
	public Situation cross(Situation sit, int Position);

	/**
	 * Method for mutation with given probability.
	 * @param probability Probability for mutation
	 * @return new Situation after Mutation
	 */
	public Situation mutation(double probability);

	/**
	 * Changes the situation to a random situation based on the given language
	 */
	public void randomizeSituation();

	/**
	 * Changes the situation to a random situation based on the given language,
	 * using a given probability for wildcard symbols.
	 * @param wildprob Probability for wildcards.
	 */
	public void randomizeSituation(double wildprob);

	/**
	 * Output for Debugging.
	 */
	public void debug_out();

	/**
	 * Compares the Situation with another one. Returns the number of exact
	 * matches, without using wildcards.
	 * @param sit Situation to compare with
	 * @return Absolute number of matches
	 */
	public int compare(Situation sit);

	/**
	 * Compares the Situation with another one. Returns the number of matches
	 * using wildcards, i.e. counting a symbol that is subsumed by another
	 * symbol as a match.
	 * @param sit Situation to compare with
	 * @return Absolute number of matches
	 */
	public int compareSubsumtive(Situation sit);

	/**
	 * Criterion for a situation to be matched by another situation, should be
	 * used for specifying different types of Situations in subclasses.
	 * @param sit Situation to compare with
	 * @return true, if Situation is mached, otherwise false.
	 */
	public boolean isMatched(Situation sit);

	/**
	 * Criterion for a situation to be matched by another situation, should be
	 * used for specifying different types of Situations in subclasses.
	 * @param sit Situation to compare with
	 * @return true, if Situation is mached, otherwise false.
	 */
	public boolean isMatchedSubsumtive(Situation sit);

	/**
	 * Returns the number of elements in the Situation that are not wildcards.
	 * @return Number of non-wildcards as int
	 */
	public int specificMembers();
}