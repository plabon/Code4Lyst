package com.jukti.code4lyst.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.jukti.clearscoredemo.utils.TestUtil
import com.jukti.code4lyst.data.model.ResponseWrapper
import com.jukti.code4lyst.domain.GetDogBreedsUseCase
import com.jukti.code4lyst.domain.GetImagesByBreedUseCase
import com.jukti.code4lyst.domain.model.BreedDomainModel
import com.jukti.code4lyst.domain.model.ImageDomainModel
import com.jukti.code4lyst.presentation.DogSearchViewModel
import com.jukti.code4lyst.presentation.DogSearchViewState
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private val testDispatcher = coroutineRule.testDispatcher

    private val getDogBreedUseCase = mock(GetDogBreedsUseCase::class.java)
    private val getImagesByBreedUseCase = mock(GetImagesByBreedUseCase::class.java)

    private lateinit var dogSearchViewModel: DogSearchViewModel

    @Before
    fun setup(){
        coroutineRule.runBlockingTest {
            val responseFlow = flow<ResponseWrapper<List<BreedDomainModel>>> {
                emit(TestUtil.createBreedResponseWrapper())
            }
            `when`(getDogBreedUseCase.execute()).thenReturn(responseFlow)
            dogSearchViewModel = DogSearchViewModel(getDogBreedUseCase,getImagesByBreedUseCase)
        }
    }

    @Test
    fun testCallGetDogBreedUseCaseAndVerifyViewState() {
        coroutineRule.runBlockingTest {
            dogSearchViewModel.mState.test {
                assertEquals(awaitItem(), DogSearchViewState.SuccessResponseBreed(TestUtil.createDogBreedTestResponse()))
                dogSearchViewModel.fetchAllBreeds()
                assertEquals(awaitItem(), DogSearchViewState.IsLoading(true))
                assertEquals(awaitItem(), DogSearchViewState.IsLoading(false))
                assertEquals(awaitItem(), DogSearchViewState.SuccessResponseBreed(TestUtil.createDogBreedTestResponse()))
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun testCallGetImagesUseCaseAndVeifyViewState() {
        coroutineRule.runBlockingTest {
            dogSearchViewModel.images.test {
                val responseFlow = flow<ResponseWrapper<List<ImageDomainModel>>> {
                    emit(TestUtil.createImageResponseWrapper())
                }
                `when`(getImagesByBreedUseCase.execute(1,10)).thenReturn(responseFlow)
                dogSearchViewModel.setSelectedBreedId(1)
                assertEquals(awaitItem(), DogSearchViewState.IsLoading(true))
                assertEquals(awaitItem(), DogSearchViewState.IsLoading(false))
                assertEquals(awaitItem(), DogSearchViewState.SuccessResponseImage(TestUtil.createDogImagesTestResponse()))
                cancelAndConsumeRemainingEvents()
            }
        }
    }





}