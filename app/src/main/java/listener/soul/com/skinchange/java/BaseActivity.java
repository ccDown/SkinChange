package listener.soul.com.skinchange.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * @author kuan
 *         Created on 2018/1/9.
 * @description
 */

public class BaseActivity extends AppCompatActivity {

    protected SkinFactory skinFactory ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinManager.getSkinManager().init(this);
        // 注意需在调用super.onCreate(savedInstanceState);之前设置
        skinFactory = new SkinFactory();
        LayoutInflaterCompat.setFactory(getLayoutInflater(),skinFactory);

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        skinFactory.remove();
    }
}
