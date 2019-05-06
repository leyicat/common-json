package com.leyicat.common.json;

/**
 * 过滤类型<br>
 * FilterFeature.Include:包含，只序列化制定的属性<br>
 * FilterFeature.Except:排除，序列化排除属性以外的其他所有属性
 */
public enum FilterFeature {
	Include,Except
}
