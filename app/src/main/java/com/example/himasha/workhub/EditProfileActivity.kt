package com.example.himasha.workhub

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileActivity constructor() : AppCompatActivity() {
    private var profileBio: EditText? = null
    private var profileAddress: EditText? = null
    private var profileWebsite: EditText? = null
    private var profileTelephone: EditText? = null
    private var profileEmail: EditText? = null
    private var editProfile: Button? = null
    private var auth: FirebaseAuth? = null
    private var workhub: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        auth = FirebaseAuth.getInstance()
        workhub = FirebaseDatabase.getInstance().getReference().child("users").child(
            auth.getCurrentUser()!!.getUid()
        )
        profileBio = findViewById<View>(R.id.editprofilebioET) as EditText?
        profileAddress = findViewById<View>(R.id.editprofileaddressET) as EditText?
        profileWebsite = findViewById<View>(R.id.editprofileWebsiteET) as EditText?
        profileTelephone = findViewById<View>(R.id.editprofileTelephoneET) as EditText?
        profileEmail = findViewById<View>(R.id.editprofileEmailET) as EditText?
        editProfile = findViewById<View>(R.id.editProfileBTN) as Button?
        workhub.addValueEventListener(object : ValueEventListener {
            public override fun onDataChange(dataSnapshot: DataSnapshot) {
                profileBio!!.setText(dataSnapshot.child("userBio").getValue() as String?)
                profileAddress!!.setText(dataSnapshot.child("userAddress").getValue() as String?)
                profileWebsite!!.setText(dataSnapshot.child("userWebsite").getValue() as String?)
                profileTelephone!!.setText(
                    dataSnapshot.child("userTelephone").getValue() as String?
                )
                profileEmail!!.setText(dataSnapshot.child("userEmail").getValue() as String?)
            }

            public override fun onCancelled(databaseError: DatabaseError) {}
        })
        editProfile!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val bio: String = profileBio!!.getText().toString()
                val address: String = profileAddress!!.getText().toString()
                val website: String = profileWebsite!!.getText().toString()
                val telephone: String = profileTelephone!!.getText().toString()
                val email: String = profileEmail!!.getText().toString()
                workhub.addValueEventListener(object : ValueEventListener {
                    public override fun onDataChange(dataSnapshot: DataSnapshot) {
                        workhub.child("userBio").setValue(bio)
                        workhub.child("userAddress").setValue(address)
                        workhub.child("userWebsite").setValue(website)
                        workhub.child("userTelephone").setValue(telephone)
                        workhub.child("userEmail").setValue(email)
                    }

                    public override fun onCancelled(databaseError: DatabaseError) {}
                })
                Toast.makeText(this@EditProfileActivity, "Changes saved!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}