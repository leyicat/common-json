package com.leyicat.common.json;

/**
 * bean属性过滤器:<br>
 * Class<T> beanClass:需要进行属性过滤的类<br>
 * String fields:字段列表，多个字段使用逗号(,)分割<br>
 * FilterFeature feature:过滤类型，包含(FilterFeature.Include)或排除(FilterFeature.Except)<br>
 * @param <T>
 */
public class BeanFilter<T> {
	private Class<T> beanClass;
	private FilterFeature feature;
	private String fields;
	
	public BeanFilter(Class<T> beanClass,String fields){
		this(beanClass,fields,FilterFeature.Include);
	}
	
	public BeanFilter(Class<T> beanClass,String fields,FilterFeature feature){
		this.beanClass = beanClass;
		this.feature = feature;
		this.fields = fields;
	}
	
	public Class<T> getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(Class<T> beanClass) {
		this.beanClass = beanClass;
	}
	public FilterFeature getFeature() {
		return feature;
	}
	public void setFeature(FilterFeature feature) {
		this.feature = feature;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
}
