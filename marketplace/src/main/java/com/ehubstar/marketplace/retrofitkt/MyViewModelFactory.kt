package com.ehubstar.marketplace.retrofitkt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

 class MyViewModelFactory(val mainRepository: MainRepository) : ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
         return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
             MainViewModel(this.mainRepository) as T
         } else {
             throw IllegalArgumentException("ViewModel not found")
         }
     }
 }