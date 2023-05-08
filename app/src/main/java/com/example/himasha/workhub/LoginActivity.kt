package com.example.himasha.workhub

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity constructor() : AppCompatActivity() {
    private var lEmail: TextView? = null
    private var lPass: TextView? = null
    private var lLoginBTN: Button? = null
    private var lForgotPassBTN: Button? = null
    private var lLinktoSignupBTN: Button? = null
    private var progress: ProgressDialog? = null
    private var auth: FirebaseAuth? = null
    private var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getSupportActionBar()!!.hide()
        auth = FirebaseAuth.getInstance()
        pref = getSharedPreferences("Users", 0)
        progress = ProgressDialog(this)
        lEmail = findViewById<View>(R.id.emailLoginET) as TextView?
        lPass = findViewById<View>(R.id.passwordLoginET) as TextView?
        lLoginBTN = findViewById<View>(R.id.loginBTN) as Button?
        lLinktoSignupBTN = findViewById<View>(R.id.linktoregisterBTN) as Button?
        lForgotPassBTN = findViewById<View>(R.id.forgotpasswordBTN) as Button?
        lLinktoSignupBTN!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val intent: Intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        lForgotPassBTN!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val userEmail: String = lEmail!!.getText().toString().trim({ it <= ' ' })
                if (!TextUtils.isEmpty(userEmail)) {
                    if (validateEmail(userEmail) == true) {
                        progress!!.setMessage("Verifying Email address, Please wait!")
                        progress!!.show()
                        auth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(object : OnCompleteListener<Void?> {
                                public override fun onComplete(task: Task<Void?>) {
                                    if (task.isSuccessful()) {
                                        progress!!.dismiss()
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "An email has been sent to you with password reset details. Please check your Emails.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        progress!!.dismiss()
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Cannot find an account with provided email!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email Invalid, Please enter a valid Email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter the email that you signed up with!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        lLoginBTN!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val userEmail: String = lEmail!!.getText().toString().trim({ it <= ' ' })
                val userPass: String = lPass!!.getText().toString().trim({ it <= ' ' })
                if (!TextUtils.isEmpty(userEmail) || !TextUtils.isEmpty(userPass)) {
                    if (validateEmail(userEmail) == true) {
                        progress!!.setMessage("Signing In, Please Wait!")
                        progress!!.show()
                        auth.signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                                public override fun onComplete(task: Task<AuthResult?>) {
                                    if (task.isSuccessful()) {
                                        val editor: SharedPreferences.Editor = pref.edit()
                                        editor.putBoolean(Constants.IS_LOGGED_IN, true)
                                        editor.putString(
                                            Constants.EMAIL, auth.getCurrentUser()!!
                                                .getEmail()
                                        )
                                        editor.putString(
                                            Constants.UNIQUE_ID, auth.getCurrentUser()!!
                                                .getUid()
                                        )
                                        editor.apply()
                                        progress!!.dismiss()
                                        val intent: Intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        progress!!.dismiss()
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Email or password you entered is incorrect!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email Invalid, Please enter a valid Email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
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
        val EMAIL_PATTERN: String =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
}