package tg.holdemcasino.tgames.network.datasource

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import tg.holdemcasino.tgames.network.data.GameResponseItem

interface GameApi {

    @POST("gamecontent")
    suspend fun getClientId(@Query("appid") appid: String): Response<GameResponseItem>

}