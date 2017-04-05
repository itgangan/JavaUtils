package com.javautils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.Test;

/**
 * 测试时，需要在D盘准备好2张图片2个文件：a.jpg,b.jpg,a.rar,a.txt
 * 
 * @author ganxiangyong
 * @date 2015年1月21日 下午4:55:49
 */
public class MailUtilsTest {

	private String receivers = "455036379@qq.com,773720699@qq.com";
	private String subject = "测试";

	// 测试发送html格式的正文
	@Test
	public void testSendEmail() throws MessagingException {
		String content = "<span style='color:green'>第一行绿色</span><br /><span style='color:red'>第二行红色</span><br />";
		MailUtils.sendEmail(receivers, subject, content);
	}

	// 测试发送带附件的邮件(附件为文件路径)
	@Test
	public void testSendEmail2(){
		String content = "<span style='color:green'>第一行绿色</span><br /><span style='color:red'>第二行红色</span><br />";
		String[] affixs = new String[] { "F:/a.txt", "F:/a.rar" };
		try {
			MailUtils.sendEmail(receivers, subject, content, affixs);
		} catch (MessagingException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	// 测试发送带附件的邮件(附件为文件)
	@Test
	public void testSendEmail3() {
		String content = "<span style='color:green'>第一行绿色</span><br /><span style='color:red'>第二行红色</span><br />";
		List<File> affixs = Arrays.asList(new File("F:/a.txt"), new File(
				"F:/a.rar"));
		try {
			MailUtils.sendEmail(receivers, subject, content, affixs);
		} catch (MessagingException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	// 发送带附件并且邮件正文内容中有图片的邮件
	@Test
	public void testSendEmail4(){
		String content = "<span style='color:green'>第一行绿色</span><br /><span style='color:red'>第二行红色</span><br />第一张图a.jpg<img src='cid:a.jpg'><br />第二张图b.jpg<img src='cid:b.jpg'>";
		List<File> affixs = Arrays.asList(new File("D:/a.txt"), new File(
				"D:/a.rar"));
		List<String> pics = Arrays.asList("D:/a.jpg", "D:/b.jpg");
		try {
			MailUtils.sendEmail(receivers, subject, content, affixs, pics);
		} catch (MessagingException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
