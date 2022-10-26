package com.cms.skinlibrary

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import com.cms.skinlibrary.utils.ToastUtils
import java.util.*

/**
 * @author: Mr.You
 * @create: 2022-10-21 11:44
 * @description:
 **/
class SkinApplicationLifecycle : Application.ActivityLifecycleCallbacks  {
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinApplicationLifecycle()
        }
    }

    fun registerLifeCycle(application: Application,observable: Observable) {
        application.registerActivityLifecycleCallbacks(this)
        observable.addObserver(SkinFactory.instance)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            SkinFactory.instance.registerActivity(activity)
        }else{
            ToastUtils.show(activity,"本换肤方案最高仅支持api28")
        }

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

}