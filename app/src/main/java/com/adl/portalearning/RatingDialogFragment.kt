package com.adl.portalearning

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import com.adl.portalearning.model.ModelVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_rating_dialog.*
import kotlinx.android.synthetic.main.fragment_rating_dialog.view.*
import kotlinx.android.synthetic.main.item_main_activity.*
import java.math.RoundingMode
import java.text.DecimalFormat

class RatingDialogFragment: DialogFragment() {

    lateinit var data: ModelVideo
    lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating_dialog, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = Firebase.auth.currentUser
        btnAddRating.setOnClickListener({
            val rateVid = ratingBar.getRating()
            val bundle = arguments
            val model: ModelVideo? = bundle?.getParcelable("data")
            Log.d("rateIS =","judul = ${model?.title}+ user = ${model?.uid} + rate = ${rateVid}")
            if(model != null && user != null) {
                model.id?.let { it1 -> addRatingVideo(it1,user.uid,rateVid) }
                dismiss()
            }
            })
    }
    fun addRatingVideo(vidId:String,userId:String,rating:Float){
        val timestamp = ""+System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["videoId"] = "$vidId"
        hashMap["userId"] = "$userId"
        hashMap["rating"] = "$rating"

        val dbReference = FirebaseDatabase.getInstance().getReference("Rates")
        dbReference.child(timestamp)
            .setValue(hashMap)
            .addOnSuccessListener { taskSnapshot ->
                Log.d("addrate","Succeses")
            }
            .addOnFailureListener{ e ->
                Log.d("addrate","failed")
            }
        calcurate(vidId)

    }
    fun calcurate(vidId:String){
        var temp: Float=0.0f
        var count= 0
        val rootRef = FirebaseDatabase.getInstance().reference
        val usersRef = rootRef.child("Rates")
        val okQuery = usersRef.orderByChild("videoId").equalTo("${vidId}")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if(ds.exists()){
                        val rateVal = ds.child("ratingValue").getValue(String::class.java)!!.toFloat()
                        temp += rateVal
                        count += 1
                    }

                    Log.d("isi","${ds.child("ratingValue").getValue(String::class.java)}")
                }
                Log.d("isi","${temp} + ${count}")
                val mean = temp / count
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.DOWN
                val roundoff = df.format(mean)
                updateRate(roundoff.toFloat(),vidId)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        okQuery.addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateRate(res:Float,vidId:String){
        val ref = FirebaseDatabase.getInstance().getReference("Videos").child(vidId)
        println("!!!!!!!!!!! Firebase reference: ${ref.toString()}")


        ref.child("rating").setValue(res.toString())

    }

}