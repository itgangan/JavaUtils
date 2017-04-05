package com.javautils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LockUtils {

	private LockUtils() {
		ScheduledExecutorService executeService = Executors.newSingleThreadScheduledExecutor();
		executeService.scheduleAtFixedRate(new clearLock(), 1, 5, TimeUnit.MINUTES);
		executeService.submit(new clearLock());
	}

	private static final ConcurrentHashMap<Long, Lock> map = new ConcurrentHashMap<Long, Lock>();

	/**
	 * 获取某个id所对应的锁
	 * 
	 * @param id
	 * @return
	 */
	public static Lock getLock(long id) {
		Lock obj = map.get(id);
		if (obj == null) {
			obj = map.putIfAbsent(id, new Lock(System.currentTimeMillis()));
		}
		return obj;
	}

	/**
	 * 删除id所对应的锁
	 * 
	 * @param id
	 */
	public static void delLock(long id) {
		map.remove(id);
	}

	// 锁对象
	private static class Lock {
		private long crateTime;

		public long getCrateTime() {
			return crateTime;
		}

		public Lock(long createTime) {
			this.crateTime = createTime;
		}
	}

	// 清理过期锁的定时器
	private class clearLock implements Runnable {

		@Override
		public void run() {
			for (Long id : map.keySet()) {
				Lock lock = map.get(id);
				if (lock != null) {
					long createTime = lock.getCrateTime();
					long expiredTime = createTime + 1800; // 半小时过期
					if (System.currentTimeMillis() > expiredTime) {
						delLock(id);
					}
				}
			}
		}
	}

}
