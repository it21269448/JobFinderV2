package com.example.himasha.workhub

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity() : AppCompatActivity() {
    private var fulname: EditText? = null
    private var email: EditText? = null
    private var telephone: EditText? = null
    private var password: EditText? = null
    private var confPassword: EditText? = null
    private var registerBTN: Button? = null
    private var linktologin: Button? = null
    private var progress: ProgressDialog? = null
    private var auth: FirebaseAuth? = null
    private var workhub: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
        workhub = FirebaseDatabase.getInstance().reference.child("users")
        progress = ProgressDialog(this)
        fulname = findViewById<View>(R.id.fulNameRegisterET) as EditText
        email = findViewById<View>(R.id.emailRegisterET) as EditText
        telephone = findViewById<View>(R.id.telephoneRegisterET) as EditText
        password = findViewById<View>(R.id.passwordRegisterET) as EditText
        confPassword = findViewById<View>(R.id.confPasswordRegisterET) as EditText
        registerBTN = findViewById<View>(R.id.registerBTN) as Button
        linktologin = findViewById<View>(R.id.linktologinBTN) as Button
        linktologin!!.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        registerBTN!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val userName = fulname!!.text.toString().trim { it <= ' ' }
                val userEmail = email!!.text.toString().trim { it <= ' ' }
                val userTelephone = telephone!!.text.toString().trim { it <= ' ' }
                val userPass = password!!.text.toString().trim { it <= ' ' }
                val userConfPass = confPassword!!.text.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass) && !TextUtils.isEmpty(
                        userConfPass
                    ) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userTelephone)
                ) {
                    if (validateEmail(userEmail) == true) {
                        if ((userConfPass == userPass)) {
                            progress!!.setMessage("Signing Up, Please Wait!")
                            progress!!.show()
                            auth.createUserWithEmailAndPassword(userEmail, userPass)
                                .addOnCompleteListener(
                                    OnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val uid = auth.getCurrentUser()!!.uid
                                            val newuser = User()
                                            newuser.userEmail = userEmail
                                            newuser.userName = userName
                                            newuser.userTelephone = userTelephone
                                            workhub.child(uid).setValue(newuser)
                                            progress!!.dismiss()
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "You have been successfuly registered, please login to continue!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(
                                                this@RegisterActivity,
                                                LoginActivity::class.java
                                            )
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            progress!!.dismiss()
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                task.exception!!.message + " Try a different Email",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                        } else {
                            progress!!.dismiss()
                            Toast.makeText(
                                this@RegisterActivity,
                                "Passwords you entered do not match, Please try again!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Email Invalid, Please enter a valid Email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "You cannot have one or more empty fields!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    fun validateEmail(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
}