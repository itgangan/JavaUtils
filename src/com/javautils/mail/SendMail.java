package com.javautils.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.javautils.PropertyUtils;


/**
 * 发送邮件
 * 
 * @author ganxiangyong
 * @date 2015年11月12日 下午4:56:07
 */
class SendMail {
	private static Properties properties = null;

	private static String host;
	private static String port;
	public static String sender;
	private static String username;
	private static String password;

	private static boolean validate = true;

	private static Session session = null;

	static {
		String fileName = "email-config.properties";
		String path = MailUtils.class.getResource("/").getPath();
		properties = PropertyUtils.loadProperties(path + fileName);

		host = PropertyUtils.getValueByKey(properties, "email.host");
		port = PropertyUtils.getValueByKey(properties, "email.port");
		username = PropertyUtils.getValueByKey(properties, "email.username");
		password = PropertyUtils.getValueByKey(properties, "email.password");
		sender = username;

		session = session();
	}

	private SendMail() {
	}

	/**
	 * 发送邮件
	 * 
	 * @param m
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void sendEmail(MailModel m) throws MessagingException,
			IOException {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, m.getReceivers());
		msg.setSubject(m.getSubject());

		// 添加文本内容
		MimeMultipart mmp = new MimeMultipart();
		MimeBodyPart textBp = new MimeBodyPart();
		textBp.setText(m.getContent(), "UTF-8", "html");
		mmp.addBodyPart(textBp);

		// 向超文本内容加填充图片
		if (m.getPics() != null && m.getPics().size() > 0) {
			for (String pic : m.getPics()) {
				MimeBodyPart picBp = new MimeBodyPart();
				DataHandler dh = new DataHandler(new FileDataSource(pic));
				picBp.setDataHandler(dh);
				picBp.setContentID(dh.getName());
				mmp.addBodyPart(picBp);
			}
		}

		// 添加附件
		if (m.getAffixs() != null && m.getAffixs().size() > 0) {
			for (File affix : m.getAffixs()) {
				MimeBodyPart bp = new MimeBodyPart();
				bp.attachFile(affix);
				mmp.addBodyPart(bp);
			}
		}

		msg.setContent(mmp);
		send(session, msg);
	}

	// ================================================================
	// private

	// 发送邮件
	private static void send(Session session, MimeMessage msg)
			throws MessagingException {
		Transport transport = session.getTransport("smtp");
		transport.connect(host, username, password);
		Transport.send(msg);
		transport.close();
	}

	// 生成Session
	private static Session session() {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", validate ? "true" : "false");

		Authenticator auth = null;
		if (validate) {
			auth = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};
		}

		Session session = Session.getInstance(props, auth);
		return session;
	}

}
