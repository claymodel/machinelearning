package org.apache.mahout.lcs.util;

import java.util.*;

public class StandardSituation implements Situation, Cloneable {
	private Symbol[] symbols;
	private int length;
	private Language lang;

	/**
	 * Constructor, creates a string-like sequence of Symbols, with given
	 * language and lenght, initializes all places in sequence with the default
	 * symbol of the language
	 * @param l Language to use
	 * @param Length Length of the sequence
	 */
	public StandardSituation(Language l, int Length) {
		length = Length;
		symbols = new Symbol[length];
		lang = l;
		for (int lauf = 0; lauf < length; lauf++) {
			setElement(lauf, l.getDefault());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		StandardSituation s = null;
		try {
			s = (StandardSituation) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		s.symbols = (Symbol[]) symbols.clone();
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#getLength()
	 */
	public int getLength() {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#size()
	 */
	public int size() {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#getLanguage()
	 */
	public Language getLanguage() {
		return lang;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#setLanguage(classifierLibrary.Language)
	 */
	public void setLanguage(Language lang) {
		this.lang = lang;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#compatibility(classifierLibrary.Situation)
	 */
	public boolean compatibility(Situation sit) {
		return (this.getLength() == sit.getLength())
				&& this.getLanguage().contains(sit.getLanguage());
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#cross(classifierLibrary.Situation, int)
	 */
	public Situation cross(Situation sit, int Position) {
		/*
		 * if (compatibility(sit) == false) { throw new
		 * SimLibraryError("Situations incompatible"); }
		 */
		StandardSituation s = (StandardSituation) sit;
		StandardSituation c = (StandardSituation) clone();
		for (int lauf = Position; lauf < length; lauf++) {
			c.setElement(lauf, s.getElement(lauf));
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#mutation(double)
	 */
	public Situation mutation(double Probability) {
		Random r = new Random();
		StandardSituation c = new StandardSituation(lang, getLength());
		Symbol s = null;
		for (int lauf = 0; lauf < length; lauf++) {
			if (r.nextDouble() < Probability) {
				s = lang.randomSymbol();
				while (getElement(lauf) == s) {
					s = lang.randomSymbol();
				}
				c.setElement(lauf, lang.randomSymbol());
			} else {
				c.setElement(lauf, getElement(lauf));
			}
		}
		return c;
	}

	/**
	 * Sets the Element of the Situation/sequence at given position to the given
	 * Symbol.
	 * @param Position Position at which the Symbol has to be set
	 * @param Symbol Symbol to set
	 */
	public void setElement(int Position, Symbol Symbol) {
		if ((0 <= Position) && (Position < length)) {
			symbols[Position] = Symbol;
		}
	}

	/**
	 * Returns the Symbol at a given position in the Situation/sequence.
	 * @param position Position of wanted Symbol
	 * @return Symbol Symbol at wanted position, null if position does not exist
	 */
	public Symbol getElement(int position) {
		Symbol s = null;
		if ((0 <= position) && (position < length)) {
			s = symbols[position];
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#randomizeSituation()
	 */
	public void randomizeSituation() {
		for (int lauf = 0; lauf < length; lauf++) {
			this.setElement(lauf, lang.randomSymbol());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#randomizeSituation(double)
	 */
	public void randomizeSituation(double wildprob) {
		Symbol s;
		Random r = new Random();
		for (int lauf = 0; lauf < length; lauf++) {
			if ((r.nextDouble() > wildprob) && lang.hasWildcards()) {
				s = lang.randomSymbol();
				while (s.hasMembers() == false) {
					s = lang.randomSymbol();
				}
			} else {
				s = lang.randomSymbol();
				while (s.hasMembers() == false) {
					s = lang.randomSymbol();
				}
			}
			this.setElement(lauf, s);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#debug_out()
	 */
	public void debug_out() {
		DebugOutput.print("StandardSituation: ");
		for (int lauf = 0; lauf < symbols.length; lauf++) {
			if (lauf > 0) {
				DebugOutput.print(",");
			}
			DebugOutput.print(this.getElement(lauf).getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#compare(classifierLibrary.Situation)
	 */
	public int compare(Situation sit) {
		int count = 0;
		StandardSituation s = (StandardSituation) sit;
		for (int lauf = 0; lauf < getLength(); lauf++) {
			if (getElement(lauf).equals(s.getElement(lauf))) {
				count++;
			}
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#compareSubsumtive(classifierLibrary.Situation)
	 */
	public int compareSubsumtive(Situation sit) {
		StandardSituation s = (StandardSituation) sit;
		int count = 0;
		for (int lauf = 0; lauf < getLength(); lauf++) {
			if (s.getElement(lauf) == null)
				throw new SimLibraryError(
						"Situation to compare contains NULL Symbol");
			if (getElement(lauf).subsumes(s.getElement(lauf))
					|| getElement(lauf).subsumes(this.getLanguage(),
							s.getElement(lauf))) {
				count++;
			}
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#isMatched(classifierLibrary.Situation)
	 */
	public boolean isMatched(Situation sit) {
		if (compare(sit) == size())
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#isMatchedSubsumtive(classifierLibrary.Situation)
	 */
	public boolean isMatchedSubsumtive(Situation sit) {
		if (compareSubsumtive(sit) == size())
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.Situation#specificMembers()
	 */
	public int specificMembers() {
		int count = 0;
		for (int lauf = 0; lauf < getLength(); lauf++) {
			if (getElement(lauf).hasMembers() == false) {
				count++;
			}
		}
		return count;
	}
}