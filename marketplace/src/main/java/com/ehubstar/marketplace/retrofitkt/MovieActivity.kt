package com.ehubstar.marketplace.retrofitkt

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ehubstar.marketplace.databinding.MpActivityMovieBinding

class MovieActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private val adapter = MovieAdapter()
    lateinit var binding: MpActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MpActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rv.adapter = adapter

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)

        viewModel.movieList.observe(this) { list ->
            adapter.setMovies(list)
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) { loading ->
            binding.progressDialog.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.getAllMovies()

    }

}