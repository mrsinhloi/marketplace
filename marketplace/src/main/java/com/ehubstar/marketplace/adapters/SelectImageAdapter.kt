package com.ehubstar.marketplace.adapters

import android.content.Context
import android.graphics.Bitmap
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.ehubstar.marketplace.R
import com.ehubstar.marketplace.base.BaseAdapter
import com.ehubstar.marketplace.base.ItemClickListener
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class SelectImageAdapter(
    context: Context,
    list: ArrayList<String>,
    val listener: ItemClickListener
) : BaseAdapter<String>(context, list) {

    var spacing = 20
    var spaceSmall = 2
    var imgSize = 100


    init {
        spacing = context.resources.getDimensionPixelOffset(R.dimen.mp_padding_start)
        spaceSmall = context.resources.getDimensionPixelOffset(R.dimen.mp_padding_small)
        imgSize = context.resources.getDimensionPixelOffset(R.dimen.mp_photo_height)

        if (!list.contains(ITEM_ADD_PHOTO)) {
            list.add(ITEM_ADD_PHOTO)
        }
    }

    companion object {
        const val ITEM_ADD_PHOTO = "ITEM_ADD_PHOTO"
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return R.layout.mp_select_item_no_image
        } else {
            return R.layout.mp_select_item_has_image
        }
    }

    override fun configure(position: Int, holder: RecyclerView.ViewHolder) {
        val item = list[position]
        if (item == ITEM_ADD_PHOTO) {
            with(holder.itemView) {
                val mpItemAdd = findViewById<LinearLayout>(R.id.mpItemAdd)

                //neu chi co 1 item thi cho full
                val full = screenWidth - spacing * 4 - spaceSmall * 2
                if (itemCount == 1) {
                    mpItemAdd.layoutParams.width = full
                } else {
                    mpItemAdd.layoutParams.width = full / 2
                }

                mpItemAdd.setOnClickListener {
                    listener.onItemClick(position, item)
                }
            }
        } else {
            with(holder.itemView) {

                //show cover
                val multi = MultiTransformation<Bitmap>(
                    CenterCrop(),
                    RoundedCornersTransformation(corners, 0)
                )

                val img = findViewById<ImageView>(R.id.img)
                Glide.with(context)
                    .load(item)
                    .override(imgSize, imgSize)
                    .apply(RequestOptions.bitmapTransform(multi))
                    .into(img)

                //remove
                val remove = findViewById<FrameLayout>(R.id.close)
                remove.setOnClickListener {
                    list.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }

    }


}
