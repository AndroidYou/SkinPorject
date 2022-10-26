package com.cms.skinlibrary

import android.content.Context
import android.content.res.TypedArray




/**
 * @author: Mr.You
 * @create: 2022-10-21 10:45
 * @description:
 **/
object SkinTools {
    /**
     * 获得theme中的属性中定义的 资源id
     * @param context
     * @param attrs
     * @return
     */
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val a: TypedArray = context.obtainStyledAttributes(attrs)
        for (i in attrs.indices) {
            resIds[i] = a.getResourceId(i, 0)
        }
        a.recycle()
        return resIds
    }
}