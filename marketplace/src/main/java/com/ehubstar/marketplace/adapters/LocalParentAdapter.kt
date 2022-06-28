package com.ehubstar.marketplace.adapters

import android.content.Context
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ehubstar.marketplace.R
import com.ehubstar.marketplace.base.BaseAdapter
import com.ehubstar.marketplace.base.ItemClickListener
import com.ehubstar.marketplace.models.local.District
import com.ehubstar.marketplace.models.local.Province
import com.ehubstar.marketplace.utils.MyUtils

class LocalParentAdapter(
    context: Context,
    list: ArrayList<Province>,
    val listener: ItemClickListener
) : BaseAdapter<Province>(context, list), Filterable {

    companion object {
        var provinceSelected: Province? = null
        var districtSelected: District? = null
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.mp_list_item_parent
    }


    override fun configure(position: Int, holder: RecyclerView.ViewHolder) {
        if(position<itemCount){
            val item: Province = list[position]

//        holder.itemView.findViewById<TextView>(R.id.txt1).text = item.name
            with(holder.itemView) {
                val tv: TextView = findViewById(R.id.mp_txt_parent)
                val rv: RecyclerView = findViewById(R.id.mp_rv_child)
//            rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

                //tv
                tv.text = item.name
                if (item.isExpanded) {
                    setIconRight(tv, R.drawable.ic_round_keyboard_arrow_up_24)
                    rv.visibility = View.VISIBLE
                } else {
                    setIconRight(tv, R.drawable.ic_round_keyboard_arrow_down_24)
                    rv.visibility = View.GONE
                }
                tv.setOnClickListener {
                    list[position].isExpanded = !item.isExpanded
                    notifyItemChanged(position)

                    //collapse tat ca
//                collapseAll(item.id)
                }

                //rv
                val adapter = LocalChildAdapter(
                    context, item.districts
                ) { _, itemObj ->
                    if (itemObj is District) {

                        /*if (provinceSelected != null) {
                            //clear cai cu da chon
                            clearSelection(provinceSelected?.id!!)
                        }*/
                        provinceSelected = item
                        districtSelected = itemObj

                        notifyItemChanged(position)

                        listener.onItemClick(position, itemObj)
                    }
                }
//            adapter.clearSelection()
                rv.adapter = adapter

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
    }

    private fun collapseAll(id: String) {
        //clear select cua group truoc do
        for (i in 0 until list.size) {
            val p = list[i]
            if (p.isExpanded && p.id != id) {
                list[i].isExpanded = false
                notifyItemChanged(i)
            }
        }
    }
    /*private fun clearSelection(id: String) {
        //clear select cua group truoc do
        for (i in 0 until list.size) {
            val p = list[i]
            if (p.id == id) {
                for (j in 0 until p.districts.size) {
                    list[i].districts[j].isSelected = false
                }
                break
            }
        }
        notifyDataSetChanged()
    }*/

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    list = rootList
                } else {
                    val resultList = ArrayList<Province>()
                    val key = MyUtils.getUnsignedString(charSearch)//.lowercase(Locale.ROOT)
                    for (row in rootList) {
                        val name = MyUtils.getUnsignedString(row.name)//.lowercase(Locale.ROOT)
                        if (name.contains(key)) {
                            resultList.add(row)
                        } else {
                            //neu khong co trong parent thi tim trong children, neu co thi lay cha va con
                            val childs = ArrayList<District>()
                            for (i in 0 until row.districts.size) {
                                val item = row.districts[i]
                                val name =
                                    MyUtils.getUnsignedString(item.name)//.lowercase(Locale.ROOT)
                                if (name.contains(key)) {
                                    childs.add(item)
                                }
                            }
                            if (childs.size > 0) {
                                val copy = row.clone()
                                copy.districts = childs
                                resultList.add(copy)
                            }
                        }
                    }
                    list = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = list
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = results?.values as ArrayList<Province>
                notifyDataSetChanged()
            }

        }
    }


}
