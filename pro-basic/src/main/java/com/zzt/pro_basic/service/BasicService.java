package com.zzt.pro_basic.service;



import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zzt.client.annotation.RemoteInvoke;
import com.zzt.user.model.User;
import com.zzt.user.remote.UserRemote;

@Service
public class BasicService {
	
	@RemoteInvoke
	private UserRemote userRemote;

	public void testSaveUser() {
	    User u = new User();
	    u.setId(1);
	    u.setName("张三");
	    Object response = userRemote.saveUser(u);
	    System.out.println(JSONObject.toJSONString(response));
	}

}
