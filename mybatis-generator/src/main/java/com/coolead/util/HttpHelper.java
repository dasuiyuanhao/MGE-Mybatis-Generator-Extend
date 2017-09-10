package com.coolead.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@SuppressWarnings("all")
public class HttpHelper {
	public static String post(String url, Map<String, String> params) throws ClientProtocolException, IOException {
		HttpClient client = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("charset", HTTP.UTF_8);

		// 默认超时时间为15s。
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
				.setConnectionRequestTimeout(150000).setStaleConnectionCheckEnabled(true).build();
		httpPost.setConfig(requestConfig);

		// 请求的参数信息传递
		List<NameValuePair> paires = new ArrayList<>();
		if (params != null) {
			Set<String> keys = params.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				//				paires.add(new BasicNameValuePair(key, URLDecoder.decode(params.get(key), "UTF-8")));

				String data = params.get(key);
				if (data != null) {
					data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
					data = data.replaceAll("\\+", "%2B");
				}
				paires.add(new BasicNameValuePair(key, URLDecoder.decode(data, "UTF-8")));
			}
		}

		if (paires.size() > 0) {
			HttpEntity entity = new UrlEncodedFormEntity(paires, HTTP.UTF_8);
			httpPost.setEntity(entity);
		}
		HttpResponse response = client.execute(httpPost);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300) {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}

		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity, "UTF-8");
		return body;

	}

	public static String get(String url, Map<String, String> params) throws ClientProtocolException, IOException {
		HttpClient client = HttpClients.createDefault();
		if (params.size() > 0) {
			String urlparams = "?"; // not U
			for (Map.Entry<String, String> entry : params.entrySet()) {
				urlparams += entry.getKey() + "=" + entry.getValue() + "&";
			}
			url = url + urlparams;
		}

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("charset", HTTP.UTF_8);

		// 默认超时时间为15s。
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
				.setConnectionRequestTimeout(150000).setStaleConnectionCheckEnabled(true).build();
		httpGet.setConfig(requestConfig);
		HttpResponse response = client.execute(httpGet);
		int status = response.getStatusLine().getStatusCode();
		if (status < 200 || status >= 300) {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity, "UTF-8");
		return body;
	}

	/**
	 * post请求带header
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> headers)
			throws ClientProtocolException, IOException {
		HttpClient client = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("charset", HTTP.UTF_8);
		if (headers != null && headers.size() > 0) {
			headers.forEach((k, v) -> {
				if (!StringUtils.isBlank(k)) {
					httpPost.setHeader(k, v);
				}
			});
		}

		// 默认超时时间为15s。
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
				.setConnectionRequestTimeout(150000).setStaleConnectionCheckEnabled(true).build();
		httpPost.setConfig(requestConfig);

		// 请求的参数信息传递
		List<NameValuePair> paires = new ArrayList<>();
		if (params != null) {
			Set<String> keys = params.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				paires.add(new BasicNameValuePair(key, URLDecoder.decode(params.get(key), "UTF-8")));
			}
		}

		if (paires.size() > 0) {
			HttpEntity entity = new UrlEncodedFormEntity(paires, HTTP.UTF_8);
			httpPost.setEntity(entity);
		}
		HttpResponse response = client.execute(httpPost);

		int status = response.getStatusLine().getStatusCode();

		if (status < 200 || status >= 300) {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}

		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity, "UTF-8");
		return body;

	}

	/**
	 * get请求带header
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers)
			throws ClientProtocolException, IOException {
		HttpClient client = HttpClients.createDefault();
		if (params.size() > 0) {
			String urlparams = "?"; // not U
			for (Map.Entry<String, String> entry : params.entrySet()) {
				urlparams += entry.getKey() + "=" + entry.getValue() + "&";
			}
			url = url + urlparams;
		}

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("charset", HTTP.UTF_8);
		if (headers != null && headers.size() > 0) {
			headers.forEach((k, v) -> {
				if (!StringUtils.isBlank(k)) {
					httpGet.setHeader(k, v);
				}
			});
		}

		// 默认超时时间为15s。
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
				.setConnectionRequestTimeout(150000).setStaleConnectionCheckEnabled(true).build();
		httpGet.setConfig(requestConfig);
		HttpResponse response = client.execute(httpGet);
		int status = response.getStatusLine().getStatusCode();
		if (status < 200 || status >= 300) {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity, "UTF-8");
		return body;
	}
}
