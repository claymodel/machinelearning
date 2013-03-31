package org.apache.mahout.lcs.util;

import java.util.*;

public class Relation implements Cloneable {
	/**
	 * <p>Library simAuxLibrary</p><p>Class Relation, internal Class
	 * RNode</p><p>Internal class for encapsulation Object in relations.</p>
	 * <p>Copyright by Klaus Hufschlag 2004, 2005, 2006<p> all rights reserved.</p>
	 * 
	 * @author Klaus Hufschlag
	 * @version 1.4
	 */
	private class RNode implements Cloneable {
		HashSet parents;
		HashSet children;
		Object obj;

	
		/**
		 * Construktor 
		 * @param o Object to encapsulate
		 */
		public RNode(Object o) {
			parents = new HashSet();
			children = new HashSet();
			obj = o;
		}

		/**
		 * hasAsParent checks if a given node is a parent to the instance (given the relation).
		 * 
		 * @param check node to check
		 * @return true, if node is parent
		 */
		public boolean hasAsParent(RNode check) {
			if (parents.contains(check))
				return true;
			Iterator i = parents.iterator();
			while (i.hasNext())
				if (((RNode) i.next()).hasAsParent(check))
					return true;
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#clone()
		 */
		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException ex) {
				return null;
			}
		}
	}

	
	private Hashtable nodes;

	/**
	 * Standardconstructor, creates an empty instance. 
	 */
	public Relation() {
		nodes = new Hashtable();
	}

	/**
	 * addRel method for adding a new parent-child relation.
	 * @param parent parent to set
	 * @param child child to set
	 */
	public void addRel(Object parent, Object child) {
		if (!nodes.containsKey(parent)) {
			nodes.put(parent, new RNode(parent));
		}
		if (!nodes.containsKey(child)) {
			nodes.put(child, new RNode(child));
		}
		RNode p = (RNode) nodes.get(parent);
		RNode c = (RNode) nodes.get(child);
		p.children.add(c);
		c.parents.add(p);
	}

	/**
	 * delRel, method to remove a given relation, specified by parent and child
	 * 
	 * @param parent parent to specify relation
	 * @param child child to specifiy relation
	 */
	public void delRel(Object parent, Object child) {
		RNode p = (RNode) nodes.get(parent);
		RNode c = (RNode) nodes.get(child);
		if (p != null)
			p.children.remove(c);
		if (c != null)
			c.parents.remove(p);
	}

	/**
	 * remove method to remove an Object of all relations.
	 * @param o Object to remove
	 */
	public void remove(Object o) {
		RNode rem = (RNode) nodes.get(o);
		if (rem != null){
		Iterator i = rem.parents.iterator();
		while (i.hasNext())
			((RNode) i.next()).children.remove(rem);
		i = rem.children.iterator();
		while (i.hasNext())
			((RNode) i.next()).parents.remove(rem);
		nodes.remove(o);}
	}

	/**
	 * remove method to remove all relations.
	 */
	public void remove() {
		nodes.clear();
	}

	
	/**
	 * getParents method returns all direct parent Objects.
	 * @param child child to find parents
	 * @return parent-Objects
	 */
	public ArrayList getParents(Object child) {
		RNode c = (RNode) nodes.get(child);
		ArrayList a = new ArrayList();
		Iterator i = c.parents.iterator();
		while (i.hasNext()) {
			a.add(((RNode) i.next()).obj);
		}
		return a;
	}

	
	/**
	 * getChildren method return all direct child Objects.
	 * @param parent to find children
	 * @return child-Objects
	 */
	public ArrayList getChildren(Object parent) {
		RNode c = (RNode) nodes.get(parent);
		ArrayList a = new ArrayList();
		Iterator i = c.children.iterator();
		while (i.hasNext()) {
			a.add(((RNode) i.next()).obj);
		}
		return a;
	}

	
	/**
	 * checkRel checks whether there is a (direct or indirect) relation between the objects
	 * @param parent Object to be checked for being superordinate
	 * @param child Object to be checked for being subordinate
	 * @return true, if relation exists
	 */
	public boolean checkRel(Object parent, Object child) {
		RNode p = (RNode) (nodes.get(parent));
		RNode c = (RNode) (nodes.get(child));
		if ((p == null) || (c == null))
			return false;
		return c.hasAsParent(p);
	}


	/**
	 * add adds the entire oder of another relation to the Instance. (Should only 
	 * be used with special care as otherwise side-effect may occur.)
	 * @param r Relation to integrate
	 */
	public void add(Relation r) {
		Enumeration e = r.nodes.elements();
		while (e.hasMoreElements()) {
			RNode n = (RNode) e.nextElement();
			Iterator i = n.children.iterator();
			while (i.hasNext()) {
				this.addRel(n.obj, ((RNode) i.next()).obj);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Relation r = null;
		try {
			r = (Relation) super.clone();
		} catch (CloneNotSupportedException ex) {
			return null;
		}
		Enumeration e = this.nodes.elements();
		r.nodes = new Hashtable();
		while (e.hasMoreElements()) {
			RNode n = (RNode) ((RNode) e.nextElement()).clone();
			r.nodes.put(n.obj, n);
		}
		return r;
	}
}