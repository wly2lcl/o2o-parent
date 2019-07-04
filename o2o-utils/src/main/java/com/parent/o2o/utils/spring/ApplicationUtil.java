/**********************************************************      
 
 * 项目名称：o2o-utils   
 * 类名称：ApplicationUtil   
 * 类描述：   spring工具类
 * 创建人：王璐瑶 
 * 创建时间：2017年1月17日 上午10:31:04   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationUtil.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		// 循环取上下文中的bean
//		for (String a : applicationContext.getBeanDefinitionNames()) {
//			System.out.println(a);
//		}
		return applicationContext.getBean(name);
	}

}
