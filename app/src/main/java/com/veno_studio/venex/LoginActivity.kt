package com.veno_studio.venex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    var database: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance() //auth 초기화
        database = FirebaseFirestore.getInstance() //firestore 초기화

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener { login_check() }
        signup_btn.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
    }

    override fun onStart() {
        super.onStart()
        moveActivity(auth?.currentUser)
    }


    fun login_check(){
        if(email_text.text.toString().isNullOrEmpty() || password_text.text.toString().isNullOrEmpty()){
            Toast.makeText(this, getString(R.string.emailANDpassoword_null), Toast.LENGTH_SHORT).show()
        } else {
            progress_bar.visibility = View.VISIBLE
            auth?.signInWithEmailAndPassword(email_text.text.toString(), password_text.text.toString()) //위에 설정한 auth호출 후 이메일, 패스워드가 맞다면 아래실행
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        moveActivity(auth?.currentUser)
                    }else{
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this, getString(R.string.not_found_account), Toast.LENGTH_SHORT).show() //토스트 띄워주기
                    }
                }
        }
    }

    fun moveActivity(user: FirebaseUser?){
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}
