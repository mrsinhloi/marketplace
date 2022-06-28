package com.ehubstar.marketplace.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ehubstar.marketplace.R
import com.ehubstar.marketplace.base.BaseActivity
import com.ehubstar.marketplace.base.BaseAdapter
import com.ehubstar.marketplace.base.MyBindingInterface
import com.ehubstar.marketplace.databinding.MpActivityMovieBinding
import com.ehubstar.marketplace.models.Movie

class MH00_Template(override val bindingInflater: (LayoutInflater) -> MpActivityMovieBinding = MpActivityMovieBinding::inflate) :
    BaseActivity<MpActivityMovieBinding>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MH00_Template::class.java))
        }
    }

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        ////
//        setSupportActionBar(binding.toolbar)
//        binding.toolbar.setNavigationOnClickListener { finish() }
//        binding.toolbar.title = ""//getString(R.string.choose_division)


//        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        rv.itemAnimator = DefaultItemAnimator()
//        getData()

    }

   /* var adapter: MH00_TemplateAdapter? = null
    private suspend fun getData() {
        if (!isFinishing) {
            if (MyUtils.checkInternetConnection(context)) {
                ProgressUtils.show(context)
                val retrofitService = RetrofitService.getInstance()
                retrofitService.getAllMovies()
            }
        }
    }*/

    /*fun initAdapter(){
        val adapter = BaseAdapter<Movie>(ArrayList<Movie>(), R.layout.mh00_template_row, object : MyBindingInterface<Movie> {
            override fun bindData(item: Movie, view: View) {
                view.findViewById<TextView>(R.id.txt1).text = "abc"
            }

        })
    }*/
}