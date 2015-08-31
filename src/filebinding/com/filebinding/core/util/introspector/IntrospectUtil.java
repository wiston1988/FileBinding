package com.filebinding.core.util.introspector;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filebinding.core.exception.ConfigException;

public class IntrospectUtil {
	private static Log log = LogFactory.getLog(IntrospectUtil.class);
	private static Map<String, Map<String, PropertyDescriptor>> propertyDescriptorCache = new HashMap<String, Map<String, PropertyDescriptor>>();

	public static Object getPropertyValue(String className, String fieldName, Object bean)
									throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ConfigException{
		return getPropertyValue(introspectReadbleProperty(className, fieldName), bean);
	}

	public static Object getPropertyValue(List<PropertyDescriptor> propertyDescriptors, Object bean)
									throws IllegalAccessException, IllegalArgumentException,InvocationTargetException{
		Object pojoValue = bean;//loop from root bean

		int size = propertyDescriptors.size();
		for(int i=0;i<size;i++)
			pojoValue = propertyDescriptors.get(i).getReadMethod().invoke(pojoValue);

		return pojoValue;
	}

	public static void setPropertyValue(String className, String fieldName, Object bean, Object value)
									throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ConfigException{
		setPropertyValue(introspectWriteableProperty(className, fieldName), bean, value);
	}

	public static void setPropertyValue(List<PropertyDescriptor> propertyDescriptors, Object bean, Object value)
									throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		Object currBean = bean;//loop from root bean

		int size = propertyDescriptors.size();
		for(int i=0;i<size;i++){
			PropertyDescriptor pd = propertyDescriptors.get(i);

			if(i == size - 1){
				pd.getWriteMethod().invoke(currBean, value);
			}else{
				Object propertyBean = pd.getReadMethod().invoke(currBean);
				if(propertyBean == null){
					propertyBean = pd.getPropertyType().newInstance();
					pd.getWriteMethod().invoke(currBean, propertyBean);
				}

				currBean = propertyBean;
			}
		}
	}

	public static List<PropertyDescriptor> introspectReadbleProperty(String className, String fieldName)throws ConfigException{
		return introspectProperty(className, fieldName, AccessibleType.READABLE);
	}

	public static List<PropertyDescriptor> introspectWriteableProperty(String className, String fieldName)throws ConfigException{
		return introspectProperty(className, fieldName, AccessibleType.WRITEABLE);
	}

	public static List<PropertyDescriptor> introspectReadWriteableProperty(String className, String fieldName)throws ConfigException{
		return introspectProperty(className, fieldName, AccessibleType.READWRITEABLE);
	}

	private static  List<PropertyDescriptor> introspectProperty(String className, String fieldName, AccessibleType accessibleType)throws ConfigException{
		String info = "\n IntrospectUtil::introspectProperty - ";
		
		List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();

		String[] propertyNames = fieldName.split("\\.");
		int length = propertyNames.length;
		String currClass = className;

		for(int i=0;i<length;i++){
			String currProperty = propertyNames[i];
			PropertyDescriptor descriptor = getPropertyDescriptor(currClass, currProperty);
			if(descriptor == null){
				log.error(info + "Failed to get Property Descriptor of property ["+currClass+"::"+currProperty+"]");
				throw new ConfigException("Failed to get Property Descriptor of property ["+currClass+"::"+currProperty+"]");
			}else if(descriptor.getWriteMethod() == null && (accessibleType == AccessibleType.WRITEABLE || accessibleType == AccessibleType.READWRITEABLE)){
				log.error(info + "Failed to get Write Method property ["+currClass+"::"+currProperty+"]");
				throw new ConfigException("Failed to get Write Method property ["+currClass+"::"+currProperty+"]");
			}else if(descriptor.getReadMethod() == null && (accessibleType == AccessibleType.READABLE || accessibleType == AccessibleType.READWRITEABLE || i<length-1)){//For Import, read method is also need when it's not a leaf property
				log.error(info + "Failed to get Read Method property ["+currClass+"::"+currProperty+"]");
				throw new ConfigException("Failed to get Read Method property ["+currClass+"::"+currProperty+"]");
			}

			propertyDescriptors.add(descriptor);
			currClass = descriptor.getPropertyType().getName();
		}

		return propertyDescriptors;
	}

	public static PropertyDescriptor getPropertyDescriptor(String className, String propertyName)throws ConfigException{
		final String info = "\n IntrospectUtil::getPropertyDescriptor - ";

		Map<String, PropertyDescriptor> classPropertyDes = loadDescriptors(className);
		if(classPropertyDes == null){			
			log.error(info + "Failed to get Property Descriptors of class ["+className+"]");
			throw new ConfigException("Failed to get Property Descriptors of class ["+className+"]");
		}

		return classPropertyDes.get(propertyName);
	}

	public static Map<String, PropertyDescriptor> loadDescriptors(String className) throws ConfigException {
		String info = "\n IntrospectUtil::loadDescriptors - ";

		if(className == null || className.trim().length() == 0)
			return null;

		Map<String, PropertyDescriptor> map = propertyDescriptorCache.get(className);
		if(map != null){
			return map;
		}else{
			map = new HashMap<String, PropertyDescriptor>();
		}

		PropertyDescriptor[] descriptors = null;		
		try{
			Class recordClass = Class.forName(className);

			descriptors = Introspector.getBeanInfo(recordClass).getPropertyDescriptors();				
		}catch(ClassNotFoundException e){
			log.error(info + "Failed to find class ["+className+"]", e);
			throw new ConfigException("Failed to find class ["+className+"]", e);
		}catch(Exception e){
			log.error(info + "Failed to get Property Descriptors of class ["+className+"]", e);
			throw new ConfigException("Failed to get Property Descriptors of class ["+className+"]", e);
		}

		if(descriptors == null)//unexpected
			return null;

		for (PropertyDescriptor descriptor : descriptors){
			map.put(descriptor.getName(), descriptor);
		}

		propertyDescriptorCache.put(className, map);

		return map;
	}

	public static boolean isTypeMatch(Class<?> superClass, Class<?> subClass){
		if(superClass == null || subClass == null)
			return false;

		return superClass.isAssignableFrom(subClass);
	}

	private static enum AccessibleType{
		READABLE,
		WRITEABLE,
		READWRITEABLE
	}
//	public static void main(String[] args) throws ConfigException{
//		introspectReadWriteableProperty("filebinding.model.AdvancedBean","people.name");
//	}
}
