package com.upload;

/**
 * 非法请求异常
 * 
 * @author ganxiangyong
 * @date 2017-03-30
 *
 */
public class RequestException extends RuntimeException {

	private String msg; // 异常对应的描述信息
	private static final long serialVersionUID = 1L;

	public RequestException() {
		super();
	}

	public RequestException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
