package com.example.himasha.workhub

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {
    private var editTextName: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextExperience: EditText? = null
    private var buttonSubmit: Button? = null
    private var buttonCancel: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextExperience = findViewById(R.id.editTextExperience)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        buttonCancel = findViewById(R.id.buttonCancel)
        buttonSubmit.setOnClickListener(View.OnClickListener {
            // Get the text from the EditTexts
            val name = editTextName.getText().toString()
            val email = editTextEmail.getText().toString()
            val experience = editTextExperience.getText().toString()

            // Validate the input
            if (name.isEmpty() || email.isEmpty() || experience.isEmpty()) {
                Toast.makeText(
                    this@FeedbackActivity,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Do something with the input, such as send it to a server
                Toast.makeText(
                    this@FeedbackActivity,
                    "Submitted: $name, $email, $experience",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        buttonCancel.setOnClickListener(View.OnClickListener { // Clear the EditTexts and show a message
            editTextName.setText("")
            editTextEmail.setText("")
            editTextExperience.setText("")
            Toast.makeText(this@FeedbackActivity, "Canceled", Toast.LENGTH_SHORT).show()
        })
    }
}