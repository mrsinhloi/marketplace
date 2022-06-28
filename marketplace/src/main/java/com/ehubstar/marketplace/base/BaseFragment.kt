package com.namphuongso.acv.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ehubstar.marketplace.db.TinyDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

//https://mickbitsoftware.medium.com/essential-android-base-classes-for-view-binding-and-coroutine-scope-ed5b4c2d59ea
abstract class BaseFragment<B : ViewBinding> : Fragment(), CoroutineScope by CoroutineScope(
    Dispatchers.Main
) {

    protected lateinit var binding: B
        private set

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        coroutineContext[Job]?.cancel()
        super.onDestroyView()
    }

    lateinit var db: TinyDB
    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = TinyDB(context)
    }
}