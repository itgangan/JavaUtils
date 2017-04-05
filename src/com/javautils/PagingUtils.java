package com.javautils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ganxiangyong
 * @date 2015年1月28日 下午2:23:52
 */
public class PagingUtils {

	/**
	 * 分页获取数据
	 * 
	 * @param result
	 *            所有的数据集合
	 * @param pageNum
	 *            请求的页码
	 * @param pageSize
	 *            每页条数
	 * @param comparator
	 *            比较器用于排序，如果没有则传为空
	 * @return
	 */
	public static <T> List<T> paging(List<T> result, int pageNum, int pageSize,
			Comparator<T> comparator) {
		if (pageNum < 1 || pageSize < 1 || result == null || result.isEmpty()) {
			return new ArrayList<T>();
		}

		if (comparator != null) {
			Collections.sort(result, comparator);
		}

		// paging return
		int total = result.size();
		int fromIndex = pageSize * (pageNum - 1);
		fromIndex = fromIndex >= total ? 0 : fromIndex;
		int toIndex = fromIndex + pageSize;
		toIndex = toIndex > total ? total : toIndex;

		// fromIndex > toIndex,return empty list
		if (fromIndex > toIndex) {
			return new ArrayList<T>();
		} else {
			return result.subList(fromIndex, toIndex);
		}
	}

	/**
	 * calcTotalPage:计算总页数
	 * 
	 * @param pageSize
	 *            每页数据条数
	 * @param totalCount
	 *            总条数
	 * @return 总页数
	 */
	public static int calcTotalPage(int pageSize, int totalCount) {
		if (totalCount < 1) {
			return 1;
		}
		int total = totalCount / pageSize;
		if (totalCount % pageSize == 0) {
			return total;
		} else {
			return total + 1;
		}
	}

}
