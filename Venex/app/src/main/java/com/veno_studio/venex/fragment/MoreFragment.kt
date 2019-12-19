package com.veno_studio.venex.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.veno_studio.venex.R
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment(){

    var auth : FirebaseAuth? = null

    var moreView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        moreView = inflater.inflate(R.layout.fragment_more, container, false)

        auth = FirebaseAuth.getInstance()



        return moreView
    }

    override fun onResume() {
        super.onResume()
        sgin_log_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(activity, "로그아웃이 되었습니다.",Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
    }
}