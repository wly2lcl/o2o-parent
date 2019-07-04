/**********************************************************      
  
 * 类名称：UserService   
 * 类描述：   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.api.rpc.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.parent.o2o.api.rpc.IUserService;
import com.parent.o2o.api.service.user.UserInfoService;
import com.parent.o2o.model.user.UserInfo;
import com.parent.o2o.utils.constant.Constant;

@Service
@Path("user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({})
public class UserService implements IUserService {
	
	@Resource
	private UserInfoService userInfoService;

    @POST
    @Path("saveuserinfo")
	@Override
	public Map<String, Object> saveUserInfo(Map<String, Object> param) throws RuntimeException {
    	UserInfo userInfo = new UserInfo();
    	userInfo.setUserCode("123");
    	userInfo.setUserName("wz");
    	userInfo.setUserPwd("456");
    	userInfo.setCreateTime(new Date());
		return userInfoService.saveUserInfo(userInfo);
	}
    
//    @GET
//    @Path("login/{code}/{pwd}")
//	@Override
//	public Map<String, Object> login(@PathParam("code") String code, @PathParam("pwd") String pwd) throws RuntimeException {
//		return userInfoService.saveUserInfo(code);
//	}
}

  