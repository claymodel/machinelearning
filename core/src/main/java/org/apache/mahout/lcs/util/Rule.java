package org.apache.mahout.lcs.util;


public class Rule implements Cloneable {
	private Situation condition;
	private Situation action;

	/**
	 * Constructor with Condition and Action-Part of the rule as parameters.
	 * @param Condition Situation to be set as condition
	 * @param Action Situation to be set as action
	 */
	public Rule(Situation Condition, Situation Action) {
		this();
		condition = Condition;
		action = Action;
	}

	/**
	 * Constructor, creating an empty Rule.
	 */
	public Rule() {
		condition = null;
		action = null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		Rule r = null;
		{
			r = (Rule) super.clone();
			r.setCondition((Situation) condition.clone());
			r.setAction((Situation) action.clone());
			return r;
		}
	}

	/**
	 * Returns a pointer to condition part of the rule.
	 * @return Situation used as condition
	 */
	public Situation getCondition() {
		return condition;
	}

	/**
	 * Returns a pointer to the action part of the rule.
	 * @return Situation used as action
	 */
	public Situation getAction() {
		return action;
	}

	/**
	 * Sets the condition part of the rule to the given situation.
	 * @param Condition Situation to be set for condition.
	 */
	public void setCondition(Situation Condition) {
		condition = Condition;
	}

	/**
	 * Sets the action part of the rule to the given situation.
	 * @param Action Situation to be set for action.
	 */
	public void setAction(Situation Action) {
		action = Action;
	}

	/**
	 * Checks compatibility (in sense of comparability) of the rule with a rule
	 * given for comparison.
	 * @param Rule rule to compare with.
	 * @return true, if compareable, else false
	 */
	public boolean compatibility(Rule Rule) {
		return (getCondition().compatibility(Rule.getCondition()))
				&& (getAction().compatibility(Rule.getAction()));
	}

	/**
	 * Performs a crossover of the rule at given position with Kreuzt die Regel
	 * an der another rule given, returning the result as a <b>new</b> rule.
	 * @param toCross Rule to cross with
	 * @param Position Position at which to cross
	 * @return new rule after crossover
	 */
	public Rule cross(Rule toCross, int Position) {
		/*
		 * if (compatibility(toCross) == false) { throw new
		 * SimLibraryError("Rules incompatible"); }
		 */
		Rule n = new Rule(getCondition(), getAction());
		if (Position < getCondition().getLength()) {
			n.setCondition(getCondition().cross(toCross.getCondition(),
					Position));
			Position = getAction().getLength();
		}
		n.setAction(getAction().cross(toCross.getAction(),
				Position - getAction().getLength()));
		return n;
	}

	/**
	 * Method sets rule parts to random situations.
	 */
	public void randomizeRule() {
		getAction().randomizeSituation();
		getCondition().randomizeSituation();
	}

	/**
	 * Method for mutation with a given probability. (Implementation of effect
	 * of probability depends on implemenation of Situation.)
	 * @see classifierLibrary.Situation.mutation
	 * @param Probability Probability for mutation
	 * @return rule after mutation
	 */
	public Rule mutation(double Probability) {
		Rule r = new Rule(getCondition(), getAction());
		r.setCondition(r.getCondition().mutation(Probability));
		r.setAction(r.getAction().mutation(Probability));
		return r;
	}

	/**
	 * Method returns relative fit of the condition part of the rule to a
	 * situation to compare with, taking wildcards / subordinate relations of
	 * symbols into account.
	 * @param sit Situation for comparison.
	 * @return relative fit
	 */
	public double conditionMatchSubsumptive(Situation sit) {
		return (double) getCondition().compareSubsumtive(sit) / sit.size();
	}

	/**
	 * Method returns relative fit of the condition part of the rule to a
	 * situation to compare with, <b>not</b> taking wildcards / subordinate
	 * relations of symbols into account.
	 * @param sit Situation, mit der verglichen werden soll.
	 * @return relative �bereinstimmung
	 */
	public double conditionMatch(Situation sit) {
		return (double) getCondition().compare(sit) / sit.size();
	}

	/**
	 * Method returns number of specific elements (i.e. non wildcards) in
	 * relation to condition size.
	 * @return relativer Anteil spezifischer Elemente (d.h. nicht Wildcards)
	 */
	public double getSpecifity() {
		return (double) getCondition().specificMembers()
				/ getCondition().size();
	}

	/**
	 * Method for output of debugging information.
	 */
	public void debug_out() {
		DebugOutput.print("Rule Condition ");
		getCondition().debug_out();
		DebugOutput.print(" Rule Action ");
		getAction().debug_out();
		DebugOutput.print(";");
	}

	/**
	 * Method returns the lenght (for sets this should be the size of the
	 * defined basic set) of the situations in condition and action part of the
	 * rule.
	 * @return total length of the rule
	 */
	public int getLength() {
		return getCondition().getLength() + getAction().getLength();
	}

	/**
	 * Method returns the size (for sets this should be their actual number of
	 * elements) of the situations in condition and action part of the rule.
	 * @return total size of the rule
	 */
	public double size() {
		return getCondition().size() + getAction().size();
	}
}