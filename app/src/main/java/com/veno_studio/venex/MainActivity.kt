package com.veno_studio.venex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.veno_studio.venex.fragment.ChattingFragment
import com.veno_studio.venex.fragment.MoreFragment
import com.veno_studio.venex.fragment.NoticeFragment
import com.veno_studio.venex.fragment.PeopleFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView =
            findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.people_btn -> {
                val people = PeopleFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, people)
                    .commit()
                Log.e("애러", "공지 프래그먼트가 실행되었습니다.")
                return true
            }
            R.id.chatting_btn -> {
                val chatting = ChattingFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, chatting)
                    .commit()
                return true
            }
            R.id.notice_btn -> {
                val Notice = NoticeFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, Notice)
                    .commit()
                return true
            }
            R.id.more_btn -> {
                val more_move = MoreFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, more_move)
                    .commit()
                return true
            }

        }
        return false
    }

    override fun onResume() {
        super.onResume()

        val people = PeopleFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, people)
            .commit()
        return
    }

}
