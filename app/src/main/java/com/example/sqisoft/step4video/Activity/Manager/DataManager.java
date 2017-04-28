package com.example.sqisoft.step4video.Activity.Manager;

import android.app.Activity;

/**
 * Created by SQISOFT on 2017-04-27.
 */

public class DataManager {
    private static DataManager instance = null;

    private DataManager(){

    }

    public static DataManager getInstance(){
        if(instance == null){
            instance = new DataManager();
        }
        return instance;
    }

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
