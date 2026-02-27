import com.example.core.network.adapter.safeApiCall
import com.example.core.network.result.NetworkResult
import com.example.di.networkModule
import com.example.network.ApiService
import com.example.network.getPostWithValidation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin

fun main() = runBlocking {
    val koinApp = startKoin {
        modules(networkModule)
    }
    
    val apiService = koinApp.koin.get<ApiService>()

    println("--- Starting Production-Ready Network Call ---")

    // Example 1: 使用带输入校验的 API 调用（正常情况）
    println("\n=== Example 1: Valid Input ===")
    safeApiCall { apiService.getPostWithValidation(1) }.collect { result ->
        when (result) {
            is NetworkResult.Loading -> println("Status: Loading...")
            is NetworkResult.Success -> {
                println("Status: Success")
                println("Data: ${result.data}")
            }
            is NetworkResult.Error -> {
                println("Status: Error")
                println("Message: ${result.message}")
                result.exception?.printStackTrace()
            }
        }
    }
    
    // Example 2: 测试输入校验（无效输入）
    println("\n=== Example 2: Invalid Input (Input Validation) ===")
    safeApiCall { apiService.getPostWithValidation(-1) }.collect { result ->
        when (result) {
            is NetworkResult.Loading -> println("Status: Loading...")
            is NetworkResult.Success -> {
                println("Status: Success")
                println("Data: ${result.data}")
            }
            is NetworkResult.Error -> {
                println("Status: Error (Expected - Input Validation)")
                println("Message: ${result.message}")
            }
        }
    }
}
