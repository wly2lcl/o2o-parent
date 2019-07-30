/**********************************************************      
  
 * 类名称：Bootstrap   
 * 类描述：   
 * 创建时间：2019年4月22日 下午1:08:23   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.parent.o2o.dwh.web.IndexController;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Bootstrap extends WebMvcConfigurationSupport {

	// http://127.0.0.1:8080/health_check
	public static void main(String[] args) {
		log.info("Server path:" + System.getProperty("user.dir"));
		log.info("maxMemory：" + Runtime.getRuntime().maxMemory());
		log.info("totalMemory：" + Runtime.getRuntime().totalMemory());
		log.info("freeMemory：" + Runtime.getRuntime().freeMemory());	
		IndexController.setProName("o2o-dwh");
		SpringApplication.run(Bootstrap.class, args);
	}

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //定义一个转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter() {
            @Override
            protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                if(obj != null && obj.getClass()==String.class) {
                    outputMessage.getBody().write(obj.toString().getBytes());
                }else {
                    super.writeInternal(obj, outputMessage);
                }
            }
        };
        //添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //在转换器中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //将转换器添加到converters中
        converters.add(fastConverter);
    }

}
