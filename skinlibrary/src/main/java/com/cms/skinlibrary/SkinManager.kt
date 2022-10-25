package com.cms.skin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import java.lang.Exception
import java.util.Observable

/**
 * @author: Mr.You
 * @create: 2022-10-20 11:14
 * @description:皮肤管理器
 **/
class SkinManager private constructor() :Observable(){
    private var mContext: Context? = null

    companion object {
        const val TAG = "skinManager"
        val instance: SkinManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinManager()
        }
    }

    fun install(application: Application) {

        mContext = application.applicationContext
        SkinResources.init(application)
        SkinSharePreference.init(application)
        SkinApplicationLifecycle.instance.registerLifeCycle(application,this)

        loadSkinPath(SkinSharePreference.instance?.getSkinPath())
    }

     fun loadSkinPath(skinPath: String?) {
         Log.i(TAG, "loadSkinPaths: "+skinPath)
        if (TextUtils.isEmpty(skinPath)) {
            SkinResources.instance?.reset()
            SkinSharePreference.instance?.reset()
        }
        try {
            //下面通过反射创建AssetManager
            val assetManager = AssetManager::class.java.newInstance()
            val method = assetManager.javaClass.getMethod("addAssetPath",String::class.java)
            method.invoke(assetManager, skinPath)

            val resources = mContext?.resources
            val appResources = Resources(assetManager, resources?.displayMetrics, resources?.configuration)
            val packageManager = mContext?.packageManager
            /**
             * 检索包存档文件中定义的应用程序包的总体信息
            参数:
            archiveFilePath—存档文件标志的路径—修改返回数据的附加选项标志。
            返回:
            一个PackageInfo对象，它包含有关包存档的信息。如果不能解析包，则返回null。
             */

            if (skinPath != null) {
                val packageInfo = packageManager?.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                val packName = packageInfo?.packageName
                SkinResources.instance?.applySkin(appResources, packName)
                SkinSharePreference.instance?.setSkinPath(skinPath)
            }
        } catch (e: Exception) {
        }
        setChanged()
        notifyObservers(null)
    }


}