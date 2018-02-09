package com.throle.throle;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TEST on 11/24/2017.
 */

public class ProblemManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "throle-problem";

    private static final String IS_PROBLEM_SELECTED = "IsProblemSelected";

    private static final String PROBLEM = "nothing";




    public ProblemManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsProblemSelected(Boolean isProblem) {
        editor.putBoolean(IS_PROBLEM_SELECTED, isProblem);
        editor.commit();
    }

    public void setProblem(String problem) {
        editor.putString(PROBLEM, problem);
        editor.commit();
    }

    public boolean IsProblemSelected() {
        return pref.getBoolean(IS_PROBLEM_SELECTED, false);
    }

    public String getProblem() {
        return pref.getString(PROBLEM, "none");
    }
}
