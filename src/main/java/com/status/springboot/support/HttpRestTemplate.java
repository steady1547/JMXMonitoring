package com.status.springboot.support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpRestTemplate {
	@Autowired
	RestTemplate rt ;
	
	@PostConstruct
	private void init(){
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(10000);
		httpRequestFactory.setReadTimeout(5000);
		HttpClient httpClient = HttpClientBuilder.create()
		 .setMaxConnTotal(10)
		 .setMaxConnPerRoute(100)
		 .build();
		httpRequestFactory.setHttpClient(httpClient);
		rt = new RestTemplate(httpRequestFactory);
	}
	
	
	private HttpEntity<String> getEntity(String body){
		return new HttpEntity<String>(body);
	}
	
	public String get(String url, String params){
		return exchange(url, null, HttpMethod.GET);
	}
	
	public String post(String url, String body){
		return exchange(url, body, HttpMethod.POST);
	}
	
	private String exchange(String url, String body, HttpMethod method){
		ResponseEntity<String> response = rt.exchange(url, method, getEntity(body), String.class);
		System.out.println(response.getStatusCodeValue());
		System.out.println(response.getBody());
		return response.getBody();
	}
	
	private String encode(String paramString) throws UnsupportedEncodingException{
		return URLEncoder.encode(paramString, "UTF8");
	}
}
