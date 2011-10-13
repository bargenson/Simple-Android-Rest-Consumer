package com.supinfo.simplerestconsumer.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.supinfo.simplerestconsumer.model.Student;

public class JsonStudentConverter {
	
	private static final String BIRTH_DATE_FIELD = "birthDate";
	private static final String LAST_NAME_FIELD = "lastName";
	private static final String FIRST_NAME_FIELD = "firstName";
	private static final String ID_BOOSTER_FIELD = "idBooster";
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	public Student convertToStudent(JSONObject object) throws JSONException {
		Student student = new Student();
		
		student.setIdBooster(object.getLong(ID_BOOSTER_FIELD));
		student.setFirstName(object.getString(FIRST_NAME_FIELD));
		student.setLastName(object.getString(LAST_NAME_FIELD));
		String formatedDate = object.getString(BIRTH_DATE_FIELD);
	    try {
			student.setBirthDate(dateFormat.parse(formatedDate));
		} catch (ParseException e) {
			Log.e("JsonStudentConverter", "Parse Exception!", e);
		}
	    
	    return student;
	}
	
	public JSONObject convertToJson(Student student) throws JSONException {
		JSONObject jsonStudent = new JSONObject();
		
		jsonStudent.put(ID_BOOSTER_FIELD, student.getIdBooster());
		jsonStudent.put(FIRST_NAME_FIELD, student.getFirstName());
		jsonStudent.put(LAST_NAME_FIELD, student.getLastName());
		jsonStudent.put(BIRTH_DATE_FIELD, dateFormat.format(student.getBirthDate()));
		
		return jsonStudent;
	}

}
