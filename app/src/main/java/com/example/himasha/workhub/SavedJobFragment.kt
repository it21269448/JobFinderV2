package com.example.himasha.workhub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SavedJobFragment : Fragment() {
    var actionButton: FloatingActionButton? = null
    var job_list: RecyclerView? = null
    var workhub: DatabaseReference? = null
    private val removeJob: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_job, container, false)
        initViews(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Job, JobViewHolder> =
            object : FirebaseRecyclerAdapter<Job, JobViewHolder>(
                Job::class.java,
                R.layout.saved_job_row,
                JobViewHolder::class.java,
                workhub!!.orderByChild("isSaved").equalTo("1")
            ) {
                override fun populateViewHolder(
                    viewHolder: JobViewHolder,
                    model: Job,
                    position: Int
                ) {
                    val job_key = getRef(position).key
                    viewHolder.setKey(job_key)
                    viewHolder.setJobName(model.jobName)
                    viewHolder.setJobBudget(model.jobBudget)
                    viewHolder.setJobLocation(model.jobLocationName)
                    viewHolder.setJobDate(model.jobPostedDate)
                    viewHolder.mview.setOnClickListener {
                        val intent = Intent(activity, SingleJobView::class.java)
                        intent.putExtra("job_id", job_key)
                        startActivity(intent)
                    }
                    viewHolder.mview.findViewById<View>(R.id.remove_btn).setOnClickListener { v ->
                        workhub!!.child(job_key).removeValue()
                        notifyItemRemoved(position)
                        Toast.makeText(v.context, "Job removed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        job_list!!.adapter = firebaseRecyclerAdapter
    }

    class JobViewHolder(var mview: View) : RecyclerView.ViewHolder(
        mview
    ) {
        var key: String? = null
        fun setKey(key: String?) {
            this.key = key
        }

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

    private fun initViews(view: View) {
        workhub = FirebaseDatabase.getInstance().reference.child("jobs")
        job_list = view.findViewById<View>(R.id.job_list) as RecyclerView
        job_list!!.setHasFixedSize(true)
        job_list!!.layoutManager = LinearLayoutManager(this.activity)
    }

    companion object {
        fun newInstance(): SavedJobFragment {
            return SavedJobFragment()
        }
    }
}