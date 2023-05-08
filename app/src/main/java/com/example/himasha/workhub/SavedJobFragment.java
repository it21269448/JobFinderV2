package com.example.himasha.workhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SavedJobFragment extends Fragment {
    FloatingActionButton actionButton;
    RecyclerView job_list;
    DatabaseReference workhub;

    private Button removeJob;

    public static SavedJobFragment newInstance() {
        SavedJobFragment fragment = new SavedJobFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_job, container, false);

        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Job,JobViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Job, JobViewHolder>(
                Job.class,
                R.layout.saved_job_row,
                JobViewHolder.class,
                workhub.orderByChild("isSaved").equalTo("1")

        ) {
            @Override
            protected void populateViewHolder(JobViewHolder viewHolder, Job model, int position) {
                final String job_key = getRef(position).getKey();
                viewHolder.setKey(job_key);
                viewHolder.setJobName(model.getJobName());
                viewHolder.setJobBudget(model.getJobBudget());
                viewHolder.setJobLocation(model.getJobLocationName());
                viewHolder.setJobDate(model.getJobPostedDate());

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SingleJobView.class);
                        intent.putExtra("job_id", job_key);
                        startActivity(intent);
                    }
                });

                viewHolder.mview.findViewById(R.id.remove_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference jobRef = workhub.child(job_key);
                        model.setIsSaved("0");
                        notifyItemRemoved(position);

                        Toast.makeText(v.getContext(), "Job removed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        job_list.setAdapter(firebaseRecyclerAdapter);
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder{

        View mview;
        String key;

        public JobViewHolder(View itemView) {
            super(itemView);

            mview = itemView;
        }

        public void setKey(String key){
            this.key = key;
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

    private void initViews(View view) {
        workhub = FirebaseDatabase.getInstance().getReference().child("jobs");

        job_list = (RecyclerView)view.findViewById(R.id.job_list);
        job_list.setHasFixedSize(true);
        job_list.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

}
