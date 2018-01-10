package listener.soul.com.skinchange.kotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import listener.soul.com.skinchange.R
import java.io.File

class MainActivity : BaseActivity() {
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this,permissions[0])!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, permissions, 0)
        }

        val file = File(Environment.getExternalStorageDirectory(),"skin.apk")
        SkinManager.getInstance()!!.loadSkin(file.absolutePath)

        button.setOnClickListener { skinFactory!!.apply() }

    }
}
