package com.wanmei.sns.mq.listener;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import com.wanmei.sns.mq.JMSProducer;
import com.wanmei.sns.mq.msg.CreateGroupMsg;
import com.xunbao.mall.bean.business.ContestInfo;
import com.xunbao.mall.service.ServiceFactory;
import com.xunbao.sns.constants.CommonConstants;

public class CreateGroupListener implements MessageListener {

	private static Logger logger = Logger.getLogger(CreateGroupMsg.class);
	private static final int RETRY_MAX_TIMES = 5;

	@Override
	public void onMessage(Message message) {
		if (message instanceof ActiveMQObjectMessage) {
			ActiveMQObjectMessage acmessage = (ActiveMQObjectMessage) message;
			try {
				Object object = acmessage.getObject();
				if (object instanceof CreateGroupMsg) {
					CreateGroupMsg msg = (CreateGroupMsg) object;
					if (msg.getWaitid() == 0) {
						long waitid = ServiceFactory.getMerchantService().getCreateGroupWaitid(msg);
						if (waitid == 0) {
							// logger.warn("getWaitId fail. msg:" + msg);
						} else {
							msg.setWaitid(waitid);
						}
						JMSProducer.getInstance().send(CommonConstants.CREATE_GROUP_QUEUE, msg);
					} else if (msg.getWaitid() > 0) {
						if (msg.getGroupid() > 0) {
							handMsg(msg);
						} else {
							long groupid = 0;
							try {
								groupid = ServiceFactory.getMerchantService().getCreateGroupGroupId(msg);
							} catch (Exception e) {
								try {
									TimeUnit.SECONDS.sleep(1);
								} catch (InterruptedException e1) {
									logger.error(e1);
								}
								logger.error("getGroupId hessian fail. msg:" + msg + ",e:" + e);
							}
							if (groupid > 0) {
								msg.setGroupid(groupid);
								handMsg(msg);
							} else {
								msg.autoIncrement();
								if (msg.getCount() < 5000) {
									try {
										TimeUnit.SECONDS.sleep(1);
									} catch (InterruptedException e1) {
										logger.error(e1);
									}
									JMSProducer.getInstance().send(CommonConstants.CREATE_GROUP_QUEUE, msg);
								} else {
									logger.error("getGroupId retry times > 5000. msg:" + msg);
								}

							}
						}
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}

		}
	}

	// private int consumerMsg(CreateGroupMsg msg) {
	// int retrySleepMillis = 1000;// 1秒重试
	// int retryTimes = 0;
	//
	// do {
	// int r = handMsg(msg);
	// if (r != 0) {
	// int sleepMillis = retrySleepMillis * (1 << retryTimes);
	// try {
	// logger.info("consumerMsg retry " + (retryTimes + 1));
	// Thread.sleep(sleepMillis);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// } while (++retryTimes < RETRY_MAX_TIMES);
	//
	// }

	private int handMsg(CreateGroupMsg msg) {
		int result = -1;
		if ("contest".equals(msg.getGroupNote())) {
			ContestInfo contest = ServiceFactory.getContestService().getContestInfo(msg.getContestid());
			if (contest != null) {
				contest.setGroupid(msg.getGroupid());
				result = ServiceFactory.getContestService().updateContestInfo(contest);
			}
		} else if ("badminton".equals(msg.getGroupNote())) {
			result = ServiceFactory.getMerchantService().addGroupid2OrgInfo(msg.getOrgid(), msg.getGroupid());
		}
		logger.info("updateGroupid r:" + result + "msg:" + msg);
		return result;
	}
}
