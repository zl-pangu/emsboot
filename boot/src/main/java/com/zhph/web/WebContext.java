package com.zhph.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于封装本地线程的 Request 与 Response 对象
 * 
 * @ClassName: WebContext
 * @author lichangchun@zhphfinance.com
 * @date 2017年8月23日 下午4:57:10
 *
 */
public class WebContext {
	private static ThreadLocal<HttpServletRequest> _request = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> _response = new ThreadLocal<HttpServletResponse>();

	public static void setRequest(HttpServletRequest request) {
		_request.set(request);
	}
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = _request.get();
		return request;
	}
	public static void removeRequest() {
		_request.remove();
	}

	public static void setResponse(HttpServletResponse response) {
		_response.set(response);
	}

	public static HttpServletResponse getResponse() {
		HttpServletResponse response = _response.get();
		return response;
	}

	public static void removeResponse() {
		_response.remove();
	}
}