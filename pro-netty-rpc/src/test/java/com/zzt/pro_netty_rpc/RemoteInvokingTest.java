package com.zzt.pro_netty_rpc;

//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.ContextConfiguration;
//
//import com.zzt.netty.annotation.RemoteInvoke;
//import com.zzt.user.bean.User;
//import com.zzt.user.remote.UserRemote;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes=RemoteInvokingTest.class)
//@ComponentScan("com.zzt")
//public class RemoteInvokingTest {
//	
//	@RemoteInvoke
//	private UserRemote userRemote;
//
//	@Test
//	public void testSaveUser() {
//	    User u = new User();
//	    u.setId(1);
//	    u.setName("张三");
//	    userRemote.saveUser(u);
//	}
//
//	@Test
//	public void testSaveUsers() {
//		List<User> users = new ArrayList<User>();
//		User u = new User();
//		u.setId(1);
//		u.setName("张三");
//		users.add(u);
//		userRemote.saveUsers(users);
//
//	}
//
//
//}
