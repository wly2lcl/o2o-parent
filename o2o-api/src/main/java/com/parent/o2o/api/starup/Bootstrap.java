/**********************************************************      
  
 * 类名称：Bootstrap   
 * 类描述：   
 * 创建人：王璐瑶
 * 创建时间：2018年1月12日 上午8:06:23   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.api.starup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.AccessControlException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.parent.o2o.utils.room.RoomName;

public class Bootstrap {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	private static Bootstrap daemon = null;
	// 是否阻塞
	protected boolean await = true;
	// 关闭端口
	private int port = 9886;
	// 主机地址
	private String address = "localhost";
	// 系统阻塞socket
	private volatile ServerSocket awaitSocket = null;
	// 是否停止等待
	private volatile boolean stopAwait = false;
	// 关闭系统指令
	private String shutdown = "SHUTDOWN";
	// 配置文件列表
	protected String[] configFileNameLst;
	//
	protected String[] configFileLst;
	// logback配置文件
	protected String logbackFile = "../conf/logback.xml";

	private Random random = null;

	/**
	 * ********************************************************<br>
	 * 项目名称：RCP <br>
	 * 方法说明：系统启动 <br>
	 * 参数： @throws Exception <br>
	 * 返回值： void <br>
	 * 修改备注： <br>
	 *********************************************************
	 * 
	 */
	private void start() throws Exception {
		logger.info("------------------------Server startup------------------------");
		logger.info("Server path:" + System.getProperty("user.dir"));
		logger.info("maxMemory：" + Runtime.getRuntime().maxMemory());
		logger.info("totalMemory：" + Runtime.getRuntime().totalMemory());
		logger.info("freeMemory：" + Runtime.getRuntime().freeMemory());
		long t1 = System.nanoTime();
		// spring容器
		AbstractApplicationContext context = null;
		// 判断是本地调试启动还是正式启动服务
		if (existsAll("../conf/" + "applicationContext.xml")) {
			logger.info("startup with release");
			context = new FileSystemXmlApplicationContext("../conf/" + "applicationContext.xml");
		} else {
			logger.info("startup with Development");
			context = new ClassPathXmlApplicationContext("applicationContext.xml");
		}
		// 启动spring容器
		context.start();
		long t2 = System.nanoTime();
		logger.info("Server startup in " + ((t2 - t1) / 1000000) + " ms");
		if (await) {
			await();
		}
		context.close();
		logger.info("------------------------Server shutdown------------------------");
	}

	/**
	 * ********************************************************<br>
	 * 项目名称：RCP <br>
	 * 方法说明：系统退出 <br>
	 * 参数： <br>
	 * 返回值： void <br>
	 * 修改备注： <br>
	 *********************************************************
	 * 
	 */
	private void stop() {
		try (Socket socket = new Socket(address, port); OutputStream stream = socket.getOutputStream()) {
			String shutdown = this.shutdown;
			for (int i = 0; i < shutdown.length(); i++) {
				stream.write(shutdown.charAt(i));
			}
			stream.flush();
		} catch (ConnectException ce) {
			logger.error("stopServer.connectException [" + address + ":" + port + "]", ce);
			ce.printStackTrace();
		} catch (IOException e) {
			logger.error("stopServer.IOException", e);
			e.printStackTrace();
		}
	}

	/**
	 * ********************************************************<br>
	 * 项目名称：RCP <br>
	 * 方法说明：进程等待 <br>
	 * 参数： <br>
	 * 返回值： void <br>
	 * 修改备注： <br>
	 *********************************************************
	 * 
	 */
	private void await() {
		try {
			awaitSocket = new ServerSocket(port, 1, InetAddress.getByName(address));
		} catch (IOException e) {
			logger.error("await: create[" + address + ":" + port + "]: ", e);
			return;
		}
		try {

			while (!stopAwait) {
				ServerSocket serverSocket = awaitSocket;
				if (serverSocket == null) {
					break;
				}

				Socket socket = null;
				StringBuilder command = new StringBuilder();
				try {
					InputStream stream;
					long acceptStartTime = System.currentTimeMillis();
					try {
						socket = serverSocket.accept();
						socket.setSoTimeout(10 * 1000);
						stream = socket.getInputStream();
					} catch (SocketTimeoutException ste) {
						logger.error(
								"Provider.accept.timeout:" + Long.valueOf(System.currentTimeMillis() - acceptStartTime),
								ste);
						continue;
					} catch (AccessControlException ace) {
						logger.error("Provider.accept security exception: " + ace.getMessage(), ace);
						continue;
					} catch (IOException e) {
						if (stopAwait) {
							break;
						}
						logger.error("Provider.await: accept: ", e);
						break;
					}

					// 读取
					int expected = 1024;
					while (expected < shutdown.length()) {
						if (random == null)
							random = new Random();
						expected += (random.nextInt() % 1024);
					}
					while (expected > 0) {
						int ch = -1;
						try {
							ch = stream.read();
						} catch (IOException e) {
							ch = -1;
						}
						if (ch < 32)
							break;
						command.append((char) ch);
						expected--;
					}
				} finally {
					try {
						if (socket != null) {
							socket.close();
						}
					} catch (IOException e) {
						// Ignore
					}
				}

				boolean match = command.toString().equals(shutdown);
				if (match) {
					break;
				} else {

				}
			}
		} finally {
			ServerSocket serverSocket = awaitSocket;
			awaitSocket = null;

			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}
	
	private void getHeathPort(String[] args) {
		try {
			if (args.length > 1) {
				port = Integer.valueOf(args[1]);
			}
		} catch (Exception e) {
		}
	}
	// 获取机房分组名称
	private void getRoom(String[] args) {
		try {
			if (args.length > 2) {
				RoomName.setRoom(args[2]);
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) throws Exception {
		if (daemon == null) {
			Bootstrap bootstrap = new Bootstrap();

			daemon = bootstrap;
		} else {
		}
		daemon.getHeathPort(args);
		daemon.getRoom(args);
		try {
			String command = "start";
			if (args.length > 0) {
				command = args[0];
			}

			if (command.equals("start")) {
				daemon.start();
			} else if (command.equals("stop")) {
				daemon.stop();
			} else {
			}

			// System.in.read(); // 按任意键退出

		} catch (Throwable t) {
			if (t instanceof InvocationTargetException && t.getCause() != null) {
				t = t.getCause();
			}
			t.printStackTrace();
			System.exit(1);
		}
	}
	
	private static boolean existsAll(String... paths) {
		File file = null;
		for (String path : paths) {
			file = new File(path);
			if (!file.exists()) {
				return false;
			}
		}
		return true;
	}

}
