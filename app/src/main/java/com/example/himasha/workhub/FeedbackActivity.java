package com.example.himasha.workhub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextExperience;
    private Button buttonSubmit;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextExperience = findViewById(R.id.editTextExperience);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditTexts
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String experience = editTextExperience.getText().toString();

                // Validate the input
                if (name.isEmpty() || email.isEmpty() || experience.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Do something with the input, such as send it to a server
                    Toast.makeText(FeedbackActivity.this, "Submitted: " + name + ", " + email + ", " + experience, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the EditTexts and show a message
                editTextName.setText("");
                editTextEmail.setText("");
                editTextExperience.setText("");
                Toast.makeText(FeedbackActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
