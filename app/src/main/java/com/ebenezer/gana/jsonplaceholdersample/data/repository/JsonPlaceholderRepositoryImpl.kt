package com.ebenezer.gana.jsonplaceholdersample.data.repository

import com.ebenezer.gana.jsonplaceholdersample.data.models.CommentItem
import com.ebenezer.gana.jsonplaceholdersample.data.models.PostItem
import com.ebenezer.gana.jsonplaceholdersample.data.network.api.JsonPlaceholderApi
import com.ebenezer.gana.jsonplaceholdersample.utils.Result
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class JsonPlaceholderRepositoryImpl @Inject constructor(
    private val jsonPlaceholderApi: JsonPlaceholderApi
) : JsonPlaceholderRepository {

    override suspend fun getPosts(): Result<Response<List<PostItem>>> {
        return try {
            val data = jsonPlaceholderApi.getPosts()
            Result.Success(data)
        } catch (error: HttpException) {
            Result.Error(error)
        } catch (error: UnknownHostException) {
            Result.Error(error)
        } catch (error: Exception) {
            Result.Error(error)
        }
    }

    override suspend fun getComment(postId: Int): Result<Response<List<CommentItem>>> {
        return try {
            val data = jsonPlaceholderApi.getComments(postId)
            Result.Success(data)
        } catch (error: HttpException) {
            Result.Error(error)
        } catch (error: UnknownHostException) {
            Result.Error(error)
        } catch (error: Exception) {
            Result.Error(error)
        }
    }
}