package com.status.springboot.repository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.status.springboot.support.HttpRestTemplate;

public class ResourcesRepository {
	/**
	 * influeDB
	 * @author line play
	 *
	 */
	private class DBInfo{
		final static String DB_URL = "http://localhost:8086";//@value
		final static String WRITE_PATH = "/write";
		final static String WRITE_URL = DB_URL + WRITE_PATH;
		final static String DATABASE_NAME = "mydb";  
	}
	
	@Autowired
	HttpRestTemplate rt;
	
	public void write(){
		try{
			StringBuffer url = new StringBuffer(DBInfo.WRITE_URL)
			.append("?db=").append(DBInfo.DATABASE_NAME);
			String response = rt.post(url.toString(),
					"cpu_load_short,host=server9999,region=us-west value=0.9999 1434055562000000000");
			System.out.println(response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
