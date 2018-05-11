package com.zhph.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.model.sys.SysCalendarPool;

public class HttpClientHelperTest {

	@Test
	public void testPostForm() {
		String postUrl = "http://cs.zhph.lan/zhph_commonServices/webservice/vocation/query";
		Map<String, String> paramMap = new HashMap<String,String>();
		paramMap.put("startDate", "20171121");
		paramMap.put("endDate", "20171131");
		paramMap.put("flag", "0");
		Json json = HttpClientHelper.postForm(postUrl, paramMap );
		JSONObject js = JSONObject.parseObject((String) json.getObj());
			JSONArray array = js.getJSONArray("data");
			if(array != null){
				System.out.println(array.size());
				for (Object o : array) {
					System.out.println(JSONObject.parseObject(o.toString(),SysCalendarPool.class));
					System.out.println(o);
				}
			}
		System.out.println(js);
		//System.out.println(json.toString());
	}

}
