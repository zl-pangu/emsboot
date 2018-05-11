package com.zhph.redis;

import java.util.concurrent.TimeUnit;

public interface BaseRedisDao<K, V> {
	public void add(K key, V value);

	public void add(K key, V value, long timeout, TimeUnit unit);

	public void delete(K key);

	public V get(K key);
}
