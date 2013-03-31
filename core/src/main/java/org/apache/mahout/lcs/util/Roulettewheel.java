package org.apache.mahout.lcs.util;

import java.util.*;

public class Roulettewheel {
	private ArrayList Wheel;
	private static Random rand = null;

	/**
	 * <p>Library simAuxLibrary </p> <p>Internal Class "element", not to be
	 * used by other classes.</p> <p>(c) Klaus Hufschlag 2006</p>
	 */
	class element {
		private double value;
		private Object pointer;

		public element(double value, Object pointer) {
			this.value = value;
			this.pointer = pointer;
		}

		public void setpointer(Object pointer) {
			this.pointer = pointer;
		}

		public Object getpointer() {
			return pointer;
		}

		public double getvalue() {
			return value;
		}
	}

	/**
	 * Standardconstructor, creates an empty Instance.
	 */
	public Roulettewheel() {
		Wheel = new ArrayList(100);
		if (rand == null) {
			rand = new Random();
		}
	}

	/**
	 * add is for adding an Object to the instances List of Objects for choice.
	 * @param value absolute Weight of the Object
	 * @param anObject Object to add
	 */
	public void add(double value, Object anObject) {
		if (value < 0) {
			throw new SimLibraryError("Value < 0 in Roulettewheel");
		}
		element e = new element(value, anObject);
		Wheel.add(e);
	}

	/**
	 * remove removes an object from the instances internal list of objects.
	 * @param AnObject the object to remove
	 */
	public void remove(Object AnObject) {
		Iterator i = Wheel.iterator();
		LinkedList l = new LinkedList();
		element e = null;
		while (i.hasNext()) {
			e = (element) i.next();
			if (e.getpointer() == AnObject) {
				l.add(e);
			}
		}
		Wheel.removeAll(l);
		Wheel.trimToSize();
	}

	/**
	 * sum_values calculates the sum of absolute wheigths of elements contained
	 * in the instances list.
	 * @return sum of the weights
	 */
	public double sum_values() {
		double sum = 0;
		Iterator i = Wheel.iterator();
		while (i.hasNext()) {
			sum += ((element) i.next()).getvalue();
		}
		return sum;
	}

	/**
	 * choice performs an roulettewheel-choice with the elements of the
	 * instances list.
	 * @return chosen Object
	 */
	public Object choice() {
		double sum = 0;
		double thresh = rand.nextDouble();
		element e;
		if (thresh < 0) {
			thresh = -thresh;
		}
		thresh = thresh * sum_values();
		Iterator i = Wheel.iterator();
		while (i.hasNext()) {
			e = (element) i.next();
			sum += e.getvalue();
			if (sum >= thresh) {
				return e.getpointer();
			}
		}
		return null;
	}

	/**
	 * pointers_iterator returns an Iterator of all objects contained in
	 * Roulettewheel
	 * @return Iterator
	 */
	public Iterator pointers_iterator() {
		ArrayList a = new ArrayList();
		Iterator i = Wheel.iterator();
		while (i.hasNext()) {
			a.add(((element) i.next()).getpointer());
		}
		return a.iterator();
	}

	/**
	 * getWeight returns the relative weight / the probability of the object in
	 * the Roulettewheel.
	 * @param anObject Object to get weight for
	 * @return relative weight of the object
	 */
	public double getWeight(Object anObject) {
		Iterator i = Wheel.iterator();
		double sum = 0;
		while (i.hasNext()) {
			element e = (element) i.next();
			if (e.getpointer() == anObject)
				sum += e.getvalue();
		}
		if (sum_values() == 0)
			return 0;
		return sum / sum_values();
	}

	/**
	 * removes all entries from Roulettewheel.
	 */
	public void empty_wheel() {
		this.Wheel.removeAll(this.Wheel);
		System.gc();
	}

	/**
	 * debug_out Output for debugging purposes.
	 */
	public void debug_out() {
		Iterator i = Wheel.iterator();
		element e = null;
		double sum = 0;
		while (i.hasNext()) {
			e = (element) i.next();
			sum += e.getvalue();
			DebugOutput.print(" " + e.getvalue() + " " + sum + ";");
		}
	}
}