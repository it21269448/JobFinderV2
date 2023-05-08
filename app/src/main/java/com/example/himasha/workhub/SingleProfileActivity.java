package com.example.himasha.workhub;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleProfileActivity extends AppCompatActivity {
    private String user_key = null;
    private FirebaseAuth auth;
    private DatabaseReference workhubUsers;

    private TextView userName;
    private TextView userEmail;
    private TextView prohomeaddress;
    private TextView prowebsite;
    private TextView protelephone;
    private TextView probio;
    private EditText review;

    private Button callBTN;
    private Button msgBTN;
    private Button emailBTN;
    private Button addreview;

    RecyclerView review_list;

    private String tele;
    private String userN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_profile);
        user_key = getIntent().getExtras().getString("user_id");

        auth = FirebaseAuth.getInstance();
        workhubUsers = FirebaseDatabase.getInstance().getReference().child("users");

        userName = (TextView) findViewById(R.id.singleProfileUserName);
        userEmail = (TextView) findViewById(R.id.singleProfileEmail);
        protelephone = (TextView)findViewById(R.id.singleProfileTelephone);
        prohomeaddress = (TextView)findViewById(R.id.singleProfileAddress);
        prowebsite = (TextView)findViewById(R.id.singleProfileWebsite);
        probio = (TextView)findViewById(R.id.singleProfileBio);
        review = (EditText) findViewById(R.id.addReviewET);

        callBTN = (Button) findViewById(R.id.singleProfileCallBTN);
        msgBTN = (Button) findViewById(R.id.singleProfileMsgBTN);
        emailBTN = (Button) findViewById(R.id.singleProfileEmailBTN);
        addreview = (Button) findViewById(R.id.submitReviewBTN);

        review_list = (RecyclerView)findViewById(R.id.reviews_list);
        review_list.setHasFixedSize(true);
        review_list.setLayoutManager(new LinearLayoutManager(this));

        workhubUsers.child(user_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText((String) dataSnapshot.child("userName").getValue());
                userEmail.setText((String) dataSnapshot.child("userEmail").getValue());
                tele = (String) dataSnapshot.child("userTelephone").getValue();
                protelephone.setText((String)dataSnapshot.child("userTelephone").getValue());
                prohomeaddress.setText((String)dataSnapshot.child("userAddress").getValue());
                prowebsite.setText((String)dataSnapshot.child("userWebsite").getValue());
                probio.setText((String)dataSnapshot.child("userBio").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        workhubUsers.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userN = (String) dataSnapshot.child("userName").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        callBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + tele.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

            }
        });

        msgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+ tele.trim()));
                startActivity(sendIntent);

            }
        });

        emailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                sendIntent.setData(Uri.parse(auth.getCurrentUser().getEmail()));
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                startActivity(sendIntent);

            }
        });

        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = review.getText().toString();
                if (!TextUtils.isEmpty(reviewText))
                {
                    String postingUser = auth.getCurrentUser().getUid();
                    String reviewingUser = user_key;
                    String reviewid= workhubUsers.child(user_key).child("reviews").push().getKey();
                    Review newRe = new Review();
                    newRe.setReview(reviewText);
                    newRe.setReviewedUserName(userN);
                    newRe.setReviewedUser(reviewingUser);
                    newRe.setReviewingUser(postingUser);

                    workhubUsers.child(user_key).child("reviews").child(reviewid).setValue(newRe);
                    Toast.makeText(SingleProfileActivity.this,"Review posted!",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(SingleProfileActivity.this,"You haven't said anything!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Review,ReviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(
                Review.class,
                R.layout.review_row,
                ReviewViewHolder.class,
                workhubUsers.child(user_key).child("reviews")
        ) {
            @Override
            protected void populateViewHolder(ReviewViewHolder viewHolder, Review model, int position) {
                final String job_key = getRef(position).getKey();
                viewHolder.setReview(model.getReview());
                viewHolder.setReviewUser(model.getReviewedUserName());

                viewHolder.mview.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reviewId = getRef(position).getKey();
                        workhubUsers.child(user_key).child("reviews").child(reviewId).removeValue();
                        Toast.makeText(SingleProfileActivity.this, "Review deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        review_list.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public ReviewViewHolder(View itemView) {
            super(itemView);

            mview = itemView;
        }

        public void setReview(String review){
            TextView jName = (TextView) mview.findViewById(R.id.reviewContentTV);
            jName.setText(review);
        }

        public void setReviewUser(String reuser){
            TextView jBudget = (TextView) mview.findViewById(R.id.reviewPostedUserTV);
            jBudget.setText(reuser);
        }
    }


}
