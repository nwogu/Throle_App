package com.throle.throle;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PrefManager prefManager;
    PrefMan prefMan;
    ProblemManager problemManager;
    private ArrayList<String> problemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager = new PrefManager(this);
        prefMan = new PrefMan(this);

        if (!prefManager.getSessionStart().equals("")){
            startActivity(new Intent(MainActivity.this, Users.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

        problemManager = new ProblemManager(this);
        problemList = new ArrayList<>();

        problemList.add("Self Loathing");
        problemList.add("Jealousy/Anger");
        problemList.add("School stress");
        problemList.add("Trauma");
        problemList.add("Illness");
        problemList.add("Dietary stress");
        problemList.add("Substance abuse");
        problemList.add("Social Stress");
        problemList.add("Relationship");
        problemList.add("Marriage");
        problemList.add("Bankruptcy");
        problemList.add("Isolation");
        problemList.add("Work-related");
        problemList.add("Procrastination");
        problemList.add("Career Stress");
        problemList.add("I don't know");

        ListView listView = (ListView)findViewById(R.id.problem_list);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.problem_layout, problemList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String problem = problemList.get(i);
                problemManager.setProblem(problem);
                startActivity(new Intent(MainActivity.this, Users.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

    }
}
