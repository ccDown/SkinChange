package listener.soul.com.skinchange.java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author kuan
 *         Created on 2018/1/9.
 * @description
 */

public class SkinFactory implements LayoutInflaterFactory {

    private List<SkinView> cacheList = new ArrayList<>();
    private static final String TAG = "soulListener";
    private static final String[] prefixList = {"android.widget.",
            "android.view.",
            "android.webkit."};

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Log.e(TAG, "onCreateView: ");

        View view = null;
//      需要换肤的控件收集到一个容器中
        if (name.contains(".")) {
            //自定义控件
            view = createView(context, attrs, name);

        } else {
            for (String pre : prefixList) {
                view = createView(context, attrs, pre + name);
                if (view != null) {
                    //包名正确  实例化成功
                    break;
                }
            }
        }

        if (view != null) {
            parseSkinView(context, attrs, view);
        }

        return view;
    }

    //解析需要换肤的控件
    private void parseSkinView(Context context, AttributeSet attrs, View view) {

        List<SkinItem> skinItems = new ArrayList<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String atteValue = attrs.getAttributeValue(i);
            if (attrName.equals("background") || attrName.equals("textColor")) {
                //需要换肤的控件
                int id = Integer.parseInt(atteValue.substring(1));
                String entryName = context.getResources().getResourceEntryName(id);

                String typeName = context.getResources().getResourceTypeName(id);

                SkinItem skinItem = new SkinItem(attrName, entryName, id, typeName);
                skinItems.add(skinItem);
            }
        }

        if (!skinItems.isEmpty()) {
            SkinView skinView = new SkinView(view, skinItems);
            cacheList.add(skinView);
//            应用换肤   XML加载过程中换肤
            skinView.apply();
        }

    }

    private View createView(Context context, AttributeSet attrs, String name) {
        try {
            Class viewClazz = context.getClassLoader().loadClass(name);
            Constructor constructor = viewClazz.getConstructor(new Class[]{Context.class, AttributeSet.class});
            return (View) constructor.newInstance(context, attrs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return null;
    }

    public void apply() {
        for (SkinView skinView:cacheList) {
            skinView.apply();
        }
    }

    public void remove() {
        cacheList = null ;
    }

    class SkinView {
        private View view;
        private List<SkinItem> list;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        //应用换肤
        @SuppressLint("NewApi")
        public void apply() {
            for (SkinItem skinItem : list) {
                Log.e(TAG, "apply: "+skinItem.toString());
                if (skinItem.getAttrName().equals("background")){
                    if ("textColor".equals(skinItem.getAttrType())){
                        ((TextView)view).setTextColor(SkinManager.getSkinManager().getColor(skinItem.getRefId()));
                    }
                    if ("color".equals(skinItem.getAttrType())){
                        view.setBackgroundColor(SkinManager.getSkinManager().getColor(skinItem.getRefId()));
                    }else if ("drawable".equals(skinItem.getAttrType())){
                        view.setBackground(SkinManager.getSkinManager().getDrawable(skinItem.getRefId()));
                    }
                }
            }
        }
    }
    class SkinItem {
        //属性名称
        private String attrName;
        //属性值
        private String arrtValue;
        //属性id
        private int refId;
        //属性类型
        private String attrType;

        public SkinItem(String attrName, String arrtValue, int refId, String attrType) {
            this.attrName = attrName;
            this.arrtValue = arrtValue;
            this.refId = refId;
            this.attrType = attrType;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public String getArrtValue() {
            return arrtValue;
        }

        public void setArrtValue(String arrtValue) {
            this.arrtValue = arrtValue;
        }

        public int getRefId() {
            return refId;
        }

        public void setRefId(int refId) {
            this.refId = refId;
        }

        public String getAttrType() {
            return attrType;
        }

        public void setAttrType(String attrType) {
            this.attrType = attrType;
        }

        @Override
        public String toString() {
            return "SkinItem{" +
                    "attrName='" + attrName + '\'' +
                    ", arrtValue='" + arrtValue + '\'' +
                    ", refId=" + refId +
                    ", attrType='" + attrType + '\'' +
                    '}';
        }
    }
}



