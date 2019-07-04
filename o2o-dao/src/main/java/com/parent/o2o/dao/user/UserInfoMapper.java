package com.parent.o2o.dao.user;

import com.parent.o2o.dao.BaseMapper;
import com.parent.o2o.model.user.UserInfo;

public interface UserInfoMapper extends BaseMapper{
	
	/**
	 * 
	  ************************************
	  * 方法名称: saveUserInfo
	  * 方法描述: 注册用户信息
	  * 参数名称: @param userInfo
	  * 参数名称: @return
	  * 作    者    : 王璐瑶
	  * 创建时间: 2018年02月09日 上午11:06:45
	  * 修改备注:
	  ************************************
	 */
	public void saveUserInfo(UserInfo userInfo);
}
