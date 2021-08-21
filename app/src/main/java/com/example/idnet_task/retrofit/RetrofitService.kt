package com.example.idnet_task.retrofit

import com.example.idnet_task.model.Cars
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("/{cars}")
    fun getPosts(@Path("cars") cars: String = "cars"): Call<List<Cars>>

    @POST("cars")
    fun postCar(@Body cars: Cars): Call<Cars>

    @DELETE("cars/{id}")
    fun deleteCar(@Path("id") id: Int): Call<Unit>

    @FormUrlEncoded
    @PATCH("cars/{id}")
    fun updateCar(
        @Path("id") id: Int?,
        @Field("carImage") carImage: String,
        @Field("carName") carName: String,
        @Field("descriptionText") descriptionText: String

    ): Call<Cars>
}