package com.veno_studio.venex

import android.content.Intent
import android.net.sip.SipErrorCode.TIME_OUT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

class SplashActivity : AppCompatActivity() { //스플레쉬는 서버점검 및 앱 점검 때 사용하기 위해 쓰임

    var TIME: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            Log.i("메인 이벤트", "정상작동")
            finish()
            Log.i("종료 이벤트", "정상작동")
        }, TIME)
    }
}
