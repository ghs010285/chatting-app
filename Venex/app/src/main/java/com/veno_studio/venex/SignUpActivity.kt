package com.veno_studio.venex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.veno_studio.venex.model.UserModel
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null
    var database : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup_user.setOnClickListener { signup_check() }
        login_btn.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java))
            finish() }
    }

    fun signup_check(){
        if(signup_email.text.toString().isNullOrEmpty() || signup_password1.text.toString().isNullOrEmpty() || signup_password2.text.toString().isNullOrEmpty()){
            Toast.makeText(this, getString(R.string.emailANDpassoword_null),Toast.LENGTH_SHORT).show()
        } else if(signup_password1.text.toString() != signup_password2.text.toString()){
            Toast.makeText(this, getString(R.string.checking_your_password),Toast.LENGTH_SHORT).show()
        } else {
            progress_bar.visibility = View.VISIBLE
            auth?.createUserWithEmailAndPassword(signup_email.text.toString(), signup_password1.text.toString())
                ?.addOnCompleteListener { task ->
                    Toast.makeText(this, "회원가입중입니다. 잠시만 기달려 주세요", Toast.LENGTH_SHORT).show()
                    var uid = task.result!!.user?.uid
                    var email_text = task.result!!.user?.email
                    var userModel = UserModel()
                    userModel.userEmail = signup_email.text.toString()
                    userModel.password = signup_password1.text.toString()
                    userModel.userName = signup_name.text.toString()
                    userModel.uid = FirebaseAuth.getInstance().uid

                    if (uid != null) {
                        database?.collection("user")?.document(uid)?.set(userModel)?.addOnCompleteListener { task ->
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                }
        }




    }
}
