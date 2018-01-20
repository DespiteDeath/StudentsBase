package com.example.studentsviewer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBStudents {

	private Context context;
	private String DB_NAME = "students.db";
	private static final int DB_VERSION = 1;
	
	private SQLiteDatabase db;
	private static DBStudents dbStudents;
	
	private static final String TABLE_NAME_GROUPS = "Groups";
	private static final String TABLE_NAME_STUDENTS = "Students";

	
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAMEGROUP = "NameGroup";
    private static final String COLUMN_NAMEDEPARTMENT = "NameDepartment";
    private static final String COLUMN_TOTALSTUDENTS = "TotalStudents";

    private static final String COLUMN_NAMEFIRST = "NameFirst";
    private static final String COLUMN_NAMESECOND = "NameSecond";
    private static final String COLUMN_NAMETHIRD = "NameThird";
    private static final String COLUMN_DATEBIRTH = "DateBirth";
    private static final String COLUMN_GROUP = "Group2";
    
    
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAMEGROUP = 1;
    private static final int NUM_COLUMN_NAMEDEPARTMENT = 2;
    private static final int NUM_COLUMN_TOTALSTUDENTS = 3;
    
    private static final int NUM_COLUMN_NAMEFIRST = 1;
    private static final int NUM_COLUMN_NAMESECOND = 2;
    private static final int NUM_COLUMN_NAMETHIRD = 3;
    private static final int NUM_COLUMN_DATEBIRTH = 4;
    private static final int NUM_COLUMN_GROUP = 5;
    
    String TAG = "DBStudents";

	public static DBStudents getInstance(Context context) {
		if (dbStudents == null) {
			dbStudents = new DBStudents(context);
		}
		return dbStudents;
	}
	
	public DBStudents(Context context) {		
		OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
		//createTablesIfNeedBe();
	}

	
    public long insertGroup(String NameGroup, String NameDepartment) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAMEGROUP, NameGroup);
        cv.put(COLUMN_NAMEDEPARTMENT, NameDepartment);
        return db.insert(TABLE_NAME_GROUPS, null, cv);
    }

    public int updateGroup(Group g) {
        ContentValues cv=new ContentValues();       
        cv.put(COLUMN_NAMEGROUP, g.getNameGroup());
        cv.put(COLUMN_NAMEDEPARTMENT, g.getNameDepartment());
        
        return db.update(TABLE_NAME_GROUPS, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(g.getId())});
    }

    
    public long deleteGroup(long id) {
    	int result=0;    	
        
    	try{
        	result=db.delete(TABLE_NAME_GROUPS, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        }catch(android.database.sqlite.SQLiteException e){	
        }
        Log.d(TAG, "deleteGroup "+result);

        return result;
    }
    
    public Group selectGroup(long id) {
        Cursor cursor = db.query(TABLE_NAME_GROUPS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        String ng = cursor.getString(NUM_COLUMN_NAMEGROUP);
        String nd = cursor.getString(NUM_COLUMN_NAMEDEPARTMENT);

        return new Group(id, ng, nd, 0);
    }

    
    //SELECT groups._id, students.studentname, classes.classname, classes.attend, classes.late, classes.dtime
    //FROM groups, students
    //WHERE groups._id=students.name
    

	public ArrayList<Group> selectAllGroups() {
		Cursor cursor = db.query(TABLE_NAME_GROUPS, null, null, null, null, null, null);
		
		
		ArrayList<Group> arr = new ArrayList<Group>();
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
            do {
            	long id = cursor.getLong(NUM_COLUMN_ID);
            	String ng = cursor.getString(NUM_COLUMN_NAMEGROUP);
            	String nd = cursor.getString(NUM_COLUMN_NAMEDEPARTMENT);
            	int ts = cursor.getInt(NUM_COLUMN_TOTALSTUDENTS);
            	arr.add(new Group(id, ng, nd, ts));
            }while(cursor.moveToNext());
		}		
        //Log.d(TAG, "selectAllGroups");	
		return arr;
	}// selectAllGroups()
	
	/*public String getNameGroup(int groupID) {
		String str=;

		return str;
	}// getNameGroup()*/
	
	public long insertStudent(String nameFirst, String nameSecond,
		String nameThird, String dateBirth, long groupID) {
        
		ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAMEFIRST, nameFirst);
        cv.put(COLUMN_NAMESECOND, nameSecond);
        cv.put(COLUMN_NAMETHIRD, nameThird);
        cv.put(COLUMN_DATEBIRTH, dateBirth);
        cv.put(COLUMN_GROUP, groupID);
//        Log.d(TAG, "insertStudent()_1");
        
        long result=-1;
    	//result=db.insert(TABLE_NAME_STUDENTS, null, cv);
    	
        try{
        	result=db.insertOrThrow(TABLE_NAME_STUDENTS, null, cv);
        }
        catch(android.database.sqlite.SQLiteException e){
        	Log.e(TAG, e.toString());
        }
        
        //Log.d(TAG, "insertStudent() result="+result);
        
        return result;
	}
	
    public int updateStudent(Student s) {
        ContentValues cv=new ContentValues();       

        cv.put(COLUMN_NAMEFIRST, s.getNameFirst());
        cv.put(COLUMN_NAMESECOND, s.getNameSecond());
        cv.put(COLUMN_NAMETHIRD, s.getNameThird());
        cv.put(COLUMN_DATEBIRTH, s.getDateBirth());
        cv.put(COLUMN_GROUP, s.getGroupID());
        return db.update(TABLE_NAME_STUDENTS, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(s.getId())});
    }
    
    public void deleteStudent(long id) {
    	db.delete(TABLE_NAME_STUDENTS, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });        
    }
    
    public Student selectStudent(long id) {
        Cursor cursor = db.query(TABLE_NAME_STUDENTS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        String nf = cursor.getString(NUM_COLUMN_NAMEFIRST);
        String ns = cursor.getString(NUM_COLUMN_NAMESECOND);
        String nt = cursor.getString(NUM_COLUMN_NAMETHIRD);
        String bd = cursor.getString(NUM_COLUMN_DATEBIRTH);
        int gid = cursor.getInt(NUM_COLUMN_GROUP);
        //Group g=

        return new Student(id, nf, ns, nt, bd, gid);
    }
    
	
	public ArrayList<Student> selectAllStudents() {
		//Log.d(TAG, "selectAllStudents_1");
		Cursor cursor = db.query(TABLE_NAME_STUDENTS, null, null, null, null, null, null);
		        
		ArrayList<Student> arr = new ArrayList<Student>();
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
            do {
            	long id = cursor.getLong(NUM_COLUMN_ID);
            	String nf = cursor.getString(NUM_COLUMN_NAMEFIRST);
            	String ns = cursor.getString(NUM_COLUMN_NAMESECOND);
            	String nt = cursor.getString(NUM_COLUMN_NAMETHIRD);
            	String bd = cursor.getString(NUM_COLUMN_DATEBIRTH);
            	int gid = cursor.getInt(NUM_COLUMN_GROUP);

            	arr.add(new Student(id, nf, ns, ns, bd, gid));
            }while(cursor.moveToNext());
		}
		
		return arr;
	}// selectAllStudents()
	
	public ArrayList<Student> getStudentsFromGroup(long idGroup) {
		Cursor cursor = db.query(TABLE_NAME_STUDENTS, null, COLUMN_GROUP + " = ?", new String[]{String.valueOf(idGroup)}, null, null, null);
        
		ArrayList<Student> arr = new ArrayList<Student>();
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
            do {
            	long id = cursor.getLong(NUM_COLUMN_ID);
            	String nf = cursor.getString(NUM_COLUMN_NAMEFIRST);
            	String ns = cursor.getString(NUM_COLUMN_NAMESECOND);
            	String nt = cursor.getString(NUM_COLUMN_NAMETHIRD);
            	String bd = cursor.getString(NUM_COLUMN_DATEBIRTH);
            	int gid = cursor.getInt(NUM_COLUMN_GROUP);

            	arr.add(new Student(id, nf, ns, ns, bd, gid));
            }while(cursor.moveToNext());
		}
		
		return arr;
	} // getStudentsFromGroup()
	
    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query1 = "CREATE TABLE " + TABLE_NAME_GROUPS + " (" +
                    COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMEGROUP + " TEXT, " +
                    COLUMN_NAMEDEPARTMENT + " TEXT, " +
                    COLUMN_TOTALSTUDENTS +" INTEGER);";
            
            //Log.d(TAG, "onCreate_1 "+query1);
            
            String query2 = "CREATE TABLE " + TABLE_NAME_STUDENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMEFIRST + " TEXT, " +
                    COLUMN_NAMESECOND + " TEXT, " +
                    COLUMN_NAMETHIRD + " TEXT, " +
                    COLUMN_DATEBIRTH + " TEXT, " + 
                    COLUMN_GROUP + " INTEGER, " + 
					" FOREIGN KEY ("+ COLUMN_GROUP +") REFERENCES " +
                    TABLE_NAME_GROUPS+"("+COLUMN_ID+"));";           
           
            //Log.d(TAG, "onCreate_2 "+query2);
           
            db.execSQL(query1);
            db.execSQL(query2);
        }        

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUPS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS);
            onCreate(db);
        }
        
        public void onOpen(SQLiteDatabase db){
            super.onOpen(db);
            db.execSQL("PRAGMA foreign_keys=ON");
            //Log.d(TAG, "onOpen()_Pragma");
        }
    }// OpenHelper



}// DBStudents
