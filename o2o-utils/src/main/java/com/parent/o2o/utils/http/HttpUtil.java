package com.parent.o2o.utils.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.parent.o2o.utils.log.ThreadLocalLogger;
import com.parent.o2o.utils.string.StrUtils;

public class HttpUtil {
	private static final Logger logger = ThreadLocalLogger.getLogger(HttpUtil.class);

	static {
		trustAllHosts();
	}

	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
			public void checkClientTrusted(X509Certificate[] chain, String authType)  {}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String httpPost(String url, String params,String contentType, Map<String, String> props) {
		return http(url,params,"POST",contentType,props,10,10,null);
	}
    
    public static String httpPost(String url, String params,String contentType, Map<String, String> props,int connTimeoutSeconds,int readTimeoutSeconds) {
        return http(url,params,"POST",contentType,props,connTimeoutSeconds,readTimeoutSeconds,null);
    }
	
	/*
	 * 请求JSON数据
	 * */
	public static String doPostJson(String url, String postData) throws Exception {
		return doPostJson(url, postData, null);
	}
	
	/*
	 * 请求JSON数据
	 * */
	public static String doPostJson(String url, String postData,int connTimeoutSeconds,int readTimeoutSeconds) throws Exception {
		return doPostJson(url, postData, null, connTimeoutSeconds, readTimeoutSeconds);
	}
	
	/*
	 * 请求JSON数据
	 * */
	public static String doPostJson(String url, String postData,Map<String, String> headMap) throws Exception {
		return httpPost(url, postData, "application/json", headMap);
	}
	
	public static String doPostJson(String url, String postData,Map<String, String> headMap,int connTimeoutSeconds,int readTimeoutSeconds) throws Exception {
		return httpPost(url, postData, "application/json", headMap, connTimeoutSeconds, readTimeoutSeconds);
	}
	
	public static <T> T doPostObject(String url, String postData, Class<T> clazz) throws Exception{
		return JSON.parseObject(doPostJson(url, postData), clazz);
	}

	public static String httpGet(String url, String params,String contentType, Map<String, String> props) {
		return httpGet(StrUtils.isBlank(params) ? url : (url + "?" + params), null, contentType, props, null);
	}
	
	public static String httpGet(String url, String params,String contentType, Map<String, String> props, String charset) {
		return http(StrUtils.isBlank(params) ? url : (url + "?" + params), null, "GET", contentType, props,10,10,charset);
	}

    public static String httpGet(String url, String params,String contentType, Map<String, String> props,int connTimeoutSeconds,int readTimeoutSeconds) {
        return http(StrUtils.isBlank(params) ? url : (url + "?" + params), null, "GET", contentType, props,connTimeoutSeconds,readTimeoutSeconds,null);
    }

	public static String httpPut(String url, String params,String contentType, Map<String, String> props) {
		return http(url, params, "PUT", contentType, props,10,10,null);
	}

    public static String httpPut(String url, String params,String contentType, Map<String, String> props,int connTimeoutSeconds,int readTimeoutSeconds) {
        return http(url, params, "PUT", contentType, props,connTimeoutSeconds,readTimeoutSeconds,null);
    }

	public static String httpDelete(String url, String params,String contentType, Map<String, String> props) {
		return http(url, params, "DELETE", contentType, props,10,10,null);
	}

    public static String httpDelete(String url, String params,String contentType, Map<String, String> props,int connTimeoutSeconds,int readTimeoutSeconds) {
        return http(url, params, "DELETE", contentType, props,connTimeoutSeconds,readTimeoutSeconds,null);
    }

	public static byte[] downloadFile(String url,String contentType, Map<String, String> props) {
		HttpURLConnection con = null;
		try {
			con = buildHttpConnection(url);
			con.setRequestMethod("GET");
			con.setDoInput(true);
			if(StrUtils.isNotBlank(contentType)) {
				con.setRequestProperty("Content-Type", contentType);
			}
			con.setUseCaches(false);
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			if (props != null) {
				for (Entry<String, String> entry : props.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					con.setRequestProperty(key, value);
				}
			}
			InputStream is = con.getInputStream();
			byte[] tempByte = new byte[1024];
			int len;
			ByteArrayOutputStream baso = new ByteArrayOutputStream();
			while ((len = is.read(tempByte)) != -1) {
				baso.write(tempByte,0,len);
			}
			return baso.toByteArray();
		} catch (Exception e) {
			logger.error(StrUtils.append("下载文件异常:", e.getMessage()), e);
			return null;
		}  finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

	private static String http(String url, String params, String method,String contentType, Map<String, String> props,
	        int connTimeoutSeconds, int readTimeoutSeconds, String charset) {
		if(StrUtils.isBlank(charset)){
			charset = "UTF-8";
		}
		long start = System.nanoTime();
		HttpURLConnection con = null;
		try {
			con = buildHttpConnection(url);
			con.setRequestMethod(method);
			con.setDoInput(true);
			if(StrUtils.isNotBlank(contentType)) {
				con.setRequestProperty("Content-Type", contentType);
			}
			con.setUseCaches(false);
			con.setConnectTimeout(connTimeoutSeconds * 1000);
			con.setReadTimeout(readTimeoutSeconds * 1000);
			if (props != null) {
				for (Entry<String, String> entry : props.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					con.setRequestProperty(key, value);
				}
			}
			if(!method.equals("GET") && !method.equals("DELETE")) {
				con.setDoOutput(true);
				OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
				osw.write(params);
				osw.flush();
				osw.close();
			}
			StringBuilder buffer = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
			logger.info("提交"+method+"请求耗时:" + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)) + "毫秒;url:" + url + ";result:" + buffer.toString());
			return buffer.toString();
		} catch (Exception e) {
			logger.error(StrUtils.append("提交"+method+"请求异常耗时:" + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)) + "毫秒;url:" + url + ";异常信息:", e.getMessage()), e);
			return null;
		}  finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

	private static HttpURLConnection buildHttpConnection(String url) throws IOException {
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		if (con instanceof HttpsURLConnection) {
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) con;
			httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String s, SSLSession sslSession) {
					return true;
				}
			});
		}
		return con;
	}
	
	/**
	 * ********************************************************<br>      
	 * 方法说明：form data post请求  <br>
	 * 参数： @param serverUrl
	 * 参数： @param map
	 * 参数： @return 
	 * 返回值： String <br>   
	 * 创建人：王璐瑶  <br>
	 * 创建时间：2018年9月6日 下午6:34:04 <br>   
	 * 修改备注： <br>   
	   
	 *********************************************************
	 */
	public static String httpPostFormData(String serverUrl, Map<String, Object> map, String imgKey, String path) {
		HttpURLConnection connection = null;
		OutputStream out = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(serverUrl);
			connection = (HttpURLConnection) url.openConnection();
			
			// 发送POST请求必须设置如下两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----------tcsl");
			
			// 传输内容
			StringBuffer contentBody = new StringBuffer("------------tcsl");
			
			// 尾
			String endBoundary = "\r\n------------tcsl--\r\n";
			out = connection.getOutputStream();
			Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				contentBody.append("\r\n")
				.append("Content-Disposition: form-data; name=\"")
				.append(entry.getKey() + "\"")
				.append("\r\n")
				.append("\r\n")
				.append(entry.getValue())
				.append("\r\n")
				.append("--")
				.append("----------tcsl");
			}
			
			if (path != null) {
				String filename = path;
				if (path.indexOf("/") != -1) {
					filename = path.substring(path.lastIndexOf("/") + 1);
				} else {
					filename = imgKey + path.substring(path.lastIndexOf("."));
				}
				
				String contentType = "application/octet-stream";
                if (path.endsWith(".png")) {
                    contentType = "image/png"; 
                } else if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".jpe")) {
                    contentType = "image/jpeg";
                } else if (path.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (path.endsWith(".ico")) {
                    contentType = "image/image/x-icon";
                }
                contentBody.append("\r\n");
				contentBody.append("Content-Disposition: form-data; name=\"" + imgKey + "\"; filename=\"" + filename + "\"\r\n");
				contentBody.append("Content-Type:" + contentType + "\r\n\r\n");
			}
			
			String boundaryMessage1 = contentBody.toString();
			out.write(boundaryMessage1.getBytes("utf-8"));
			
			if (path != null) {
				HttpURLConnection conn = null;
				InputStream is = null;
				try {
					URL imgUrl = new URL(path);
					conn = (HttpURLConnection) imgUrl.openConnection();
					conn.setDoInput(true);
					conn.connect();
					is = conn.getInputStream();
					
					int bytes = 0;
	                byte[] bufferOut = new byte[1024];
	                while ((bytes = is.read(bufferOut)) != -1) {
	                    out.write(bufferOut, 0, bytes);
	                }
	                is.close();
	                conn.disconnect();
				} catch(Exception e) {
					if (is != null) {
						is.close();
					}
					if (conn != null) {
						conn.disconnect();
					}
					throw new RuntimeException("读取图片流异常：" + e.getMessage(), e);
				}
			}
			
			out.write(endBoundary.getBytes("utf-8"));
			out.flush();
			out.close();
			
			// 从服务器获得回答的内容
			String strLine = "";
			String strResponse = "";
			in = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			while ((strLine = reader.readLine()) != null) {
				strResponse += strLine + "\n";
			}
			return strResponse;
		} catch (Exception e) {
			logger.error(StrUtils.append("httpPostFormData异常:", e.getMessage()), e);
			return null;
		}  finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("BufferedReader对象close异常：" + e.getMessage(), e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("InputStream对象close异常：" + e.getMessage(), e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("OutputStream对象close异常：" + e.getMessage(), e);
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	
	/**
	 * ********************************************************<br>      
	 * 方法说明：读取http图片获取md5<br>
	 * 参数： @return 
	 * 返回值： String <br>   
	 * 创建人：王璐瑶  <br>
	 * 创建时间：2018年9月12日 下午5:11:45 <br>   
	 * 修改备注： <br>   
	   
	 *********************************************************
	 */
	public static String getImgMd5(String path) {
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			String md5 = DigestUtils.md5Hex(is);
			is.close();
			conn.disconnect();
			return md5;
		} catch(Exception e) {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e1) {
				logger.error("InputStream对象close异常：" + e.getMessage(), e);
			}
			if (conn != null) {
				conn.disconnect();
			}
			throw new RuntimeException("读取http图片获取md5异常：" + e.getMessage(), e);
		}
	}
	
	/**
	 * ********************************************************<br>      
	 * 方法说明：上传文件<br>
     * 参数：urlStr 
     * 参数：textMap 
     * 参数：fileMap 
     * 返回值： String <br>   
	 * 创建人：王璐瑶  <br>
	 * 创建时间：2018年12月13日 下午2:11:45 <br>   
	 * 修改备注： <br>   
	   
	 *********************************************************
	 */
    public static String formUpload(String urlStr, Map<String, String> textMap,  
            Map<String, String> fileMap) {  
        String res = "";  
        HttpURLConnection conn = null;  
        String BOUNDARY = "---------------------------"; //boundary就是request头和上传文件内容的分隔符  
        try {  
            URL url = new URL(urlStr);  
            conn = (HttpURLConnection) url.openConnection();  
            conn.setConnectTimeout(5000);  
            conn.setReadTimeout(30000);  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("Connection", "Keep-Alive");  
            conn  
                    .setRequestProperty("User-Agent",  
                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
            conn.setRequestProperty("Content-Type",  
                    "multipart/form-data; boundary=" + BOUNDARY);  
  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            // text  
            if (textMap != null) {  
                StringBuffer strBuf = new StringBuffer();  
                for (String key : textMap.keySet()) {
                	String inputValue = textMap.get(key);  
                    if (inputValue == null) {  
                        continue;  
                    }  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
                            "\r\n");  
                    strBuf.append("Content-Disposition: form-data; name=\""  
                            + key + "\"\r\n\r\n");  
                    strBuf.append(inputValue); 
				}
                out.write(strBuf.toString().getBytes());  
            }  
  
            // file  
            if (fileMap != null) {  
            	for (String key : fileMap.keySet()) {
					String inputValue = fileMap.get(key);  
                    if (inputValue == null) {  
                        continue;  
                    }
                    File file = new File(inputValue);  
                    String filename = file.getName();  
                    String contentType = new MimetypesFileTypeMap()  
                            .getContentType(file);  
                    if (filename.endsWith(".png")) {  
                        contentType = "image/png";  
                    }  
                    if (contentType == null || contentType.equals("")) {  
                        contentType = "application/octet-stream";  
                    }  
  
                    StringBuffer strBuf = new StringBuffer();  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
                            "\r\n");  
                    strBuf.append("Content-Disposition: form-data; name=\""  
                            + key + "\"; filename=\"" + filename  
                            + "\"\r\n");  
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
  
                    out.write(strBuf.toString().getBytes());  
  
                    DataInputStream in = new DataInputStream(  
                            new FileInputStream(file));  
                    int bytes = 0;  
                    byte[] bufferOut = new byte[1024];  
                    while ((bytes = in.read(bufferOut)) != -1) {  
                        out.write(bufferOut, 0, bytes);  
                    }  
                    in.close();
				}
            }  
  
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
            out.write(endData);  
            out.flush();  
            out.close();  
  
            // 读取返回数据  
            StringBuffer strBuf = new StringBuffer();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                strBuf.append(line).append("\n");  
            }  
            res = strBuf.toString();  
            reader.close();  
            reader = null;  
        } catch (Exception e) {  
            System.out.println("发送POST请求出错。" + urlStr);  
            e.printStackTrace();  
        } finally {  
            if (conn != null) {  
                conn.disconnect();  
                conn = null;  
            }  
        }  
        return res;  
    }
}
