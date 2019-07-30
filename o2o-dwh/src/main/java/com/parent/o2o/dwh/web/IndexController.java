/**********************************************************      
  
 * 类名称：IndexController   
 * 类描述：   
 * 创建时间：2019年4月22日 上午11:45:05   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.dwh.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	private static String proName = "o2o-web";
	
	public static String getProName() {
		return proName;
	}

	public static void setProName(String proName) {
		IndexController.proName = proName;
	}

	@RequestMapping(value = "/health_check")
	public String index() {
		return "<html><body><h2>Hello " + proName + "</h2></body></html>";
	}
	
	@RequestMapping(value = "/init")
	public String init() {
		try {
			return "<html><body><h2>OK " + proName + "</h2></body></html>";
		} catch (Exception e) {
			return "<html><body><h2>ERROR " + proName + "</h2></body></html>";
		}
	}
}
