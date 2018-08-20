package com.wanmei.sns.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.commons.lang3.StringUtils;

import com.wanmei.sns.mq.MQConfig;

public class JMSConsumer implements ExceptionListener {

	public final static int DEFAULT_QUEUE_PREFETCH = 10;

	private int queuePrefetch = DEFAULT_QUEUE_PREFETCH;

	private String brokerUrl;
	private String userName;
	private String password;
	private MessageListener messageListener;
	private Connection connection;
	private Session session;
	private String queue;

	public JMSConsumer(String brokerUrl, String queue, String userName, String password) {
		this.brokerUrl = brokerUrl;
		this.queue = queue;
		this.userName = userName;
		this.password = password;
	}

	private JMSConsumer(String queue) {
		this(MQConfig.BROKER_URL, queue, MQConfig.USER_NAME, MQConfig.PASS_WORD);
	}

	private JMSConsumer() {
		this(MQConfig.BROKER_URL, MQConfig.QUEUE_NAME, MQConfig.USER_NAME, MQConfig.PASS_WORD);
	}

	public static synchronized JMSConsumer getInstance(String queue) {
		if (StringUtils.isNotBlank(queue)) {
			return new JMSConsumer(queue);
		} else {
			return new JMSConsumer();
		}
	}

	/**
	 * 初始化MQ消费者（调用前，请注意调用JMSConsumer.setMessageListener设置消息监听器）
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.userName, this.password, this.brokerUrl);
		connection = connectionFactory.createConnection();
		ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
		prefetchPolicy.setQueuePrefetch(queuePrefetch);
		((ActiveMQConnection) connection).setPrefetchPolicy(prefetchPolicy);
		connection.setExceptionListener(this);
		connection.start();
		session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(this.queue);
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(this.messageListener);
	}

	public void shutdown() {
		try {
			if (session != null) {
				session.close();
				session = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onException(JMSException paramJMSException) {
		paramJMSException.printStackTrace();
	}

	public int getQueuePrefetch() {
		return queuePrefetch;
	}

	public void setQueuePrefetch(int queuePrefetch) {
		this.queuePrefetch = queuePrefetch;
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

}
