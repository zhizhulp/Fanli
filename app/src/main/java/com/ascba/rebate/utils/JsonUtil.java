package com.ascba.rebate.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
	private static Gson gson = new Gson();

	@SuppressWarnings("hiding")
	public static <T> T parseJson(String response, Class<T> clazz) {
		try {
			return gson.fromJson(response, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T parseJson(String response, Type type) {
		try {
			return gson.fromJson(response, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 把json 字符串转化成list
	 */
	public static <T>  List<T> stringToList(String json ,Class<T> cls  ){
		Gson gson = new Gson();
		List<T> list = new ArrayList<T>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for(final JsonElement elem : array){
			list.add(gson.fromJson(elem, cls));
		}
		return list ;
	}

	public static String toJson(Object object) {
		try {
			return gson.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}