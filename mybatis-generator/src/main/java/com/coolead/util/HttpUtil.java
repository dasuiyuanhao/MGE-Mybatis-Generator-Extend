package com.coolead.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @功能描述:HTTP请求辅助工具类
 **/
@Component
public class HttpUtil {
	public final static String ACCEPT_JSON = "application/json";
	private final static Log logger = LogFactory.getLog(HttpUtil.class);

	/**
	 * @功能描述: 以POST方式请求外部URL
	 * @return 以JSON字符串的方式返回请结果
	 */
	public static String post(String url, Map<String, String> params) throws Exception {
		return post(url,params,null);
	}

	/**
	 * @功能描述: 以POST方式请求外部URL
	 * @param headers 请求的http头
	 * @return 以JSON字符串的方式返回请结果
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			logger.info("请求地址为:" + url);
			httpPost.setHeader("charset", "UTF-8");
			if(null!=headers){
				for(Entry<String,String> entry:headers.entrySet()){
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			List<NameValuePair> paires = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : params.entrySet()) {
//				paires.add(new BasicNameValuePair(entry.getKey(), entry.getValue() != null ? URLDecoder.decode(entry.getValue(), "UTF-8") : ""));//不应该decode,否则如果有%或者+会出错
				paires.add(new BasicNameValuePair(entry.getKey(), entry.getValue() != null ? entry.getValue() : ""));
			}
			HttpEntity entity = new UrlEncodedFormEntity(paires, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse res = client.execute(httpPost);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("请求出现异常，状态码为：" + status);
			}
			HttpEntity entityRes = res.getEntity();
			return EntityUtils.toString(entityRes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.POST异常UnsupportedEncodingException:", e);
			throw e;
		} catch (ClientProtocolException e) {
			logger.error("HttpUtil.POST异常ClientProtocolException:", e);
			throw e;
		} catch (IOException e) {
			logger.error("HttpUtil.POST异常IOException:", e);
			throw e;
		} catch (Exception e) {
			logger.error("HttpUtil.POST异常:", e);
			throw e;
		} finally {
			httpPost.releaseConnection();
		}

	}
	
	/**
	 * @功能描述: 以GET方式请求外部URL
	 */
	public static String get(String url,  Map<String, String> headers) {
		HttpClient client = HttpClients.createDefault();
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet( url);
			httpGet.setHeader("charset", "UTF-8");

			if(null!=headers){
				for(Entry<String,String> entry:headers.entrySet()){
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}

			HttpResponse res = client.execute(httpGet);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("请求出现异常，状态码为：" + status);
			}
			HttpEntity entityRes = res.getEntity();
			return EntityUtils.toString(entityRes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚UnsupportedEncodingException'}";
		} catch (ClientProtocolException e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚ClientProtocolException'}";
		} catch (IOException e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚IOException'}";
		} finally {
			httpGet.releaseConnection();
		}
	}

	public static String get(String url) {
		HttpClient client = HttpClients.createDefault();
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet( url);
			httpGet.setHeader("charset", "UTF-8");
			HttpResponse res = client.execute(httpGet);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("请求出现异常，状态码为：" + status);
			}
			HttpEntity entityRes = res.getEntity();
			return EntityUtils.toString(entityRes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚UnsupportedEncodingException'}";
		} catch (ClientProtocolException e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚ClientProtocolException'}";
		} catch (IOException e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'GET请求抛锚IOException'}";
		} finally {
			httpGet.releaseConnection();
		}
	}
	
	public static String postBody(String type,String url,String body, Map<String, String> headers) throws Exception{
		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = null;
		try{
			httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", type);
			httpPost.setHeader("Content-Type", type+";charset=utf-8");
			if(null!=headers){
				for(Entry<String,String> entry:headers.entrySet()){
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			BasicHttpEntity requestBody = new BasicHttpEntity();
	        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
	        requestBody.setContentLength(body.getBytes("UTF-8").length);
	        httpPost.setEntity(requestBody);
	        // 执行客户端请求
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}catch (ClientProtocolException e) {
			logger.error("HttpUtil.postBody异常:", e);
			return "{success:'0',msg:'Post请求抛锚ClientProtocolException'}";
		} catch (IOException e) {
			logger.error("HttpUtil.GET异常:", e);
			return "{success:'0',msg:'Post请求抛锚IOException'}";
		} finally {
			httpPost.releaseConnection();
		}
	}
	//post请求发json
	public static String postJson(String url, Map<String,String> headers, String params) throws IOException {
		logger.info("postJson请求的url为:"+url+",header为："+JSONObject.toJSONString(headers)+",参数为："+JSONObject.toJSONString(params));

		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try{
			httpPost.setHeader("charset", "UTF-8");

			if(headers != null) {
				headers.keySet().forEach(key->httpPost.setHeader(key,headers.get(key)));
			}
			// 默认超时时间为15s。
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
			httpPost.setConfig(requestConfig);

			StringEntity entity = new StringEntity(params,"UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);

			HttpResponse res = client.execute(httpPost);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}

			HttpEntity entityRes = res.getEntity();

			String result = EntityUtils.toString(entityRes, "UTF-8");
			logger.info("当前请求的返回结果为："+result);
			return result;
		}catch (ClientProtocolException e) {
			logger.error("HttpUtil.postJson:", e);
			return "{success:'0',msg:'Post请求抛锚ClientProtocolException'}";
		} catch (IOException e) {
			logger.error("HttpUtil.postJson异常:", e);
			return "{success:'0',msg:'Post请求抛锚IOException'}";
		} finally {
			httpPost.releaseConnection();
		}
	}
	
	public static String put(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		HttpClient client = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);
		try {
			logger.debug("请求地址为:" + url);
			httpPut.setHeader("charset", "UTF-8");

			if(headers != null) {
				headers.keySet().forEach(key->httpPut.setHeader(key,headers.get(key)));
			}
			// 默认超时时间为15s。
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
			httpPut.setConfig(requestConfig);
	
			StringEntity entity = new StringEntity(JSON.toJSONString(params),"UTF-8");
			entity.setContentType("application/json");
			httpPut.setEntity(entity);
			HttpResponse res = client.execute(httpPut);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("请求出现异常，状态码为：" + status);
			}
			HttpEntity entityRes = res.getEntity();
			return EntityUtils.toString(entityRes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.POST异常:", e);
			throw e;
		} catch (ClientProtocolException e) {
			logger.error("HttpUtil.POST异常:", e);
			throw e;
		} catch (IOException e) {
			logger.error("HttpUtil.POST异常:", e);
			throw e;
		} finally {
			httpPut.releaseConnection();
		}

	}
	
	public static String put(String url, String params, Map<String, String> headers) throws Exception {
		HttpClient client = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);
		try {
			logger.debug("请求地址为:" + url);
			httpPut.setHeader("charset", "UTF-8");

			if(headers != null) {
				headers.keySet().forEach(key->httpPut.setHeader(key,headers.get(key)));
			}
			// 默认超时时间为15s。
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
			httpPut.setConfig(requestConfig);
	
			StringEntity entity = new StringEntity(params, "UTF-8");
			entity.setContentType("application/json");
			httpPut.setEntity(entity);
			HttpResponse res = client.execute(httpPut);
			int status = res.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300) {
				throw new ClientProtocolException("请求出现异常，状态码为：" + status);
			}
			HttpEntity entityRes = res.getEntity();
			return EntityUtils.toString(entityRes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("HttpUtil.POST异常:", e);
			throw e;
		} catch (ClientProtocolException e) {
			logger.error("HttpUtil.POST异常:", e);
			throw e;
		} catch (IOException e) {
			logger.error("HttpUtil.POST异常:", e);
			throw e;
		} finally {
			httpPut.releaseConnection();
		}

	}

}
