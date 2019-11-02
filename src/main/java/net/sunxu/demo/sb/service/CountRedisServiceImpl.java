package net.sunxu.demo.sb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class CountRedisServiceImpl implements CountRedisService {

    private static final String PREFIX = "count.";

    @Autowired
    private StringRedisTemplate template;

    private HashOperations<String, Long, Long> countHashOperations;

    @PostConstruct
    public void initial() {
        countHashOperations = template.opsForHash();
    }

    @Override
    public int getCount(String key, Long id) {
        Long res = countHashOperations.get(PREFIX + key, id);
        return res == null ? 0 : res.intValue();
    }

    @Override
    public void addCount(String key, Long id) {
        countHashOperations.increment(PREFIX + key, id, 1L);
    }

    @Override
    public void reduceCount(String key, Long id) {
        countHashOperations.increment(PREFIX + key, id, -1L);
    }

    @Override
    public void remove(String key, Long id) {
        countHashOperations.delete(PREFIX + key, id);
    }
}
