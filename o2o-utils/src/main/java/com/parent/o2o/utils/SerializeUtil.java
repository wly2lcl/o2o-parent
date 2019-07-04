package com.parent.o2o.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**********************************************************
 * 类名称：redis序列化工具 <br>
 * 类描述： <br>
 * 创建人：王璐瑶 <br>
 * 创建时间：2018年2月14日下午2:47:11 <br>
 * 修改备注： <br>
 **********************************************************/
public class SerializeUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerializeUtil.class);

	/**
	 * 对象序列化
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}
}
