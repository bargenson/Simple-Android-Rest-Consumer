package com.supinfo.simplerestconsumer.dialog;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.simplerestconsumer.R;
import com.supinfo.simplerestconsumer.model.Student;
import com.supinfo.simplerestconsumer.validator.StudentValidator;
import com.supinfo.simplerestconsumer.validator.ValidationException;

public class AddStudentDialog extends Dialog {
	
	private StudentValidator studentValidator = new StudentValidator();
	
	private Student student;
	
	
	public AddStudentDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.add_student_dialog_title);
		setContentView(R.layout.add_student_dialog);
		
		Button cancelButton = (Button) findViewById(R.id.cancel);
		Button submitButton = (Button) findViewById(R.id.submit);
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				cancel();
			}
		});
		
		submitButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Student student = new Student();
				
				try {
					TextView idBoosterTextView = (TextView) findViewById(R.id.id_booster);
					String idBoosterText = idBoosterTextView.getText().toString();
					student.setIdBooster(Long.valueOf(idBoosterText));
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
					studentValidator.validate(student);
					
					AddStudentDialog.this.student = student;
					
					dismiss();
				} catch (NumberFormatException e) {
					Toast.makeText(getContext(), R.string.add_student_validation_error_message, Toast.LENGTH_SHORT).show();
				} catch (ValidationException e) {
					Toast.makeText(getContext(), R.string.add_student_validation_error_message, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public Student getStudent() {
		return student;
	}

}
