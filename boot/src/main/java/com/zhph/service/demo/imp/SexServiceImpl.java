package com.zhph.service.demo.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.zhph.mapper.demo.SexMapper;
import com.zhph.model.Sex;
import com.zhph.service.demo.SexService;

@Service
public class SexServiceImpl implements SexService {
	
	@Autowired
	private SexMapper sexMapper;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public List<Sex> getAll() {
		
		List<Sex> dataList =sexMapper.getAll();
		return dataList;
		
	}	

	@Cacheable(value="fff")
	public  void test(){
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set("mykey4", "random1="+Math.random());
		System.out.println(valueOperations.get("mykey4"));
	}
	
	@Cacheable(value="sex")
	public Sex findById(int id){
       System.err.println("DemoInfoServiceImpl.findById()=========从数据库中进行获取的....id="+id);
		Sex sex = sexMapper.findById(id);
       return sex;
	}

}
