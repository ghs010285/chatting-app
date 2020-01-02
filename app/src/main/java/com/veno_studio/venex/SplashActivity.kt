package com.veno_studio.venex

import android.content.Intent
import android.net.sip.SipErrorCode.TIME_OUT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashActivity : AppCompatActivity() { //스플레쉬는 서버점검 및 앱 점검 때 사용하기 위해 쓰임

    lateinit var remoteConfig : FirebaseRemoteConfig
    private val FIREBASE_FATCH_TIME: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        network_check()
    }

    fun network_check(){
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.network_checking)
//        remoteConfig.setDefaults(R.xml.app_update_versions)
        fetchAction()
    }
    fun fetchAction() {
        remoteConfig.fetch(FIREBASE_FATCH_TIME)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    remoteConfig.activateFetched()
                } else {

                }
                displayWelcomeMessage()

            }
    }

    fun displayWelcomeMessage() {
        val caps: Boolean = remoteConfig.getBoolean("welcome_message_caps")
        val splashMesseage: String = remoteConfig.getString("welcome_message")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        if (caps) {
            builder.setMessage(splashMesseage)
                .setCancelable(false)
                .setPositiveButton("확인") { dialog, which -> finish() }
            builder.create().show()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}
