package com.example.studentsviewer;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class GroupsActivity extends Activity {
	
	int groupORstudent=0;
	boolean allStudents=true;
	
	DBStudents dbStudents;
	Context context;
	ListView listview;
	groupListAdapter   adapterGroup;
	studentListAdapter adapterStudent;
	
	ArrayList<Student>studentGroup;
	
	
	ArrayList<Group> groups;
	
	
	long currIDGroup;
	
	String TAG = "GroupsActivity";
	
    final int ADD_GROUP      = 0;
    final int UPDATE_GROUP   = 1;
    final int ADD_STUDENT    = 2;
    final int UPDATE_STUDENT = 3;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
		context=this;
		dbStudents=DBStudents.getInstance(context);
        listview=(ListView)findViewById(R.id.list);
        
        groups = dbStudents.selectAllGroups();
        
        adapterGroup=new groupListAdapter(context, groups);
        
        final EditText search =(EditText)findViewById(R.id.editText1);
        search.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Pattern p = Pattern.compile(search.getText().toString() + ".*");
				ArrayList<Group> newList = new ArrayList<Group>();
				
				for(Group g:groups){
					 Matcher m = p.matcher(g.getNameGroup());
					 if(m.matches()){
						 newList.add(g);
					 }
				}
				
				adapterGroup.setArrayMyData(newList);
				adapterGroup.notifyDataSetChanged();
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
        	
        });
       
        
        
        
        
        adapterStudent=new studentListAdapter(context, dbStudents.selectAllStudents());
         
       
       Log.d(TAG, "onCreate_1");
       listview.setAdapter(adapterGroup);
       registerForContextMenu(listview);
        

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	

            	if (groupORstudent==0) {
            		//groupORstudent=1;
            		

                	studentGroup=dbStudents.getStudentsFromGroup(id);
                	currIDGroup=id;
                	
                	
                	//adapterStudentFromGroup=new studentListAdapter(context, dbStudents.selectAllStudentsFromGroup());
                	
                	if(studentGroup.size()==0) {
                		Toast toast = Toast.makeText(getApplicationContext(),
    	            			   "В группе нет студентов", Toast.LENGTH_SHORT);
                		toast.show();
                		return;
                	} else {
                		Log.d(TAG, "onItemClick()");
	                	adapterStudent.setArrayMyData(studentGroup);
	                	listview.setAdapter(adapterStudent);
	                	groupORstudent=1;
	                	allStudents=false;
	                    	
	                    for(int i=0;i<studentGroup.size();i++) {
	                    	Student s = studentGroup.get(i);
	                    	Log.d(TAG, "nameFirst = " + s.getNameFirst());
	                    }	
                	}// else
                	
                }            	
            		//listview.setAdapter(adapterStudent);
            		
            	}// onItemClick
            	
          }); // listview.setOnItemClickListener()       

	}// onCreate()
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addGroup:
                Intent iG = new Intent(context, AddGroupActivity.class);
                startActivityForResult (iG, ADD_GROUP);
                updateGroupList();
                return true;
                
            case R.id.addStudent:
                Intent iS = new Intent(context, AddStudentActivity.class);
                
                List<Group> groups = dbStudents.selectAllGroups();
                if(groups.size()==0) {
                	Toast toast = Toast.makeText(getApplicationContext(), 
	            			   "Сначала создайте группу", Toast.LENGTH_SHORT);
                	toast.show();
                	return true;
                }
                
                startActivityForResult (iS, ADD_STUDENT);
                updateStudentList();
                return true;                
                
            case R.id.groupsORstudents:
            	Log.d(TAG, "groupsORstudents="+groupORstudent+" allStudents="+allStudents);

            	if (groupORstudent==0) {
            		groupORstudent=1;
            		if(!allStudents) {
            			Log.d(TAG, "allStudents");
            			adapterStudent.setArrayMyData(dbStudents.selectAllStudents());
            			allStudents=true;
            		}
            		listview.setAdapter(adapterStudent);
            	}
            	else {
            		groupORstudent=0;
            		listview.setAdapter(adapterGroup);
            	}

            	return true;     
                
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                
        switch(item.getItemId()) {
            case R.id.edit:
            	if(groupORstudent==0) {
	                Intent i = new Intent(context, AddGroupActivity.class);
	                Group g = dbStudents.selectGroup(info.id);
	                Log.d(TAG, "groupID = "+info.id);
	                i.putExtra("Group", g);
	                startActivityForResult(i, UPDATE_GROUP);
	                //updateGroupList();
            	} else {
            		Intent i = new Intent(context, AddStudentActivity.class);
	                Student s = dbStudents.selectStudent(info.id);
	                Log.d(TAG, "studentID = "+info.id);
	                i.putExtra("Student", s);
	                startActivityForResult(i, UPDATE_STUDENT);
            		if(!allStudents) {
            			//updateStudentListFromGroup();
            		} else {    	                
    	                //updateStudentList();	
            		}
            	}
            	
            	//registerForContextMenu(listview);
                return true;
            case R.id.delete:
            	if(groupORstudent==0) {
	                if(dbStudents.deleteGroup(info.id)!=1){
	                	Toast toast = Toast.makeText(getApplicationContext(), 
		            			   "Сначала удалите студентов", Toast.LENGTH_SHORT);
	                	toast.show();
	                } else
	                	updateGroupList();
            	} else {
            		if(!allStudents) {

            			dbStudents.deleteStudent(info.id);
            			studentGroup=dbStudents.getStudentsFromGroup(currIDGroup);
            			updateStudentListFromGroup();
            			allStudents=false;

            		} else {
            			dbStudents.deleteStudent(info.id);
            			updateStudentList();
            		}

            		Log.d(TAG, "Delete id="+info.id);
            		
            	}

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void updateGroupList () {
        adapterGroup.setArrayMyData(dbStudents.selectAllGroups());
        adapterGroup.notifyDataSetChanged();
    }
    
    private void updateStudentList() {
        adapterStudent.setArrayMyData(dbStudents.selectAllStudents());
        adapterStudent.notifyDataSetChanged();
    }
    private void updateStudentListFromGroup() {
        adapterStudent.setArrayMyData(studentGroup);
        adapterStudent.notifyDataSetChanged();
    }
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
        if (resultCode == Activity.RESULT_OK) {        	
        	//Log.d(TAG, "onActivityResult="+resultCode);
        	Group g = (Group) data.getExtras().getSerializable("Group");
        	Student s = (Student) data.getExtras().getSerializable("Student");
        	
        	switch (requestCode) {
            case ADD_GROUP:
            	//Log.d(TAG, "onActivityResult_ADDGROUP");
            	dbStudents.insertGroup(g.getNameGroup(), g.getNameDepartment());
            	updateGroupList();
                return;
            case UPDATE_GROUP:
            	dbStudents.updateGroup(g);
            	updateGroupList();
                return;
            case ADD_STUDENT:            	
            	if(dbStudents.insertStudent(s.getNameFirst(), s.getNameSecond(), s.getNameThird(), s.getDateBirth(), s.getGroupID())==-1) {
            		Toast toast = Toast.makeText(getApplicationContext(), 
	            			   "!!!!!", Toast.LENGTH_SHORT);
            		toast.show();
            	}
            	else
            		if(!allStudents) {
            			updateStudentListFromGroup();
            		} else {    	                
    	                updateStudentList();	
            		}
                return;
                
            case UPDATE_STUDENT:
            	dbStudents.updateStudent(s);
            	if(!allStudents) {
            		studentGroup=dbStudents.getStudentsFromGroup(currIDGroup);                	
        			updateStudentListFromGroup();
        		} else {    	                
	                updateStudentList();	
        		}
                return;
        	}        	
        	
        }        
    }
	
	 class groupListAdapter extends BaseAdapter {
	        private LayoutInflater mLayoutInflater;
	        private ArrayList<Group> arrayGroups;
	        
	        public groupListAdapter (Context ctx, ArrayList<Group> arr) {
	            mLayoutInflater = LayoutInflater.from(ctx);
	            setArrayMyData(arr);
	        }

	        public ArrayList<Group> getArrayMyData() {
	            return arrayGroups;
	        }

	        public void setArrayMyData(ArrayList<Group> arrayMyData) {
	            this.arrayGroups = arrayMyData;
	        }

	        public int getCount () {
	            return arrayGroups.size();
	        }

	        public Object getItem (int position) {
	            return position;
	        }

	        public long getItemId (int position) {
	        	Group g = arrayGroups.get(position);
	            if (g != null) {
	                return g.getId();
	            }
	            return 0;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	        	
	            if (convertView == null) convertView = mLayoutInflater.inflate(R.layout.item_group, null);

	            TextView tvNameGroup=(TextView)convertView.findViewById(R.id.nameGroup);
	            TextView tvNameDepartment=(TextView)convertView.findViewById(R.id.nameDepartment);
	           
	            Group g = arrayGroups.get(position);	            
	            
	            tvNameGroup.setText(g.getNameGroup());
	            tvNameDepartment.setText(g.getNameDepartment());

	           // Log.d(TAG, "Adapter::getView()"+g.getNameGroup());
	           // Log.d(TAG, "Adapter::getView()"+g.getNameDepartment());
	           // Log.d(TAG, "Adapter::getView()"+g.getTotalStudents());
	            
	            return convertView;
	        }	        
	 }//  class groupListAdapter extends BaseAdapter
	 
	 
	 
	 
	 class studentListAdapter extends BaseAdapter {
	        private LayoutInflater mLayoutInflater;
	        private ArrayList<Student> arrayStudents;
	        
	        public studentListAdapter (Context ctx, ArrayList<Student> arr) {
	            mLayoutInflater = LayoutInflater.from(ctx);
	            setArrayMyData(arr);
	        }

	        public ArrayList<Student> getArrayMyData() {
	            return arrayStudents;
	        }

	        public void setArrayMyData(ArrayList<Student> arrayMyData) {
	            this.arrayStudents = arrayMyData;
	        }

	        public int getCount () {
	            return arrayStudents.size();
	        }

	        public Object getItem (int position) {
	            return position;
	        }

	        public long getItemId (int position) {
	        	Student s = arrayStudents.get(position);
	            if (s != null) {
	                return s.getId();
	            }
	            return 0;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	        	
	            if (convertView == null)
	                convertView = mLayoutInflater.inflate(R.layout.item_student, null);
 
	            TextView tvNameFirst=(TextView)convertView.findViewById(R.id.nameFirst);
	            TextView tvNameSecond=(TextView)convertView.findViewById(R.id.nameSecond);
	            TextView tvNameThird=(TextView)convertView.findViewById(R.id.nameThird);
	            TextView tvDateBirth=(TextView)convertView.findViewById(R.id.dateBirth);
	            TextView tvGroup=(TextView)convertView.findViewById(R.id.group);

	            Student s = arrayStudents.get(position);	            
	                       
	            tvNameFirst.setText(s.getNameFirst());
	            tvNameSecond.setText(s.getNameSecond());
	            tvNameThird.setText(s.getNameThird());
	            tvDateBirth.setText(s.getDateBirth());
	            

	            Group g=dbStudents.selectGroup(s.getGroupID());
	            tvGroup.setText(g.getNameGroup());
	            
	            return convertView;
	        }// getView()	        
	 }//  class studentListAdapter extends BaseAdapter

	 
	
}// class GroupsActivity
