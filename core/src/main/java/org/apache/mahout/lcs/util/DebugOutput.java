package org.apache.mahout.lcs.util;

import java.io.*;
import java.util.HashSet;
import java.util.Hashtable;

public class DebugOutput {
	private static final Hashtable outputs = new Hashtable();
	private static final HashSet deactivated = new HashSet();
	private static final Object defaultOut = new Object();

	/**
	 * Private constructor, no external instatiation.
	 */
	private DebugOutput() {
	}

	/**
	 * Setting standard channel for output des Standard-Ausgabekanals.</p>
	 * @param out Writer-Object as standard console.
	 */
	public static void setWriter(Writer out) {
		setWriter(defaultOut, out);
	}

	/**
	 * Define a console used for a key object.
	 * @param key key object
	 * @param out Writer-Object as console
	 */
	public static void setWriter(Object key, Writer out) {
		outputs.put(key, out);
	}

	/**
	 * Returns the console for the key object.
	 * @param key key object
	 * @return defined writer
	 */
	public static Writer getWriter(Object key) {
		Writer out = (Writer) outputs.get(key);
		if (out == null)
			out = (Writer) outputs.get(defaultOut);
		return out;
	}

	/**
	 * Activates console for the given key.
	 * @param key key object
	 */
	public static void turnOn(Object key) {
		deactivated.remove(key);
	}

	/**
	 * Deactivates console for the given key.
	 * @param key key object
	 */
	public static void turnOff(Object key) {
		deactivated.add(key);
	}

	/**
	 * Checks, wheter the console for a key is activated.
	 * @param key key object
	 * @return true, if console is activated
	 */
	public static boolean isOn(Object key) {
		return !(deactivated.contains(key));
	}

	/**
	 * Activates standard console.
	 */
	public static void turnOn() {
		turnOn(defaultOut);
	}

	/**
	 * Deactivates standard console.
	 */
	public static void turnOff() {
		turnOff(defaultOut);
	}

	/**
	 * Checks activation of standard console.
	 * @return true
	 */
	public static boolean isOn() {
		return isOn(defaultOut);
	}

	/**
	 * Output to the console assigned for the given key.
	 * @param key key object
	 * @param string output-string
	 */
	public static void print(Object key, String string) {
		if (isOn(key)) {
			Writer out = getWriter(key);
			if (out == null)
				throw new SimLibraryError("DebugOutput: can't find Writer");
			{
				try {
					out.write(string);
					out.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Output on standard console.
	 * @param string output-string
	 */
	public static void print(String string) {
		print(defaultOut, string);
	}

	/**
	 * Output with linefeed to console assigned to key.
	 * @param key key object
	 * @param string auszugebender String
	 */
	public static void println(Object key, String string) {
		print(key, string + "\n");
	}

	/**
	 * Output of a single linefeed to console assigned to key.
	 * @param key key object
	 */
	public static void println(Object key) {
		println(key, "");
	}

	/**
	 * Output of a single linefeed to standard console.
	 */
	public static void println() {
		println(defaultOut);
	}

	/**
	 * Output of a string with linefeed to standard console. <b>Pay attention! 
	 * </b> If the string is also used as a key object, this will result in the
	 * output of a linefeed to the console assigned to the key.
	 * @param string auszugebender String
	 */
	public static void println(String string) {
		if (outputs.contains(string))
			println(string, "");
		else {
			print(string);
			println();
		}
	}

	static {
		/** Default: Outpup to System.out */
		setWriter(new OutputStreamWriter(System.out));
	}
}