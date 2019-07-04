package com.parent.o2o.utils.mail;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**********************************************************
 * 类名称：MailUtil <br>
 * 类描述： 发送邮件工具类 <br>
 * 创建人：王璐瑶 <br>
 * 创建时间：2018年7月19日上午8:40:04 <br>
 * 修改备注： <br>
 **********************************************************/
public class MailUtil {
	/** 邮件服务器host*/
	private String host;
	/** 发送人*/
	private String from;
	/** 密码*/
	private String password;
	/** 用户名*/
	private String username;
	/** 超时时间*/
	private String timeout;

	/**********************************************************
	 * 方法说明： 发送简单的邮件<br>
	 * 参数： @param mailTo
	 * 参数： @param subject
	 * 参数： @param text <br> 
	 * 创建人：王璐瑶  <br>
	 * 创建时间：2018年7月19日 上午11:16:03<br>   
	 * 修改备注： <br>
	 *********************************************************
	 */
	public void sendSimpleMail(String[] mailTo, String subject, String text) {
		try {
			JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
			javaMailSender.setHost(host);
			javaMailSender.setUsername(username);
			javaMailSender.setPassword(password);
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(mailTo);
			message.setFrom(from);
			message.setSubject(subject);
			message.setText(text);
			Properties prop = new Properties();
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.timeout",timeout);
			javaMailSender.setJavaMailProperties(prop);
			javaMailSender.send(message);
		} catch(Exception e) {
			
		}
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

}
