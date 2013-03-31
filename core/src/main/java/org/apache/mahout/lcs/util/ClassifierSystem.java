package org.apache.mahout.lcs.util;

import java.util.*;

public class ClassifierSystem implements Cloneable {
	/**
	 * Variable <code>ruleset</code> List of Classifiers
	 */
	private ArrayList ruleset;
	private String name = "";
	private Classifier sample = null;

	/**
	 * Constructor
	 */
	public ClassifierSystem() {
		ruleset = new ArrayList();
	}

	/**
	 * Enhanced Constructor, initializing the CS following given parameters.
	 * @see init()
	 * @param name Name of the CS
	 * @param sample Sample-classifier, CS will be filled with clones of the
	 *        sample.
	 * @param size Number of classifiers
	 * @param randomized Classifiers are randomized if parameter is set to true.
	 */
	public ClassifierSystem(String name, Classifier sample, int size,
			boolean randomized) {
		this(); // Ruft zun�chst obige Variante des Konstruktors auf.
		setName(name);
		this.init(sample, size, randomized);
	}

	/**
	 * CS is initialized with a given number of classifiers following a sample
	 * (by cloning the sample). If randomized-parameter is true, the
	 * randomization-method of the clones is called, therefore the LCS-set then
	 * will be random.
	 * @param sample Sample-classifier, CS will be filled with clones of the
	 *        sample.
	 * @param size Number of classifiers
	 * @param randomized Classifiers are randomized if parameter is set to true.
	 */
	public void init(Classifier sample, long size, boolean randomized) {
		this.sample = sample;
		Object creation = null;
		for (long lauf = 0; lauf < size; lauf++) {
			creation = sample.clone();
			if (randomized) {
				((Classifier) creation).getRule().randomizeRule();
			}
			ruleset.add(creation);
		}
	}

	/**
	 * Clone-method creates a duplicate of the cs. <b>ATTENTION: </b>
	 * Classifiers themselves are not cloned, only the references to them!
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		try {
			ClassifierSystem lcs = (ClassifierSystem) super.clone();
			lcs.ruleset = (ArrayList) ruleset.clone();
			return lcs;
		} catch (CloneNotSupportedException ex) {
			return null;
		}
	}

	/**
	 * Used for setting the name of the instance.
	 * @param name Name to be set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returning the name of the instance.
	 * @return Name of CS
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the sample classifier defined for the instance.
	 * @return Sample-Classifier
	 */
	public Classifier getSample() {
		return sample;
	}

	/**
	 * Returns the number of Classifiers contained.
	 * @return Number of Classifiers
	 */
	public int size() {
		return ruleset.size();
	}

	/**
	 * Adds a Classifier to the instances' internal ruleset.
	 * @param cl Classifier to be added
	 * @throws SimLibraryError
	 */
	public void add(Classifier cl) throws SimLibraryError {
		if (cl == null) {
			throw new SimLibraryError("Null can not be added to CS");
		}
		if (ruleset.isEmpty() == false) {
			/*
			 * Classifier c = (Classifier) ruleset.get(0); if
			 * (c.compatibility(cl) == false) { throw new
			 * SimLibraryError("Classifier Type does not match"); }
			 */
		}
		ruleset.add(cl);
		ruleset.trimToSize();
	}

	/**
	 * (Delegate)-Function returns an iterator to the set of classifiers.
	 * @return Iterator �ber alle Classifer des CS
	 * @see java.util.ArrayList#iterator()
	 */
	public Iterator iterator() {
		return ruleset.iterator();
	}

	/**
	 * Adds all Classifiers of another instance to the instances' internal
	 * ruleset.
	 * @param cs CS whose classifiers are to be added to the CS
	 */
	public void add(ClassifierSystem cs) {
		Iterator i = cs.iterator();
		while (i.hasNext()) {
			add((Classifier) i.next());
		}
	}

	/**
	 * Returns a classifier at a given position
	 * @param index Position
	 * @return Classifier at given position
	 */
	public Classifier getClassifier(int index) {
		if (index < ruleset.size()) {
			return (Classifier) ruleset.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Function removes a given Classifier from CS.
	 * @param cl Classifier der entfernt werden soll.
	 */
	public void remove(Classifier cl) {
		ruleset.remove(cl);
	}

	/**
	 * Removes all classifiers.
	 */
	public void remove() {
		ruleset.removeAll(ruleset);
	}

	/**
	 * Removes (subtracts) all classifiers contained in a given CS from this
	 * instance.
	 * @param cs CS, whose classifiers are to be removed from the CS.
	 */
	public void remove(ClassifierSystem cs) {
		ruleset.removeAll(cs.ruleset);
	}

	/**
	 * Method for debugging purposes.
	 */
	public void debug_out() {
		Iterator i = ruleset.iterator();
		DebugOutput.println("Classifier System:" + name + " Size: " + size());
		while (i.hasNext()) {
			((Classifier) i.next()).debug_out();
			DebugOutput.println();
		}
	}

	/**
	 * Creates a clone of the instance and fills it with mutations of all
	 * classfiers, handing over a given probability for mutation.
	 * @param probability Probability for mutation
	 * @return new CS with mutated classifiers
	 */
	public ClassifierSystem mutation(double probability) {
		ClassifierSystem c = (ClassifierSystem) clone();
		c.remove();
		Iterator i = iterator();
		while (i.hasNext()) {
			c.add(((Classifier) i.next()).mutation(probability));
		}
		return c;
	}

	/**
	 * Creates a clone of the instance and fills clone with pairwise crossover
	 * (at random positions) of all classfiers.
	 * @return neues CS mit paarweise gekreuzten Classifiern
	 */
	public ClassifierSystem crossover() {
		ClassifierSystem c = (ClassifierSystem) clone();
		c.remove();
		Random rand = new Random();
		/*
		 * Jede zweite Regel mit der folgenden, die anderen mit der
		 * zuvorliegenden Regel kreuzen. Bei ungrader Anzahl letzte und erste
		 * Regel kreuzen.
		 */
		int pos;
		for (int lauf = 0; lauf < size(); lauf = lauf + 2) {
			pos = rand.nextInt(getClassifier(0).getRule().getLength());
			if (lauf + 1 == size()) {
				c.add(getClassifier(lauf).cross(getClassifier(0), pos));
				c.add(getClassifier(0).cross(getClassifier(lauf), pos));
			} else {
				c.add(getClassifier(lauf).cross(getClassifier(lauf + 1), pos));
				c.add(getClassifier(lauf + 1).cross(getClassifier(lauf), pos));
			}
		}
		return c;
	}

	/**
	 * Creates a new LCS with a selection of classifiers, having a
	 * criterion-value above (or below, if minimize is true) a given threshold.
	 * The criterion value should be returned by the function, whose name is
	 * defined in parameter 'which'.
	 * @param thresh Threshold value
	 * @param minimize If true, values must be below or equal thresh, otherwise
	 *        above or equal.
	 * @param which Name of a functional method of the classifiers returning a
	 *        numeric value as criterion for selection.
	 * @return new CS with classifiers following the criteria for selection
	 */
	public ClassifierSystem csSelection(double thresh, boolean minimize,
			String which) {
		Iterator i = iterator();
		Classifier c = null;
		double sign = 1;
		if (minimize) {
			sign = -1;
		}
		ClassifierSystem lcs = null;
		lcs = (ClassifierSystem) clone();
		lcs.remove();
		while (i.hasNext()) {
			c = (Classifier) i.next();
			if ((sign * c.callFunction(which)) >= (sign * thresh)) {
				lcs.add(c);
			}
		}
		return lcs;
	}

	/**
	 * Returns an array with minimum and maximum numerical value a given
	 * functional method (of the classifiers) reaches in the CS
	 * @param which Name of a classifier's method returning a numerical value
	 * @return Array like {min,max}
	 */
	public double[] getExtremeValues(String which) {
		Iterator i = iterator();
		Classifier c = null;
		double actual, min, max;
		if (i.hasNext()) {
			min = max = ((Classifier) i.next()).callFunction(which);
		} else {
			return null;
		}
		while (i.hasNext()) {
			c = (Classifier) i.next();
			actual = c.callFunction(which);
			if (actual < min) {
				min = actual;
			}
			if (actual > max) {
				max = actual;
			}
		}
		double[] r = {min, max};
		return r;
	}

	/**
	 * Calculates the total of a given functional method called for all
	 * classifiers in the CS.
	 * @param which Name of a functional method that returns a numerical value
	 * @return Total of returned values
	 */
	public double getTotal(String which) {
		Iterator i = iterator();
		double sum = 0;
		while (i.hasNext()) {
			sum += ((Classifier) i.next()).callFunction(which);
		}
		return sum;
	}

	/**
	 * Calculates the average of a given functional method called for all
	 * classifiers in the CS.
	 * @param which Name of a functional method that returns a numerical value
	 * @return Average value
	 */
	public double getAverage(String which) {
		return getTotal(which) / (double) size();
	}

	/**
	 * Returns roulettewheel-choice, using a given method of the classifiers for
	 * determining probability/weight. Probablility is inverted if 'Invert' is
	 * true.
	 * @param which NName of a functional method that returns a numerical value
	 * @param invert If true, the wheigts for probablity are inverted.
	 * @return Chosen classifier.
	 */
	public Classifier RoulettewheelChoice(String which, boolean invert) {
		Classifier c = null;
		Iterator i = iterator();
		Roulettewheel r = new Roulettewheel();
		Roulettewheel zeros = new Roulettewheel();
		double value;
		while (i.hasNext()) {
			c = (Classifier) i.next();
			value = c.callFunction(which);
			if (invert) {
				if (value == 0) {
					zeros.add(1, c);
				} else {
					r.add(1 / value, c);
				}
			} else {
				r.add(value, c);
			}
		}
		if (zeros.sum_values() > 0) {
			c = (Classifier) zeros.choice();
		} else {
			c = (Classifier) r.choice();
		}
		return c;
	}

	/**
	 * Returns a subset of the CS of a given size. If given size is not smaller
	 * than actual size, the entire set of CS is returned.
	 * @param size Size of subset
	 * @return new CS with randomly chosen classifiers.
	 */
	public ClassifierSystem randomSubset(long size) {
		Roulettewheel roulette = new Roulettewheel();
		Classifier c;
		if (size > this.size()) {
			return (ClassifierSystem) clone();
		}
		Iterator i = iterator();
		ClassifierSystem neu = (ClassifierSystem) clone();
		neu.remove();
		while (i.hasNext()) {
			roulette.add(1, i.next());
		}
		for (long lauf = 0; lauf < size; lauf++) {
			c = (Classifier) roulette.choice();
			roulette.remove(c);
			neu.add(c);
		}
		return neu;
	}

	/**
	 * Returns a subset of the CS with all rules that match a given action with
	 * their action part. If parameter 'subsumtive' is true, wildcards are used.
	 * @param action Action used as criterion for selection
	 * @param subsumptive If set to true, wildcards are used
	 * @return Subset of CS as a new CS
	 */
	public ClassifierSystem filterbyaction(Situation action, boolean subsumptive) {
		ClassifierSystem ASet = (ClassifierSystem) clone();
		ASet.remove();
		Iterator i = iterator();
		Classifier c = null;
		while (i.hasNext()) {
			c = (Classifier) i.next();
			if (subsumptive) {
				if (c.getRule().getAction().isMatchedSubsumtive(action)) {
					ASet.add(c);
				}
			} else {
				if (c.getRule().getAction().isMatched(action)) {
					ASet.add(c);
				}
			}
		}
		return ASet;
	}

	/**
	 * Returns a subset of the CS with all rules that match a given action with
	 * their action part. No wildcards are used.
	 * @param action Action used as criterion for selection
	 * @return Subset of CS as a new CS
	 */
	public ClassifierSystem filterbyaction(Situation action) {
		return filterbyaction(action, false);
	}

	/**
	 * Returns a subset of the CS with all rules that match a given condition
	 * with their action part. If parameter 'subsumtive' is true, wildcards are
	 * used.
	 * @param condition Condition to be used as criterion for selection
	 * @param subsumtive If set to true, wildcards are used
	 * @return Subset of CS as a new CS
	 */
	public ClassifierSystem filterByCondition(Situation condition,
			boolean subsumtive) {
		ClassifierSystem ASet = (ClassifierSystem) clone();
		ASet.remove();
		Iterator i = iterator();
		Classifier c = null;
		while (i.hasNext()) {
			c = (Classifier) i.next();
			if (subsumtive) {
				if (c.getRule().getCondition().isMatchedSubsumtive(condition)) {
					ASet.add(c);
				}
			} else {
				if (c.getRule().getCondition().isMatched(condition)) {
					ASet.add(c);
				}
			}
		}
		return ASet;
	}

	/**
	 * Returns a subset of the CS with all rules that match a given condition
	 * with their condition part. No wildcards are used.
	 * @param condition Condition to be used as criterion for selection
	 * @return Subset of CS as a new CS
	 */
	public ClassifierSystem filterByCondition(Situation condition) {
		return filterByCondition(condition, false);
	}

	/**
	 * Returns a subset of the CS with all rules having the same condition as given.
	 * @param condition Condition to be used as criterion for selection
	 * @return Subset of CS as a new CS
	 */
	public ClassifierSystem allWithCondition(Situation condition) {
		ClassifierSystem ASet = (ClassifierSystem) clone();
		ASet.remove();
		Iterator i = iterator();
		Classifier c = null;
		while (i.hasNext()) {
			c = (Classifier) i.next();
			int comp = c.getRule().getCondition().compare(condition);
			if ((comp == condition.size()) && (comp == c.getRule().getCondition().size())) {
				ASet.add(c);
			}
		}
		return ASet;
	}

/**
 * Counts the number of distinct conditions in CS.
 * @return number of conditions as long
 */
public int getConditionCount () {
		ClassifierSystem s = (ClassifierSystem) this.clone();
		int count = 0;
		while (s.size() > 0)
		{Situation sit = s.getClassifier(0).getRule().getCondition();
		s.remove(s.allWithCondition(sit));
		 count++;
		}
		return count;
	}}