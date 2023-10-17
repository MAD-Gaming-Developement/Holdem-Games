package tg.holdemcasino.tgames.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tg.holdemcasino.tgames.BaseApp
import tg.holdemcasino.tgames.network.data.GameResponseItem
import tg.holdemcasino.tgames.network.repository.GameRepo
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class GameViewModels  @Inject constructor(
    val gameRepo: GameRepo
) : ViewModel() {

    val clientGameLiveData = MutableLiveData<GameResponseItem>()

    fun fetchClientData() = viewModelScope.launch {
        try {
            val response = gameRepo.getGameResponse(BaseApp.appCode)
            if (response.isSuccessful){
                val  data = response.body()
                clientGameLiveData.postValue(data)

                Log.d("apiReposnse",response.toString())
            }else
                Log.d("apiResponse", "Network call failed")
        }
        catch (e:Exception){
            Log.d("apiResponse",e.toString())
        }
    }
}