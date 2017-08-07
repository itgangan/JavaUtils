package com.javautils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

	/**
	 * reflectInvocation:反射调用某个方法
	 * 
	 * @param tClass
	 *            calss对象
	 * @param methodName
	 *            要调用的方法名
	 * @param args
	 *            方法参数列表
	 * @param argsTypes
	 *            方法参数class列表
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> Object reflectInvocation(Class<T> tClass, String methodName, Object[] args, Class<T>[] argsTypes) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {

		Method m = tClass.getDeclaredMethod(methodName, argsTypes);
		m.setAccessible(true);

		return m.invoke(tClass.newInstance(), args);
	}

	/**
	 * 反射获取某个对象的值
	 * 
	 * @param obj
	 *            对象
	 * @param fieldName
	 *            属性名
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		try {
			Class<?> clazz = obj.getClass();
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	/**
	 * 获取一个类的静态常量
	 * 
	 * @param clazz
	 * @return
	 */
	public static Map<String, Object> getFieldValue(Class<?> clazz) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			try {
				map.put(field.getName(), field.get(null));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
