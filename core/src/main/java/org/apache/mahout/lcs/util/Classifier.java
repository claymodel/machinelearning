package org.apache.mahout.lcs.util;

import java.lang.reflect.*;
import java.util.*;

public class Classifier implements Cloneable {
	private Rule rul;
	private static long count = 0;
	private long id;
	// private HashMap methCalled = new HashMap();
	/**
	 * Variable <code>rand</code> Random-number generator, quite often used,
	 * therefore declared as static variable.
	 */
	static Random rand = null;

	/**
	 * Constructor
	 * @param r rule, becoming part of the classifier.
	 */
	public Classifier(Rule r) {
		id = count++;
		rul = r;
		if (rand == null) {
			rand = new Random();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Classifier c = null;
		try {
			c = (Classifier) super.clone();
			c.id = count++;
			c.rul = (Rule) rul.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return c;
	}

	/**
	 * Returns the rule part of the classifier.
	 * @return the classifier's rule
	 */
	public Rule getRule() {
		return rul;
	}

	/**
	 * Sets rule-part of the classifier to the given rule.
	 * @param r rule to be set for the classifier
	 */
	public void setRule(Rule r) {
		rul = r;
	}

	/**
	 * Computes relative degree of a classifiers condition matching a given
	 * situation. <b>Uses wildcards.</b>
	 * @param sit situation for comparison
	 * @return relative degree of matching
	 */
	public double conditionMatchSubsumptive(Situation sit) {
		return getRule().conditionMatchSubsumptive(sit);
	}

	
	/**
	 * Checks compatibility of instance to another instance.
	 * @param cl Classifier against which compatibility is to be checked.
	 * @return boolean
	 */
	public boolean compatibility(Classifier cl) {
		return (this.getClass() == cl.getClass())
				&& (this.getRule().compatibility(cl.getRule()));
	}

	/**
	 * Method for debugging-purposes.
	 */
	public void debug_out() {
		DebugOutput.print("(" + getID() + ") ");
		getRule().debug_out();
		DebugOutput.println("");
	}

	/**
	 * Returns Classifier that is a result of crossing with another given
	 * classifier, crossing at defined position. Should be overridden in derived
	 * classes to include crossing of additional parameters, should then be
	 * called using <b>super</b>.
	 * @param cl Classifier to cross with
	 * @param position position for crossover
	 * @return Classifier after crossover
	 */
	public Classifier cross(Classifier cl, int position) {
		Classifier c = null;
		c = (Classifier) this.clone();
		c.setRule(rul.cross(cl.getRule(), position));
		return c;
	}

	/**
	 * <p>Returns Classifier as a result of crossing with another classifier,
	 * crossing at random position.</p>
	 * @param cl Classifier tp cross with
	 * @return Classifier after crossover
	 */
	public Classifier cross(Classifier cl) {
		return cross(cl, rand.nextInt(cl.getRule().getLength()));
	}

	/**
	 * <p>Returns mutation of the current classifier. Mutation of elements with
	 * given probability.</p>
	 * @param probability probability of mutation
	 * @return classifier after mutation
	 */
	public Classifier mutation(double probability) {
		Classifier c = null;
		c = (Classifier) clone();
		c.setRule(c.getRule().mutation(probability));
		return c;
	}

	/**
	 * <p>Returns running number of classifier-object for debugging purposes.</p>
	 * @return ID of classifier object.
	 */
	public long getID() {
		return id;
	};

	/**
	 * <p>Returns size of the classifiers rule, if situations are defined as
	 * sets should be size of the sets.</p>
	 * @return sit as double
	 */
	public double size() {
		return getRule().size();
	}

	/**
	 * <p>Calls a functional method of the classifier specified by its name (no
	 * further parameters). Enables calling sub-class-specific functions even
	 * when using objects declared with generic classes.</p>
	 * @param which name of function
	 * @return return value of function (numeric only)
	 */
	public double callFunction(String which) {
		Object ret = null;
		Method meth = null;
		
		try {
			meth = this.getClass().getMethod(which, null);
		} catch (SecurityException ex1) {
		} catch (NoSuchMethodException ex1) {
			ex1.printStackTrace();
			
		}
		try {
			ret = meth.invoke(this, null);
		} catch (InvocationTargetException ex) {
		} catch (IllegalArgumentException ex) {
		} catch (IllegalAccessException ex) {
		} catch (SecurityException ex) {
		}
		String sval = ret.toString();
		double val = Double.valueOf(sval).doubleValue();
		return val;
	}
}