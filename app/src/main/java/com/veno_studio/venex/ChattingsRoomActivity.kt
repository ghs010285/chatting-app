package com.veno_studio.venex

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.veno_studio.venex.model.ChattingModel
import com.veno_studio.venex.model.UserModel
import kotlinx.android.synthetic.main.activity_chattings_room.*
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener











class ChattingsRoomActivity : AppCompatActivity() {

    var fireDatabase : FirebaseDatabase? = null
    var auth : FirebaseAuth? = null
    var YouUID: String? = null
    var uid : String? = null
    var chattingRoom : String? = null
    private val destinatonUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattings_room)

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
            FirebaseDatabase.getInstance().reference.child("chatrooms").push().setValue(chattingModel).addOnSuccessListener { checkRoom() }

        }
        checkRoom()
    }

    fun checkRoom(){
        progress_bar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().reference.child("chatrooms").orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object :
            ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (item in dataSnapshot.children) {
                        val chatModel = item.getValue<ChattingModel>(ChattingModel::class.java)
                        val comment = ChattingModel.Comment()
                        if (chatModel!!.users.containsKey(YouUID)) {
                            chattingRoom = item.key
                            chatting_send.isEnabled = true
                            chttings_recyclerView?.adapter = Chatting_Message()
                            chttings_recyclerView.layoutManager = LinearLayoutManager(this@ChattingsRoomActivity)
                        }
                        comment.uid = uid
                        comment.message = chatting_input_text.getText().toString()
                        FirebaseDatabase.getInstance().reference.child("chatrooms").child(chattingRoom!!).child("message").push().setValue(comment).addOnCompleteListener {
                            //sendGcm()
                            chatting_input_text.setText("")
                        }

                        progress_bar.visibility = View.GONE
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
                        chttings_recyclerView.scrollToPosition(comments.size -1)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@ChattingsRoomActivity, getString(R.string.chatting_send_error),Toast.LENGTH_SHORT).show()
                    }
                })


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)


            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val messageViewHolder = holder as MessageViewHolder

            messageViewHolder.textView_message.setText(comments[position].message)


            if (comments[position].uid == uid) {//내 채팅 메세지

                messageViewHolder.textView_message.text = comments[position].message
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.mychat)
                messageViewHolder.linearLayout_destination.visibility = View.INVISIBLE
                messageViewHolder.textView_message.textSize = 17f
                messageViewHolder.linearLayout_main.gravity = Gravity.RIGHT
            
            } else {//상대방 채팅 메세지

                Glide.with(holder.itemView.context)
                    .load(UserModel().userImage)
                    .apply(RequestOptions().circleCrop())
                    .into(messageViewHolder.imageView_profile)
                messageViewHolder.textview_name.setText(UserModel().userName)
                messageViewHolder.linearLayout_destination.visibility = View.VISIBLE
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.youchat)
                messageViewHolder.textView_message.text = comments[position].message
                messageViewHolder.textView_message.textSize = 17f
                messageViewHolder.linearLayout_main.gravity = Gravity.LEFT


            }
        }

        override fun getItemCount(): Int {
            return comments.size
        }

         private inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
             var textView_message: TextView = view.findViewById(R.id.message_text) as TextView
             var textview_name: TextView = view.findViewById(R.id.item_message_username) as TextView
             var imageView_profile: ImageView = view.findViewById(R.id.item_message_profile_image) as ImageView
             var linearLayout_destination: LinearLayout = view.findViewById(R.id.item_message_layout) as LinearLayout
             var linearLayout_main: LinearLayout = view.findViewById(R.id.item_message_main) as LinearLayout
         }
    }



}
