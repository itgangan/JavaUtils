package com.javautils;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

/**
 * @author ganxiangyong
 * @date 2015年1月22日 上午10:08:42
 * 
 */
public class MailUtils {
	private static Properties properties = null;

	private static String host;
	private static String port;
	private static String sender;
	private static String username;
	private static String password;

	private static boolean validate = true;

	private static Session session = null;
	static {
		String path = "src/email-config.properties";
		properties = PropertyUtils.loadProperties(path);

		host = PropertyUtils.getValueByKey(properties, "email.host");
		port = PropertyUtils.getValueByKey(properties, "email.port");
		username = PropertyUtils.getValueByKey(properties, "email.username");
		password = PropertyUtils.getValueByKey(properties, "email.password");
		sender = username;
		
		session = genSession();
	}

	/**
	 * 发送普通的文本邮件，支持html格式
	 * 
	 * @param receivers
	 *            收件人地址，多个收件人以逗号分隔（例如：123456@qq.com,hello@.163.com）
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @throws MessagingException
	 */
	public static void sendEmail(String receivers, String subject,
			String content) throws MessagingException {
		MimeMessage msg = new MimeMessage(session);
		msg.setSubject(subject);
		msg.setText(content, "UTF-8", "html");
		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, receivers);

		send(session, msg);
	}

	/**
	 * 发送带附件的邮件
	 * 
	 * @param receivers
	 *            收件人地址，多个收件人以逗号分隔（例如：123456@qq.com,hello@.163.com）
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param affixs
	 *            邮件附件文件路径列表
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void sendEmail(String receivers, String subject,
			String content, String[] affixs) throws MessagingException,
			IOException {
		MimeMultipart mp = new MimeMultipart();

		// 添加文本内容
		MimeBodyPart textBp = new MimeBodyPart();
		textBp.setText(content, "UTF-8", "html");
		mp.addBodyPart(textBp);

		// 添加附件
		if (affixs != null && affixs.length > 0) {
			for (String affix : affixs) {
				MimeBodyPart bp = new MimeBodyPart();
				bp.attachFile(affix);
				mp.addBodyPart(bp);
			}
		}

		MimeMessage msg = new MimeMessage(session);
		msg.setSubject(subject);
		msg.setContent(mp);
		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, receivers);

		send(session, msg);
	}

	/**
	 * 发送带附件的邮件
	 * 
	 * @param receivers
	 *            收件人地址，多个收件人以逗号分隔（例如：123456@qq.com,hello@.163.com）
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param affixs
	 *            邮件附件文件列表
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void sendEmail(String receivers, String subject,
			String content, List<File> affixs) throws MessagingException,
			IOException {

		MimeMultipart mp = new MimeMultipart();

		// 添加文本内容
		MimeBodyPart textBp = new MimeBodyPart();
		textBp.setText(content, "UTF-8", "html");
		mp.addBodyPart(textBp);

		// 添加附件
		if (affixs != null && affixs.size() > 0) {
			for (File affix : affixs) {
				MimeBodyPart bp = new MimeBodyPart();
				bp.attachFile(affix);
				mp.addBodyPart(bp);
			}
		}

		MimeMessage msg = new MimeMessage(session);
		msg.setSubject(subject);
		msg.setContent(mp);
		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, receivers);

		send(session, msg);
	}

	/**
	 * 发送带附件并且邮件正文内容中有图片的邮件
	 * 
	 * @param receivers
	 *            收件人地址，多个收件人以逗号分隔（例如：123456@qq.com,hello@.163.com）
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容（如果内容包含要的图片，那么书写格式应该为：<img src='cid:abc.jpg' />）
	 * @param affixs
	 *            邮件附件文件列表
	 * @param pics
	 *            需要在邮件正文内容中展示的图片路径
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void sendEmail(String receivers, String subject,
			String content, List<File> affixs, List<String> pics)
			throws MessagingException, IOException {

		MimeMultipart mp = new MimeMultipart();

		// 添加文本内容
		MimeBodyPart textBp = new MimeBodyPart();
		textBp.setText(content, "UTF-8", "html");
		mp.addBodyPart(textBp);

		// 向超文本内容加填充图片
		if (pics != null && pics.size() > 0) {
			for (String pic : pics) {
				MimeBodyPart bp = new MimeBodyPart();
				DataHandler dh = new DataHandler(new FileDataSource(pic));
				bp.setDataHandler(dh);
				bp.setContentID(dh.getName());
				mp.addBodyPart(bp);
			}
		}
		// 添加附件
		if (affixs != null && affixs.size() > 0) {
			for (File affix : affixs) {
				MimeBodyPart bp = new MimeBodyPart();
				bp.attachFile(affix);
				mp.addBodyPart(bp);
			}
		}

		MimeMessage msg = new MimeMessage(session);
		msg.setSubject(subject);
		msg.setContent(mp);
		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, receivers);

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
	private static Session genSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", validate ? "true" : "false");

		Authenticator auth = null;
		if (validate) {
			auth = new MyAuthenticator(username, password);
		}
		Session session = Session.getInstance(props, auth);

		return session;
	}

}

// ===================================================================
// Authenticator

class MyAuthenticator extends Authenticator {
	private String username;
	private String password;

	public MyAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}
