package listener.soul.com.skinchange.java;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author kuan
 *         Created on 2018/1/9.
 * @description
 */

public class SkinManager {

    //皮肤apk包名
    private String skinPackage;
    //代表外置卡皮肤APP的resource
    private Resources skinResource;

    private Context context;
    public void init(Context context){
        this.context = context.getApplicationContext();
    }

    private SkinManager(){}
    private static SkinManager skinManager = null;
    public static SkinManager getSkinManager(){
        if (skinManager == null){
            synchronized (SkinManager.class){
                if (skinManager == null){
                    skinManager = new SkinManager();
                }
            }
        }
        return  skinManager;
    }

    /**
     * 加载皮肤
     * @param path
     */
    public void loadSkin(String path){

        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",String.class);

            addAssetPath.invoke(assetManager,path);

            skinResource = new Resources(
                    assetManager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());
            PackageManager packageManager = context.getPackageManager();
            //拿到皮肤的包名
            skinPackage = packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES).packageName;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 替换颜色
     * @param resId
     * @return
     */
    public int getColor(int resId){
        if (skinResource == null){
            return resId;
        }

        String resName = context.getResources().getResourceEntryName(resId);

        int skinId = skinResource.getIdentifier(resName,"color",skinPackage);
        if (skinId==0){
            return resId;
        }

        return skinResource.getColor(skinId);
    }

    /**
     * 替换drawable
     * @param refId
     * @return
     */
    public Drawable getDrawable(int refId){
        if (skinResource == null){
            return ContextCompat.getDrawable(context,refId);
        }

        String resName = context.getResources().getResourceEntryName(refId);

        int skinId = skinResource.getIdentifier(resName,"drawable",skinPackage);
        if (skinId==0){
            return ContextCompat.getDrawable(context,refId);
        }

        return skinResource.getDrawable(skinId);
    }
}
