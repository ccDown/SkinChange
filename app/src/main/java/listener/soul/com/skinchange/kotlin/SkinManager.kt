package listener.soul.com.skinchange.kotlin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

/**
 * @description
 * @author kuan
 * Created on 2018/1/10.
 */
class SkinManager(){
    companion object {
        private var mInstance:SkinManager? = null

        public fun getInstance(): SkinManager? {
            if (mInstance == null){
                synchronized(SkinManager::class.java){
                    if (mInstance == null) {
                        mInstance = SkinManager()
                    }
                }
            }
            return mInstance
        }
    }

    fun init(context: Context): Unit {
        this.context = context
    }

    //上下文
    var context :Context?=null
    //皮肤资源
    var skinResource :Resources?=null
    //皮肤apk包名
    var skinPackage : String ? = null

    /**
     * 加载皮肤
     */
    public fun loadSkin(path: String): Unit {
        val assetManager = AssetManager::class.objectInstance
        val addAssetPath = assetManager!!::class.java.getMethod("addAssetPath",String::class.java);

        addAssetPath.invoke(assetManager,path)

        skinResource = Resources(assetManager,
                context!!.resources.displayMetrics,
                context!!.resources.configuration)

        val packageManager = context!!.packageManager

        //拿到皮肤的包名
        skinPackage = packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES).packageName
    }

    /**
     * 替换颜色
     */
    fun getColor(resId: Int): Int {
        if (skinResource==null){
            return resId
        }

        val resName = context!!.resources.getResourceEntryName(resId)

        val skinId = skinResource!!.getIdentifier(resName,"color",skinPackage)
        if (skinId == 0){
            return resId
        }
        return skinResource!!.getColor(skinId)
    }

    fun getDrawable(refId: Int): Drawable? {
        if (skinResource == null){
            return ContextCompat.getDrawable(context, refId);
        }

        val resName = context!!.resources.getResourceEntryName(refId)
        val skinId = skinResource!!.getIdentifier(resName,"drawable",skinPackage)
        if (skinId == 0){
            return ContextCompat.getDrawable(context,refId)
        }
        return skinResource!!.getDrawable(skinId)
    }

}