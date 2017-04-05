package com.javautils.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;

/**
 * 多线程处理发送邮件
 * 
 * @author ganxiangyong
 * @date 2015年1月22日 上午10:08:42
 * 
 */
public class MailUtils {
	private static ExecutorService executor = Executors.newFixedThreadPool(5);

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
		MailModel m = new MailModel();
		m.setReceivers(receivers);
		m.setSubject(subject);
		m.setContent(content);

		executor.submit(new SendMailTask(m));
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
		MailModel m = new MailModel();
		m.setReceivers(receivers);
		m.setSubject(subject);
		m.setContent(content);
		List<File> files = new ArrayList<File>();
		if (affixs != null) {
			for (String affix : affixs) {
				files.add(new File(affix));
			}
		}
		m.setAffixs(files);

		executor.submit(new SendMailTask(m));
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
		MailModel m = new MailModel();
		m.setReceivers(receivers);
		m.setSubject(subject);
		m.setContent(content);
		m.setAffixs(affixs);

		executor.submit(new SendMailTask(m));
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
			String content, List<String> affixs, List<String> pics)
			throws MessagingException, IOException {
		MailModel m = new MailModel();
		m.setReceivers(receivers);
		m.setSubject(subject);
		m.setContent(content);
		List<File> files = new ArrayList<File>();
		if (affixs != null) {
			for (String affix : affixs) {
				files.add(new File(affix));
			}
		}
		m.setAffixs(files);
		m.setPics(pics);

		executor.submit(new SendMailTask(m));
	}

}
