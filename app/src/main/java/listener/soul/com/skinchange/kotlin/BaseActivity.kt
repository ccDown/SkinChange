package listener.soul.com.skinchange.kotlin

import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.support.v4.view.LayoutInflaterFactory
import android.support.v7.app.AppCompatActivity

/**
 * @description
 * @author kuan
 * Created on 2018/1/9.
 */
open class BaseActivity:AppCompatActivity(){
    var skinFactory : SkinFactory?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        SkinManager.getInstance()!!.init(this)
        skinFactory = SkinFactory()
        LayoutInflaterCompat.setFactory(layoutInflater,skinFactory as LayoutInflaterFactory)
        super.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        skinFactory!!.remove()
    }
}