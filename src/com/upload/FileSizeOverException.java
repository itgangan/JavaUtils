package com.upload;

/**
 * 文件大小超过限制异常
 * 
 * @author ganxiangyong
 * @date 2017-03-30
 *
 */
public class FileSizeOverException extends RuntimeException {

	private String msg; // 异常对应的描述信息
	private static final long serialVersionUID = 1L;

	public FileSizeOverException() {
		super();
	}

	public FileSizeOverException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
