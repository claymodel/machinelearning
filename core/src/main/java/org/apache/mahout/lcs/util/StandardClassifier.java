package org.apache.mahout.lcs.util;

public class StandardClassifier extends Classifier {
	private double strength = 0;

	/**
	 * Constructor
	 * @param r Rule for the Classifier
	 * @param strength Value for strength
	 */
	public StandardClassifier(Rule r, double strength) {
		super(r);
		setStrength(strength);
	};

	/**
	 * Sets the classifier's strength-attribute
	 * @param value Value for strength to be set
	 */
	public void setStrength(double value) {
		strength = value;
	}

	/**
	 * Returns the classifier's strength-attribute
	 * @return Strength value as double
	 */
	public double getStrength() {
		return strength;
	}

	/**
	 * Crossover-Method, extending the inherited crossover-method, setting the
	 * strenght value of the resulting classifier to the average of the previous
	 * ones.
	 * @param c StandardClassifier to cross with
	 * @param Position Position for crossover
	 * @return new StandardClassifier as result of crossover
	 * @see classifierLibrary.Classifier#cross(classifierLibrary.Classifier,int)
	 */
	public StandardClassifier cross(StandardClassifier c, int Position) {
		StandardClassifier n = (StandardClassifier) super.cross(c, Position);
		n.setStrength((this.getStrength() + c.getStrength()) / 2);
		return n;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Classifier#callFunction(java.lang.String)
	 */
	public double callFunction(String which) {
		// just a shortcut for better performance, avoiding dynamic function
		// call:
		if (which.equals("getStrength"))
			return getStrength();
		else
			return super.callFunction(which);
	}
}