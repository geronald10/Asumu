package net.ridhoperdana.asumu;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ManageExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        setContentView(R.layout.activity_manage_expense);

        EditText keyExpense = (EditText)findViewById(R.id.input_key_daily);
        EditText valueExpense = (EditText)findViewById(R.id.input_value_daily);
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setId(R.id.constraintLayout);
//        setContentView(constraintLayout);
//        ConstraintLayout.LayoutParams clpcontactUs = new ConstraintLayout.LayoutParams(
//                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        Button buttonAddMoreExpense = (Button)findViewById(R.id.add_more_expense);
        buttonAddMoreExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                layout.addView(linearLayout);
            }
        });

    }
}
