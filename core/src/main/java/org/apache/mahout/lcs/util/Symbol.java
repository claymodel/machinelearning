package org.apache.mahout.lcs.util;

import java.util.*;

public class Symbol {
	private String name;
	private long number;
	protected static long counter;
	private static Language global;

	/**
	 * Returns any Symbol-Instance given his name.
	 * @param name Name of Symbol
	 * @return Symbol-Object with given name
	 */
	public static Symbol byName(String name) {
		if (global == null) {
			global = new Language("Global");
		}
		return global.symbolByName(name);
	}

	/**
	 * Generates a valid proposal for a non existing Symbol name.
	 * @return proposal for Symbol-name
	 */
	public static String autoName() {
		long nr = counter + 1;
		while (byName("AutoName" + nr) != null)
			nr++;
		return "AutoName" + nr;
	}

	/**
	 * Constructor
	 * @param Name Name of the Symbol to create
	 */
	public Symbol(String Name) {
		if (global == null) {
			global = new Language("Global");
		}
		if (byName(Name) != null) {
			throw new SimLibraryError("Duplicate Symbol Name");
		}
		name = Name;
		number = counter++;
		DebugOutput.println("Creation of Symbol " + name + " (" + number + ")");
		global.add(this);
	}

	/**
	 * @return Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 */
	public void debug_out() {
		DebugOutput.print(getName());
	}

	/**
	 * Checks the existence of subordinate Symbols in a given Language.
	 * @param s Language the check refers to.
	 * @return true, if subordinate Symbols exist
	 */
	public boolean hasMembers(Language s) {
		if (s.getChildren(this).size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks the existence of subordinate Symbols in the global / default
	 * Language.
	 * @return true, if subordinate Symbols exist
	 */
	public boolean hasMembers() {
		return hasMembers(global);
	}

	/**
	 * Makes a given Symbol a subordinate of the instance in a given language.
	 * @param m Language the relation should be defined for
	 * @param s Symbol to become subordinate
	 */
	public void addMember(Language m, Symbol s) {
		m.add(s, this);
	}

	/**
	 * Makes a given Symbol a subordinate of the instance in the global /
	 * default language.
	 * @param s Symbol to become subordinate
	 */
	public void addMember(Symbol s) {
		addMember(global, s);
	}

	/**
	 * Makes a given Set of Symbols a subordinate to the instance in a given
	 * language.
	 * @param m Language the relation should be defined for
	 * @param s SymbolSet of Symbols to become subordinate
	 */
	public void addMember(Language m, SymbolSet s) {
		Iterator i = s.iterator();
		while (i.hasNext()) {
			m.add((Symbol) i.next(), this);
		}
	}

	/**
	 * Makes a given Set of Symbols a subordinate to the instance in the global /
	 * default language.
	 * @param s SymbolSet of Symbols to become subordinate
	 */
	public void addMember(SymbolSet s) {
		addMember(global, s);
	}

	/**
	 * Checks, wether a given Symbol is a subordinate to the instance in a given
	 * Language.
	 * @param m Language in which the relation should be checked.
	 * @param s Symbol which is to be checked for being subordinate
	 * @return true, if Symbol s is subordinate to the instance.
	 */
	public boolean subsumes(Language m, Symbol s) {
		if (s == this)
			return true;
		if (s == null)
			throw new SimLibraryError(
					"NULL can not be checked for being subsumed.");
		return m.checkRel(this, s);
	}

	/**
	 * Checks, wether a given Symbol is a subordinate to the instance in the
	 * global / default Language.
	 * @param s Symbol which is to be checked for being subordinate
	 * @return true, if Symbol s is subordinate to the instance.
	 */
	public boolean subsumes(Symbol s) {
		return subsumes(global, s);
	}

	/**
	 * Returns a Language of all existing Symbols (i.e. returns the global /
	 * default Language).
	 * @return Language containing all defined Symbols.
	 */
	public static Language getAllSymbols() {
		if (global == null) {
			global = new Language("Global");
		}
		return global;
	}
}