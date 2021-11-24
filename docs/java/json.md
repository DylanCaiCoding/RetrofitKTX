# 提交 Json 数据

Retrofit 提交 Json 数据需要把请求方法的参数改成 RequestBody 并加上 `@Body` 注解，例如：

```java
@Post("/user/login")
Single<ApiResponse<User>> login(@Body RequestBody requestBody);
```

如果是想直接用键值对请求，可以用 RequestBodyFactory 创建 RequestBody 对象，例如：

```java
Map<String, Object> params = new HashMap<>();
params.put("username", username);
params.put("password", password);
api.login(RequestBodyFactory.create(params))
  ...
```

如果已有请求的 Json 的实体类，例如：

```java
public class UserForm {
  private String username;
  private String pwd;

  public UserForm(String username, String pwd) {
    this.username = username;
    this.pwd = pwd;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }
}
```

那么可以将该类对象用 RequestBodyFactory 转成 RequestBody 对象，例如：

```java
UserForm userForm = new UserForm(username, pwd);
api.login(RequestBodyFactory.create(userForm))
  ...
```