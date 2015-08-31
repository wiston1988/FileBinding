package com.filebinding.core.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseTypeConverter{

	private static BaseTypeConverter instance = new BaseTypeConverter();

	private static final String NULL_STRING = "null";
	private final Map<Class, Object> primitiveDefaults;

	public static BaseTypeConverter getInstance(){
		return instance;
	}

	private BaseTypeConverter(){
		Map<Class, Object> map = new HashMap<Class, Object>();
		map.put(Boolean.TYPE, Boolean.FALSE);
		map.put(Byte.TYPE, Byte.valueOf((byte) 0));
		map.put(Short.TYPE, Short.valueOf((short) 0));
		map.put(Character.TYPE, new Character((char) 0));
		map.put(Integer.TYPE, Integer.valueOf(0));
		map.put(Long.TYPE, Long.valueOf(0L));
		map.put(Float.TYPE, new Float(0.0f));
		map.put(Double.TYPE, new Double(0.0));
		map.put(BigInteger.class, new BigInteger("0"));
		map.put(BigDecimal.class, new BigDecimal(0.0));
		primitiveDefaults = Collections.unmodifiableMap(map);
	}

	public Object convertIfNecessary(Object value, Class toType){
		if(value == null)//If null, get default value
			return primitiveDefaults.get(toType);

		Object result = null;

		if (value.getClass().isArray() && toType.isArray()) {
			Class componentType = toType.getComponentType();

			int length = Array.getLength(value);
			result = Array.newInstance(componentType, length);

			for (int i = 0; i < length; i++) {
				Array.set(result, i, convertIfNecessary(Array.get(value, i),componentType));//recursion
			}
		}else if(value.getClass() == toType){
			result = value;
		}else{
			result = convertNotArray(value, toType);
		}

		return result;
	}
	
	public Object convertNotArray(Object value, Class toType){
		Object result = null;
		
		if ((toType == Integer.class) || (toType == Integer.TYPE))
			result = Integer.valueOf((int) longValue(value));
		if ((toType == Double.class) || (toType == Double.TYPE))
			result = new Double(doubleValue(value));
		if ((toType == Boolean.class) || (toType == Boolean.TYPE))
			result = booleanValue(value) ? Boolean.TRUE : Boolean.FALSE;
		if ((toType == Byte.class) || (toType == Byte.TYPE))
			result = Byte.valueOf((byte) longValue(value));
		if ((toType == Character.class) || (toType == Character.TYPE))
			result = new Character((char) longValue(value));
		if ((toType == Short.class) || (toType == Short.TYPE))
			result = Short.valueOf((short) longValue(value));
		if ((toType == Long.class) || (toType == Long.TYPE))
			result = Long.valueOf(longValue(value));
		if ((toType == Float.class) || (toType == Float.TYPE))
			result = new Float(doubleValue(value));
		if (toType == BigInteger.class)
			result = bigIntValue(value);
		if (toType == BigDecimal.class)
			result = bigDecValue(value);
		if (toType == String.class)
			result = stringValue(value);
		
		return result;
	}

	/**
	 * Evaluates the given object as a long integer.
	 * 
	 * @param value
	 *            an object to interpret as a long integer
	 * @return the long integer value implied by the given object
	 * @throws NumberFormatException
	 *             if the given object can't be understood as a long integer
	 */
	public static long longValue(Object value) throws NumberFormatException {
		if (value == null)
			return 0L;

		Class c = value.getClass();
		if (c.getSuperclass() == Number.class)
			return ((Number) value).longValue();
		if (c == Boolean.class)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (c == Character.class)
			return ((Character) value).charValue();
		return Long.parseLong(stringValue(value, true));
	}

	/**
	 * Evaluates the given object as a double-precision floating-point number.
	 * 
	 * @param value
	 *            an object to interpret as a double
	 * @return the double value implied by the given object
	 * @throws NumberFormatException
	 *             if the given object can't be understood as a double
	 */
	public static double doubleValue(Object value) throws NumberFormatException {
		if (value == null)
			return 0.0;

		Class c = value.getClass();
		if (c.getSuperclass() == Number.class)
			return ((Number) value).doubleValue();
		if (c == Boolean.class)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (c == Character.class)
			return ((Character) value).charValue();
		String s = stringValue(value, true);

		return (s.length() == 0) ? 0.0 : Double.parseDouble(s);
	}

	/**
	 * Evaluates the given object as a BigInteger.
	 * 
	 * @param value
	 *            an object to interpret as a BigInteger
	 * @return the BigInteger value implied by the given object
	 * @throws NumberFormatException
	 *             if the given object can't be understood as a BigInteger
	 */
	public static BigInteger bigIntValue(Object value)
	throws NumberFormatException {
		if (value == null)
			return BigInteger.valueOf(0L);

		Class c = value.getClass();
		if (c == BigInteger.class)
			return (BigInteger) value;
		if (c == BigDecimal.class)
			return ((BigDecimal) value).toBigInteger();
		if (c.getSuperclass() == Number.class)
			return BigInteger.valueOf(((Number) value).longValue());
		if (c == Boolean.class)
			return BigInteger.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
		if (c == Character.class)
			return BigInteger.valueOf(((Character) value).charValue());

		return new BigInteger(stringValue(value, true));//FIXME, shall we throw exception here instead of return default value
	}

	/**
	 * Evaluates the given object as a BigDecimal.
	 * 
	 * @param value
	 *            an object to interpret as a BigDecimal
	 * @return the BigDecimal value implied by the given object
	 * @throws NumberFormatException
	 *             if the given object can't be understood as a BigDecimal
	 */
	public static BigDecimal bigDecValue(Object value)
	throws NumberFormatException {
		if (value == null)
			return BigDecimal.valueOf(0L);
		Class c = value.getClass();
		if (c == BigDecimal.class)
			return (BigDecimal) value;
		if (c == BigInteger.class)
			return new BigDecimal((BigInteger) value);
		if (c.getSuperclass() == Number.class)
			return new BigDecimal(((Number) value).doubleValue());
		if (c == Boolean.class)
			return BigDecimal.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
		if (c == Character.class)
			return BigDecimal.valueOf(((Character) value).charValue());

		return new BigDecimal(stringValue(value, true));//FIXME, shall we throw exception here instead of return default value
	}

	/**
	 * Evaluates the given object as a boolean: if it is a Boolean object, it's
	 * easy; if it's a Number or a Character, returns true for non-zero objects;
	 * and otherwise returns true for non-null objects.
	 * 
	 * @param value
	 *            an object to interpret as a boolean
	 * @return the boolean value implied by the given object
	 */
	public static boolean booleanValue(Object value) {//FIXME, shall we throw exception here instead of return default value
		if (value == null)
			return false;

		Class c = value.getClass();
		if (c == Boolean.class)
			return ((Boolean) value).booleanValue();
		if (c == Character.class)
			return ((Character) value).charValue() != 0;
		if (value instanceof Number)
			return ((Number) value).doubleValue() != 0;
		return true; // non-null
	}

	/**
	 * Evaluates the given object as a String and trims it if the trim flag is
	 * true.
	 * 
	 * @param value
	 *            an object to interpret as a String
	 * @return the String value implied by the given object as returned by the
	 *         toString() method, or "null" if the object is null.
	 */
	public static String stringValue(Object value, boolean trim) {
		String result;

		if (value == null) {
			result = NULL_STRING;//return "null", so that there will be no NullPointerException, but NumberFormatException
		} else {
			result = value.toString();
			if (trim) {
				result = result.trim();
			}
		}
		return result;
	}

	/**
	 * Evaluates the given object as a String.
	 * 
	 * @param value
	 *            an object to interpret as a String
	 * @return the String value implied by the given object as returned by the
	 *         toString() method, or "null" if the object is null.
	 */
	public static String stringValue(Object value) {
		return stringValue(value, false);
	}
}