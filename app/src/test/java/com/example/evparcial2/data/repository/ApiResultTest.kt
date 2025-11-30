package com.example.evparcial2.data.repository

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ApiResultTest {

    @Test
    fun `Success debe contener datos correctos`() {
        val data = "Test Data"
        val result = ApiResult.Success(data)
        
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat(result.data).isEqualTo(data)
    }

    @Test
    fun `Error debe contener mensaje y código`() {
        val message = "Error occurred"
        val code = 404
        val result = ApiResult.Error(message, code)
        
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        assertThat(result.message).isEqualTo(message)
        assertThat(result.code).isEqualTo(code)
    }

    @Test
    fun `Error debe funcionar sin código`() {
        val message = "Error without code"
        val result = ApiResult.Error(message)
        
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        assertThat(result.message).isEqualTo(message)
        assertThat(result.code).isNull()
    }

    @Test
    fun `Loading debe ser instancia única`() {
        val loading1 = ApiResult.Loading
        val loading2 = ApiResult.Loading
        
        assertThat(loading1).isInstanceOf(ApiResult.Loading::class.java)
        assertThat(loading1).isSameInstanceAs(loading2)
    }

    @Test
    fun `onSuccess debe ejecutarse solo para Success`() {
        var executed = false
        val successResult = ApiResult.Success("data")
        
        successResult.onSuccess { 
            executed = true
            assertThat(it).isEqualTo("data")
        }
        
        assertThat(executed).isTrue()
    }

    @Test
    fun `onSuccess no debe ejecutarse para Error`() {
        var executed = false
        val errorResult = ApiResult.Error("error")
        
        errorResult.onSuccess { executed = true }
        
        assertThat(executed).isFalse()
    }

    @Test
    fun `onSuccess no debe ejecutarse para Loading`() {
        var executed = false
        val loadingResult = ApiResult.Loading
        
        loadingResult.onSuccess { executed = true }
        
        assertThat(executed).isFalse()
    }

    @Test
    fun `onError debe ejecutarse solo para Error`() {
        var executed = false
        var receivedMessage = ""
        var receivedCode: Int? = null
        
        val errorResult = ApiResult.Error("test error", 500)
        
        errorResult.onError { message, code ->
            executed = true
            receivedMessage = message
            receivedCode = code
        }
        
        assertThat(executed).isTrue()
        assertThat(receivedMessage).isEqualTo("test error")
        assertThat(receivedCode).isEqualTo(500)
    }

    @Test
    fun `onError no debe ejecutarse para Success`() {
        var executed = false
        val successResult = ApiResult.Success("data")
        
        successResult.onError { _, _ -> executed = true }
        
        assertThat(executed).isFalse()
    }

    @Test
    fun `onError no debe ejecutarse para Loading`() {
        var executed = false
        val loadingResult = ApiResult.Loading
        
        loadingResult.onError { _, _ -> executed = true }
        
        assertThat(executed).isFalse()
    }

    @Test
    fun `onLoading debe ejecutarse solo para Loading`() {
        var executed = false
        val loadingResult = ApiResult.Loading
        
        loadingResult.onLoading { executed = true }
        
        assertThat(executed).isTrue()
    }

    @Test
    fun `onLoading no debe ejecutarse para Success`() {
        var executed = false
        val successResult = ApiResult.Success("data")
        
        successResult.onLoading { executed = true }
        
        assertThat(executed).isFalse()
    }

    @Test
    fun `onLoading no debe ejecutarse para Error`() {
        var executed = false
        val errorResult = ApiResult.Error("error")
        
        errorResult.onLoading { executed = true }
        
        assertThat(executed).isFalse()
    }

    @Test
    fun `debe encadenar operaciones correctamente`() {
        var successExecuted = false
        var errorExecuted = false
        var loadingExecuted = false
        
        val result = ApiResult.Success("test")
            .onSuccess { successExecuted = true }
            .onError { _, _ -> errorExecuted = true }
            .onLoading { loadingExecuted = true }
        
        assertThat(successExecuted).isTrue()
        assertThat(errorExecuted).isFalse()
        assertThat(loadingExecuted).isFalse()
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
    }

    @Test
    fun `Success con diferentes tipos debe funcionar correctamente`() {
        val intResult = ApiResult.Success(42)
        val stringResult = ApiResult.Success("hello")
        val listResult = ApiResult.Success(listOf(1, 2, 3))
        
        assertThat(intResult.data).isEqualTo(42)
        assertThat(stringResult.data).isEqualTo("hello")
        assertThat(listResult.data).hasSize(3)
        assertThat(listResult.data).contains(2)
    }

    @Test
    fun `Error con diferentes códigos debe funcionar correctamente`() {
        val error404 = ApiResult.Error("Not Found", 404)
        val error500 = ApiResult.Error("Server Error", 500)
        val errorNoCode = ApiResult.Error("Generic Error")
        
        assertThat(error404.code).isEqualTo(404)
        assertThat(error500.code).isEqualTo(500)
        assertThat(errorNoCode.code).isNull()
    }
}