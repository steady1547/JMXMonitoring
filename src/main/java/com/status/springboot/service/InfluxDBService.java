package com.status.springboot.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.status.springboot.model.MemoryPool;
import com.status.springboot.support.CalcUtil;
import com.status.springboot.support.HttpRestTemplate;
import com.status.springboot.support.MonitorUtils;

@Service
public class InfluxDBService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String BASE_URI = "http://localhost:8086";

    private static final String URI_PARAM_SEPARATOR = "?";
    private static final String URI_PARAM_ONCATENATOR = "&";

    private static final String QUERY_URI = "/query";
    private static final String WRITE_URI = "/write";

    private static final String QUERY_PARAM = "q=";
    private static final String DB_PARAM = "db=";
    private static final String PRETTY_PARAM_TRUE = "pretty=true";

    private static final String QUERY_PARAM_CREATE_DATABASE = "CREATE DATABASE ";
    private static final String QUERY_PARAM_DROP_DATABASE = "DROP DATABASE ";

    private static final String QUERY_PARAM_SHOW_DATABASES = "SHOW DATABASES";
    private static final String QUERY_PARAM_SHOW_MEASUREMENTS = "SHOW MEASUREMENTS";
    private static final String QUERY_PARAM_SHOW_TAG_KEYS = "SHOW TAG KEYS";
    private static final String QUERY_PARAM_SHOW_FIELD_KEYS = "SHOW FIELD KEYS";

    @Autowired
    private HttpRestTemplate httpRestTemplate;

    public String writeOldGenUsage(String database, MemoryPool pool) throws Exception{
    	StringBuilder bodyString = new StringBuilder(database).append(",");
    	bodyString.append("host=").append(pool.getHostname());
    	bodyString.append(",ip=").append(pool.getIp());
    	bodyString.append(",object_name=").append(MonitorUtils.getObjectNameTag(pool.getObjectName()));
    	bodyString.append(" max=").append(pool.getUsage().getMax());
    	bodyString.append(",used=").append(pool.getUsage().getUsed());
		bodyString.append(",percent=").append(CalcUtil.usagePercent(pool));
    	return this.writingData(database, bodyString.toString());
    }
    
    
    /**
    * 데이터베이스 생성
    *
    * @param databaseName
    * @return
    */
    public String createDatabase(
            String databaseName) {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(QUERY_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(QUERY_PARAM)
                .append(QUERY_PARAM_CREATE_DATABASE)
                .append(databaseName);

        logger.info("InfluxDBService # createDatabase - uri : {}", uri.toString());

        return httpRestTemplate.post(uri.toString(), null);
    }

    /**
    * 데이터베이스 리스트 조회
    *
    * @return
    */
    public String showDatabases() {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(QUERY_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(PRETTY_PARAM_TRUE)
                .append(URI_PARAM_ONCATENATOR)
                .append(QUERY_PARAM)
                .append(QUERY_PARAM_SHOW_DATABASES)
        ;

        logger.info("InfluxDBService # showDatabases - uri : {}", uri.toString());

        return httpRestTemplate.post(uri.toString(), null);
    }

    /**
    * 데이터베이스 삭제
    *
    * @param databaseName
    * @return
    */
    public String dropDatabase(
            String databaseName) {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(QUERY_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(QUERY_PARAM)
                .append(QUERY_PARAM_DROP_DATABASE)
                .append(databaseName)
        ;

        logger.info("InfluxDBService # dropDatabase - uri : {}", uri.toString());

        return httpRestTemplate.post(uri.toString(), null);
    }

    /**
    * measurement 리스트 조회
    * - 테이블과 비슷한 개념..
    *
    * @param databaseName
    * @return
    */
    public String showMeasurements(
            String databaseName) {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(QUERY_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(PRETTY_PARAM_TRUE)
                .append(URI_PARAM_ONCATENATOR)
                .append(DB_PARAM)
                .append(databaseName)
                .append(URI_PARAM_ONCATENATOR)
                .append(QUERY_PARAM)
                .append(QUERY_PARAM_SHOW_MEASUREMENTS)
                ;

        logger.info("InfluxDBService # showMeasurements - uri : {}", uri.toString());

        return httpRestTemplate.post(uri.toString(), null);

    }

    /**
    * 태그 키 리스트 조회
    *
    * @param databaseName
    * @param measurement
    * @return
    */
    public String showTagKeys(
            String databaseName,
            String measurement) {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(QUERY_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(PRETTY_PARAM_TRUE)
                .append(URI_PARAM_ONCATENATOR)
                .append(DB_PARAM)
                .append(databaseName)
                .append(URI_PARAM_ONCATENATOR)
                .append(QUERY_PARAM)
                .append(QUERY_PARAM_SHOW_TAG_KEYS)
        ;

        if (StringUtils.isNotEmpty(measurement)) {
            uri.append(" FROM ").append(measurement);
        }

        logger.info("InfluxDBService # showTagKeys - uri : {}", uri.toString());

        return httpRestTemplate.post(uri.toString(), null);
    }

    /**
    * 필드 키 리스트 조회
    *
    * @param databaseName
    * @param measurement
    * @return
    */
    public String showFieldKeys(
            String databaseName,
            String measurement) {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(QUERY_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(PRETTY_PARAM_TRUE)
                .append(URI_PARAM_ONCATENATOR)
                .append(DB_PARAM)
                .append(databaseName)
                .append(URI_PARAM_ONCATENATOR)
                .append(QUERY_PARAM)
                .append(QUERY_PARAM_SHOW_FIELD_KEYS)
        ;

        if (StringUtils.isNotEmpty(measurement)) {
            uri.append(" FROM ").append(measurement);
        }

        logger.info("InfluxDBService # showFieldKeys - uri : {}", uri.toString());

        return httpRestTemplate.post(uri.toString(), null);
    }

    /**
    * 데이터 저장
    *
    * @param databaseName
    * @param body
    * @return
    */
    public String writingData(
            String databaseName,
            String body) {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(WRITE_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(DB_PARAM)
                .append(databaseName)
        ;

        logger.info("InfluxDBService # writingData - uri : {}", uri.toString());
        logger.info("InfluxDBService # writingData - body : {}", body);

        return httpRestTemplate.post(uri.toString(), body);
    }

    /**
    * 데이터 조회
    *
    * @param databaseName
    * @param queryString
    * @return
    */
    public String queringData(
            String databaseName,
            String queryString) {

        StringBuilder uri = new StringBuilder(BASE_URI)
                .append(QUERY_URI)
                .append(URI_PARAM_SEPARATOR)
                .append(PRETTY_PARAM_TRUE)
                .append(URI_PARAM_ONCATENATOR)
                .append(DB_PARAM)
                .append(databaseName)
                .append(URI_PARAM_ONCATENATOR)
                .append(QUERY_PARAM)
                .append(queryString)
        ;

        logger.info("InfluxDBService # queringData - uri : {}", uri.toString());

        return httpRestTemplate.post(uri.toString(), null);
    }

}