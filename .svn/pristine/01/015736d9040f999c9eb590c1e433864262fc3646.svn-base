package com.netease.filebinding.core.strategy.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.filebinding.core.config.DocumentMappingConfiguration;
import com.netease.filebinding.core.config.DocumentRecordConfiguration;

public class BaseStrategy{

	protected Log log;

	protected String tag;
	protected Map valueContext;

	protected DocumentMappingConfiguration configuration;

	protected BaseStrategy(){
		log = LogFactory.getLog(this.getClass());
		valueContext = new HashMap();
	}

	public void setMappingConfiguration(DocumentMappingConfiguration configuration){
		this.configuration = configuration;
	}

	protected DocumentRecordConfiguration getRecordConfiguration(String tag){
		return this.configuration.getRecordConfiguration(tag);
	}

	/***All the method in BaseStrategy will be Deprecated, we'll use proxy to handle these***/
	
	public void setTag(String tag){
		this.tag = tag;
	}

	protected void addBeans(String tag, List beansList){
		if(beansList == null || beansList.isEmpty())
			return;

		List list = (List)valueContext.get(tag);

		if(list == null){
			list = new ArrayList();
			valueContext.put(tag, list);//Default is a empty list
		}

		list.addAll(beansList);
	}

	protected void addBean(String tag, Object bean){
		List list = (List)valueContext.get(tag);

		if (list == null){
			list = new ArrayList();
			valueContext.put(tag, list);
		}

		list.add(bean);
	}

	protected void clearBeans(String tag){
		List list = (List)valueContext.get(tag);
		if (list == null){
			list = new ArrayList();
			valueContext.put(tag, list);
		}else{
			list.clear();
		}
	}

	protected List getBeans(String tag){
		return (List)valueContext.get(tag);
	}
	
	public Object getValue(Object key){
		if(valueContext == null)
			return null;
		
		return valueContext.get(key);
	}
	
	public void setValue(Object key, Object value){
		if(valueContext == null)
			valueContext = new HashMap();
		
		valueContext.put(key, value);
	}
}
