package com.ebenezer.gana.jsonplaceholdersample.data.network.api

import com.ebenezer.gana.jsonplaceholdersample.data.models.CommentItem
import com.ebenezer.gana.jsonplaceholdersample.data.models.PostItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceholderApi {

    @GET("/posts")
    suspend fun getPosts(): Response<List<PostItem>>

    @GET("/comments")
    suspend fun getComments(@Query("postId") postId: Int): Response<List<CommentItem>>
}