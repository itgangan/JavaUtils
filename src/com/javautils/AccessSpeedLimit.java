package com.javautils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * 访问速度限制
 * 
 * @author ganxiangyong
 * @date 2017-5-19
 */
public class AccessSpeedLimit {
	// 访问配置对象，几秒允许访问几次
	private class LimitConfig {
		private int seconds; // 多少秒
		private int count; // 次数

		public LimitConfig(int seconds, int count) {
			if (seconds < 1 || count < 1) {
				throw new IllegalArgumentException("seconds and count less than 1");
			}
			this.seconds = seconds;
			this.count = count;
		}
	}

	// 访问记录对象，该key真实被访问的次数
	private class AccessRecord {
		private String key; // 目的是为了通过AccessRecord对象找到recordMap中的对象
		private long deadline; // 该key访问限制过期时间
		private int maxAccessCount; // 配置的最大访问次数
		private int accessCount; // 当前访问次数

		public AccessRecord(String key, LimitConfig limit) {
			this.key = key;
			this.deadline = System.currentTimeMillis() + (limit.seconds * 1000);
			this.maxAccessCount = limit.count;
			this.accessCount = 1;
		}

		public boolean isTimeOut() {
			return this.deadline < System.currentTimeMillis();
		}

		public boolean canAccess() {
			boolean isTimeOut = this.deadline < System.currentTimeMillis();
			if (!isTimeOut) {
				return accessCount++ <= maxAccessCount;
			}
			return true;
		}
	}

	// 访问速度限制配置
	private List<LimitConfig> limits = new ArrayList<LimitConfig>();

	// key与真实访问记录对象，一对多的关系是因为同一个key可以配置多种不同的限制策略（可以配置同时满足2秒内可访问3次，并且5秒内只可访问5）
	private HashMap<String, List<AccessRecord>> recordMap = new HashMap<String, List<AccessRecord>>();

	// pq的存在目的是为了将要过期的访问记录对象clear，避免开启一个后台线程来清理map中过期的对象
	private PriorityQueue<AccessRecord> pq = new PriorityQueue<AccessRecord>(256, new Comparator<AccessRecord>() {
		@Override
		public int compare(AccessRecord o1, AccessRecord o2) {
			return (int) (o1.deadline - o2.deadline);
		}
	});

	/**
	 * 配置访问速度（多少时间内可以访问多少次）
	 * 
	 * @param seconds
	 *            几秒内
	 * @param count
	 *            访问次数
	 */
	public void config(int seconds, int count) {
		limits.add(new LimitConfig(seconds, count));
		// 这里需要对limit排序，从而避免用户先配置（5,5）再配置（2,3）会使2秒内第4次访问的时候返回false，但是（5,5）的条件的实际访问次数会变成4次，其实用户只成功访问3次
		Collections.sort(limits, new Comparator<LimitConfig>() {
			@Override
			public int compare(LimitConfig o1, LimitConfig o2) {
				return o1.seconds - o2.seconds;
			}
		});
	}

	/**
	 * 是否允许操作
	 * 
	 * @param key
	 * @return
	 */
	public synchronized boolean canAccess(String key) {
		clearExpireKey();

		List<AccessRecord> recordList = recordMap.get(key);
		if (recordList == null) {
			recordList = initAccessRecord(key);
		}
		for (AccessRecord record : recordList) {
			if (!record.canAccess()) {
				return false;
			}
		}

		return true;
	}

	// 清除recordMap和pq中key过期的对象
	private void clearExpireKey() {
		AccessRecord record = pq.peek();
		while (record != null) {
			if (record.isTimeOut()) {
				// 从pq中清除掉已经过期的record
				AccessRecord timeOutRecord = pq.poll();
				// 如果当前record的key在recordMap中全部过期，清除map中key对应的记录
				clearMapWhenAllRecordExpired(timeOutRecord);

				record = pq.peek();
			} else {
				break;
			}
		}
	}

	// 如果record的key在map中的所有的记录都过期，则删除map中的key
	private void clearMapWhenAllRecordExpired(AccessRecord record) {
		List<AccessRecord> allRecords = recordMap.get(record.key);
		boolean allRecordTimeOut = true;
		if (allRecords != null) {
			for (AccessRecord accessRecord : allRecords) {
				allRecordTimeOut &= accessRecord.isTimeOut();
			}
			if (allRecordTimeOut) {
				recordMap.remove(record.key);
			}
		}
	}

	// 第一次访问，初始化访问记录
	private List<AccessRecord> initAccessRecord(String key) {
		List<AccessRecord> recordList = new ArrayList<AccessRecord>();
		for (LimitConfig limit : limits) {
			AccessRecord record = new AccessRecord(key, limit);
			recordList.add(record);
			pq.add(record);
		}
		recordMap.put(key, recordList);
		return recordList;
	}

	// 测试
	public static void main(String[] args) throws InterruptedException {
		AccessSpeedLimit sl = new AccessSpeedLimit();
		sl.config(5, 5);
		sl.config(2, 3);

		System.out.println(sl.canAccess("1"));
		System.out.println(sl.canAccess("1"));
		System.out.println(sl.canAccess("1"));
		System.out.println(sl.canAccess("1"));
		TimeUnit.SECONDS.sleep(3);
		System.out.println(sl.canAccess("1"));
		System.out.println(sl.canAccess("1"));
		System.out.println(sl.canAccess("1"));
		System.out.println(sl.canAccess("1"));
		TimeUnit.SECONDS.sleep(3);
		System.out.println(sl.canAccess("1"));
	}

}
