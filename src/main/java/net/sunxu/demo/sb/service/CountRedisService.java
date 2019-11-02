package net.sunxu.demo.sb.service;

public interface CountRedisService {

    int getCount(String key, Long id);

    void addCount(String key, Long id);

    void reduceCount(String key, Long id);

    void remove(String key, Long id);
}
