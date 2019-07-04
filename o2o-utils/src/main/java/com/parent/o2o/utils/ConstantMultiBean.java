/**********************************************************      
  
 * 类名称：ConstantMultiBean   
 * 类描述：   
 * 创建人：王璐瑶
 * 创建时间：2017年7月7日 上午10:18:49   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.utils;

import java.util.List;
import java.util.Map;

import com.parent.o2o.utils.string.StrUtils;

public class ConstantMultiBean extends ConstantBean {
	
	public List<Map<String, String>> propertiesLst;
	
	public List<Map<String, String>> getPropertiesLst() {
		return propertiesLst;
	}

	public void setPropertiesLst(List<Map<String, String>> propertiesLst) {
		this.propertiesLst = propertiesLst;
	}
	
	public String get(String propertyName){
		for (Map<String, String> map : propertiesLst) {
			if (StrUtils.isNotBlank(map.get(propertyName))) {
				return map.get(propertyName);
			}
		}
		return null;
	}

}

  