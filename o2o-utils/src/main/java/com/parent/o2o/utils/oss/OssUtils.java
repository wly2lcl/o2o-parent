package com.parent.o2o.utils.oss;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.UploadFileRequest;

/**
 * 阿里云OSS操作工具
 *
 */
public class OssUtils {

	/**
	 * @see com.aliyun.oss.OSSClient
	 */
	private OSSClient ossClient;
	
	private static final String ossEndpoint = "http://oss-cn-shanghai.aliyuncs.com";
	
	private static final String ossAccessKeyId = "LTAIRa4dnxeXKjkS";
	
	private static final String ossAccessKeySecret = "p6sHPM0OnLGoG2GspDIW8qmzi45Zry";
	
	private static final String ossBucket = "wly";

	/**
	 * 创建{@link com.aliyun.oss.OSSClient}对象
	 * @return {@link com.aliyun.oss.OSSClient}对象
	 */
	private OSSClient getClient() {
		ClientConfiguration conf = new ClientConfiguration();
		conf.setSupportCname(true);
		return new OSSClient(ossEndpoint, ossAccessKeyId, ossAccessKeySecret, conf);
	}
	
	/**
	 * 初始化{@link #ossClient}
	 */
	public void init() {
		ossClient = getClient();
	}
	
	/**
	 * 关闭{@link #ossClient}
	 */
	public void disable() {
		ossClient.shutdown();
	}
	
	/**
	 * 上传文件到阿里云oss
	 * @param fileName 文件名
	 * @param fileInputStream 文件{@link java.io.InputStream}对象
	 */
	public void upload(String fileName, InputStream fileInputStream) throws UnknownHostException {
		ossClient.putObject(ossBucket, fileName, fileInputStream);
		
	}
	
	/**
	 * 下载oss上文件
	 * @param fileName oss上文件名
	 * @return
	 */
	public InputStream download(String fileName) {
		return ossClient.getObject(new GetObjectRequest(ossBucket, fileName)).getObjectContent();
	}
	
	/**
	 * 删除oss上文件
	 * @param fileFile oss上文件名
	 */
	public void deleteFile(String fileFile) {
		ossClient.deleteObject(ossBucket, fileFile);
	}
	
	/**
	 * 获取文件名、原信息
	 * @param prefix 文件名前缀
	 * @return
	 */
	public List<Map<String, String>> showList(String prefix, Map<String, String> map) {
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		String nextMarker = null;
		ObjectListing objectListing = null;
		do {
			objectListing = ossClient.listObjects(new ListObjectsRequest(ossBucket).withMarker(nextMarker).withMaxKeys(1000).withPrefix(prefix));
			List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
			for (OSSObjectSummary s : sums) {
				ObjectMetadata metadata = ossClient.getObjectMetadata(ossBucket, s.getKey());
				Map<String, String> fileMap = metadata.getUserMetadata();
				
				if ((map.get("begintime") == null || (map.get("begintime") != null && map.get("begintime").equals(fileMap.get("begintime")))) 
						&& (map.get("endtime") == null || (map.get("endtime") != null && map.get("endtime").equals(fileMap.get("endtime")))) 
						&& (map.get("gcid") == null || (map.get("gcid") != null && map.get("gcid").equals(fileMap.get("gcid")))) 
						&& (map.get("mcid") == null || (map.get("mcid") != null && map.get("mcid").equals(fileMap.get("mcid")))) 
						&& (map.get("payid") == null || (map.get("payid") != null && map.get("payid").equals(fileMap.get("payid"))))) {
						fileMap.put("NAME", s.getKey().substring(s.getKey().indexOf("_") + 1));
						fileMap.put("LINK", s.getKey());
						res.add(fileMap);
				}
			}
			nextMarker = objectListing.getNextMarker();
		} while(objectListing.isTruncated());
		return res;
	}
	
	/**
	 * 上传文件到阿里云oss
	 * @param fileName 文件名
	 * @param fileInputStream 文件{@link java.io.InputStream}对象
	 * @param map 自定义原信息
	 */
	public void upload(String fileName, InputStream fileInputStream, Map<String, String> map) throws UnknownHostException {
		ObjectMetadata meta =  new ObjectMetadata();
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			meta.addUserMetadata(entry.getKey(), entry.getValue());
		}
		ossClient.putObject(ossBucket, fileName, fileInputStream, meta);
		
	}
	
	/**
	 * 上传文件到阿里云oss(断点续传)
	 * @param fileName 文件名
	 * @param fileUrl 文件路径
	 * @param map 自定义原信息
	 */
	public void uploadToContinue(String fileName, String fileUrl) throws Throwable {
		// 创建OSSClient实例。
		OSSClient ossClient = new OSSClient(ossEndpoint, ossAccessKeyId, ossAccessKeySecret);
		ObjectMetadata meta = new ObjectMetadata();
		// 指定上传的内容类型。
		meta.setContentType("text/plain");
		// 设置断点续传请求。
		UploadFileRequest uploadFileRequest = new UploadFileRequest(ossBucket,fileName);
		//UploadFileRequest单独设置参数（Bucket名字和Object名字上面已经设置，此处仅做展示用，所以是注释掉的）如下：
		//设置Bucket名字，必选参数。
		uploadFileRequest.setBucketName(ossBucket);
		//设置Object名字，必选参数。
		uploadFileRequest.setKey(fileName);
		// 指定上传的本地文件，必选参数。
		uploadFileRequest.setUploadFile(fileUrl);
		// 指定上传并发线程数，默认为1。
		uploadFileRequest.setTaskNum(5);
		// 指定上传的分片大小，从100KB到5GB，单位是Byte，默认为100K。
		uploadFileRequest.setPartSize(1 * 1024 * 1024);
		// 开启断点续传，默认关闭。
		uploadFileRequest.setEnableCheckpoint(true);
		//本地记录分片上传的结果。开启断点续传时，需要在本地记录分片上传的结果，如果上传过程中某一分片上传失败，再次上传时会从Checkpoint文件中记录的点继续上传，这要求再次调用时要指定与上次相同的Checkpoint文件。上传完成后，Checkpoint文件会被删除。默认与待上传的本地文件同目录，为uploadFile.ucp。
//		uploadFileRequest.setCheckpointFile("<yourCheckpointFile>");
		//Object的元数据。
		uploadFileRequest.setObjectMetadata(meta);
		//设置Callback，上传成功后的回调，Callback类型的。
//		uploadFileRequest.setCallback("<yourCallbackEvent>");
		// 断点续传上传。
		ossClient.uploadFile(uploadFileRequest);
		// 关闭Client。
		ossClient.shutdown();
	}
}
