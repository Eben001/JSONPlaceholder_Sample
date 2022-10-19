package com.ebenezer.gana.jsonplaceholdersample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.jsonplaceholdersample.data.models.PostItem
import com.ebenezer.gana.jsonplaceholdersample.data.repository.JsonPlaceholderRepository
import com.ebenezer.gana.jsonplaceholdersample.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: JsonPlaceholderRepository
) : ViewModel() {

    private val _postsResult = MutableStateFlow<Result<List<PostItem>>?>(null)
    val postsResult: StateFlow<Result<List<PostItem>>?> = _postsResult
        .stateIn(
            initialValue = Result.Loading(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val _errorMessageSharedFlow = MutableSharedFlow<String>()
    val errorMessageSharedFlow = _errorMessageSharedFlow.asSharedFlow()

    fun getPosts() {
        _postsResult.value = Result.Loading()
        viewModelScope.launch {
            when (val response = repository.getPosts()) {
                is Result.Error -> {
                    when (response.exception) {
                        is HttpException -> _errorMessageSharedFlow.emit("Something went wrong. Please try again")
                        is UnknownHostException -> _errorMessageSharedFlow.emit("Please check your internet connection and try again")
                        is IOException -> _errorMessageSharedFlow.emit("Network Failure")
                        else -> _errorMessageSharedFlow.emit("Something went wrong. Please try again")
                    }
                }
                is Result.Success -> {
                    if (response.data.isSuccessful) {
                        response.data.body()?.let {
                            _postsResult.value = Result.Success(it)
                        }
                    }
                }
                else -> {}
            }

        }
    }

}