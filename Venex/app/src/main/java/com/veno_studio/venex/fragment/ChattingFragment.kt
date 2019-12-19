package com.veno_studio.venex.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.veno_studio.venex.R
import com.veno_studio.venex.model.UserModel
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChattingFragment : Fragment(){

    var auth : FirebaseAuth? = null
    var database : FirebaseFirestore? = null

        var item_view = 20

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        var mainView = inflater.inflate(R.layout.fragment_chat, container, false)

        mainView?.chatting_recyclerView?.adapter = ChattingRecyclerViewList()
        mainView?.chatting_recyclerView?.layoutManager = LinearLayoutManager(activity)

        return mainView
    }

    inner class ChattingRecyclerViewList : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var userList: ArrayList<UserModel>

        init{
            userList = ArrayList()
            //var uid = FirebaseAuth.getInstance().currentUser?.uid

            database?.collection("user")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                userList.clear()
                for(snapshot in querySnapshot!!.documents){
                    val item = snapshot.toObject(UserModel::class.java)
                    if( auth != null){
                        userList.add(item!!)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val item_View = LayoutInflater.from(parent.context).inflate(R.layout.item_chatting, parent, false)

            return CustomViewHolder(item_View)
        }

        override fun getItemCount(): Int {
            return userList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val userCustomHolder = (holder as CustomViewHolder).itemView
        }

        inner class CustomViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)


    }

}