package com.javautils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
	public static <T> Object reflectInvocation(Class<T> tClass,
			String methodName, Object[] args, Class<T>[] argsTypes)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
	
		Method m = tClass.getDeclaredMethod(methodName, argsTypes);
		m.setAccessible(true);

		return m.invoke(tClass.newInstance(), args);
	}
}
