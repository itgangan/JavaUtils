package com.wanmei.sns.mq;

import java.util.Properties;

import com.wanmei.sns.util.ConfigLoader;

public class MQConfig {

	public static String SERVER_URL;

	public static int MAX_CONNECTION;
	public static int MAXIMUM_ACTIVE;
	public static int IDLE_TIMEOUT;
	public static boolean BLOCK_IF_SESSION_POOL_IS_FULL;
	public static long EXPIRE_TIMEOUT;

	// 设置连接的最大连接数
	public static int DEFAULT_MAX_CONNECTIONS;
	// 设置每个连接中使用的最大活动会话数
	public static int DEFAULT_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION;
	// 线程池数量
	public static int DEFAULT_THREAD_POOL_SIZE;
	// 强制使用同步返回数据的格式
	public static boolean DEFAULT_USE_ASYNC_SEND_FOR_JMS;
	// 是否持久化消息
	public static boolean DEFAULT_IS_PERSISTENT;
	// 连接地址
	public static String BROKER_URL;
	// 队列名称
	public static String QUEUE_NAME;
	// 用户名
	public static String PASS_WORD;
	// 密码
	public static String USER_NAME;
	// 队列预取策略
	public static int DEFAULT_QUEUE_PREFETCH;

	static {
		Properties properties = ConfigLoader.loadProperties("activemq_conf.properties");

		SERVER_URL = properties.getProperty("server_url").trim();
		MAX_CONNECTION = Integer.parseInt(properties.getProperty("maxConnections"));
		MAXIMUM_ACTIVE = Integer.parseInt(properties.getProperty("maximumActive"));
		IDLE_TIMEOUT = Integer.parseInt(properties.getProperty("idleTimeout"));
		BLOCK_IF_SESSION_POOL_IS_FULL = Boolean.parseBoolean(properties.getProperty("blockIfSessionPoolIsFull"));
		EXPIRE_TIMEOUT = Long.parseLong(properties.getProperty("expireTimeout"));

		DEFAULT_MAX_CONNECTIONS = Integer.parseInt(properties.getProperty("DEFAULT_MAX_CONNECTIONS"));
		DEFAULT_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION = Integer.parseInt(properties.getProperty("DEFAULT_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION"));
		DEFAULT_THREAD_POOL_SIZE = Integer.parseInt(properties.getProperty("DEFAULT_THREAD_POOL_SIZE"));
		DEFAULT_USE_ASYNC_SEND_FOR_JMS = Boolean.valueOf(properties.getProperty("DEFAULT_USE_ASYNC_SEND_FOR_JMS"));
		DEFAULT_IS_PERSISTENT = Boolean.valueOf(properties.getProperty("DEFAULT_IS_PERSISTENT"));
		BROKER_URL = properties.getProperty("BROKER_URL").trim();
		QUEUE_NAME = properties.getProperty("QUEUE_NAME").trim();
		PASS_WORD = properties.getProperty("PASS_WORD").trim();
		USER_NAME = properties.getProperty("USER_NAME").trim();
		DEFAULT_QUEUE_PREFETCH = Integer.parseInt(properties.getProperty("DEFAULT_QUEUE_PREFETCH"));
	}

}
