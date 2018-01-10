package listener.soul.com.skinchange.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.LayoutInflaterFactory
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * @description
 * @author kuan
 * Created on 2018/1/9.
 */
class SkinFactory : LayoutInflaterFactory {

    companion object {
        val prefixList = arrayOf("android.widget.",
            "android.view.",
            "android.webkit.")
    }

    private var cacheList = ArrayList<SkinView>()


    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View? {
        var view : View? = null
        //需要换肤的控件收集到一个容器中
        if (name!!.contains(".")){
            view = createView(context,attrs,name)
        }else {
            for (pre in prefixList){
                view = createView(context,attrs,pre+name)
                if (view != null){
                    break
                }
            }
        }

        if (view!=null){
            parseSkinView(context,attrs,view)
        }

        return view
    }

    private fun parseSkinView(context: Context?, attrs: AttributeSet?, view: View) {
        val skinItems :ArrayList<SkinItem>? = null

        for (i in 0..attrs!!.attributeCount){
            val attrName = attrs.getAttributeName(i)
            val attrValue = attrs.getAttributeValue(i)
            if (attrName.equals("background")|| attrName.equals("textColor")){
                val id = attrValue.substring(1).toInt()
                val entryName = context!!.resources.getResourceEntryName(id)
                val typeName = context.resources.getResourceTypeName(id)

                val skinItem = SkinItem(attrName,entryName,id,typeName)
                skinItems!!.add(skinItem)
            }
        }
        if (!skinItems!!.isEmpty()){
            val skinView = SkinView(view,skinItems)
            cacheList.add(skinView)
            skinView.apply()
        }
    }

    private fun createView(context: Context?, attrs: AttributeSet?, name: String): View? {
        val viewClazz = context!!.classLoader.loadClass(name)
        val constructor = viewClazz.getConstructor(*arrayOf(Context::class.java,AttributeSet::class.java))
        return constructor.newInstance(context,attrs) as View
    }

    fun apply(): Unit {
        cacheList.forEach { it.apply() }
    }

    fun remove(){
        cacheList.clear()
    }

    class SkinItem(//属性名称
            var attrName: String, //属性值
            var arrtValue: String, //属性id
            var refId: Int, //属性类型
            var attrType: String) {

        override fun toString(): String {
            return "SkinItem{" +
                    "attrName='" + attrName + '\'' +
                    ", arrtValue='" + arrtValue + '\'' +
                    ", refId=" + refId +
                    ", attrType='" + attrType + '\'' +
                    '}'
        }
    }

    class SkinView(view:View?,list:List<SkinItem>?){
        val list = list
        val view  = view
        @SuppressLint("NewApi")
        /**
        * 应用换肤
        */
        fun apply(): Unit {
            list!!.forEach {
                if ("textColor".equals(it.attrType)){
                    (view as TextView).setTextColor(SkinManager.getInstance()!!.getColor(it.refId))
                }

                if ("color".equals(it.attrType)){
                    view!!.setBackgroundColor(SkinManager.getInstance()!!.getColor(it.refId))
                }else if ("drawable".equals(it.attrType)){
                    view!!.background = SkinManager.getInstance()!!.getDrawable(it.refId)
                }
            }
        }
    }

}