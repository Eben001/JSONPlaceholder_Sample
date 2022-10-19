package com.ebenezer.gana.jsonplaceholdersample.ui.postDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.jsonplaceholdersample.data.models.CommentItem
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
class PostDetailsViewModel @Inject constructor(
    private val
    repository: JsonPlaceholderRepository
) : ViewModel() {
    private val _commentResult = MutableStateFlow<Result<List<CommentItem>>?>(null)
    val commentResult: StateFlow<Result<List<CommentItem>>?> = _commentResult
        .stateIn(
            initialValue = Result.Loading(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val _errorMessageSharedFlow = MutableSharedFlow<String>()
    val errorMessageSharedFlow = _errorMessageSharedFlow.asSharedFlow()


    fun getComments(postId:Int){
        _commentResult.value = Result.Loading()
        viewModelScope.launch {
            when(val response = repository.getComment(postId)){
                is Result.Error -> {
                    when (response.exception) {
                        is HttpException -> _errorMessageSharedFlow.emit("Something went wrong. Please try again")
                        is UnknownHostException -> _errorMessageSharedFlow.emit("Unable to get comments. Please check your internet connection and try again")
                        is IOException -> _errorMessageSharedFlow.emit("Network Failure")
                        else -> _errorMessageSharedFlow.emit("Something went wrong. Please try again")
                    }
                }
                is Result.Success -> {
                    if (response.data.isSuccessful) {
                        response.data.body()?.let {
                            _commentResult.value = Result.Success(it)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}