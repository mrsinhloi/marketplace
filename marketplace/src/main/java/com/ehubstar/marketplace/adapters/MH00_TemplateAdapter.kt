package com.ehubstar.marketplace.adapters

import android.content.Context
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ehubstar.marketplace.R
import com.ehubstar.marketplace.base.BaseAdapter
import com.ehubstar.marketplace.base.ItemClickListener
import com.ehubstar.marketplace.models.Movie
import com.ehubstar.marketplace.utils.MyUtils
import java.util.*

class MH00_TemplateAdapter(
    context: Context,
    list: ArrayList<Movie>,
    val listener: ItemClickListener
) : BaseAdapter<Movie>(context, list), Filterable {

    override fun getItemViewType(position: Int): Int {
        return R.layout.mh00_template_row
    }

    override fun configure(position: Int, holder: RecyclerView.ViewHolder) {
        val item: Movie = list[position]

//        holder.itemView.findViewById<TextView>(R.id.txt1).text = item.name
        with(holder.itemView) {
            val tv = findViewById<TextView>(R.id.txt1)
        }
        /*holder.itemView.linearRoot.setOnClickListener {
            //go to list member + admin
            listener.onItemClick(position, item)
            //check
            item.IsSelected = !item.IsSelected
            list[position].IsSelected = item.IsSelected
            notifyItemChanged(position)
        }*/
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    list = rootList
                } else {
                    val resultList = ArrayList<Movie>()
                    val key = charSearch.toLowerCase(Locale.ROOT)
                    for (row in rootList) {
                        val name = MyUtils.getUnsignedString(row.name).toLowerCase(Locale.ROOT)
                        if (row.category?.toLowerCase(Locale.ROOT)?.contains(key) == true ||
                            name.contains(key)
                        ) {
                            resultList.add(row)
                        }
                    }
                    list = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = list
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = results?.values as ArrayList<Movie>
                notifyDataSetChanged()
            }

        }
    }


}
