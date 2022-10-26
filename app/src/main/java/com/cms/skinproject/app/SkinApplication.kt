package com.cms.skinproject.app

import android.app.Application
import com.cms.skinlibrary.SkinManager

/**
 * @author: Mr.You
 * @create: 2022-10-25 16:44
 * @description:
 **/
class SkinApplication:Application (){
    override fun onCreate() {
        super.onCreate()
        SkinManager.instance.install(this)

    }
}