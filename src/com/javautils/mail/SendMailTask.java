package com.javautils.mail;

import java.io.IOException;

import javax.mail.MessagingException;

/**
 * 创建发邮件的线程
 * 
 * @author ganxiangyong
 * @date 2015年11月12日 下午4:53:45
 */
class SendMailTask implements Runnable {
	private MailModel m = null;

	public SendMailTask(MailModel m) {
		this.m = m;
	}

	@Override
	public void run() {
		try {
			SendMail.sendEmail(m);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
