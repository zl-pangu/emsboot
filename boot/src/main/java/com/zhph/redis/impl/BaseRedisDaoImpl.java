package com.zhph.redis.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.zhph.redis.BaseRedisDao;

@Repository
public class BaseRedisDaoImpl<K, V> implements BaseRedisDao<K, V> {

	@Resource
	protected RedisTemplate<K, V> redisTemplate;

	@Override
	public void add(K key, V value) {
		ValueOperations<K, V> ops = redisTemplate.opsForValue();
		ops.set(key, value);
	}

	@Override
	public void add(K key, V value, long timeout, TimeUnit unit) {
		ValueOperations<K, V> ops = redisTemplate.opsForValue();
		ops.set(key, value, timeout, unit);
	}

	@Override
	public void delete(K key) {
		redisTemplate.delete(key);
	}

	@Override
	public V get(K key) {
		ValueOperations<K, V> ops = redisTemplate.opsForValue();
		return ops.get(key);
	}

}
