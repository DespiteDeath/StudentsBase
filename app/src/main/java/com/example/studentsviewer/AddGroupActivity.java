package com.example.studentsviewer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddGroupActivity extends Activity {
	private Button btnSave, btnCancel;
    private EditText etNameGroup,etNameDepartment;
    private long GroupID;
    
	String TAG = "AddGroupActivity";
    
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_group);
	        
	        etNameGroup=(EditText)findViewById(R.id.etNameGroup);
	        etNameDepartment=(EditText)findViewById(R.id.etNameDepartment);
	        
	        btnSave=(Button)findViewById(R.id.btnSave);
	        btnCancel=(Button)findViewById(R.id.btnCancel);
	        
	        //Log.d(TAG, "onCreate_1");
	        
	        if(getIntent().hasExtra("Group")){
	            Group g=(Group)getIntent().getSerializableExtra("Group");
	            etNameGroup.setText(g.getNameGroup());
	            etNameDepartment.setText(g.getNameDepartment());
	           
	            GroupID=g.getId();
	        }
	        else
	            GroupID=-1;
	        
	        
	        btnSave.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            
	            	String s1=etNameGroup.getText().toString();
	            	String s2=etNameDepartment.getText().toString();
	            	 

	            	if(!s1.isEmpty() && !s2.isEmpty()) 
	            	{
	            		Group group=new Group(GroupID, s1, s2, 0);
	            		Intent intent=getIntent();
	            		intent.putExtra("Group", group);	            		
	            		setResult(RESULT_OK,intent);
	            		finish();
	            	}
	            	else {
	            		Toast toast = Toast.makeText(getApplicationContext(), 
		            			   "Заполните пустые поля или отмените действие", Toast.LENGTH_SHORT);
	            		toast.show();
	            	}
	            		
	                
	            }
	        });	        
	        
	        btnCancel.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                finish();
	            }
	        });
	 }// onCreate()
	 
	 
}// AddGroupActivity
