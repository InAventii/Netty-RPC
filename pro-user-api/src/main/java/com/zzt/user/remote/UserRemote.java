package com.zzt.user.remote;

import java.util.List;


import com.zzt.user.model.User;


public interface UserRemote {
	public Object saveUser(User user);
	public Object saveUsers(List<User> users);


}
