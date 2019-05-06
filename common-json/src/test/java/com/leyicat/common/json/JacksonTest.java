package com.leyicat.common.json;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.leyicat.common.JsonUtil;

public class JacksonTest {
	private User user;
	
	@Before
	public void buildData(){
		Dept dep = new Dept();
		dep.setId(11L);
		dep.setDeptName("部门名称1");
		
		user = new User();
		user.setId(98L);
		user.setUserName("用户名1");
		user.setDept(dep);
		user.setCreateTime(new Date());
	}
	
	@Test
	public void toJsonTest(){
		String json = JsonUtil.toJSON(user);
		System.out.println("toJsonTest:"+json);
		Assert.assertNotNull(json);
	}
	
	@Test
	public void toJsonTestNull(){
		String json = JsonUtil.toJSON(null);
		System.out.println("toJsonTestNull:"+json);
		Assert.assertEquals("null", json);
	}
	
	@Test
	public void toJavaObjectTest(){
		String json = "{\"id\":\"11\",\"deptName\":\"部门名称1\"}";
		Dept dept = JsonUtil.toJavaObject(json, Dept.class);
		System.out.println("toJavaObjectTest:"+json);
		Assert.assertTrue(dept.getId().equals(11L) 
				&& dept.getDeptName().equals("部门名称1")
				&& null==dept.getSubs());
	}
	
	@Test
	public void toJavaObjectNestTest(){
		String json = "{\"id\":\"98\",\"userName\":\"用户名1\",\"dept\":{\"id\":\"11\",\"deptName\":\"部门名称1\"},\"createTime\":\"2019-05-06 16:26:46\"}";
		User u = JsonUtil.toJavaObject(json, User.class);
		System.out.println("toJavaObjectNestTest:"+json);
		Assert.assertTrue(u.getId().equals(98L) 
				&& u.getUserName().equals("用户名1")
				&& u.getDept().getId().equals(11L)
				&& u.getDept().getDeptName().equals("部门名称1"));
	}
	
	@Test
	public void toJavaArrayTest(){
		String json = "[{\"id\":\"21\",\"deptName\":\"部门名称2\"},{\"id\":\"11\",\"deptName\":\"部门名称1\"}]";
		List<Dept> list = JsonUtil.toJavaArray(json, Dept.class);
		if(null!=list)System.out.println("toJavaArrayTest:"+JsonUtil.toJSON(list.get(0)));
		Assert.assertTrue(list.size()==2);
	}
	
	@Test
	public void toJSONObjectTest(){
		String json = "{\"id\":\"11\",\"deptName\":\"部门名称1\"}";
		JsonNode node = JsonUtil.toJSONObject(json);
		System.out.println("toJSONObjectTest:"+json);
		Assert.assertTrue(node.get("id").asLong()==11
				&& node.get("deptName").asText().equals("部门名称1"));
	}
	
	@Test
	public void toJSONIncludeFieldTest(){
		String json = JsonUtil.toJSON(user, "id,createTime");
		System.out.println("toJSONIncludeFieldTest:"+json);
		Assert.assertTrue(json.indexOf("createTime")>=0
				&& json.indexOf("id")>=0
				&& json.indexOf("userName")==-1
				&& json.indexOf("dept")==-1);
	}
	
	@Test
	public void toJSONExceptFieldTest(){
		String json = JsonUtil.toJSON(user, "createTime",FilterFeature.Except);
		System.out.println("toJSONExceptFieldTest:"+json);
		Assert.assertTrue(json.indexOf("createTime")==-1);
	}
	
	@Test
	public void toJSONBeanFilterTest(){
		String json = JsonUtil.toJSON(user,
				new BeanFilter<>(Dept.class,"deptName",FilterFeature.Except),
				new BeanFilter<>(User.class,"id,dept",FilterFeature.Include));
		System.out.println("toJSONBeanFilterTest:"+json);
		Assert.assertTrue(json.indexOf("deptName")==-1
				&& json.indexOf("id")>=0
				&& json.indexOf("dept")>=0
				&& json.indexOf("userName")==-1);
	}
}
