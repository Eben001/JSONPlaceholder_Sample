package com.ebenezer.gana.jsonplaceholdersample.data.repository

import com.ebenezer.gana.jsonplaceholdersample.data.models.CommentItem
import com.ebenezer.gana.jsonplaceholdersample.data.models.PostItem
import com.ebenezer.gana.jsonplaceholdersample.utils.Result
import retrofit2.Response

interface JsonPlaceholderRepository {

    suspend fun getPosts(): Result<Response<List<PostItem>>>
    suspend fun getComment(postId:Int): Result<Response<List<CommentItem>>>
}