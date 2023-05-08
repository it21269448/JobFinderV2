package com.example.himasha.workhub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JobFragment constructor() : Fragment() {
    var actionButton: FloatingActionButton? = null
    var job_list: RecyclerView? = null
    var workhub: DatabaseReference? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_job, container, false)
        initViews(view)
        return view
    }

    public override fun onStart() {
        super.onStart()
        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Job, JobViewHolder> =
            object : FirebaseRecyclerAdapter<Job, JobViewHolder>(
                Job::class.java,
                R.layout.job_row,
                JobViewHolder::class.java,
                workhub
            ) {
                override fun populateViewHolder(
                    viewHolder: JobViewHolder,
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
        job_list!!.setAdapter(firebaseRecyclerAdapter)
    }

    class JobViewHolder constructor(var mview: View) : RecyclerView.ViewHolder(
        mview
    ) {
        fun setJobName(jobname: String?) {
            val jName: TextView = mview.findViewById<View>(R.id.jobrowname) as TextView
            jName.setText(jobname)
        }

        fun setJobBudget(jobbudget: String?) {
            val jBudget: TextView = mview.findViewById<View>(R.id.jobrowbudget) as TextView
            jBudget.setText("Rs." + jobbudget)
        }

        fun setJobLocation(jobLocation: String?) {
            val jLocation: TextView = mview.findViewById<View>(R.id.jobrowlocation) as TextView
            jLocation.setText(jobLocation)
        }

        fun setJobDate(jobDate: String?) {
            val jDate: TextView = mview.findViewById<View>(R.id.jobrowdate) as TextView
            jDate.setText("Added on " + jobDate)
        }
    }

    private fun initViews(view: View) {
        workhub = FirebaseDatabase.getInstance().getReference().child("jobs")
        workhub.keepSynced(true)
        actionButton = view.findViewById<View>(R.id.floatingActionButton2) as FloatingActionButton?
        job_list = view.findViewById<View>(R.id.job_list) as RecyclerView?
        job_list!!.setHasFixedSize(true)
        job_list!!.setLayoutManager(LinearLayoutManager(getActivity()))
        actionButton!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val intent: Intent = Intent(getActivity(), AddJobActivity::class.java)
                startActivity(intent)
            }
        })
    }

    companion object {
        fun newInstance(): JobFragment {
            val fragment: JobFragment = JobFragment()
            return fragment
        }
    }
}