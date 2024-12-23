package com.zzt.user.remote;

import java.util.List;

import javax.annotation.Resource;

import com.zzt.netty.annotation.Remote;
import com.zzt.netty.util.ResponseUtil;
import com.zzt.user.model.User;
import com.zzt.user.service.UserService;

@Remote
public class UserRemoteImpl implements UserRemote{
	@Resource
	private UserService userService;

	public Object saveUser(User user) {
	    userService.save(user);
	    return ResponseUtil.createSuccessResult(user);

	}
	public Object saveUsers(List<User> users) {
	    userService.saveList(users);
	    return ResponseUtil.createSuccessResult(users);
	}  

}
