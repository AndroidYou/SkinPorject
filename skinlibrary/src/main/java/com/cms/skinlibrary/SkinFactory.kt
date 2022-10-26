package com.cms.skinlibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View
import androidx.core.view.LayoutInflaterCompat
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.*
import kotlin.collections.HashMap

/**
 * @author: Mr.You
 * @create: 2022-10-20 11:32
 * @description:
 **/
class SkinFactory : Factory2, Observer{
    private val sConstructorMap: HashMap<String, Constructor<out View?>> = HashMap<String, Constructor<out View?>>()
    private val mConstructorSignature = arrayOf<Class<*>>(
        Context::class.java, AttributeSet::class.java
    )
    private val mClassPrefixList = arrayOf(
        "android.widget.",
        "android.webkit.",
        "android.app.",
        "android.view."
    )
    companion object {
        const val TAG = "SkinFactory"
        val instance: SkinFactory by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinFactory()
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    fun registerActivity(activity: Activity) {
        val classInflater =activity.layoutInflater
        val mFactorySet =  LayoutInflater::class.java.getDeclaredField("mFactorySet")
        mFactorySet.isAccessible = true
        mFactorySet.set(classInflater, false)
        LayoutInflaterCompat.setFactory2(classInflater,this)

    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        //创建view
       var view = tryCreateView(name,context,attrs)
        if (view==null){
            view = createView(name,context,attrs)
        }
        Log.i(TAG, "onCreateView: $view")
        if (view!=null){
             // 收集view属性
            SkinAttributeSet.instance.lookAttributeSet(view,attrs)
        }
        return view
    }


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }


    private fun tryCreateView(name: String, context: Context, attrs: AttributeSet):View? {
        if (-1 != name.indexOf('.')) {
          return null
        }
        for (i in mClassPrefixList.indices){
            var view =  createView( "${mClassPrefixList[i]}$name", context, attrs)
            if (view!=null){
                return view
            }
        }
        return null
    }

    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
         val constructor = findConstructor(context, name)
         try {
             return constructor!!.newInstance(context, attrs)
         } catch (e: Exception) {
         }
         return null
     }

     private fun findConstructor(context: Context, name: String): Constructor<out View>? {
         var constructor: Constructor<out View>? = sConstructorMap[name] as? Constructor<out View>
         if (constructor == null) {
             try {
                 val clazz = context.classLoader.loadClass(name).asSubclass(
                     View::class.java
                 )
                 constructor = clazz.getConstructor(*mConstructorSignature)
                 sConstructorMap[name] = constructor
             } catch (e: Exception) {
             }
         }
         return constructor
     }

    override fun update(o: Observable?, arg: Any?) {
        Log.i(TAG, "update: 收到通知")
        SkinAttributeSet.instance.applySkin()
    }
}