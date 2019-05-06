package com.leyicat.common.json;

import java.util.Set;

public class Dept {
	private Long id;
	private String deptName;
	private Set<Long> subs;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Set<Long> getSubs() {
		return subs;
	}
	public void setSubs(Set<Long> subs) {
		this.subs = subs;
	}
}
