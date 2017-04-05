package com.javautils.mail;

import java.io.File;
import java.io.Serializable;
import java.util.List;

class MailModel implements Serializable {
	private static final long serialVersionUID = 6980964696915204640L;

	private String receivers; // 收件人地址，多个收件人以逗号分隔（例如：123456@qq.com,hello@.163.com）
	private String subject; // 主题
	private String content; // 邮件内容（如果内容包含要的图片，那么书写格式应该为：<img
							// src='cid:abc.jpg'/>）
	private List<File> affixs; // 附件
	private List<String> pics; // 当正文中有图片时，正文的输写方式为<img
								// src='cid:b.jpg'>，List<String>中的内容为："D:/b.jpg"

	public String getReceivers() {
		return receivers;
	}

	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<File> getAffixs() {
		return affixs;
	}

	public void setAffixs(List<File> affixs) {
		this.affixs = affixs;
	}

	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
	}
}
