/**********************************************************      
 
 * 项目名称：o2o-utils   
 * 类名称：UrlSignTool   
 * 类描述：     
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.utils.pay;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class UrlSignTool {
	
	public static String map2Str(TreeMap<String, String> params) throws Exception {
		String ret = "";
		if (params == null || params.isEmpty())
			return ret;
		Set<String> keySet = params.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = params.get(key);
			ret += key + "=" + URLEncoder.encode(value, "UTF-8") + "&";
		}
		return ret.substring(0,ret.length()-1);
	}

}

  