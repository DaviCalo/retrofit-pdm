package com.smd.retrofitexemplo.network

import com.smd.retrofitexemplo.model.MarsPhoto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>

    // Exemplos fictícios:
    @POST("photos")
    suspend fun createPhoto(@Body photo: MarsPhoto): MarsPhoto

    @PUT("photos/{id}")
    suspend fun updatePhoto(
        @Path("id") photoId: String,
        @Body photo: MarsPhoto
    ): MarsPhoto

    @DELETE("photos/{id}")
    suspend fun deletePhoto(@Path("id") photoId: String): Unit
}
