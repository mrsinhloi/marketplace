package com.ehubstar.marketplace.retrofitkt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ehubstar.marketplace.models.Movie
import kotlinx.coroutines.*

class MainViewModel constructor(val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val movieList = MutableLiveData<List<Movie>>()
    var job:Job? = null

    var loading = MutableLiveData<Boolean>()

    val exceptionHandler = CoroutineExceptionHandler{_, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")

    }

    fun getAllMovies(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getAllMovies()
            //tro ve main thread
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    movieList.postValue(response.body())
                    loading.postValue(false)
                }else{
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun onError(message:String){
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}