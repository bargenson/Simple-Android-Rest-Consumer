package com.supinfo.simplerestconsumer.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.supinfo.simplerestconsumer.R;
import com.supinfo.simplerestconsumer.adapter.StudentListAdapter;
import com.supinfo.simplerestconsumer.dao.StudentDao;
import com.supinfo.simplerestconsumer.dialog.AddStudentDialog;
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
				dialog.setMessage(getResources().getText(R.string.fetching_data_dialog_message));
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
			
			menu.add(Menu.NONE, DELETE_STUDENT_MENU_ID, Menu.NONE, R.string.remove_student_menu_item);
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
				dialog.setMessage(getResources().getString(R.string.deleting_student_dialog_message));
				dialog.show();
			}
			
			@Override
			protected void onCancelled() {
				Toast.makeText(StudentRepositoryActivity.this, R.string.delete_student_error_message, Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
			
			@Override
			protected void onPostExecute(Void result) {
				Toast.makeText(StudentRepositoryActivity.this, R.string.delete_student_success_message, Toast.LENGTH_LONG).show();
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
		AddStudentDialog addStudentDialog = new AddStudentDialog(this);
		addStudentDialog.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss(DialogInterface dialog) {
				AddStudentDialog addStudentDialog = (AddStudentDialog) dialog;
				
				Student studentToAdd = addStudentDialog.getStudent();
				if(studentToAdd != null) {
					addStudent(studentToAdd);
				}
				
				removeDialog(NEW_STUDENT_DIALOG_ID);
			}
		});
		return addStudentDialog;
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
				dialog.setMessage(getResources().getString(R.string.adding_student_dialog_message));
				dialog.show();
			}
			
			@Override
			protected void onCancelled() {
				Toast.makeText(StudentRepositoryActivity.this, R.string.add_student_error_message, Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
			
			@Override
			protected void onPostExecute(Void result) {
				Toast.makeText(StudentRepositoryActivity.this, R.string.add_student_success_message, Toast.LENGTH_LONG).show();
				loadStudents();
			}
			
		}.execute(student);
	}
	
}