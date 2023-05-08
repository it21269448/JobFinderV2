package com.example.himasha.workhub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SearchFragment constructor() : Fragment() {
    var search_list: RecyclerView? = null
    var workhub: DatabaseReference? = null
    var searchtext: AutoCompleteTextView? = null
    var selectedjobKey: String? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        return view
    }

    public override fun onStart() {
        super.onStart()
    }

    class SearchJobViewHolder constructor(var mview: View) : RecyclerView.ViewHolder(
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
        val skills: Array<String> = arrayOf(
            "Home Cleaner",
            "Nanny",
            "Baker",
            "Cook",
            "Maid",
            "Fllor technician",
            "Pet Sitter",
            "Plumber",
            "Waiter",
            "General Cashier",
            "Chef",
            "Housekeeper",
            "Kitchen Technician",
            "Carpenter",
            "Driver",
            "Brickmason",
            "Cement Mason",
            "Blockmason",
            "Concrete Finisher",
            "Drywall and Ceiling Tile Installer",
            "Drywall and Ceiling Tile Installer",
            "Tile and Marble Setter",
            "Pipe Fitter",
            "Pipe Fitter",
            "Maintenance and Repair Worker",
            "Carpet Installer",
            "Electric Motor, Power Tool, and Related Repairer",
            "Electrician",
            "Electrical Power-Line Installer and Repairer",
            "Welder",
            "Refrigeration Mechanic and Installer",
            "Refrigeration Mechanic and Installer",
            "Motorcycle Mechanic",
            "Mechanical Engineering Technician",
            "Gardener"
        )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter((getActivity())!!, android.R.layout.select_dialog_item, skills)
        workhub = FirebaseDatabase.getInstance().getReference().child("jobs")
        searchtext = view.findViewById<View>(R.id.searchjobET) as AutoCompleteTextView?
        searchtext!!.setThreshold(1)
        searchtext!!.setAdapter(adapter)
        search_list = view.findViewById<View>(R.id.search_list) as RecyclerView?
        search_list!!.setHasFixedSize(true)
        search_list!!.setLayoutManager(LinearLayoutManager(getActivity()))
        searchtext!!.setOnItemClickListener(object : OnItemClickListener {
            public override fun onItemClick(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Job, SearchJobViewHolder> =
                    object : FirebaseRecyclerAdapter<Job, SearchJobViewHolder>(
                        Job::class.java,
                        R.layout.search_row,
                        SearchJobViewHolder::class.java,
                        workhub.orderByChild("jobKeyWord").equalTo(adapter.getItem(position))
                    ) {
                        override fun populateViewHolder(
                            viewHolder: SearchJobViewHolder,
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
                                    val intent: Intent =
                                        Intent(getActivity(), SingleJobView::class.java)
                                    intent.putExtra("job_id", job_key)
                                    startActivity(intent)
                                }
                            })
                        }
                    }
                search_list!!.setAdapter(firebaseRecyclerAdapter)
            }
        })
    }

    companion object {
        fun newInstance(): SearchFragment {
            val fragment: SearchFragment = SearchFragment()
            return fragment
        }
    }
}