/**
 * 
 */
package com.utility.etc.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.reflect.FieldUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Minu.Kim
 */
public class JsonUtil {

	/**
	 * Convert Object To Pretty JSON String
	 * 
	 * @param item
	 * @return
	 */
	public static String toJsonString(Object item) {
		return toJsonString(item, true);
	}

	/**
	 * Convert Object To JSON String
	 * 
	 * @param item
	 * @param pretty
	 * @return
	 */
	public static String toJsonString(Object item, boolean pretty) {
		if (pretty) {
			return new GsonBuilder().setPrettyPrinting().create().toJson(item);
		} else {
			return new Gson().toJson(item);
		}
	}

	/**
	 * Convert JSON String To Object
	 * 
	 * @param jsonStr
	 * @param inputType
	 * @return
	 */
	public static <T> T jsonToObject(String jsonStr, Class<T> inputType) {
		return new Gson().fromJson(jsonStr, inputType);
	}

	/**
	 * Convert Object To JSON String
	 * 
	 * @param item
	 * @return
	 */
	public static String toUnderScoreJsonString(Object item) {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		return gson.toJson(item);
	}

	/**
	 * Convert JSON String To Object
	 * 
	 * @param jsonStr
	 * @param inputType
	 * @return
	 */
	public static <T> T underScoreJsonToObject(String jsonStr, Class<T> inputType) {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		return gson.fromJson(jsonStr, inputType);
	}

	/**
	 * json Content를 JSONArray로 파싱
	 * 
	 * @param content
	 * @return
	 */
	public static JSONArray parseJsonArray(String content) throws Exception {
		return (JSONArray) new JSONParser().parse(content);
	}

	public static <T> List<T> jsonArrayToObjectList(String content, Class<T> clazz) throws Exception {
		List<T> list = new ArrayList<T>();

		JSONArray jsonArray = parseJsonArray(content);
		for (int i = 0; i < jsonArray.size(); i++) {
			String jsonString = jsonArray.get(i).toString();
			T t = jsonToObject(jsonString, clazz);
			list.add(t);
		}

		return list;
	}

	/**
	 * Map Data를 객체에 Bind
	 * 
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static Object setValue(Class<?> clazz, Map<String, Object> map) throws Exception {
		Object instance = null;
		instance = clazz.newInstance();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Object value = map.get(fieldName);

			if (value != null)
				FieldUtils.writeDeclaredField(instance, fieldName, value, true);
		}

		return instance;
	}
}
