package com.status.springboot;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

import com.status.springboot.model.ServerInfo;
import com.status.springboot.pool.MonitoringServerPool;
import com.status.springboot.repository.ServerInfoRepository;

@SpringBootApplication
@EnableScheduling//스케쥴러 활성화
public class StatusSpringBootApplication implements CommandLineRunner{

	@Autowired
	private ServerInfoRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(StatusSpringBootApplication.class, args);
	}
	
	/**
	 * 스케쥴 등록
	 * @return
	 */
	@Bean 
	public TaskScheduler taskScheduler() { 
		return new ConcurrentTaskScheduler(); 
	} 

	/**
	 * initialize
	 */
	@Override
	public void run(String... args) throws Exception {
		/*
			repository.deleteAll();
			repository.save(new ServerInfo("10.113.132.154", "hostname..", 18001));
		*/
		MonitoringServerPool pool = MonitoringServerPool.getInstance();
		pool.init(new HashSet<ServerInfo>(repository.findAll()));
		
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}
}
