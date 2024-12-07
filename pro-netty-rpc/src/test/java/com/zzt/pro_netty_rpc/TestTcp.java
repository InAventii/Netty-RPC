package com.zzt.pro_netty_rpc;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import com.zzt.netty.client.ClientRequest;
//import com.zzt.netty.client.TcpClient;
//import com.zzt.netty.util.Response;
//import com.zzt.user.bean.User;
//
//public class TestTcp {
//
//    @Test
//    public void testGetResponse() {
//        ClientRequest request = new ClientRequest();
//        request.setContent("测试tcp长连接请求");
//
//        Response resp = TcpClient.send(request);
//
//        System.out.println(resp.getResult());
//    }
//
//    @Test
//    public void testSaveUsers() {
//    	ClientRequest request = new ClientRequest();
//    	List<User> users = new ArrayList<User>();
//    	User u = new User();
//    	u.setId(1);
//    	u.setName("张三");
//    	users.add(u);
//    	request.setCommand("com.zzt.user.controller.UserController.saveUsers");
//    	request.setContent(users);
//    	Response resp = TcpClient.send(request);
//    	System.out.println(resp.getResult());
//
//
//    }
//}
