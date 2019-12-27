package com.veno_studio.venex.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.veno_studio.venex.ChttingsRoomActivity
import com.veno_studio.venex.R
import com.veno_studio.venex.model.UserModel
import kotlinx.android.synthetic.main.fragment_people.view.*
import kotlinx.android.synthetic.main.item_people.view.*



class PeopleFragment : Fragment(){

    var auth : FirebaseAuth? = null
    var firebase_uid : String? = null
    var database : FirebaseFirestore? = null
    var currentUid : String? = null
    var aaa : View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        aaa = inflater.inflate(R.layout.fragment_people, container, false)


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        currentUid = auth?.currentUser?.uid

        aaa?.people_recyclerview?.adapter = PeopleFragmentRecyclerViewAdapter()
        aaa?.people_recyclerview?.layoutManager = LinearLayoutManager(activity)


        val user = FirebaseAuth.getInstance().currentUser
        if(user != null) {
            database?.collection("user")?.document(currentUid!!)?.get()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var userlistModel = task.result?.toObject(UserModel::class.java)
                        aaa?.people_my_name?.text = userlistModel?.userName
                        aaa?.people_my_1line?.text = userlistModel?.userOneLine
                    }
                }
        } else {
            Toast.makeText(activity, "존재하지 않는 유저는 앱을 사용 못합니다.",Toast.LENGTH_SHORT).show()
            activity?.finish()
        }

        return aaa
    }




    inner class PeopleFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        var userList: ArrayList<UserModel>? = null
        var userListUid: ArrayList<String>? = null


        init{
            userList = ArrayList()
            userListUid = ArrayList()
            val Myuid = FirebaseAuth.getInstance().currentUser?.uid
            val user = FirebaseAuth.getInstance().currentUser
            database?.collection("user")?.orderBy("uid")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                userList?.clear()
                for(snapshot in querySnapshot!!.documents){
                    val item = snapshot.toObject(UserModel::class.java)
                    if (item != null) {
                        userList!!.add(item)
                    }
                    if(item?.uid.equals(Myuid)){
                        continue
                    }
                    userListUid?.add(item.toString())
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false) //item_notificaion_cops 호출 후 바인딩
            return CustomViewHolder(view)
        }

        override fun getItemCount(): Int {
            return userList!!.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val userCustomHodler = (holder as CustomViewHolder).itemView

            holder.itemView.setOnClickListener{
                val intent = Intent(view?.context, ChttingsRoomActivity::class.java)
                intent.putExtra("youuid", userList?.get(position)?.uid)
                startActivity(intent)
                Log.e("로그","버튼이 눌렸습니다.")
            }

            userCustomHodler.people_name.text = userList?.get(position)?.userName
            userCustomHodler.people_1line.text = userList?.get(position)?.userOneLine
        }

        inner class CustomViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)

    }
}
