package net.sunxu.demo.sb.service;

import java.util.Date;

public interface RelationRedisService {

    int getCount(String key, Long id);

    boolean exist(String key, Long id, Long subId);

    Date getDate(String key, Long id, Long subId);

    void add(String key, Long id, Long subId);

    void remove(String key, Long id, Long subId);

    void remove(String key, Long id);
}
