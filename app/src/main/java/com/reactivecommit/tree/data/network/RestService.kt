package com.reactivecommit.tree.data.network

import com.reactivecommit.tree.data.CharacterRes
import com.reactivecommit.tree.data.HouseRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestService {
    @GET("houses")
    suspend fun getHouses(@Query("page") page: Int, @Query("pageSize") pageSize: Int = 50): Response<List<HouseRes>>

    @GET("characters/{id}")
    suspend fun getCharacter(@Path("id") id: String): Response<CharacterRes>
}