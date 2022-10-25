package com.cms.skin

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat


/**
 * @author: Mr.You
 * @create: 2022-10-21 10:06
 * @description:
 **/
class SkinAttributeSet {
    companion object{
        const val TAG = "SkinAttributeSet"
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinAttributeSet()
        }
    }

    private val mSkinViews = mutableListOf<SkinView>()

    private val mAttributes: List<String> by lazy {
        listOf(
            "background",
            "src",
            "textColor",
            "drawableLeft",
            "drawableTop",
            "drawableRight",
            "drawableBottom"
        )
    }

    fun applySkin(){
        for (skinView in mSkinViews){
             skinView.applySkin()
        }
    }
    fun lookAttributeSet(view: View?, attrs: AttributeSet) {
        //定义集合保持属性
        var skinPair = mutableListOf<SkinPair>()
        for (i in 0 until attrs.attributeCount){
            //属性名称
            val attributeName = attrs.getAttributeName(i)
            if (mAttributes.contains(attributeName)){
                val attributeValue = attrs.getAttributeValue(i)
                //如果是直接使用#fff 颜色色值，则无法处理
                if (attributeValue.startsWith("#")) continue
                var resId:Int = -1
                if (attributeValue.startsWith("?")){
                    //获取资源Id
                    val attrId = attributeValue.substring(1).toInt()
                    if (view != null) {
                       resId =  SkinTools.getResId(view.context, IntArray(attrId))[0]
                    }
                }else if (attributeValue.startsWith("@")){
                     resId =  attributeValue.substring(1).toInt()
                }
                skinPair.add(SkinPair(attributeName,resId))
            }

        }
        if (skinPair.isNotEmpty()||view is SkinViewSupport){
            if (view != null) {
              mSkinViews.add(SkinView(view,skinPair).apply {
                    applySkin()
                })
            }
        }

    }


    data class SkinPair(val attributeName:String,val resId:Int)
    class SkinView(val view: View, private val skinPairs:List<SkinPair>){
        fun applySkin(){
            applySkinSupportView()
            for(skpair in skinPairs){
                Log.i("SkinFactory", "applySkin: ${skpair.attributeName}  ")
                var left: Drawable? = null
                var top: Drawable? = null
                var right: Drawable? = null
                var bottom: Drawable? = null
               when(skpair.attributeName){
                   "background"->{
                    val backGround =  SkinResources.instance?.getBackground(skpair.resId)
                       if (backGround is Int){
                           view.setBackgroundColor(backGround)
                       }else{
                           ViewCompat.setBackground(view,backGround as Drawable)
                       }
                   }
                   "src"->{
                      var background = SkinResources.instance?.getBackground(skpair.resId)
                       if (background is Int) {
                           (view as ImageView).setImageDrawable((background as Int?)?.let { ColorDrawable(it) })
                       } else {
                           (view as ImageView).setImageDrawable(background as Drawable?)
                       }
                   }
                   "textColor"->{
                       (view as TextView).setTextColor(SkinResources.instance?.getColorStateList(skpair.resId))
                   }
                   "drawableLeft"->{
                       left = SkinResources.instance?.getBackground(skpair.resId) as Drawable
                   }
                   "drawableTop"->{
                       top = SkinResources.instance?.getBackground(skpair.resId) as Drawable
                   }
                   "drawableRight"->{
                      right =  SkinResources.instance?.getBackground(skpair.resId) as Drawable
                   }
                   "drawableBottom"->{
                       bottom = SkinResources.instance?.getBackground(skpair.resId) as Drawable
                   }
               }
                if (left!=null || top!=null ||right!=null ||bottom!=null){
                    (view as TextView).setCompoundDrawables(left,top,right,bottom)
                }
            }
        }
        private fun applySkinSupportView(){
            if (view is SkinViewSupport){
                (view as SkinViewSupport).applySkin()
            }
        }
    }
}