package com.javautils;

import java.util.LinkedList;

/**
 * 循环列表，当超过设置的容量时，自动清除掉最早的元素
 * 
 * @author ganxiangyong
 * @date 2015年4月28日 下午5:00:19
 */
public class CircularList<T> {

	private LinkedList<T> list = new LinkedList<T>();
	private static int COLLECTION_SIZE = 10; // 默认容量为10

	public CircularList(int size) {
		if (size > 0) {
			COLLECTION_SIZE = size;
		}
	}

	/**
	 * 将指定对象添加到容器列表头，如果容器超过了指定容量，那么将删除容器列表尾的元素
	 * 
	 * @param t
	 *            要添加的元素
	 */
	public synchronized void add(T t) {
		if (list.size() >= COLLECTION_SIZE) {
			list.pollLast();
		}

		list.offerFirst(t);
	}

	/**
	 * 从容量中删除该元素
	 * 
	 * @param t
	 *            要删除的元素
	 */
	public synchronized void remove(T t) {
		list.remove(t);
	}

	/**
	 * 获取容器中的所有元素
	 * 
	 * @return
	 */
	public LinkedList<T> getList() {
		return list;
	}

}
