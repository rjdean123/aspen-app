package com.ryanjohndean.aspenBeta;

import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private int gradeMin;
	private int grade;
	private ProgressDialog dialog;
	private ClassSorter sorter;
	private EditText newMinGrade;
	private EditText userField;
	private EditText passField;
	private TextView dataField;
	private TextView classDataField;
	private Button loginButton;
	private Button cButton1;
	private Button cButton2;
	private Button cButton3;
	private Button cButton4;
	private Button cButton5;
	private Button cButton6;
	private Button cButton7;
	private Document data;
	private AspenScraper scraper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        LinearLayout lin = (LinearLayout) findViewById(R.id.mainLayout);
        ImageView image = new ImageView(this);
        Context context = image.getContext();
        int id = context.getResources().getIdentifier("leaf", "drawable", context.getPackageName());
        image.setImageResource(id);
        lin.addView(image);
        userField = (EditText) findViewById(R.id.usernameEditText);
        passField = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        
        sorter = new ClassSorter();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting ...");
        gradeMin = 90;
        
        try {
			scraper = new AspenScraper(dialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    
    public void myClickHandler(View view) throws Exception {
    		if ((boolean) scraper.execute(userField.getText().toString(), passField.getText().toString()).get()) {
		   		dialog.dismiss();
		   		data = scraper.getData();
		    	sorter.sortAndHandle(data);
		    	display();
    		} else {
    			dialog.dismiss();
    	    	setContentView(R.layout.aspen_data);
    	    	dataField = (TextView) findViewById(R.id.dataTextView);
    	   		dataField.setText("Failed to Connect :(\n" + scraper.getData());
    	   	}
    }
    
    public void display() {
    	setContentView(R.layout.aspen_data);
    	dataField = (Button) findViewById(R.id.dataTextView);
    	dataField.setText("Minimum Acceptable Grade: %" + gradeMin);
    	cButton1 = (Button) findViewById(R.id.class1button);
    	cButton1.setText(sorter.getTitle(1));
    	if (sorter.getNumGrade(1) < gradeMin)
    		cButton1.setTextColor(Color.RED);
    	cButton2 = (Button) findViewById(R.id.class2button);
    	cButton2.setText(sorter.getTitle(2));
    	if (sorter.getNumGrade(2) < gradeMin)
    		cButton2.setTextColor(Color.RED);
    	cButton3 = (Button) findViewById(R.id.class3button);
    	cButton3.setText(sorter.getTitle(3));
    	if (sorter.getNumGrade(3) < gradeMin)
    		cButton3.setTextColor(Color.RED);
    	cButton4 = (Button) findViewById(R.id.class4button);
    	cButton4.setText(sorter.getTitle(4));
    	try {
    		if (sorter.getNumGrade(4) < gradeMin)
    			cButton4.setTextColor(Color.RED);
    	} catch (Exception e) {
    	}
    	cButton5 = (Button) findViewById(R.id.class5button);
    	cButton5.setText(sorter.getTitle(5));
    	try {
    		if (sorter.getNumGrade(5) < gradeMin)
    			cButton5.setTextColor(Color.RED);
    	} catch (Exception e) {
    	}
    	cButton6 = (Button) findViewById(R.id.class6button);
    	cButton6.setText(sorter.getTitle(6));
    	try {
    		if (sorter.getNumGrade(6) < gradeMin)
    			cButton6.setTextColor(Color.RED);
    	} catch (Exception e) {
    	}
    	cButton7 = (Button) findViewById(R.id.class7button);
    	cButton7.setText(sorter.getTitle(7));
    	try {
    		if (sorter.getNumGrade(7) < gradeMin)
    			cButton7.setTextColor(Color.RED);
    	} catch (Exception e) {
    	}
    }
    
    public void classClicked(int pd) {
    	setContentView(R.layout.class_data);
    	String str = sorter.getTitle(pd);
    	str += "\n" + sorter.getTeacher(pd);
    	str += "\n" + sorter.getGrade(pd);
    	classDataField = (TextView) findViewById(R.id.classData);
    	classDataField.setText(str);
    }
    
    
    public void clicked1(View view) {
    	classClicked(1);
    }
    public void clicked2(View view) {
    	classClicked(2);
    }
    public void clicked3(View view) {
    	classClicked(3);
    }
    public void clicked4(View view) {
    	classClicked(4);
    }
    public void clicked5(View view) {
    	classClicked(5);
    }
    public void clicked6(View view) {
    	classClicked(6);
    }
    public void clicked7(View view) {
    	classClicked(7);
    }
    public void changeGradeClicked(View view) {
    	setContentView(R.layout.change_min);
    }
    public void changeMinClicked(View view) {
    	newMinGrade = (EditText) findViewById(R.id.minEditText);
    	int temp = gradeMin;
    	try {
    		temp = Integer.valueOf(newMinGrade.getText().toString());
    	} catch (Exception e) {
    		
    	}
    	gradeMin = temp;
    	display();
    }
    public void returnFunction(View view) {
    	display();
    }
    //hide keyboard when finished
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
    return ret;
    }
    
}