package com.example.himasha.workhub;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchFragment extends Fragment {
    RecyclerView search_list;
    DatabaseReference workhub;
    AutoCompleteTextView searchtext;
    String selectedjobKey;
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public static class SearchJobViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public SearchJobViewHolder(View itemView) {
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

    private void initViews(View view) {

        String[] skills = {"Home Cleaner", "Nanny", "Baker", "Cook", "Maid", "Fllor technician", "Pet Sitter", "Plumber"
                , "Waiter", "General Cashier", "Chef", "Housekeeper", "Kitchen Technician", "Carpenter" , "Driver" , "Brickmason"
                ,"Cement Mason", "Blockmason", "Concrete Finisher", "Drywall and Ceiling Tile Installer", "Drywall and Ceiling Tile Installer"
                ,"Tile and Marble Setter", "Pipe Fitter", "Pipe Fitter", "Maintenance and Repair Worker", "Carpet Installer"
                ,"Electric Motor, Power Tool, and Related Repairer", "Electrician", "Electrical Power-Line Installer and Repairer"
                ,"Welder", "Refrigeration Mechanic and Installer" , "Refrigeration Mechanic and Installer", "Motorcycle Mechanic"
                ,"Mechanical Engineering Technician", "Gardener"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this.getActivity(), android.R.layout.select_dialog_item, skills);

        workhub = FirebaseDatabase.getInstance().getReference().child("jobs");
        searchtext = (AutoCompleteTextView)view.findViewById(R.id.searchjobET);
        searchtext.setThreshold(1);
        searchtext.setAdapter(adapter);

        search_list = (RecyclerView)view.findViewById(R.id.search_list);
        search_list.setHasFixedSize(true);
        search_list.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        searchtext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirebaseRecyclerAdapter<Job,SearchJobViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Job, SearchJobViewHolder>(
                        Job.class,
                        R.layout.search_row,
                        SearchJobViewHolder.class,
                        workhub.orderByChild("jobKeyWord").equalTo(adapter.getItem(position))
                ) {
                    @Override
                    protected void populateViewHolder(SearchJobViewHolder viewHolder, Job model, int position) {
                        final String job_key = getRef(position).getKey();
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
                    }
                };
                search_list.setAdapter(firebaseRecyclerAdapter);

            }
        });




    }
}
