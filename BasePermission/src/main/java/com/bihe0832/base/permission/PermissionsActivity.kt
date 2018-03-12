package com.bihe0832.base.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity


class PermissionsActivity : AppCompatActivity() {

    private var mChecker: PermissionsChecker? = null // 权限检测器
    private var isRequireCheck: Boolean = false // 是否需要系统权限检测, 防止和系统提示框重叠

    // 返回传递的权限参数
    private val permissions: Array<String>
        get() = intent.getStringArrayExtra(EXTRA_PERMISSIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            throw RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!")
        }
        setContentView(R.layout.com_bihe0832_common_permissions_activity)

        mChecker = PermissionsChecker(this)
        isRequireCheck = true
    }

    override fun onResume() {
        super.onResume()
        if (isRequireCheck) {
            val permissions = permissions
            if (mChecker!!.lacksPermissions(*permissions)) {
                requestPermissions(*permissions) // 请求权限
            } else {
                allPermissionsGranted() // 全部权限都已获取
            }
        } else {
            isRequireCheck = true
        }
    }

    // 请求权限兼容低版本
    private fun requestPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    // 全部权限均已获取
    private fun allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true
            allPermissionsGranted()
        } else {
            isRequireCheck = false
            showMissingPermissionDialog()
        }
    }

    // 含有全部的权限
    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }


    private fun showMissingPermissionDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.help)
        builder.setMessage(R.string.string_help_text)
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.set)) { dialog, which -> startAppSettings() }
        builder.setNegativeButton(getString(R.string.exit)) { dialog, which ->
            setResult(PERMISSIONS_DENIED)
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // 启动应用的设置
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    companion object {

        val PERMISSIONS_GRANTED = 0 // 权限授权
        val PERMISSIONS_DENIED = 1 // 权限拒绝

        private val PERMISSION_REQUEST_CODE = 0 // 系统权限管理页面的参数
        private val EXTRA_PERMISSIONS = "com.bihe0832.base.permission.permission.extra_permission" // 权限参数
        private val PACKAGE_URL_SCHEME = "package:" // 方案

        // 启动当前权限页面的公开接口
        fun startActivityForResult(activity: Activity, requestCode: Int, vararg permissions: String) {
            val intent = Intent(activity, PermissionsActivity::class.java)
            intent.putExtra(EXTRA_PERMISSIONS, permissions)
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
        }
    }
}
