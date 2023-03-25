package com.thorcode.githubuserapp

import com.thorcode.githubuserapp.api.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileInputStream

class ApiServiceTest {

    private val mockWebServer = MockWebServer()
    private var apiService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun getUserDetail(){
        mockResponse("dummy_user_detail.json")
        runBlocking {
            val response = apiService.getDetailUser("thrqhdyt")
            Assert.assertEquals("username tidak sama", "thrqhdyt",response.login)
        }
    }

    private fun mockResponse(fileName: String){
        val inputStream = FileInputStream("D:\\KULIAH\\BANGKIT\\mobile-development\\submission\\android-fundamental\\GithubUserApp\\app\\src\\test\\java\\com\\thorcode\\githubuserapp\\$fileName")
        val buffer = inputStream
            .source().buffer()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(buffer.readString(Charsets.UTF_8))
        )
    }
}