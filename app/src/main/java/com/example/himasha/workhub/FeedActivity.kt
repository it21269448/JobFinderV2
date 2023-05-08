package com.example.himasha.workhub

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class FeedActivity constructor() : AppCompatActivity() {
    private var builder: AlertDialog.Builder? = null
    private var auth: FirebaseAuth? = null
    private var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        pref = getSharedPreferences("Users", 0)
        builder = AlertDialog.Builder(this)
        auth = FirebaseAuth.getInstance()
        val bottomNavigationView: BottomNavigationView =
            findViewById<View>(R.id.navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            public override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var selectedFragment: Fragment? = null
                when (item.getItemId()) {
                    R.id.action_item1 -> selectedFragment = JobFragment.Companion.newInstance()
                    R.id.action_item2 -> selectedFragment = SearchFragment.Companion.newInstance()
                    R.id.action_item3 -> selectedFragment = ProfileFragment.Companion.newInstance()
                }
                val transaction: FragmentTransaction =
                    getSupportFragmentManager().beginTransaction()
                transaction.replace(R.id.frame_layout, (selectedFragment)!!)
                transaction.commit()
                return true
            }
        })


        //Manually displaying the first fragment - one time only


        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
        val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        transaction.replace(R.id.frame_layout, JobFragment.Companion.newInstance())
        transaction.commit()
    }

    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.SignOut) {
            builder!!.setTitle("Confirm")
            builder!!.setMessage("Are you sure you want to logout?")
            builder!!.setPositiveButton("YES", object : DialogInterface.OnClickListener {
                public override fun onClick(dialog: DialogInterface, which: Int) {
                    auth!!.signOut()
                    val editor: SharedPreferences.Editor = pref!!.edit()
                    editor.putBoolean(Constants.IS_LOGGED_IN, false)
                    editor.putString(Constants.EMAIL, null)
                    editor.putString(Constants.UNIQUE_ID, null)
                    editor.apply()
                    dialog.dismiss()
                    startActivity(Intent(this@FeedActivity, MainActivity::class.java))
                    finish()
                }
            })
            builder!!.setNegativeButton("NO", object : DialogInterface.OnClickListener {
                public override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            val alert: AlertDialog = builder!!.create()
            alert.show()
        } else if (item.getItemId() == R.id.savedJobs) {
            val savedJobFragment: Fragment = SavedJobFragment.Companion.newInstance()
            val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            transaction.replace(R.id.frame_layout, savedJobFragment)
            transaction.commit()
        }
        return super.onOptionsItemSelected(item)
    }
}