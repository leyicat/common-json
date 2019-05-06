# common-json
Json序列化工具类，基于jackson封装。提供了Java对象转Json（序列化）和Json转Java对象（反序列化）一些列工具方法，并提供了不侵入代码（不需要注解、也不需要额外辅助类）情况下Java对象属性过滤的功能。

根据实际项目的需要，初始化了一些基础属性,具体包括：<br>
（1）对于Long和long类型序列化时当作字符串输出，主要是为了解决Java和JavaScript长整型类型范围不一致，个别情况出现精度丢失的情况<br>
（2）日期时间类型的类型，统一输出格式为yyyy-MM-dd HH:mm:ss<br>
（3）不输出null值<br>
当然，如果不需要这些默认设置或需要不同的配置值，fork代码变更或注视属性设置部分即可。<br>
<br>

使用示例：<br>
1、数据准备<br>
（1）使用到的Java对象<br>
```java
public class User {
    private Long id;
    private String userName;
    private Dept dept;
    private Date createTime;
    
    //...get和set
}
```

```java
public class Dept {
    private Long id;
    private String deptName;
    private Set<Long> subs;
    
    //...get和set
}
```

(2)准备数据<br>

```java
public void buildData(){
    Dept dep = new Dept();
    dep.setId(11L);
    dep.setDeptName("部门名称1");
    
    User user = new User();
    user.setId(98L);
    user.setUserName("用户名1");
    user.setDept(dep);
    user.setCreateTime(new Date());
    
    return user;
}
```

2、调用示例<br>
（1）Java序列化为JSON字符串<br>

```java
String json = JsonUtil.toJSON(user);
System.out.println("toJsonTest:"+json);
```

运行结果：toJavaObjectTest:{"id":"11","deptName":"部门名称1"}<br><br>

 (2)JSON自负喜欢反序列化为Java对象<br>
 
 ```java
 String json = "{\"id\":\"11\",\"deptName\":\"部门名称1\"}";
Dept dept = JsonUtil.toJavaObject(json, Dept.class);
System.out.println("toJavaObjectTest:"+dept.getId());
```

(3)属性过滤：只输出指定的字段，本例中只输出id和createTime两个属性<br><br>

```java
String json = JsonUtil.toJSON(user, "id,createTime");
System.out.println("toJSONIncludeFieldTest:"+json);
```

运行结果：toJSONIncludeFieldTest:{"id":"98","createTime":"2019-05-06 16:49:07"}<br><br>

(4)属性过滤：排除指定的字段，本例中输出除createTime外的所有其他属性<br>

```java
String json = JsonUtil.toJSON(user, "createTime",FilterFeature.Except);
System.out.println("toJSONExceptFieldTest:"+json);
```

运行结果：toJSONExceptFieldTest:{"id":"98","userName":"用户名1","dept":{"id":"11","deptName":"部门名称1"}}<br><br>

(5)属性过滤：指定多个Bean属性过滤器进行过滤，本例中Dept对象不输出deptName属性，User对象只输出id和dept属性<br>

```java
String json = JsonUtil.toJSON(user,
	new BeanFilter<>(Dept.class,"deptName",FilterFeature.Except),
	new BeanFilter<>(User.class,"id,dept",FilterFeature.Include));
System.out.println("toJSONBeanFilterTest:"+json);
```

运行结果：toJSONBeanFilterTest:{"id":"98","dept":{"id":"11"}}<br>
特别说明：<br>
（i）dept的subs属性因为全局初始化时不输出null属性顾忽略。<br>
（ii）属性过滤器BeanFilter最多定义10个<br>
