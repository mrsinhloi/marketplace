package com.ehubstar.marketplace.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import com.ehubstar.marketplace.R
import com.ehubstar.marketplace.adapters.LocalParentAdapter
import com.ehubstar.marketplace.adapters.SelectImageAdapter
import com.ehubstar.marketplace.base.BaseActivity
import com.ehubstar.marketplace.base.ItemClickListener
import com.ehubstar.marketplace.databinding.MpActivityMarketPostBinding
import com.ehubstar.marketplace.databinding.MpBottomSheetLocalsBinding
import com.ehubstar.marketplace.models.local.District
import com.ehubstar.marketplace.models.local.LocalUtils
import com.ehubstar.marketplace.models.local.Province
import com.ehubstar.marketplace.utils.MyUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.namphuongso.acv.utils.showSoftKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.aprilapps.easyphotopicker.*
import java.util.*


open abstract class MarketPostActivity(override val bindingInflater: (LayoutInflater) -> MpActivityMarketPostBinding = MpActivityMarketPostBinding::inflate) :
    BaseActivity<MpActivityMarketPostBinding>(), EasyImage.EasyImageStateHandler {
    companion object {
        const val DESCRIPTION_LENGTH = 100
        const val SPEECH_REQUEST_CODE = 0
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MarketPostActivity::class.java))
        }
    }

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        //yc quyen cho hinh
        requestAppPermission()

        //UI
        with(binding) {

            topAppBar.setNavigationOnClickListener { finish() }
//            topAppBar.setOnMenuItemClickListener()

            //spinners
            val listCategory = resources.getStringArray(R.array.arr_category).toList()
            createDropDown(listCategory, category)
            val listGroup = resources.getStringArray(R.array.arr_group).toList()
            createDropDown(listGroup, group)


            //description
            description.editText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().length < DESCRIPTION_LENGTH) {
                    description.error = getString(R.string.mp_description_hint)
                } else {
                    description.error = null
                }
            }
            //khi focus thi scroll xuong cuoi
            description.onFocusChangeListener = object : View.OnFocusChangeListener {
                override fun onFocusChange(p0: View?, p1: Boolean) {
                    if (p1) {
                        scrollView.smoothScrollTo(0, description.bottom + 10)
                    }
                }

            }


            district.editText?.setOnClickListener {
                adapter.notifyDataSetChanged()
                dialog.show()
            }
            province.editText?.setOnClickListener {
                adapter.notifyDataSetChanged()
                dialog.show()
            }

            //keyboard
//            (category.editText as? AutoCompleteTextView)?.performClick()
            acreage.editText?.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MyUtils.hideKeyboard(this@MarketPostActivity)

                    launch {
                        delay(200)
                        withContext(Dispatchers.Main) {
                            category.requestFocus()
                            (category.editText as? AutoCompleteTextView)?.showDropDown()
                        }
                    }

                    true
                }
                false
            }

            /*(category.editText as? AutoCompleteTextView)?.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 -> //show group
                    launch {
                        delay(200)
                        withContext(Dispatchers.Main) {
                            group.requestFocus()
                            (group.editText as? AutoCompleteTextView)?.showDropDown()
                        }
                    }
                }*/

            onSelectItemFromList(category, group, true)
            onSelectItemFromList(group, address, false)

            address.editText?.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MyUtils.hideKeyboard(this@MarketPostActivity)

                    launch {
                        delay(200)
                        withContext(Dispatchers.Main) {
                            district.editText?.performClick()
                        }
                    }

                    true
                }
                false
            }

            btnPost.setOnClickListener { post() }
        }

        //add text change
        addTextChangeAll()


        initLocals()
        //image
        initImageSelector()

    }

    fun createDropDown(items: List<String>, textField: TextInputLayout) {
        val adapter = ArrayAdapter(this, R.layout.mp_list_item, items)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    fun onSelectItemFromList(from: TextInputLayout, to: TextInputLayout, isShowDropDown: Boolean) {
        (from.editText as? AutoCompleteTextView)?.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 -> //show group
                launch {
                    delay(200)
                    withContext(Dispatchers.Main) {
                        if (isShowDropDown) {
                            to.requestFocus()
                            (to.editText as? AutoCompleteTextView)?.showDropDown()
                        } else {
                            showKeyboard(to)
                        }
                    }
                }
            }
    }

    fun showKeyboard(to: TextInputLayout) {
        to.requestFocus()
        to.editText?.requestFocus()
        to.editText?.showSoftKeyboard(context)
    }


    /*fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }*/


    //select location
    var provinceSelected: Province? = null
    var districtSelected: District? = null
    lateinit var sheet: MpBottomSheetLocalsBinding
    lateinit var dialog: BottomSheetDialog
    lateinit var adapter: LocalParentAdapter

    //search
    lateinit var icClear: ImageView
    lateinit var icVoice: ImageView
    lateinit var txtSearch: AppCompatEditText
    fun initLocals() {
        val local = LocalUtils.readLocalFromAssets(context)

        //init
        sheet = MpBottomSheetLocalsBinding.inflate(layoutInflater, null, false)
        dialog = BottomSheetDialog(this)

        dialog.setContentView(sheet.root)
//        dialog.show()
        with(sheet) {

            //DANH SACH TINH THANH/QUAN HUYEN
            adapter = LocalParentAdapter(context, local, object : ItemClickListener {
                override fun onItemClick(position: Int, itemObj: Any?) {
                    val province = LocalParentAdapter.provinceSelected//local[position]
                    if (itemObj is District) {
                        val district = itemObj as District
                        binding.province.editText?.setText(province?.name)
                        binding.district.editText?.setText(district.name)

                        provinceSelected = province
                        districtSelected = district

                        dialog?.dismiss()

                        launch {
                            delay(200)
                            withContext(Dispatchers.Main) {
                                showKeyboard(binding.description)
                            }
                        }


                    }
                }

            })
            rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            rv.adapter = adapter

            //SEARCH FILTER
            with(mpSearchBoxContainer) {
                txtSearch = mpSearchEditText
                icClear = mpClearSearchQuery
                icVoice = mpVoiceSearchQuery

                txtSearch.doOnTextChanged { text, _, _, _ ->
                    val query = text.toString().toLowerCase(Locale.getDefault())
                    filterWithQuery(query)
                    toggleImageView(query)
                }

                icVoice.setOnClickListener {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                    }
                    startActivityForResult(intent, SPEECH_REQUEST_CODE)
                }

                icClear.setOnClickListener {
                    txtSearch.setText("")
                }
            }


        }

        //init height
//        dialog.behavior.peekHeight = sHeight * 8/10
//        dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN
        val offsetFromTop = 200
        /*dialog.behavior.apply {
            peekHeight = 300
//            expandedOffset = offsetFromTop
            state = BottomSheetBehavior.STATE_COLLAPSED
            isFitToContents = false
        }*/

    }

    private fun toggleImageView(query: String) {
        if (query.isNotEmpty()) {
            icClear.visibility = View.VISIBLE
            icVoice.visibility = View.INVISIBLE
        } else if (query.isEmpty()) {
            icClear.visibility = View.INVISIBLE
            icVoice.visibility = View.VISIBLE
        }
    }

    private fun filterWithQuery(query: String) {
        adapter.filter.filter(query)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            // Do something with spokenText
            txtSearch.setText(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)

        //pickImages()
        easyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    if (imageFiles.isNotEmpty()) {
//                        val list = Arrays.asList(imageFiles)

                        //duyet tu duoi len tren
                        for (i in imageFiles.size - 1 downTo 0) {
                            val item = imageFiles[i]
                            //add vao phan tu 0
                            imageAdapter.addPosition(item.file.absolutePath, 0)
                        }
                        imageAdapter.notifyDataSetChanged()
                    }
                }

            })

    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    lateinit var easyImage: EasyImage
    fun pickImages() {
        easyImage = EasyImage.Builder(context) // Chooser only
            // Will appear as a system chooser title, DEFAULT empty string
            .setChooserTitle("Pick your photos")
            // Will tell chooser that it should show documents or gallery apps
            //.setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)  you can use this or the one below
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            // Setting to true will cause taken pictures to show up in the device gallery, DEFAULT false
            .setCopyImagesToPublicGalleryFolder(false) // Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
            .setFolderName("MarketPlace") // Allow multiple picking
            .allowMultiple(true)
            .setStateHandler(this)
            .build()
        easyImage.openGallery(this)
    }

    //select image
    lateinit var imageAdapter: SelectImageAdapter
    fun initImageSelector() {
        imageAdapter = SelectImageAdapter(this, ArrayList(), object : ItemClickListener {
            override fun onItemClick(position: Int, itemObj: Any?) {
                if (itemObj is String) {
                    if (itemObj == SelectImageAdapter.ITEM_ADD_PHOTO) {
                        //yc chon hinh
                        if (isGrantedAllPermisson()) {
                            pickImages()
                        } else {
                            requestAllPermission()
                        }
                    }
                }
            }

        })
        binding.rv.adapter = imageAdapter
    }

    private var easyImageState = Bundle()
    override fun restoreEasyImageState(): Bundle {
        return easyImageState
    }

    override fun saveEasyImageState(state: Bundle?) {
        if (state != null) {
            easyImageState = state
        }
    }


    fun post() {
        //check hinh
        val imgs = imageAdapter.data
        if (imgs.size < 2) {
            //scroll to top
            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, 0)
            }
            //thong bao
            MyUtils.showAlertDialog(this, R.string.mp_please_select_image)
            return
        }

        //check text
        with(binding) {

            //check not empty
            if (isEmpty(title, R.string.mp_title)) return
            if (isEmpty(price, R.string.mp_price_vnd)) return
            if (isEmpty(acreage, R.string.mp_acreage)) return
            if (isEmpty(category, R.string.mp_category)) return
            if (isEmpty(group, R.string.mp_group)) return
            if (isEmpty(address, R.string.mp_address)) return
            if (isEmpty(district, R.string.mp_district)) return
            if (isEmpty(province, R.string.mp_province)) return
            if (isEmpty(description, R.string.mp_description)) return

            //check description length
            val des = getText(description)
            if (des.length < DESCRIPTION_LENGTH) {
                MyUtils.showAlertDialog(context, R.string.mp_description_hint)
                return
            }


            //lay thong tin va post
            val title = getText(title)
            val price = getText(price)
            val acre = getText(acreage)
            val category = getText(category)
            val group = getText(group)
            val address = getText(address)
            val district = getText(district)
            val province = getText(province)

            //Posting
            beginPost(title, price, acre, category, group, address, district, province, des, imgs)


            /*if (
                title.isNotEmpty() &&
                price.isNotEmpty() &&
                acre.isNotEmpty() &&
                category.isNotEmpty() &&
                group.isNotEmpty() &&
                address.isNotEmpty() &&
                district.isNotEmpty() &&
                province.isNotEmpty() &&
                des.isNotEmpty()
            ) {
                //posting
            } else {
                MyUtils.showAlertDialog(context, "No accepted")
            }*/
        }

    }



    fun addTextChangeAll() {
        with(binding) {
            addTextChange(title, R.string.mp_title)
            addTextChange(price, R.string.mp_price_vnd)
            addTextChange(acreage, R.string.mp_acreage)
            addTextChange(category, R.string.mp_category)
            addTextChange(group, R.string.mp_group)
            addTextChange(address, R.string.mp_address)
            addTextChange(district, R.string.mp_district)
            addTextChange(province, R.string.mp_province)
//            addTextChange(description, R.string.mp_description)
        }
    }

    fun addTextChange(txt: TextInputLayout, @StringRes error: Int) {
        txt.editText?.doOnTextChanged { text, start, before, count ->
            if (text.toString().isEmpty()) {
                txt.error = getString(error)
            } else {
                txt.error = null
            }
        }
    }

    fun isEmpty(txt: TextInputLayout, @StringRes error: Int): Boolean {
        val text = txt.editText?.text?.toString() ?: ""
        if (text.isEmpty()) {
            txt.error = getString(error)
            txt.requestFocus()
            return true
        } else {
            txt.error = null
            return false
        }
    }


    fun getText(txt: TextInputLayout): String {
        return txt.editText?.text?.toString() ?: ""
    }

    abstract fun beginPost(
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
    )

}