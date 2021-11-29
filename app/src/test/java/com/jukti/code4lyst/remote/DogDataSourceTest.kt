package com.jukti.code4lyst.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jukti.code4lyst.api.DogApiService
import com.jukti.code4lyst.data.model.ResponseWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DogDataSourceTest{

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var dispatcher:CoroutineDispatcher
    lateinit var datasource:DogDataSource
    private lateinit var service: DogApiService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp(){
        dispatcher = TestCoroutineDispatcher()
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApiService::class.java)

        datasource = DogDataSource(service,dispatcher)
    }


    @Test
    fun successResponseTest() {
        runBlockingTest {
            val lambdaResult = true
            val result = datasource.safeApiCall(apiCall = {lambdaResult})
            assertEquals(ResponseWrapper.Success(lambdaResult), result)
        }
    }

    @Test
    fun ioExceptionTest() {
        runBlockingTest {
            val result = datasource.safeApiCall (apiCall = { throw IOException() })
            assertEquals(ResponseWrapper.NetworkError, result)
        }
    }


    @Test
    fun genericExceptionWithErorCodeTest() {
        val errorBody = "{'Internal server error'}".toResponseBody("application/json".toMediaTypeOrNull())

        runBlockingTest {
            val result = datasource.safeApiCall( apiCall = {
                throw HttpException(Response.error<Any>(500, errorBody))
            })
            assert(result is ResponseWrapper.GenericError)
        }
    }

    @Test
    fun unknownExceptionTest() {
        runBlockingTest {
            val result = datasource.safeApiCall(apiCall = {
                throw IllegalStateException()
            })
            assertEquals(ResponseWrapper.GenericError(), result)
        }
    }
}
