package com.leng.jingzhekt.data.remote.config

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.leng.jingzhekt.data.remote.dto.ApiResponse
import com.leng.jingzhekt.data.remote.dto.PagedResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 网络配置类
 * 负责配置Retrofit、OkHttp和Gson
 */
object NetworkConfig {
    
    // 基础URL - 请根据您的服务器地址修改
    const val BASE_URL = "http://your-server.com/api/v1/"
    
    // 超时时间配置
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
    
    /**
     * 创建Gson实例
     */
    fun createGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create()
    }
    
    /**
     * 创建OkHttpClient
     */
    fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Network", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor())
            .addInterceptor(ErrorInterceptor())
            .build()
    }
    
    /**
     * 创建Retrofit实例
     */
    fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .build()
    }
}

/**
 * 认证拦截器
 * 用于添加认证头信息
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 这里可以添加认证token
        val token = getAuthToken() // 从SharedPreferences或其他地方获取token
        
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        } else {
            originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        }
        
        return chain.proceed(newRequest)
    }
    
    private fun getAuthToken(): String? {
        // TODO: 从SharedPreferences或其他地方获取token
        return null
    }
}

/**
 * 错误处理拦截器
 * 用于统一处理HTTP错误
 */
class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        
        when (response.code) {
            401 -> {
                // 未授权，可能需要重新登录
                Log.w("Network", "Unauthorized access")
                // TODO: 处理未授权情况
            }
            403 -> {
                // 禁止访问
                Log.w("Network", "Forbidden access")
            }
            404 -> {
                // 资源未找到
                Log.w("Network", "Resource not found")
            }
            500 -> {
                // 服务器内部错误
                Log.e("Network", "Internal server error")
            }
            else -> {
                if (!response.isSuccessful) {
                    Log.e("Network", "HTTP error: ${response.code}")
                }
            }
        }
        
        return response
    }
}

/**
 * 网络异常类
 */
sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NoInternetConnection : NetworkException("No internet connection")
    class ServerError(message: String) : NetworkException("Server error: $message")
    class Unauthorized : NetworkException("Unauthorized access")
    class NotFound : NetworkException("Resource not found")
    class Timeout : NetworkException("Request timeout")
    class UnknownError(message: String) : NetworkException("Unknown error: $message")
}

/**
 * 网络结果包装类
 */
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: NetworkException) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

/**
 * 网络工具类
 */
object NetworkUtils {
    
    /**
     * 处理API响应
     */
    suspend fun <T> handleApiResponse(
        apiCall: suspend () -> retrofit2.Response<ApiResponse<T>>
    ): NetworkResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    NetworkResult.Success(body.data)
                } else {
                    NetworkResult.Error(NetworkException.ServerError(body?.message ?: "Unknown error"))
                }
            } else {
                when (response.code()) {
                    401 -> NetworkResult.Error(NetworkException.Unauthorized())
                    404 -> NetworkResult.Error(NetworkException.NotFound())
                    500 -> NetworkResult.Error(NetworkException.ServerError("Internal server error"))
                    else -> NetworkResult.Error(NetworkException.ServerError("HTTP ${response.code()}"))
                }
            }
        } catch (e: IOException) {
            NetworkResult.Error(NetworkException.NoInternetConnection())
        } catch (e: Exception) {
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Unknown error"))
        }
    }
    
    /**
     * 处理分页API响应
     */
    suspend fun <T> handlePagedApiResponse(
        apiCall: suspend () -> retrofit2.Response<ApiResponse<PagedResponse<T>>>
    ): NetworkResult<PagedResponse<T>> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    NetworkResult.Success(body.data)
                } else {
                    NetworkResult.Error(NetworkException.ServerError(body?.message ?: "Unknown error"))
                }
            } else {
                when (response.code()) {
                    401 -> NetworkResult.Error(NetworkException.Unauthorized())
                    404 -> NetworkResult.Error(NetworkException.NotFound())
                    500 -> NetworkResult.Error(NetworkException.ServerError("Internal server error"))
                    else -> NetworkResult.Error(NetworkException.ServerError("HTTP ${response.code()}"))
                }
            }
        } catch (e: IOException) {
            NetworkResult.Error(NetworkException.NoInternetConnection())
        } catch (e: Exception) {
            NetworkResult.Error(NetworkException.UnknownError(e.message ?: "Unknown error"))
        }
    }
}

