package com.parent.o2o.model;

import com.alibaba.fastjson.JSON;


public abstract class BaseModel {

	public String json() {
		try {
			return JSON.toJSONString(this);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
