package com.cms.skin

import android.content.Context
import android.content.SharedPreferences

/**
 * @author: Mr.You
 * @create: 2022-10-21 13:51
 * @description: 存储皮肤路径
 **/
class SkinSharePreference() {
    private var sharePreference: SharedPreferences? = null

    companion object {
        private const val SKIN_PATH= "sp_skin_path"
        private const val SKIN = "skin_path"
        var instance: SkinSharePreference? = null

        fun init(context: Context) {
            if (instance == null) {
                synchronized(SkinSharePreference::class) {
                    if (instance == null) {
                        instance = SkinSharePreference(context)
                    }
                }
            }
        }
    }

    private constructor(context: Context) : this() {
       sharePreference = context.getSharedPreferences(SKIN_PATH,Context.MODE_PRIVATE)
    }


    fun setSkinPath(skinPath:String){
        sharePreference?.edit()?.putString(SKIN,skinPath)?.apply()
    }
    fun getSkinPath() = sharePreference?.getString(SKIN,"")

    fun reset() = sharePreference?.edit()?.remove(SKIN)?.apply()

}