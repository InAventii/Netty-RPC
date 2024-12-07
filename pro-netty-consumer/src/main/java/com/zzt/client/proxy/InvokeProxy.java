package com.zzt.client.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;
import com.zzt.client.annotation.RemoteInvoke;
import com.zzt.client.core.TcpClient;
import com.zzt.client.param.ClientRequest;
import com.zzt.client.param.Response;
import com.zzt.user.bean.User;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.BeansException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokeProxy implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RemoteInvoke.class)) {
                field.setAccessible(true);
                Enhancer enhancer = new Enhancer();
                final Map<Method, Class> methodClassMap = new HashMap<Method, Class>();
                putMethodClass(methodClassMap,field);

                enhancer.setInterfaces(new Class[]{field.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        ClientRequest request = new ClientRequest();
                        request.setCommand(methodClassMap.get(method).getName() + "." + method.getName());
                        request.setContent(args[0]);
                        Response resp = TcpClient.send(request);

                        return resp;
                    }
                });


                try {
					field.set(bean,enhancer.create());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
        }
        return bean;
    }
    
    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
    	Method[] methods = field.getType().getDeclaredMethods();
    	for (Method m : methods) {
    	    methodClassMap.put(m, field.getType());
    	}

		
	}

	@Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
