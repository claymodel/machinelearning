package org.apache.mahout.lcs.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

public class SymbolSet implements Cloneable {
	private LinkedHashMap symbols = new LinkedHashMap();
	private Symbol defaultsymbol;
	private String name;
	private Random rand;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		SymbolSet c = null;
		try {
			c = (SymbolSet) super.clone();
			c.symbols = (LinkedHashMap) symbols.clone();
			c.setDefault(this.getDefault());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * Constructor. Creates a new (nameless) instance of SymbolSet.
	 */
	public SymbolSet() {
		symbols = new LinkedHashMap();
		defaultsymbol = null;
		rand = new Random();
		name = "";
	}

	/**
	 * Constructor. Creates a new instance of SymbolSet with a given name.
	 * @param name Name for the new SymbolSet.
	 */
	public SymbolSet(String name) {
		this();
		setName(name);
	}

	public SymbolSet(String name, String[] symbolNames) {
		this(name);
		this.addMultiSymbols(symbolNames);
	}

	/**
	 * Method for Setting the name of the instance.
	 * @param name Name to be set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method returns the name of the instance.
	 * @return Name of the instance.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method for adding a Symbol to the set.
	 * @param s Symbol to be added.
	 */
	public void add(Symbol s) {
		symbols.put(s.getName(), s);
		if (getDefault() == null) {
			setDefault(s);
		}
	}

	/**
	 * Method for adding all Symbols of another SymbolSet to the set.
	 * @param s SymbolSet of Symbols to be added.
	 */
	public void add(SymbolSet s) {
		Iterator i = s.iterator();
		while (i.hasNext()) {
			Symbol sy = (Symbol) i.next();
			if (this.contains(sy) == false) {
				this.add(sy);
			}
		}
	}

	/**
	 * Adds Symbols following a list of names to the instance. If a Symbol is
	 * not yet existing, it is created.
	 * @param names
	 */
	public void addMultiSymbols(String[] names) {
		for (int i = 0; i < names.length; i++) {
			Symbol s = Symbol.byName(names[i]);
			if (s == null)
				s = new Symbol(names[i]);
			add(s);
		}
	}

	/**
	 * Checks, whether a given Symbol is contained in the set.
	 * @param s Symbol to check
	 * @return true, if Symbol is contained
	 */
	public boolean contains(Symbol s) {
		return symbols.containsValue(s);
	}

	/**
	 * Checks, whether a Symbol given by its name is contained in the set.
	 * @param name Name of the Symbol to check
	 * @return true, if Symbol is contained
	 */
	public boolean contains(String name) {
		return symbols.containsKey(name);
	}

	/**
	 * Checks, whether a given SymbolSet is a subset of the set.
	 * @param s SymbolSet to check for being a subset of the instance
	 * @return true, if all Symbols are contained in the instance
	 */
	public boolean contains(SymbolSet s) {
		boolean cont = true;
		Iterator i = s.iterator();
		while (cont && i.hasNext()) {
			cont = contains((Symbol) i.next());
		}
		return cont;
	}

	/**
	 * Removes a Symbol from the set.
	 * @param s Symbol to remove
	 */
	public void remove(Symbol s) {
		if (s == null)
			throw new SimLibraryError("can not remove null from SymbolSet");
		symbols.remove(s.getName());
		if ((s == defaultsymbol) && (getSize() > 0))
			setDefault(randomSymbol());
		else
			setDefault((Symbol) null);
	}

	/**
	 * <p>Removes a Set of Symbols from the instance.</p>
	 * @param s SymbolSet of elements to be removed.
	 */
	public void remove(SymbolSet s) {
		Iterator i = s.iterator();
		while (i.hasNext()) {
			remove((Symbol) i.next());
		}
	}

	/**
	 * Removes all Symbols from the instance.
	 */
	public void remove() {
		symbols.clear();
		defaultsymbol = null;
	}

	/**
	 * Returns a Symbol given by its Name, if contained in the instance.
	 * @param Name Name of Symbol.
	 * @return Symbol
	 */
	public Symbol symbolByName(String Name) {
		return (Symbol) symbols.get(Name);
	}

	/**
	 * Returns a randomly selected Symbol contained in the instance.
	 * @return Symbol randomly selected from the instance
	 */
	public Symbol randomSymbol() {
		Symbol s = null;
		// DebugOutput.println ("symbols.size:"+symbols.size());
		int thresh = rand.nextInt(symbols.size());
		// DebugOutput.println ("thresh:"+thresh);
		Iterator i = iterator();
		for (int lauf = 0; lauf <= thresh; lauf++) {
			s = (Symbol) i.next();
		}
		return s;
	}

	/**
	 * Method for debugging-purposes.
	 */
	public void debug_out() {
		DebugOutput.println("SymbolSet: " + this.getName());
		if (getSize() > 0) {
			Iterator i = iterator();
			while (i.hasNext()) {
				DebugOutput.println(((Symbol) i.next()).getName());
			}
			DebugOutput.println("Default: " + this.getDefault().getName());
		} else {
			DebugOutput.println(" empty.");
		}
	}

	/**
	 * Method to define a Symbol as default-Symbol of the instance.
	 * @param Name Name of the Symbol to be set as default
	 */
	public void setDefault(String Name) {
		setDefault(symbolByName(Name));
	}

	/**
	 * Method to define a Symbol as default-Symbol of the instance.
	 * @param s Symbol to be set as default
	 */
	public void setDefault(Symbol s) {
		defaultsymbol = s;
	}

	/**
	 * Returns the default-Symbol of the instance.
	 * @return Symbol which is default
	 */
	public Symbol getDefault() {
		return defaultsymbol;
	}

	/**
	 * Returns the number of Symbols contained.
	 * @return Number of Symbols (as int)
	 */
	public int getSize() {
		return symbols.size();
	}

	/**
	 * Returns a collection-view on the instance, giving access to further java
	 * standard methods.
	 * @return View implementing the collection-interface
	 */
	public Collection asCollection() {
		return this.symbols.values();
	}

	/**
	 * Returns an iterator through alle elements contained.
	 * @return Iterator
	 */
	public Iterator iterator() {
		return this.symbols.values().iterator();
	}

	/**
	 * Generates an ArrayList of SymbolSets, representing the power set of the
	 * instance (ie. the set of all possible subsets of the set represented by
	 * the instance).
	 * @return ArrayList of SymbolSets representing the power set of the
	 *         instance.
	 */
	public ArrayList powerSet() {
		// debug_out();
		ArrayList res = new ArrayList();
		if (getSize() == 0) {
			res.add(this);
		} else {
			SymbolSet recurse = (SymbolSet) clone();
			Symbol first = recurse.randomSymbol();
			recurse.remove(first);
			ArrayList top = recurse.powerSet();
			Iterator i = top.iterator();
			while (i.hasNext()) {
				SymbolSet s = (SymbolSet) ((SymbolSet) i.next()).clone();
				s.add(first);
				res.add(s);
			}
			res.addAll(top);
		}
		return res;
	}
}
