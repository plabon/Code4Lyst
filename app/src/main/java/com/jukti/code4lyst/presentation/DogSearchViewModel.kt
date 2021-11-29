package com.jukti.code4lyst.presentation

import androidx.lifecycle.*
import com.jukti.code4lyst.data.model.ResponseWrapper
import com.jukti.code4lyst.domain.GetDogBreedsUseCase
import com.jukti.code4lyst.domain.GetImagesByBreedUseCase
import com.jukti.code4lyst.domain.model.BreedDomainModel
import com.jukti.code4lyst.domain.model.ImageDomainModel
import com.jukti.unrd.utilities.LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DogSearchViewModel @Inject constructor(
    private val getDogBreedsUseCase: GetDogBreedsUseCase,
    private val getImagesByBreedUseCase: GetImagesByBreedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<DogSearchViewState>(DogSearchViewState.Init)
    val mState: StateFlow<DogSearchViewState> get() = _state


    private val _selectedBreedId = MutableStateFlow<Int>(-1)
    fun setSelectedBreedId(value:Int){
        _selectedBreedId.value = value
    }

    init {
        fetchAllBreeds()
    }

    val images = _selectedBreedId
        .filter { query->
            return@filter query!=-1
        }
        .transformLatest<Int,DogSearchViewState> {
            getImagesByBreedUseCase.execute(it, LIMIT)
                .onStart { emit(DogSearchViewState.IsLoading(true)) }
                .catch {
                    emit(DogSearchViewState.IsLoading(false))
                    emit(DogSearchViewState.ErrorResponse("UnknownError"))
                }
                .collect {
                    emit(DogSearchViewState.IsLoading(false))
                    when(it){
                        is ResponseWrapper.NetworkError -> emit(DogSearchViewState.ShowToast("Please check your network Conection!"))
                        is ResponseWrapper.GenericError -> it.error?.let { msg ->
                            emit(DogSearchViewState.ShowToast(
                                msg
                            ))
                        }
                        is ResponseWrapper.Success -> emit(DogSearchViewState.SuccessResponseImage(it.value))
                    }
                }
        }.stateIn(
            scope = viewModelScope,
            initialValue = DogSearchViewState.IsLoading(true),
            started = SharingStarted.WhileSubscribed(5000)
        )


    fun fetchAllBreeds(){
        viewModelScope.launch {
            getDogBreedsUseCase.execute()
                .onStart {
                    _state.value = DogSearchViewState.IsLoading(true)
                }.catch {
                    _state.value = DogSearchViewState.IsLoading(false)
                    _state.value = DogSearchViewState.ErrorResponse("UnknownError")
                }
                .collect {
                    _state.value = DogSearchViewState.IsLoading(false)
                    when(it){
                        is ResponseWrapper.NetworkError -> _state.value =
                            DogSearchViewState.ShowToast("Please check your network Conection!")
                        is ResponseWrapper.GenericError -> {
                            it.error?.let { msg ->
                                _state.value = DogSearchViewState.ShowToast(
                                    msg
                                )
                            }
                        }
                        is ResponseWrapper.Success ->
                            _state.value = DogSearchViewState.SuccessResponseBreed(it.value)
                    }
                }
        }
    }



    override fun onCleared() {
        super.onCleared()
    }

    companion object{
        private const val TAG = "SearchViewModel"
    }

}

sealed class DogSearchViewState  {
    object Init : DogSearchViewState()
    data class IsLoading(val isLoading: Boolean) : DogSearchViewState()
    data class ShowToast(val message: String) : DogSearchViewState()
    data class SuccessResponseBreed(val breedList: List<BreedDomainModel>) : DogSearchViewState()
    data class SuccessResponseImage(val imageList: List<ImageDomainModel>) : DogSearchViewState()
    data class ErrorResponse(val message : String) : DogSearchViewState()
}