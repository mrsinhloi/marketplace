package com.ehubstar.marketplace.base

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ehubstar.marketplace.R


abstract class BaseAdapter<T>(var context: Context, var list: ArrayList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var inflater: LayoutInflater = LayoutInflater.from(context)
    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var corners: Int = 8
    var rootList: ArrayList<T> = ArrayList()

    init {
        // get device dimensions
        val displayMetrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(displayMetrics)

        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
        corners = context.resources.getDimensionPixelOffset(R.dimen.mp_corner_radius)
        rootList = list


        /*val LocaleBylanguageTag: Locale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }*/

    }


    override fun getItemCount(): Int = list.count()
    abstract fun configure(position: Int, holder: RecyclerView.ViewHolder)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        configure(position, holder)
    }

    fun update(items: ArrayList<T>) {
        this.list = items
        notifyDataSetChanged()
    }

    fun add(t: T) {
        list.add(t)
        notifyItemInserted(list.size - 1)
    }

    fun addAll(items: ArrayList<T>) {
        val from = list.size
        list.addAll(items)
        notifyItemRangeInserted(from, list.size - 1)
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addPosition(t: T, position: Int) {
        list.add(position, t)
        notifyItemInserted(position)
    }

    fun removerItem(t: T) {
        val postion = list.indexOf(t)
        removerPosition(postion)
    }

    fun removerPosition(position: Int) {
        if (list.size > 0) {
            if (position < list.size) {
                list.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun updatePosition(t: T, position: Int) {
        if (position < list.size) {
            list[position] = t
            notifyItemChanged(position)
        }
    }


    val data: ArrayList<T>
        get() = list as ArrayList<T>

    fun setIconRight(tv: TextView?, drawable: Int) {
        tv?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            drawable,
            0
        )
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}