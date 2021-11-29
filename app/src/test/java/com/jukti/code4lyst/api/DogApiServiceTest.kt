package com.jukti.code4lyst.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jukti.code4lyst.data.model.Breed
import com.jukti.unrd.utilities.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.collection.IsCollectionWithSize.hasSize


@RunWith(JUnit4::class)
class DogApiServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: DogApiService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApiService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }

    @Test
    fun getAllDogBreedsResponseTest() {
        runBlocking {
            enqueueResponse("get_breeds_response.json")
            val response = (service.getAllBreeds()as List<Breed>)

            val request = mockWebServer.takeRequest()
            assertThat(
                request.path,
                `is`("/v1/breeds/")
            )
            assertThat<List<Breed>>(response, notNullValue())
            assertThat(response, hasSize(greaterThan(0)))
        }
    }
}