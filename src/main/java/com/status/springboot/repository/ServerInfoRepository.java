package com.status.springboot.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.status.springboot.model.ServerInfo;
public interface ServerInfoRepository extends MongoRepository<ServerInfo, String> {
    public void deleteByIp(String ip);
}
