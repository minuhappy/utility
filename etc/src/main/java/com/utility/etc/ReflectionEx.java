package com.utility.etc;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang.reflect.FieldUtils;

public class ReflectionEx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Object setValue(Class<?> clazz) {
		Object instance = null;
		Class<?> entityClass = clazz;
		try {
			instance = entityClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		do {
			try {
				Field[] fields = entityClass.getDeclaredFields();
				for (Field field : fields) {
					String fieldName = field.getName();
					Field accessField = FieldUtils.getDeclaredField(entityClass, fieldName, true);
					Object value = accessField.get(instance);
					
					Class<?> fieldType = field.getType();

					if (fieldType.isAssignableFrom(Long.class)) {
						// value = ValueUtil.toLong(value);
					} else if (fieldType.isAssignableFrom(Integer.class)) {
						// value = ValueUtil.toInteger(value);
					} else if (fieldType.isAssignableFrom(Double.class)) {
						// value = ValueUtil.toDouble(value);
					} else if (fieldType.isAssignableFrom(Date.class)) {
						value = new Date();
					}

					accessField.set(instance, value);
				}

				entityClass = entityClass.getSuperclass();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (!entityClass.isAssignableFrom(Object.class));
		return instance;
	}
	
	/**
	 * 객체 내의 Field 값 가져오기를 실행.
	 * 
	 * @param instance
	 * @param sourceClass
	 * @param fieldName
	 * @return
	 */
	public Object getFieldValue(Object instance, Class<?> sourceClass, String fieldName) {
		Field field = FieldUtils.getField(sourceClass, fieldName, true);
		//Field field = FieldUtils.getDeclaredField(sourceClass, fieldName, true);
		if (field == null) {
			return null;
		}

		Object value = null;
		try {
			return field.get(instance);
		} catch (Exception e) {
			return value;
		}
	}
}
