package com.veno_studio.venex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.veno_studio.venex.model.ChattingModel
import kotlinx.android.synthetic.main.activity_chttings_room.*


class ChttingsRoomActivity : AppCompatActivity() {

    var fireDatabase : FirebaseDatabase? = null
    var auth : FirebaseAuth? = null
    var YouUID: String? = null
    var uid : String? = null
    var chattingRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chttings_room)

        fireDatabase = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        YouUID = intent.getStringExtra("youuid")
        uid = FirebaseAuth.getInstance().currentUser?.uid

        chatting_send.setOnClickListener { send_event() }



    }

    fun send_event(){
        val chattingModel = ChattingModel()

        uid?.let { chattingModel.users.put(it, true) }
        YouUID?.let { chattingModel.users.put(it, true) }


        if (chattingRoom == null) {
            chatting_send.isEnabled = false
            FirebaseDatabase.getInstance().reference.child("chatrooms").push().setValue(chattingModel)
                .addOnSuccessListener { checkRoom() }

        }else{

            val comment = ChattingModel.Comment()
            comment.uid = uid
            comment.message = chatting_input_text.getText().toString()
            FirebaseDatabase.getInstance().reference.child("chatrooms").child(chattingRoom!!).child("message").push().setValue(comment)
        }
        checkRoom()
    }

    fun checkRoom(){
        FirebaseDatabase.getInstance().reference.child("chatrooms").orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object :
            ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (item in dataSnapshot.children) {
                        val chatModel = item.getValue<ChattingModel>(ChattingModel::class.java)
                        if (chatModel!!.users.containsKey(YouUID)) {
                            chattingRoom = item.key
                            chatting_send.isEnabled = true
                            chttings_recyclerView?.adapter = Chatting_Message()
                            chttings_recyclerView.layoutManager = LinearLayoutManager(this@ChttingsRoomActivity)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }


     inner class Chatting_Message : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        var comments: MutableList<ChattingModel.Comment> = ArrayList()

         init {

             FirebaseDatabase.getInstance().reference.child("chatrooms").child(chattingRoom!!).child("message").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        comments.clear()

                        for (item in dataSnapshot.children) {
                            item.getValue<ChattingModel.Comment>(ChattingModel.Comment::class.java)?.let {
                                comments.add(it)
                            }
                        }

                        notifyDataSetChanged()


                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)


            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


            (holder as MessageViewHolder).textView_message.setText(comments[position].message)
        }

        override fun getItemCount(): Int {
            return comments.size
        }

        private inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView_message: TextView

            init {
                textView_message = view.findViewById(R.id.message_text) as TextView
            }
        }
    }



}
