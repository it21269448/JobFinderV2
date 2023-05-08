package com.example.himasha.workhub

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditJobActivity : AppCompatActivity() {
    private var job_key: String? = null
    private var editJobName: EditText? = null
    private var editJobDesc: EditText? = null
    private var editJobBudget: EditText? = null
    private var editLocat: TextView? = null
    private var editJobBTN: Button? = null
    private var editJobLocation: String? = null
    private var auth: FirebaseAuth? = null
    private var workhub: DatabaseReference? = null
    private var jobLong: Double? = null
    private var jobLat: Double? = null
    private var builder: AlertDialog.Builder? = null
    private val userName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_job)
        job_key = intent.extras!!.getString("job_id")
        builder = AlertDialog.Builder(this)
        auth = FirebaseAuth.getInstance()
        workhub = FirebaseDatabase.getInstance().reference.child("jobs")
        editJobName = findViewById<View>(R.id.editjobJobNameET) as EditText
        editJobDesc = findViewById<View>(R.id.editjobDescET) as EditText
        editJobBudget = findViewById<View>(R.id.editjobBudgetET) as EditText
        editLocat = findViewById<View>(R.id.locat) as TextView
        editJobBTN = findViewById<View>(R.id.editJobBTN) as Button
        val places =
            fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        workhub.child(job_key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                editJobName!!.setText(dataSnapshot.child("jobName").value as String)
                editJobDesc!!.setText(dataSnapshot.child("jobDesc").value as String)
                editJobBudget!!.setText(dataSnapshot.child("jobBudget").value as String)
                editLocat!!.text = dataSnapshot.child("jobLocationName").value as String
                places.setText(editLocat!!.text)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("LK")
//                .build();
//
//        places.setFilter(typeFilter);
//        ((View)findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
//        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.input_outline);
//        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setTextSize(18);
//        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setPadding(32,32,32,32);
        places.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                jobLat = place.latLng.latitude
                jobLong = place.latLng.longitude
                editJobLocation = place.name.toString()
            }

            override fun onError(status: Status) {}
        })
        editJobBTN!!.setOnClickListener {
            val jname = editJobName!!.text.toString()
            val jdesc = editJobDesc!!.text.toString()
            val jbudget = editJobBudget!!.text.toString()
            val jLoc = editLocat!!.text.toString()
            if (!TextUtils.isEmpty(jname) && !TextUtils.isEmpty(jdesc) && !TextUtils.isEmpty(jbudget) && !TextUtils.isEmpty(
                    jLoc
                )
            ) {
                builder!!.setTitle("Confirm")
                builder!!.setMessage("Save changes?")
                builder!!.setPositiveButton("YES") { dialog, which ->
                    workhub.child(job_key).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.ref.child("jobName").setValue(jname)
                            dataSnapshot.ref.child("jobDesc").setValue(jdesc)
                            dataSnapshot.ref.child("jobBudget").setValue(jbudget)
                            dataSnapshot.ref.child("jobLocationName").setValue(jLoc)
                            dataSnapshot.ref.child("jobLocationLong").setValue(jobLong)
                            dataSnapshot.ref.child("jobLocationLat").setValue(jobLat)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                    Toast.makeText(this@EditJobActivity, "Changes saves!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@EditJobActivity, SingleJobView::class.java)
                    intent.putExtra("job_id", job_key)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                    dialog.dismiss()
                }
                builder!!.setNegativeButton("NO") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder!!.create()
                alert.show()
            } else {
                Toast.makeText(
                    this@EditJobActivity,
                    "You cannot have one or more empty fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}