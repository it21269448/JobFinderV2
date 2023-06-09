package com.example.himasha.workhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleJobView extends AppCompatActivity {

    private String job_key = null;
    private String jobAddedUser = null;

    private TextView jobName;
    private TextView jobDesc;
    private TextView jobLocation;
    private TextView jobBudget;
    private TextView jobPostedDate;
    private TextView jobPostedUser;
    private TextView jobPostedMap;

    private Button deletejob;
    private Button editjob;
    private Button saveJob;
    private Button feedbackJob;

    private RecyclerView sugges_list;

    private CardView locationMapCardview;
    private CardView editRemoveJobcardview;
    private CardView postedUserCardview;

    private DatabaseReference workhubUsers;
    private DatabaseReference workhubJobs;
    private DatabaseReference workhubSavedJobs;
    private DatabaseReference workhubsuges;
    private FirebaseAuth auth;

    private AlertDialog.Builder builder;
    private String job_location;

    private Double jobLat;
    private Double jobLong;
    private String posteduserid;

    private String keyword;

    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_job_view);
        pref = this.getSharedPreferences("Users",0);
        job_key = getIntent().getExtras().getString("job_id");
        auth = FirebaseAuth.getInstance();
        workhubJobs = FirebaseDatabase.getInstance().getReference().child("jobs");
        workhubSavedJobs = FirebaseDatabase.getInstance().getReference().child("saved_jobs");
        workhubUsers = FirebaseDatabase.getInstance().getReference().child("users");

        builder = new AlertDialog.Builder(this);

        jobName = (TextView) findViewById(R.id.singlejobName);
        jobDesc = (TextView) findViewById(R.id.singlejobDesc);
        jobLocation = (TextView) findViewById(R.id.singlejobLocation);
        jobBudget = (TextView) findViewById(R.id.singlejobBudget);
        jobPostedDate = (TextView) findViewById(R.id.singlejobPostedDate);
        jobPostedUser = (TextView) findViewById(R.id.singlejobPostedUser);
        jobPostedMap = (TextView) findViewById(R.id.singlejobMapName);

        deletejob = (Button) findViewById(R.id.singlejobDeleteBTN);
        saveJob = (Button) findViewById(R.id.saveJobButton);
        editjob = (Button) findViewById(R.id.singlejobEditBTN);
        feedbackJob = (Button) findViewById(R.id.feedbackButton);

        sugges_list = (RecyclerView)findViewById(R.id.sugges_list);
        sugges_list.setHasFixedSize(true);
        sugges_list.setLayoutManager(new LinearLayoutManager(this));

        locationMapCardview = (CardView)findViewById(R.id.locationMapCard);
        editRemoveJobcardview = (CardView)findViewById(R.id.editremoveJobCard);
        postedUserCardview = (CardView) findViewById(R.id.postedUserCard);


        workhubJobs.child(job_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {

                keyword = (String) dataSnapshot1.child("jobKeyWord").getValue();
                final String job_name = (String) dataSnapshot1.child("jobName").getValue();
                final String job_desc = (String) dataSnapshot1.child("jobDesc").getValue();
                job_location = (String) dataSnapshot1.child("jobLocationName").getValue();
                final String job_budget = (String) dataSnapshot1.child("jobBudget").getValue();
                final String job_postedDate = (String) dataSnapshot1.child("jobPostedDate").getValue();
                final String job_postedUser = (String) dataSnapshot1.child("jobPostedUserId").getValue();
                posteduserid = (String) dataSnapshot1.child("jobPostedUserId").getValue();

                final String job_postedUserName = (String) dataSnapshot1.child("jobPostedUserName").getValue();
                jobLat = (Double) dataSnapshot1.child("jobLocationLat").getValue();
                jobLong = (Double) dataSnapshot1.child("jobLocationLong").getValue();

                jobName.setText(job_name);
                jobDesc.setText(job_desc);
                jobLocation.setText(job_location);
                jobBudget.setText("Rs."+job_budget);
                jobPostedDate.setText("Posted on "+job_postedDate);
                jobPostedUser.setText("Posted by "+job_postedUserName);

                jobPostedMap.setText("get directions to "+job_location);


                if (auth.getCurrentUser().getUid().equals(job_postedUser))
                {
                    editRemoveJobcardview.setVisibility(View.VISIBLE);
                }
                else {
                    editRemoveJobcardview.setVisibility(View.GONE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        postedUserCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posteduserid.equals(auth.getCurrentUser().getUid()))
                {
                    Intent intent = new Intent(SingleJobView.this, SingleProfileActivity.class);
                    intent.putExtra("user_id", posteduserid);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SingleJobView.this, SingleProfileActivity.class);
                    intent.putExtra("user_id", posteduserid);
                    startActivity(intent);
                }

            }
        });

        locationMapCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SingleJobView.this, MapLoadActivity.class);
                intent.putExtra("job_lat", jobLat);
                intent.putExtra("job_long",jobLong);
                startActivity(intent);
            }
        });


        editjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleJobView.this, EditJobActivity.class);
                intent.putExtra("job_id", job_key);
                startActivity(intent);
            }
        });

        deletejob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete this job?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if(job_key != null) {
                            workhubJobs.child(job_key).removeValue();

                            Intent intent2 = new Intent(SingleJobView.this, FeedActivity.class);
                            startActivity(intent2);
                            finish();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        saveJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to save this job?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        workhubJobs.child(job_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("isSaved").setValue("1");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        feedbackJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to save this job?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if(job_key != null) {
                            workhubJobs.child("saved_jobs").push();

                            Intent intent2 = new Intent(SingleJobView.this, FeedActivity.class);
                            startActivity(intent2);
                            finish();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        workhubJobs.child(job_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseRecyclerAdapter<Job,SuggesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Job, SuggesViewHolder>(
                        Job.class,
                        R.layout.job_row,
                        SuggesViewHolder.class,
                        workhubJobs.orderByChild("jobKeyWord").equalTo((String) dataSnapshot.child("jobKeyWord").getValue()).limitToLast(5)
                ) {
                    @Override
                    protected void populateViewHolder(SuggesViewHolder viewHolder, Job model, int position) {
                        final String job_key = getRef(position).getKey();
                        viewHolder.setJobName(model.getJobName());
                        viewHolder.setJobBudget(model.getJobBudget());
                        viewHolder.setJobLocation(model.getJobLocationName());
                        viewHolder.setJobDate(model.getJobPostedDate());

                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SingleJobView.this, SingleJobView.class);
                                intent.putExtra("job_id", job_key);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                };
                sugges_list.setAdapter(firebaseRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public static class SuggesViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public SuggesViewHolder(View itemView) {
            super(itemView);

            mview = itemView;
        }

        public void setJobName(String jobname){
            TextView jName = (TextView) mview.findViewById(R.id.jobrowname);
            jName.setText(jobname);
        }

        public void setJobBudget(String jobbudget){
            TextView jBudget = (TextView) mview.findViewById(R.id.jobrowbudget);
            jBudget.setText("Rs."+jobbudget);
        }

        public void setJobLocation(String jobLocation){
            TextView jLocation = (TextView) mview.findViewById(R.id.jobrowlocation);
            jLocation.setText(jobLocation);
        }

        public void setJobDate(String jobDate){
            TextView jDate = (TextView) mview.findViewById(R.id.jobrowdate);
            jDate.setText("Added on "+jobDate);
        }
    }
}
