package com.ehubstar.marketplace.base

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.ehubstar.marketplace.R
import com.ehubstar.marketplace.db.TinyDB
import com.ehubstar.marketplace.utils.MyUtils
import com.ehubstar.marketplace.utils.makeStatusBarTransparent
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

const val PERMISSION_REQUEST_CODE = 222
//https://mickbitsoftware.medium.com/essential-android-base-classes-for-view-binding-and-coroutine-scope-ed5b4c2d59ea
abstract class BaseActivity<B : ViewBinding> : AppCompatActivity(),
    EasyPermissions.PermissionCallbacks,
    CoroutineScope by CoroutineScope(
        Dispatchers.Main
    ) {

    //region VIEW BINDING
    protected lateinit var binding: B
        private set

    abstract val bindingInflater: (LayoutInflater) -> B

    open fun onViewBindingCreated(savedInstanceState: Bundle?) {}

    @CallSuper
    override fun onDestroy() {
        coroutineContext[Job]?.cancel()
        super.onDestroy()
    }
    //endregion

     var sWidth: Int = 0
     var sHeight: Int = 0
    lateinit var db: TinyDB
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        makeStatusBarTransparent(false)
        window.statusBarColor = ContextCompat.getColor(this, com.ehubstar.marketplace.R.color.mp_stroke_color)
        super.onCreate(savedInstanceState)
        context = this
        db = TinyDB(this)
        sWidth = MyUtils.getScreenWidth(this)
        sHeight = MyUtils.getScreenHeight(this)

        //VIEW BINDING
        binding = bindingInflater.invoke(layoutInflater).apply {
            setContentView(root)
        }
        onViewBindingCreated(savedInstanceState)

    }

    //permission
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //CHECK PERMISSION https://github.com/VMadalin/easypermissions-ktx
    /////////////////////////////////////////////////////////////////////////////////////////////////
    var perms = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults, this)
    }

    fun isGrantedAllPermisson():Boolean {
        return EasyPermissions.hasPermissions(this, *perms)
    }
    fun requestAllPermission(){
        EasyPermissions.requestPermissions(
            host = this,
            rationale = getString(R.string.mp_deny_permission),
            requestCode = PERMISSION_REQUEST_CODE,
            perms = perms
        )
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    fun requestAppPermission(){
        if(isGrantedAllPermisson()){

        }else{
            // Do not have permissions, request them now
            requestAllPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

    }



    ////////////////////////////////////////////////////////////////////////////////////////
    //END CHECK PERMISSION
    ////////////////////////////////////////////////////////////////////////////////////////


}