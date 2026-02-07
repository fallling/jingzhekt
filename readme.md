## 🏗️ 架构概览

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UI Layer      │    │  ViewModel      │    │  Repository     │
│   (Compose)     │◄──►│   (Hilt)        │◄──►│   (Remote)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                         │
                                                         ▼
                                               ┌─────────────────┐
                                               │ RemoteDataSource│
                                               │   (Retrofit)    │
                                               └─────────────────┘
                                                         │
                                                         ▼
                                               ┌─────────────────┐
                                               │   MySQL API     │
                                               │   (Backend)     │
                                               └─────────────────┘
```

## 📁 文件结构

```
app/src/main/java/com/leng/jingzhekt/
├── data/
│   ├── remote/
│   │   ├── api/
│   │   │   └── JingzheApiService.kt          # API接口定义
│   │   ├── config/
│   │   │   └── NetworkConfig.kt              # 网络配置
│   │   ├── datasource/
│   │   │   └── RemoteDataSource.kt           # 远程数据源
│   │   └── dto/
│   │       └── ApiModels.kt                  # API数据模型
│   └── repository/
│       └── RemoteRepository.kt               # Repository实现
├── di/
│   └── NetworkModule.kt                      # Hilt网络模块
└── presentation/
    ├── viewmodel/
    │   └── ViewModel.kt            # 示例ViewModel
    └── ui/screens/
        └── Home.kt               #Ui界面
```

## 🚀 快速开始

### 1. 配置服务器地址

在 `NetworkConfig.kt` 中修改 `BASE_URL`：

```kotlin
const val BASE_URL = "http://your-server.com/api/v1/"
```

### 2. 配置认证

在 `AuthInterceptor` 中添加认证token：

```kotlin
private fun getAuthToken(): String? {
    // 从SharedPreferences或其他地方获取token
    return "your-auth-token"
}
```

### 3. 使用示例

```kotlin
@HiltViewModel
class YourViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    
    fun loadTransactions() {
        viewModelScope.launch {
            remoteRepository.getTransactions()
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            // 处理成功数据
                        }
                        is NetworkResult.Error -> {
                            // 处理错误
                        }
                        is NetworkResult.Loading -> {
                            // 处理加载状态
                        }
                    }
                }
        }
    }
}
```

## 📋 API接口

### 交易记录
- `GET /transactions` - 获取交易记录列表
- `GET /transactions/{id}` - 获取单个交易记录
- `POST /transactions` - 创建交易记录
- `PUT /transactions/{id}` - 更新交易记录
- `DELETE /transactions/{id}` - 删除交易记录

### 分类管理
- `GET /categories` - 获取分类列表
- `POST /categories` - 创建分类
- `PUT /categories/{id}` - 更新分类
- `DELETE /categories/{id}` - 删除分类

### 账户管理
- `GET /accounts` - 获取账户列表
- `POST /accounts` - 创建账户
- `PUT /accounts/{id}` - 更新账户
- `DELETE /accounts/{id}` - 删除账户

### 统计查询
- `GET /statistics/monthly` - 获取月度统计
- `GET /statistics/account-balances` - 获取账户余额
- `GET /statistics/category-summary` - 获取分类统计

## 🔧 配置说明

### 网络超时
```kotlin
private const val CONNECT_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L
```

### 日志级别
```kotlin
val loggingInterceptor = HttpLoggingInterceptor { message ->
    Log.d("Network", message)
}.apply {
    level = HttpLoggingInterceptor.Level.BODY
}
```

## 🛠️ 错误处理

服务提供了完整的错误处理机制：

```kotlin
sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NoInternetConnection : NetworkException("No internet connection")
    class ServerError(message: String) : NetworkException("Server error: $message")
    class Unauthorized : NetworkException("Unauthorized access")
    class NotFound : NetworkException("Resource not found")
    class Timeout : NetworkException("Request timeout")
    class UnknownError(message: String) : NetworkException("Unknown error: $message")
}
```

## 📱 UI集成

使用 `RemoteDataScreen` 作为参考，展示如何在Compose中集成远程数据：

```kotlin
@Composable
fun YourScreen(viewModel: YourViewModel = hiltViewModel()) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    
    when (transactions) {
        is NetworkResult.Loading -> CircularProgressIndicator()
        is NetworkResult.Success -> TransactionList(transactions.data)
        is NetworkResult.Error -> ErrorMessage(transactions.exception.message)
    }
}
```

## 🔒 安全考虑

1. **HTTPS**: 生产环境必须使用HTTPS
2. **认证**: 实现适当的认证机制
3. **数据验证**: 在客户端和服务端都进行数据验证
4. **错误处理**: 不要暴露敏感的错误信息

## 📝 注意事项

1. 确保网络权限已添加到 `AndroidManifest.xml`
2. 在生产环境中关闭详细日志
3. 实现适当的缓存策略
4. 考虑离线模式的支持

## 🎯 下一步

1. 实现数据缓存机制
2. 添加离线支持
3. 实现数据同步
4. 添加更多统计功能
5. 优化网络性能

