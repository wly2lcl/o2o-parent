package com.parent.o2o.api.service.user;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.parent.o2o.dao.user.UserInfoMapper;
import com.parent.o2o.model.user.UserInfo;
import com.parent.o2o.utils.log.ThreadLocalLogger;

@Repository(value = "userInfoService")
public class UserInfoService {
	
	private static final Logger logger = ThreadLocalLogger.getLogger(UserInfoService.class);

	@Resource
	private UserInfoMapper userInfoMapper;

	public Map<String, Object> saveUserInfo(UserInfo userInfo) throws RuntimeException {
		logger.info("注册");
		userInfoMapper.saveUserInfo(userInfo);
		return null;
	}
}