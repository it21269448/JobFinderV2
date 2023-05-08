package com.example.himasha.workhub

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AddJobActivity : AppCompatActivity() {
    private var addJobName: EditText? = null
    private var addJobDesc: EditText? = null
    private var addJobBudget: EditText? = null
    private var submitjob: Button? = null
    private var addjobSkills: AutoCompleteTextView? = null
    private var addJobLocation: EditText? = null
    private var auth: FirebaseAuth? = null
    private var workhub: DatabaseReference? = null
    private var workhubUsers: DatabaseReference? = null
    private val tripLong: Double? = null
    private val tripLat: Double? = null
    private var builder: AlertDialog.Builder? = null
    private var userName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_job)
        builder = AlertDialog.Builder(this)
        auth = FirebaseAuth.getInstance()
        workhub = FirebaseDatabase.getInstance().reference.child("jobs")
        workhubUsers = FirebaseDatabase.getInstance().reference.child("users")
        val skills = arrayOf(
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
        val adapter = ArrayAdapter(this, android.R.layout.select_dialog_item, skills)
        addJobName = findViewById<View>(R.id.addjobJobNameET) as EditText
        addJobDesc = findViewById<View>(R.id.addjobDescET) as EditText
        addJobLocation = findViewById<View>(R.id.addLocation1) as EditText
        addJobBudget = findViewById<View>(R.id.addjobBudgetET) as EditText
        submitjob = findViewById<View>(R.id.submitjobBTN) as Button
        addjobSkills = findViewById<View>(R.id.addjobSkilltET) as AutoCompleteTextView
        addjobSkills!!.threshold = 1
        addjobSkills!!.setAdapter(adapter)

//        PlaceAutocompleteFragment places= (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("LK")
//                .build();
//
//        places.setFilter(typeFilter);
//
//        places.setHint("e.g Malabe");
//        ((View)findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
//        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.input_outline);
//        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setTextSize(18);
//        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setPadding(32,32,32,32);

//        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                tripLat = place.getLatLng().latitude;
//                tripLong = place.getLatLng().longitude;
//                addJobLocation = place.getName().toString();
//
//            }
//            @Override
//            public void onError(Status status) {
//
//            }
//
//        });
        workhubUsers.child(auth.getCurrentUser()!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userName = dataSnapshot.child("userName").value as String
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        submitjob!!.setOnClickListener {
            val jname = addJobName!!.text.toString()
            val jdesc = addJobDesc!!.text.toString()
            val jLoc = addJobLocation!!.text.toString()
            val jbudget = addJobBudget!!.text.toString()
            val skill = addjobSkills!!.text.toString()
            if (!TextUtils.isEmpty(jname) && !TextUtils.isEmpty(jdesc) && !TextUtils.isEmpty(jbudget) && !TextUtils.isEmpty(
                    jLoc
                ) && !TextUtils.isEmpty(skill)
            ) {
                builder!!.setTitle("Confirm")
                builder!!.setMessage("Are you sure you want to post this job?")
                builder!!.setPositiveButton("YES") { dialog, which ->
                    val sdf = SimpleDateFormat("MMMM d, yyyy")
                    val jdate = sdf.format(Date())
                    val juid = auth.getCurrentUser()!!.uid
                    val jobId = workhub.push().key
                    val newJob = Job()
                    newJob.jobName = jname
                    newJob.jobDesc = jdesc
                    newJob.jobBudget = jbudget
                    newJob.jobLocationName = jLoc
                    newJob.jobLocationLat = tripLat
                    newJob.jobLocationLong = tripLong
                    newJob.jobPostedDate = jdate
                    newJob.jobPostedUserId = juid
                    newJob.jobKeyWord = skill
                    newJob.jobPostedUserName = auth.getCurrentUser()!!.email
                    newJob.jobStatus = "Available"
                    workhub.child(jobId).setValue(newJob)
                    Toast.makeText(this@AddJobActivity, "Job added successfuly", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                    startActivity(Intent(this@AddJobActivity, FeedActivity::class.java))
                    finish()
                }
                builder!!.setNegativeButton("NO") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder!!.create()
                alert.show()
            } else {
                Toast.makeText(
                    this@AddJobActivity,
                    "You cannot have one or more empty fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}