package com.ehubstar.marketplace

import android.widget.Toast
import com.ehubstar.marketplace.activities.MarketPostActivity

class TestActivity : MarketPostActivity() {
    override fun beginPost(
        title: String,
        price: String,
        acre: String,
        category: String,
        group: String,
        address: String,
        district: String,
        province: String,
        des: String,
        imgs: ArrayList<String>
    ) {
        Toast.makeText(this, "$title $price $des ${imgs.size}", Toast.LENGTH_SHORT).show()
    }
}