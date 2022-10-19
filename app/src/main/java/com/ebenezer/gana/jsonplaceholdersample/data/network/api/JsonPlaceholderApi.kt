package com.ebenezer.gana.jsonplaceholdersample.data.network.api

import com.ebenezer.gana.jsonplaceholdersample.data.models.PostItem
import retrofit2.Response
import retrofit2.http.GET

interface JsonPlaceholderApi {

    @GET("/posts")
    suspend fun getPosts():Response<List<PostItem>>
}