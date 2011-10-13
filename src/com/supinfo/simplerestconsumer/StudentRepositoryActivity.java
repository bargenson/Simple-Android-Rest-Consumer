package com.supinfo.simplerestconsumer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.simplerestconsumer.adapter.StudentListAdapter;
import com.supinfo.simplerestconsumer.dao.StudentDao;
import com.supinfo.simplerestconsumer.model.Student;

import fr.bargenson.util.rest.RestClientException;

public class StudentRepositoryActivity extends ListActivity {

	private static final int NEW_STUDENT_DIALOG_ID = 0;

	private static final int DELETE_STUDENT_MENU_ID = 0;
	
	private ProgressDialog dialog;
	
	private StudentDao studentDao = new StudentDao();
	private List<Student> students = new ArrayList<Student>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dialog = new ProgressDialog(this);
        
        loadStudents();
        
        registerForContextMenu(getListView());
    }
	
	private void loadStudents() {
		new AsyncTask<Void, Void, List<Student>>() {
        	
			@Override
			protected List<Student> doInBackground(Void... params) {
				List<Student> students = studentDao.getAllStudents();
				return students;
			}
			
			@Override
			protected void onPreExecute() {
				dialog.setMessage("Fetching data...");
				dialog.show();
			}
			
			@Override
			protected void onPostExecute(List<Student> result) {
				students.clear();
				students.addAll(result);
				
				if(getListAdapter() == null) {
					setListAdapter(new StudentListAdapter(StudentRepositoryActivity.this, students));
				} else {
					((StudentListAdapter) getListAdapter()).notifyDataSetChanged();
				}
				
				dialog.dismiss();
			}
        	
		}.execute();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		if(v.equals(getListView())) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		    Student selectedStudent = (Student) getListAdapter().getItem(info.position);
			menu.setHeaderTitle(selectedStudent.getFullName());
			
			menu.add(Menu.NONE, DELETE_STUDENT_MENU_ID, Menu.NONE, R.string.remove_student);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_STUDENT_MENU_ID:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		    Student selectedStudent = (Student) getListAdapter().getItem(info.position);
			removeStudent(selectedStudent);
			return true;
		default:
			return false;
		}
	}
	
	private void removeStudent(Student student) {
		new AsyncTask<Student, Void, Void>() {

			@Override
			protected Void doInBackground(Student... params) {
				try {
					studentDao.removeStudent(params[0]);
				} catch (RestClientException e) {
					Log.e("Remove Student", "Impossible to remove Student", e);
					cancel(false);
				}
				return null;
			}
			
			@Override
			protected void onPreExecute() {
				dialog.setMessage("Delete student...");
				dialog.show();
			}
			
			@Override
			protected void onCancelled() {
				Toast.makeText(StudentRepositoryActivity.this, "Error! Student not deleted.", Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
			
			@Override
			protected void onPostExecute(Void result) {
				Toast.makeText(StudentRepositoryActivity.this, "Student deleted!", Toast.LENGTH_LONG).show();
				loadStudents();
			}
			
		}.execute(student);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.student_repository_activity_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_student:
			displayAddStudentForm();
			return true;
		case R.id.refresh:
			loadStudents();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void displayAddStudentForm() {
		showDialog(NEW_STUDENT_DIALOG_ID);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case NEW_STUDENT_DIALOG_ID:
			dialog = createAddStudentDialog();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	private Dialog createAddStudentDialog() {
		return new Dialog(this) {
			
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				
				setTitle(R.string.add_student_dialog_title);
				setContentView(R.layout.add_student_dialog);
				
				Button cancelButton = (Button) findViewById(R.id.cancel);
				Button submitButton = (Button) findViewById(R.id.submit);
				
				cancelButton.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						dismiss();
					}
				});
				
				submitButton.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						Student student = new Student();
						
						TextView idBoosterTextView = (TextView) findViewById(R.id.id_booster);
						student.setIdBooster(Long.valueOf(idBoosterTextView.getText().toString()));
						
						TextView firstNameTextView = (TextView) findViewById(R.id.first_name);
						student.setFirstName(firstNameTextView.getText().toString());
						
						TextView lastNameTextView = (TextView) findViewById(R.id.last_name);
						student.setLastName(lastNameTextView.getText().toString());
						
						DatePicker birthDateDatePicker = (DatePicker) findViewById(R.id.birth_date);
						Calendar calendar = Calendar.getInstance();
						calendar.set(
								birthDateDatePicker.getYear(), 
								birthDateDatePicker.getMonth(), 
								birthDateDatePicker.getDayOfMonth()
						);
						student.setBirthDate(calendar.getTime());
						
						addStudent(student);
						
						dismiss();
					}
				});
			}
		};
	}
	
	private void addStudent(Student student) {
		new AsyncTask<Student, Void, Void>() {

			@Override
			protected Void doInBackground(Student... params) {
				try {
					studentDao.addStudent(params[0]);
				} catch (RestClientException e) {
					Log.i("Add Student", "Impossible to add Student", e);
					cancel(false);
				}
				return null;
			}
			
			@Override
			protected void onPreExecute() {
				dialog.setMessage("Add student...");
				dialog.show();
			}
			
			@Override
			protected void onCancelled() {
				Toast.makeText(StudentRepositoryActivity.this, "Error! Student not added.", Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
			
			@Override
			protected void onPostExecute(Void result) {
				Toast.makeText(StudentRepositoryActivity.this, "Student added!", Toast.LENGTH_LONG).show();
				loadStudents();
			}
			
		}.execute(student);
	}
	
}