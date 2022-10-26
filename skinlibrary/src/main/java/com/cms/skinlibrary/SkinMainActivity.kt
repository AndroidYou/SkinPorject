package com.cms.skinlibrary
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.cms.skinlibrary.SkinSharePreference
import com.cms.skinlibrary.databinding.ActivitySkinMainBinding


class SkinMainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySkinMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySkinMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        initView()
        initListener()
    }


    private fun initView() {
             mBinding.rg.check(when(SkinSharePreference.instance?.getSkinPath()){
                 "${baseContext.cacheDir}/blue_skinresource-debug.apk" -> R.id.rb_blue
                 "${baseContext.cacheDir}/green_skinresource-debug.apk" -> R.id.rb_green
                 "${baseContext.cacheDir}/orange_skinresource-debug.apk" -> R.id.rb_orange
                 "${baseContext.cacheDir}/red_skinresource-debug.apk" -> R.id.rb_red

                 else ->R.id.rb_default
             })
    }

    private fun initListener() {
        mBinding.rg.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_blue->{
                    val skinPkg: String ="${baseContext.cacheDir}/blue_skinresource-debug.apk"
                    SkinManager.instance.loadSkinPath(skinPkg)
                }
                R.id.rb_default->{
                    SkinManager.instance.loadSkinPath(null)
                }
                R.id.rb_green ->{
                    val skinPkg: String ="${baseContext.cacheDir}/green_skinresource-debug.apk"
                    SkinManager.instance.loadSkinPath(skinPkg)
                }
                R.id.rb_orange->{
                    val skinPkg: String ="${baseContext.cacheDir}/orange_skinresource-debug.apk"
                    SkinManager.instance.loadSkinPath(skinPkg)
                }
                R.id.rb_red ->{
                    val skinPkg: String ="${baseContext.cacheDir}/red_skinresource-debug.apk"
                    SkinManager.instance.loadSkinPath(skinPkg)
                }
            }
        }
    }

}