package org.apache.mahout.lcs.util;

import java.util.*;

public class Language extends SymbolSet implements Cloneable {
	public Relation hierarchy;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Language l = (Language) super.clone();
		l.hierarchy = (Relation) hierarchy.clone();
		return l;
	}

	/**
	 * Constructor
	 * @param name Name of Language to be created.
	 */
	public Language(String name) {
		super(name);
		hierarchy = new Relation();
		DebugOutput.println(this.getClass(), "Language " + name + " created");
	}

	/**
	 * Constructor, creates an instance containing elements following a list of
	 * names.
	 * @param name Name of Language to be created
	 * @param symbolNames List of names of Symbols to be included
	 */
	public Language(String name, String[] symbolNames) {
		this(name);
		this.addMultiSymbols(symbolNames);
	}

	/**
	 * Adds a Symbol to a set, as a subordinate to a parent. If the parent is
	 * not part of the set, it will also be added to the set.
	 * @param s Element to be added
	 * @param parent Parent element.
	 */
	public void add(Symbol s, Symbol parent) {
		add(s);
		if (!contains(parent)) {
			add(parent);
		}
		hierarchy.addRel(parent, s);
		DebugOutput.println(this.getClass(), "Language " + getName()
				+ ": Symbol " + s.getName() + " added.");
	}

	/**
	 * Adds Symbols following a list of names to the instance. If a Symbol is
	 * not yet existing, it is created. Makes the Symbols subordinate to a given
	 * parent.
	 * @param names
	 * @param parent
	 */
	public void addMultiSymbols(String[] names, Symbol parent) {
		if (!this.contains(parent))
			throw new SimLibraryError("Parent not contained in Language");
		SymbolSet st = new SymbolSet("", names);
		this.add(st);
		parent.addMember(this, st);
	}

	/**
	 * @param s Symbol to be removed
	 */
	public void remove(Symbol s) {
		super.remove(s);
		hierarchy.remove(s);
		DebugOutput.println(this.getClass(), "Language " + getName()
				+ ": Symbol " + s.getName() + " removed.");
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.SymbolSet#remove()
	 */
	public void remove() {
		super.remove();
		hierarchy.remove();
		DebugOutput.println(this.getClass(), "Language " + getName()
				+ ": all Symbols removed.");
	}

	/**
	 * Checks wether the an element given as parent is really Funktion pr�ft, ob
	 * das als superordinate to the symbol given as child.
	 * @param parent
	 * @param child
	 * @return true, if parent is superordinate to child
	 */
	public boolean checkRel(Symbol parent, Symbol child) {
		return hierarchy.checkRel(parent, child);
	}

	/*
	 * (non-Javadoc)
	 * @see classifierLibrary.SymbolSet#debug_out()
	 */
	public void debug_out() {
		DebugOutput.println("Language: " + this.getName());
		if (getSize() > 0) {
			Iterator i = iterator();
			while (i.hasNext()) {
				DebugOutput.print(((Symbol) i.next()).getName());
			}
			DebugOutput.println("/n Default: " + this.getDefault().getName());
		} else {
			DebugOutput.println(" empty.");
		}
	}

	/**
	 * Checks wheter the language contains symbols that are superordinates to
	 * other symbols also contained in the language
	 * @return true, if wildcard symbols are contained
	 */
	public boolean hasWildcards() {
		Iterator i = this.iterator();
		while (i.hasNext()) {
			if (((Symbol) i.next()).hasMembers(this) == true) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all child elements of a given Symbol as defined in this Language.
	 * @param s Symbol serving as parent
	 * @return Array List of subordinate symbols
	 */
	public ArrayList getChildren(Symbol s) {
		return hierarchy.getChildren(s);
	}

	/* Debugging Information for this class is turned of. */
	static {
		DebugOutput.turnOff(Language.class);
	}
}