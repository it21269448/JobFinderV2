package com.example.himasha.workhub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SingleJobView : AppCompatActivity() {
    private var job_key: String? = null
    private val jobAddedUser: String? = null
    private var jobName: TextView? = null
    private var jobDesc: TextView? = null
    private var jobLocation: TextView? = null
    private var jobBudget: TextView? = null
    private var jobPostedDate: TextView? = null
    private var jobPostedUser: TextView? = null
    private var jobPostedMap: TextView? = null
    private var deletejob: Button? = null
    private var editjob: Button? = null
    private var saveJob: Button? = null
    private var feedbackJob: Button? = null
    private var sugges_list: RecyclerView? = null
    private var locationMapCardview: CardView? = null
    private var editRemoveJobcardview: CardView? = null
    private var postedUserCardview: CardView? = null
    private var workhubUsers: DatabaseReference? = null
    private var workhubJobs: DatabaseReference? = null
    private var workhubSavedJobs: DatabaseReference? = null
    private val workhubsuges: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var builder: AlertDialog.Builder? = null
    private var job_location: String? = null
    private var jobLat: Double? = null
    private var jobLong: Double? = null
    private var posteduserid: String? = null
    private var keyword: String? = null
    private var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_job_view)
        pref = getSharedPreferences("Users", 0)
        job_key = intent.extras!!.getString("job_id")
        auth = FirebaseAuth.getInstance()
        workhubJobs = FirebaseDatabase.getInstance().reference.child("jobs")
        workhubSavedJobs = FirebaseDatabase.getInstance().reference.child("saved_jobs")
        workhubUsers = FirebaseDatabase.getInstance().reference.child("users")
        builder = AlertDialog.Builder(this)
        jobName = findViewById<View>(R.id.singlejobName) as TextView
        jobDesc = findViewById<View>(R.id.singlejobDesc) as TextView
        jobLocation = findViewById<View>(R.id.singlejobLocation) as TextView
        jobBudget = findViewById<View>(R.id.singlejobBudget) as TextView
        jobPostedDate = findViewById<View>(R.id.singlejobPostedDate) as TextView
        jobPostedUser = findViewById<View>(R.id.singlejobPostedUser) as TextView
        jobPostedMap = findViewById<View>(R.id.singlejobMapName) as TextView
        deletejob = findViewById<View>(R.id.singlejobDeleteBTN) as Button
        saveJob = findViewById<View>(R.id.saveJobButton) as Button
        editjob = findViewById<View>(R.id.singlejobEditBTN) as Button
        feedbackJob = findViewById<View>(R.id.feedbackButton) as Button
        sugges_list = findViewById<View>(R.id.sugges_list) as RecyclerView
        sugges_list!!.setHasFixedSize(true)
        sugges_list!!.layoutManager = LinearLayoutManager(this)
        locationMapCardview = findViewById<View>(R.id.locationMapCard) as CardView
        editRemoveJobcardview = findViewById<View>(R.id.editremoveJobCard) as CardView
        postedUserCardview = findViewById<View>(R.id.postedUserCard) as CardView
        workhubJobs.child(job_key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot1: DataSnapshot) {
                keyword = dataSnapshot1.child("jobKeyWord").value as String
                val job_name = dataSnapshot1.child("jobName").value as String
                val job_desc = dataSnapshot1.child("jobDesc").value as String
                job_location = dataSnapshot1.child("jobLocationName").value as String
                val job_budget = dataSnapshot1.child("jobBudget").value as String
                val job_postedDate = dataSnapshot1.child("jobPostedDate").value as String
                val job_postedUser = dataSnapshot1.child("jobPostedUserId").value as String
                posteduserid = dataSnapshot1.child("jobPostedUserId").value as String
                val job_postedUserName = dataSnapshot1.child("jobPostedUserName").value as String
                jobLat = dataSnapshot1.child("jobLocationLat").value as Double
                jobLong = dataSnapshot1.child("jobLocationLong").value as Double
                jobName!!.text = job_name
                jobDesc!!.text = job_desc
                jobLocation!!.text = job_location
                jobBudget!!.text = "Rs.$job_budget"
                jobPostedDate!!.text = "Posted on $job_postedDate"
                jobPostedUser!!.text = "Posted by $job_postedUserName"
                jobPostedMap!!.text = "get directions to $job_location"
                if (auth.getCurrentUser()!!.uid == job_postedUser) {
                    editRemoveJobcardview!!.visibility = View.VISIBLE
                } else {
                    editRemoveJobcardview!!.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        postedUserCardview!!.setOnClickListener {
            if (posteduserid == auth.getCurrentUser()!!.uid) {
                val intent = Intent(this@SingleJobView, SingleProfileActivity::class.java)
                intent.putExtra("user_id", posteduserid)
                startActivity(intent)
            } else {
                val intent = Intent(this@SingleJobView, SingleProfileActivity::class.java)
                intent.putExtra("user_id", posteduserid)
                startActivity(intent)
            }
        }
        locationMapCardview!!.setOnClickListener {
            val intent = Intent(this@SingleJobView, MapLoadActivity::class.java)
            intent.putExtra("job_lat", jobLat)
            intent.putExtra("job_long", jobLong)
            startActivity(intent)
        }
        editjob!!.setOnClickListener {
            val intent = Intent(this@SingleJobView, EditJobActivity::class.java)
            intent.putExtra("job_id", job_key)
            startActivity(intent)
        }
        deletejob!!.setOnClickListener {
            builder!!.setTitle("Confirm")
            builder!!.setMessage("Are you sure you want to delete this job?")
            builder!!.setPositiveButton("YES") { dialog, which ->
                if (job_key != null) {
                    workhubJobs.child(job_key).removeValue()
                    val intent2 = Intent(this@SingleJobView, FeedActivity::class.java)
                    startActivity(intent2)
                    finish()
                }
                dialog.dismiss()
            }
            builder!!.setNegativeButton("NO") { dialog, which -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder!!.create()
            alert.show()
        }
        saveJob!!.setOnClickListener {
            builder!!.setTitle("Confirm")
            builder!!.setMessage("Are you sure you want to save this job?")
            builder!!.setPositiveButton("YES") { dialog, which ->
                workhubJobs.child(job_key).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.child("isSaved").setValue("1")
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
                dialog.dismiss()
            }
            builder!!.setNegativeButton("NO") { dialog, which -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder!!.create()
            alert.show()
        }
        feedbackJob!!.setOnClickListener {
            builder!!.setTitle("Confirm")
            builder!!.setMessage("Are you sure you want to save this job?")
            builder!!.setPositiveButton("YES") { dialog, which ->
                if (job_key != null) {
                    workhubJobs.child("saved_jobs").push()
                    val intent2 = Intent(this@SingleJobView, FeedActivity::class.java)
                    startActivity(intent2)
                    finish()
                }
                dialog.dismiss()
            }
            builder!!.setNegativeButton("NO") { dialog, which -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder!!.create()
            alert.show()
        }
    }

    override fun onStart() {
        super.onStart()
        workhubJobs!!.child(job_key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Job, SuggesViewHolder> =
                    object : FirebaseRecyclerAdapter<Job, SuggesViewHolder>(
                        Job::class.java,
                        R.layout.job_row,
                        SuggesViewHolder::class.java,
                        workhubJobs!!.orderByChild("jobKeyWord")
                            .equalTo(dataSnapshot.child("jobKeyWord").value as String)
                            .limitToLast(5)
                    ) {
                        override fun populateViewHolder(
                            viewHolder: SuggesViewHolder,
                            model: Job,
                            position: Int
                        ) {
                            val job_key = getRef(position).key
                            viewHolder.setJobName(model.jobName)
                            viewHolder.setJobBudget(model.jobBudget)
                            viewHolder.setJobLocation(model.jobLocationName)
                            viewHolder.setJobDate(model.jobPostedDate)
                            viewHolder.mview.setOnClickListener {
                                val intent = Intent(this@SingleJobView, SingleJobView::class.java)
                                intent.putExtra("job_id", job_key)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                sugges_list!!.adapter = firebaseRecyclerAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    class SuggesViewHolder(var mview: View) : RecyclerView.ViewHolder(
        mview
    ) {
        fun setJobName(jobname: String?) {
            val jName = mview.findViewById<View>(R.id.jobrowname) as TextView
            jName.text = jobname
        }

        fun setJobBudget(jobbudget: String?) {
            val jBudget = mview.findViewById<View>(R.id.jobrowbudget) as TextView
            jBudget.text = "Rs.$jobbudget"
        }

        fun setJobLocation(jobLocation: String?) {
            val jLocation = mview.findViewById<View>(R.id.jobrowlocation) as TextView
            jLocation.text = jobLocation
        }

        fun setJobDate(jobDate: String?) {
            val jDate = mview.findViewById<View>(R.id.jobrowdate) as TextView
            jDate.text = "Added on $jobDate"
        }
    }
}