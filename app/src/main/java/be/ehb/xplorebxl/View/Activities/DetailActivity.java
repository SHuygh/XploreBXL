package be.ehb.xplorebxl.View.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import be.ehb.xplorebxl.R;

/**
 * Created by TDS-Team on 16/03/2018.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Object selectedObject = getIntent().getSerializableExtra("selected object");


        Log.d("testtest", "onCreate: " + selectedObject.getClass().getName());

        //Class classtype = getIntent().getSerializableExtra("selected object").getClass();
    }
}
