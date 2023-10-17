package tg.holdemcasino.tgames.network.repository

import tg.holdemcasino.tgames.network.datasource.GameApi
import javax.inject.Inject

class GameRepo @Inject constructor(
    val gameApi: GameApi
) {
    suspend fun getGameResponse(appId : String) = gameApi.getClientId(appId)
}