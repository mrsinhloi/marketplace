package com.ehubstar.marketplace

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import com.ehubstar.marketplace.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.net.URL
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadImage()

//        testCoroutines()
        testScope()


    }

    private val IMAGE_URL =
        "https://images.unsplash.com/photo-1561336313-0bd5e0b27ec8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private fun getImageFromUrl(): Bitmap =
        URL(IMAGE_URL).openStream().use {
            BitmapFactory.decodeStream(it)
        }

    private fun setImage(bmp: Bitmap) {
        binding.imageView.setImageBitmap(bmp)
        showLoading(false)

    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.imageView.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.imageView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun loadImage() {
        showLoading(true)
        coroutineScope.launch {

            val remoteImageDeferred = coroutineScope.async(Dispatchers.IO) {
                getImageFromUrl()
            }
            val bitmap = remoteImageDeferred.await()
            setImage(bitmap)

            delay(3000)
            val filterBitmap = Filter.apply(bitmap)
            setImage(filterBitmap)
            /*launch(Dispatchers.Default) {
                val filterBitmap = Filter.apply(bitmap)
                withContext(Dispatchers.Main) {
                    setImage(filterBitmap)
                }
            }*/
        }
    }


    //https://viblo.asia/p/kotlin-coroutines-trong-android-ByEZkVy4KQ0
    //test////
    suspend fun readDB() {
        withContext(Dispatchers.IO) {
            printThread(3)//separate DefaultDispatcher-worker-3
        }
    }

    /**
     * 2022-06-20 11:45:06.182 3778-3778/com.ehubstar.marketplace I/System.out: my-log: 1 main
    2022-06-20 11:45:06.185 3778-3803/com.ehubstar.marketplace I/System.out: my-log: 3 DefaultDispatcher-worker-1
    2022-06-20 11:45:06.496 3778-3778/com.ehubstar.marketplace I/System.out: my-log: 2 main
    2022-06-20 11:45:06.498 3778-3806/com.ehubstar.marketplace I/System.out: my-log: 4 DefaultDispatcher-worker-4
    2022-06-20 11:45:06.513 3778-3803/com.ehubstar.marketplace I/System.out: my-log: 5 DefaultDispatcher-worker-1
    2022-06-20 11:45:06.514 3778-3778/com.ehubstar.marketplace I/System.out: my-log: User1 and User2
     */
    fun testCoroutines() {
        //global scope : activity destroy - functions run continue
        GlobalScope.launch(Dispatchers.Main + exceptionHandler) {
            printThread(1)//main
            readDB()
            printThread(2)//main

            //network call
            val user1 = fetchUser1()
            val user2 = fetchUser2()
            println("my-log: $user1 and $user2")
        }
    }

    fun printThread(from: Int) {
        println("my-log: $from ${Thread.currentThread().name}")
    }

    /////
    suspend fun fetchUser1(): String {
        /*return GlobalScope.async(Dispatchers.IO) {
            "User1"
        }.await()*/
        //==> ngan gon hon
        return withContext(Dispatchers.IO) {
            printThread(4)
            delay(3000)
//            binding.imageView.visibility = View.GONE
            "User1"
        }


    }

    suspend fun fetchUser2(): String {
        return withContext(Dispatchers.IO) {
            printThread(5)
            delay(5000)
            "User2"
        }
    }

    //test////


    //coroutineScope - activity destroy job will be destroy
    lateinit var job: Job //init in onCreate - cancel in onDestroy
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + exceptionHandler

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    /**
     * activity destroy job will be destroy
     * functions will be stopped
     */
    fun testScope() {
        launch {
            val user1 = fetchUser1()
            val user2 = fetchUser2()
            println("my-log: $user1 and $user2")
        }
    }
    //coroutineScope

    //exception
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("my-log: exception ${throwable.localizedMessage}")

    }

    @WorkerThread
    suspend fun callback() {
        val user1 = fetchUser1()

    }

}