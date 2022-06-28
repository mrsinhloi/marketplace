package com.ehubstar.marketplace.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

//https://medium.com/@strawberryinc0531/create-a-generic-adapter-for-recycler-view-android-ce22e6b4e063
/* USE
    fun initAdapter(){
        val adapter = BaseAdapter<Movie>(ArrayList<Movie>(), R.layout.mh00_template_row, object : MyBindingInterface<Movie> {
            override fun bindData(item: Movie, view: View) {
                view.findViewById<TextView>(R.id.txt1).text = "abc"
            }

        })
    }
 */
open class SimpleBaseAdapter<T : Any>(
    private val dataSet: List<T>,
    @LayoutRes val layoutID: Int,
    private val bindingInterface: MyBindingInterface<T>
) :
    RecyclerView.Adapter<SimpleBaseAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun <T : Any> bind(
            item: T,
            bindingInterface: MyBindingInterface<T>
        ) = bindingInterface.bindData(item, view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutID, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, bindingInterface)
    }

    override fun getItemCount(): Int = dataSet.size

}

interface MyBindingInterface<T : Any> {
    fun bindData(item: T, view: View)
}


