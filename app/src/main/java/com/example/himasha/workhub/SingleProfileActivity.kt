package com.example.himasha.workhub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SingleProfileActivity constructor() : AppCompatActivity() {
    private var user_key: String? = null
    private var auth: FirebaseAuth? = null
    private var workhubUsers: DatabaseReference? = null
    private var userName: TextView? = null
    private var userEmail: TextView? = null
    private var prohomeaddress: TextView? = null
    private var prowebsite: TextView? = null
    private var protelephone: TextView? = null
    private var probio: TextView? = null
    private var review: EditText? = null
    private var callBTN: Button? = null
    private var msgBTN: Button? = null
    private var emailBTN: Button? = null
    private var addreview: Button? = null
    var review_list: RecyclerView? = null
    private var tele: String? = null
    private var userN: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_profile)
        user_key = getIntent().getExtras()!!.getString("user_id")
        auth = FirebaseAuth.getInstance()
        workhubUsers = FirebaseDatabase.getInstance().getReference().child("users")
        userName = findViewById<View>(R.id.singleProfileUserName) as TextView?
        userEmail = findViewById<View>(R.id.singleProfileEmail) as TextView?
        protelephone = findViewById<View>(R.id.singleProfileTelephone) as TextView?
        prohomeaddress = findViewById<View>(R.id.singleProfileAddress) as TextView?
        prowebsite = findViewById<View>(R.id.singleProfileWebsite) as TextView?
        probio = findViewById<View>(R.id.singleProfileBio) as TextView?
        review = findViewById<View>(R.id.addReviewET) as EditText?
        callBTN = findViewById<View>(R.id.singleProfileCallBTN) as Button?
        msgBTN = findViewById<View>(R.id.singleProfileMsgBTN) as Button?
        emailBTN = findViewById<View>(R.id.singleProfileEmailBTN) as Button?
        addreview = findViewById<View>(R.id.submitReviewBTN) as Button?
        review_list = findViewById<View>(R.id.reviews_list) as RecyclerView?
        review_list!!.setHasFixedSize(true)
        review_list!!.setLayoutManager(LinearLayoutManager(this))
        workhubUsers.child(user_key).addValueEventListener(object : ValueEventListener {
            public override fun onDataChange(dataSnapshot: DataSnapshot) {
                userName!!.setText(dataSnapshot.child("userName").getValue() as String?)
                userEmail!!.setText(dataSnapshot.child("userEmail").getValue() as String?)
                tele = dataSnapshot.child("userTelephone").getValue() as String?
                protelephone!!.setText(dataSnapshot.child("userTelephone").getValue() as String?)
                prohomeaddress!!.setText(dataSnapshot.child("userAddress").getValue() as String?)
                prowebsite!!.setText(dataSnapshot.child("userWebsite").getValue() as String?)
                probio!!.setText(dataSnapshot.child("userBio").getValue() as String?)
            }

            public override fun onCancelled(databaseError: DatabaseError) {}
        })
        workhubUsers.child(auth.getCurrentUser()!!.getUid())
            .addValueEventListener(object : ValueEventListener {
                public override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userN = dataSnapshot.child("userName").getValue() as String?
                }

                public override fun onCancelled(databaseError: DatabaseError) {}
            })
        callBTN!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val uri: String = "tel:" + tele!!.trim({ it <= ' ' })
                val intent: Intent = Intent(Intent.ACTION_DIAL)
                intent.setData(Uri.parse(uri))
                startActivity(intent)
            }
        })
        msgBTN!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val sendIntent: Intent = Intent(Intent.ACTION_VIEW)
                sendIntent.setData(Uri.parse("sms:" + tele!!.trim({ it <= ' ' })))
                startActivity(sendIntent)
            }
        })
        emailBTN!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val sendIntent: Intent = Intent(Intent.ACTION_VIEW)
                sendIntent.setType("plain/text")
                sendIntent.setData(Uri.parse(auth.getCurrentUser()!!.getEmail()))
                sendIntent.setClassName(
                    "com.google.android.gm",
                    "com.google.android.gm.ComposeActivityGmail"
                )
                startActivity(sendIntent)
            }
        })
        addreview!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val reviewText: String = review!!.getText().toString()
                if (!TextUtils.isEmpty(reviewText)) {
                    val postingUser: String = auth.getCurrentUser()!!.getUid()
                    val reviewingUser: String? = user_key
                    val reviewid: String =
                        workhubUsers.child(user_key).child("reviews").push().getKey()
                    val newRe: Review = Review()
                    newRe.setReview(reviewText)
                    newRe.setReviewedUserName(userN)
                    newRe.setReviewedUser(reviewingUser)
                    newRe.setReviewingUser(postingUser)
                    workhubUsers.child(user_key).child("reviews").child(reviewid).setValue(newRe)
                    Toast.makeText(this@SingleProfileActivity, "Review posted!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@SingleProfileActivity,
                        "You haven't said anything!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Review, ReviewViewHolder> =
            object : FirebaseRecyclerAdapter<Review, ReviewViewHolder>(
                Review::class.java,
                R.layout.review_row,
                ReviewViewHolder::class.java,
                workhubUsers!!.child(user_key).child("reviews")
            ) {
                override fun populateViewHolder(
                    viewHolder: ReviewViewHolder,
                    model: Review,
                    position: Int
                ) {
                    val job_key: String = getRef(position).getKey()
                    viewHolder.setReview(model.getReview())
                    viewHolder.setReviewUser(model.getReviewedUserName())
                    viewHolder.mview.findViewById<View>(R.id.deleteButton)
                        .setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(v: View) {
                                val reviewId: String = getRef(position).getKey()
                                workhubUsers!!.child(user_key).child("reviews").child(reviewId)
                                    .removeValue()
                                Toast.makeText(
                                    this@SingleProfileActivity,
                                    "Review deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            }
        review_list!!.setAdapter(firebaseRecyclerAdapter)
    }

    class ReviewViewHolder constructor(var mview: View) : RecyclerView.ViewHolder(
        mview
    ) {
        fun setReview(review: String?) {
            val jName: TextView = mview.findViewById<View>(R.id.reviewContentTV) as TextView
            jName.setText(review)
        }

        fun setReviewUser(reuser: String?) {
            val jBudget: TextView = mview.findViewById<View>(R.id.reviewPostedUserTV) as TextView
            jBudget.setText(reuser)
        }
    }
}