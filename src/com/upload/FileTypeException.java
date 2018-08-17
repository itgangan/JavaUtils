package com.upload;
/**
 * 文件类型不合法异常
 * 
 * @author ganxiangyong
 * @date 2017-03-30
 *
 */
public class FileTypeException extends RuntimeException {

	private String msg; // 异常对应的描述信息
	private static final long serialVersionUID = 1L;

	public FileTypeException() {
		super();
	}

	public FileTypeException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
