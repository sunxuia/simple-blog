package net.sunxu.demo.sb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class RelationRedisServiceImpl implements RelationRedisService {
    private static final String PREFIX = "event.";

    @Autowired
    private StringRedisTemplate template;

    private HashOperations<String, Long, Date> eventHashOperations;

    @PostConstruct
    public void initial() {
        eventHashOperations = template.opsForHash();
    }


    @Override
    public int getCount(String key, Long id) {
        return eventHashOperations.size(getKey(key, id)).intValue();
    }

    private String getKey(String key, Long id) {
        return PREFIX + key + "." + id;
    }

    @Override
    public boolean exist(String key, Long id, Long subId) {
        return eventHashOperations.hasKey(getKey(key, id), subId);
    }

    @Override
    public Date getDate(String key, Long id, Long subId) {
        return eventHashOperations.get(getKey(key, id), subId);
    }

    @Override
    public void add(String key, Long id, Long subId) {
        eventHashOperations.put(getKey(key, id), subId, new Date());
    }

    @Override
    public void remove(String key, Long id, Long subId) {
        eventHashOperations.delete(getKey(key, id), subId);
    }

    @Override
    public void remove(String key, Long id) {
        eventHashOperations.delete(getKey(key, id));
    }
}
