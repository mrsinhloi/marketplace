package com.ehubstar.marketplace.adapters

import android.content.Context
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ehubstar.marketplace.R
import com.ehubstar.marketplace.base.BaseAdapter
import com.ehubstar.marketplace.base.ItemClickListener
import com.ehubstar.marketplace.models.local.District
import com.ehubstar.marketplace.utils.MyUtils
import java.util.*

class LocalChildAdapter(
    context: Context,
    list: ArrayList<District>,
    val listener: ItemClickListener
) : BaseAdapter<District>(context, list), Filterable {

    override fun getItemViewType(position: Int): Int {
        return R.layout.mp_list_item_child
    }

    //khi chon 1 phan tu
    var positionSelected = -1
    fun clearSelection() {
        /*positionSelected = -1
        for (i in 0 until itemCount) {
            if (list[i].isSelected) {
                list[i].isSelected = false
                notifyItemChanged(i)
            }
        }*/
    }

   /* fun selectItem(position: Int) {
        positionSelected = position
        for (i in 0 until itemCount) {
            if (position == i) {
                list[i].isSelected = true
                notifyItemChanged(i)
            } else {
                if (list[i].isSelected) {
                    list[i].isSelected = false
                    notifyItemChanged(i)
                }
            }
        }
    }*/

    override fun configure(position: Int, holder: RecyclerView.ViewHolder) {
        val item: District = list[position]

//        holder.itemView.findViewById<TextView>(R.id.txt1).text = item.name
        with(holder.itemView) {
            val tv = findViewById<TextView>(R.id.mp_txt_child)
            tv.text = item.name
            if (LocalParentAdapter.districtSelected!=null &&
                LocalParentAdapter.districtSelected?.id == item.id) {
                setIconRight(tv, R.drawable.ic_round_check_24)
            } else {
                setIconRight(tv, 0)
            }
            tv.setOnClickListener {
                listener.onItemClick(position, item)
            }

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
                    val resultList = ArrayList<District>()
                    val key = charSearch.toLowerCase(Locale.ROOT)
                    for (row in rootList) {
                        val name = MyUtils.getUnsignedString(row.name).toLowerCase(Locale.ROOT)
                        if (row.id.toLowerCase(Locale.ROOT)?.contains(key) == true ||
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
                list = results?.values as ArrayList<District>
                notifyDataSetChanged()
            }

        }
    }


}
