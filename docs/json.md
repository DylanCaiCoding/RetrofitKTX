# 提交 Json 数据

### 使用键值对请求

将方法参数定义为 RequestMap，并用 `@Body` 注解修饰，例如：

```kotlin
@Post("/user/login")
fun login(@Body params: RequestMap): Single<ApiResponse<User>>
```

发起请求，可用 `mapOf()` 方法创建键值对，例如：

```kotlin
api.login(mapOf("username" to username, "pwd" to pwd)
  ...
```

### 使用实体类请求

如果已有请求的 Json 的实体类，例如：

```kotlin
data class UserForm(val username: String, val pwd: String)
```

那么可以将该类对象作为接口方法参数，并用 `@Body` 修饰，例如：

```kotlin
@Post("/user/login")
fun login(@Body userForm: UserForm): Single<ApiResponse<User>>
```

发起请求：

```kotlin
val userForm = UserForm(username, pwd)
api.login(userForm)
  ...
```