package com.example.himasha.workhub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment constructor() : Fragment() {
    private var proname: TextView? = null
    private var proemail: TextView? = null
    private var prohomeaddress: TextView? = null
    private var prowebsite: TextView? = null
    private var protelephone: TextView? = null
    private var probio: TextView? = null
    private var editprofile: Button? = null
    private var projobs_list: RecyclerView? = null
    private var workhub: DatabaseReference? = null
    private var workhubusers: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews(view)
        return view
    }

    public override fun onStart() {
        super.onStart()
        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Job, ProJobViewHolder> =
            object : FirebaseRecyclerAdapter<Job, ProJobViewHolder>(
                Job::class.java,
                R.layout.profilejob_list,
                ProJobViewHolder::class.java,
                workhub!!.orderByChild("jobPostedUserId")
                    .equalTo(auth!!.getCurrentUser()!!.getUid())
            ) {
                override fun populateViewHolder(
                    viewHolder: ProJobViewHolder,
                    model: Job,
                    position: Int
                ) {
                    val job_key: String = getRef(position).getKey()
                    viewHolder.setJobName(model.getJobName())
                    viewHolder.setJobBudget(model.getJobBudget())
                    viewHolder.setJobLocation(model.getJobLocationName())
                    viewHolder.setJobDate(model.getJobPostedDate())
                    viewHolder.mview.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            val intent: Intent = Intent(getActivity(), SingleJobView::class.java)
                            intent.putExtra("job_id", job_key)
                            startActivity(intent)
                        }
                    })
                }
            }
        projobs_list!!.setAdapter(firebaseRecyclerAdapter)
    }

    class ProJobViewHolder constructor(var mview: View) : RecyclerView.ViewHolder(
        mview
    ) {
        fun setJobName(jobname: String?) {
            val jName: TextView = mview.findViewById<View>(R.id.projobrowname) as TextView
            jName.setText(jobname)
        }

        fun setJobBudget(jobbudget: String?) {
            val jBudget: TextView = mview.findViewById<View>(R.id.projobrowbudget) as TextView
            jBudget.setText("Rs." + jobbudget)
        }

        fun setJobLocation(jobLocation: String?) {
            val jLocation: TextView = mview.findViewById<View>(R.id.projobrowlocation) as TextView
            jLocation.setText(jobLocation)
        }

        fun setJobDate(jobDate: String?) {
            val jDate: TextView = mview.findViewById<View>(R.id.projobrowdate) as TextView
            jDate.setText("Added on " + jobDate)
        }
    }

    private fun initViews(view: View) {
        auth = FirebaseAuth.getInstance()
        workhub = FirebaseDatabase.getInstance().getReference().child("jobs")
        workhubusers = FirebaseDatabase.getInstance().getReference().child("users").child(
            auth.getCurrentUser()!!.getUid()
        )
        projobs_list = view.findViewById<View>(R.id.projobs_list) as RecyclerView?
        projobs_list!!.setLayoutManager(LinearLayoutManager(getActivity()))
        proname = view.findViewById<View>(R.id.singleProfileUserName) as TextView?
        proemail = view.findViewById<View>(R.id.singleProfileEmail) as TextView?
        protelephone = view.findViewById<View>(R.id.singleProfileTelephone) as TextView?
        prohomeaddress = view.findViewById<View>(R.id.singleProfileAddress) as TextView?
        prowebsite = view.findViewById<View>(R.id.singleProfileWebsite) as TextView?
        probio = view.findViewById<View>(R.id.singleProfileBio) as TextView?
        editprofile = view.findViewById<View>(R.id.profileEditBTN) as Button?
        workhubusers.addValueEventListener(object : ValueEventListener {
            public override fun onDataChange(dataSnapshot: DataSnapshot) {
                proname!!.setText(dataSnapshot.child("userName").getValue() as String?)
                proemail!!.setText(dataSnapshot.child("userEmail").getValue() as String?)
                protelephone!!.setText(dataSnapshot.child("userTelephone").getValue() as String?)
                prohomeaddress!!.setText(dataSnapshot.child("userAddress").getValue() as String?)
                prowebsite!!.setText(dataSnapshot.child("userWebsite").getValue() as String?)
                probio!!.setText(dataSnapshot.child("userBio").getValue() as String?)
            }

            public override fun onCancelled(databaseError: DatabaseError) {}
        })
        editprofile!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val intent: Intent = Intent(getActivity(), EditProfileActivity::class.java)
                startActivity(intent)
            }
        })
    }

    companion object {
        fun newInstance(): ProfileFragment {
            val fragment: ProfileFragment = ProfileFragment()
            return fragment
        }
    }
}