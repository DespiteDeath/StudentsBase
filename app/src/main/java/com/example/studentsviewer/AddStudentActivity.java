package com.example.studentsviewer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddStudentActivity extends Activity {
	
	private Button btnSave, btnCancel;
    private EditText etNameFirst,etNameSecond,etNameThird,etDateBirth,etGroup;
    private Spinner spGroup;
    private long StudentID;
    
    List<Group> groups;
    
	String TAG = "AddStudentActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);               
        
        etNameFirst=(EditText)findViewById(R.id.etNameFirst);
        etNameSecond=(EditText)findViewById(R.id.etNameSecond);
        etNameThird=(EditText)findViewById(R.id.etNameThird);
        etDateBirth=(EditText)findViewById(R.id.etDateBirth);
        //etGroup=(EditText)findViewById(R.id.etGroup);
        spGroup=(Spinner)findViewById(R.id.spGroup);
        
        btnSave=(Button)findViewById(R.id.btnSaveStudent);
        btnCancel=(Button)findViewById(R.id.btnCancelStudent);
        
        if(getIntent().hasExtra("Student")){       
            Student s=(Student)getIntent().getSerializableExtra("Student");
            StudentID=s.getId();
            etNameFirst.setText(s.getNameFirst());
            etNameSecond.setText(s.getNameThird());
            etNameThird.setText(s.getNameThird());
            etDateBirth.setText(s.getDateBirth());
            
        }
        else {        
            StudentID=-1;
        }        
        
        setValue();
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            
            	//if(groups.size()==0)
            		
            	String s1=etNameFirst.getText().toString();
            	String s2=etNameSecond.getText().toString();
            	String s3=etNameThird.getText().toString();
            	String s4=etDateBirth.getText().toString();
            	//int s5=Integer.parseInt(etGroup.getText().toString());
            	
            	//String p = (String)spGroup.getSelectedItem();            	
            	int pos=spGroup.getSelectedItemPosition();
            	Group g=groups.get(pos);            	
            	Log.d(TAG, "setOnClickListener pos= "+g.getId());
            	

            	//if(!s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty() && !s5.isEmpty()) 
            	{
            		Student group=new Student(StudentID,s1,s2,s3,s4,g.getId());
            		Intent intent=getIntent();
//            		Log.d(TAG, "onClick_1");
            		intent.putExtra("Student", group);
            		setResult(RESULT_OK,intent);
            		finish();
            	}
            	/*else {
            		Toast toast = Toast.makeText(getApplicationContext(), 
	            			   " ", Toast.LENGTH_SHORT);
            		toast.show();
             	}*/
            		
                
            }
        });	        
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
 }// onCreate()
	
	private void setValue() {
        DBStudents dbStudents;
        dbStudents=DBStudents.getInstance(this);
        groups = dbStudents.selectAllGroups();
        
        List<String> nameGroups=new ArrayList<String>();
        for(int i=0;i<groups.size();i++) {
        	Group g=groups.get(i);
        	String str=g.getNameGroup();
        	nameGroups.add(str);
        }
        
        ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameGroups);
		spGroup.setAdapter(adapter);
		//spGroup.setSelection(0);
	}
	
}// AddStudentActivity
