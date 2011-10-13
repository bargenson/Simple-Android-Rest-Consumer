package com.supinfo.simplerestconsumer.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.supinfo.simplerestconsumer.converter.JsonStudentConverter;
import com.supinfo.simplerestconsumer.converter.JsonStudentListConverter;
import com.supinfo.simplerestconsumer.model.Student;

import fr.bargenson.util.rest.HttpRequestMethod;
import fr.bargenson.util.rest.HttpRestClient;
import fr.bargenson.util.rest.MediaType;

public class StudentDao {

	private static final String API_URI = "http://restful-example.appspot.com/students";
	
	public List<Student> getAllStudents() {
		List<Student> students = new ArrayList<Student>();
		
		String response = new HttpRestClient(HttpRequestMethod.GET, API_URI)
								.setAcceptHeader(MediaType.JSON)
								.execute();
		
		try {
			students.addAll(new JsonStudentListConverter().convertToStudentList(new JSONObject(response)));
		} catch (JSONException e) {
			Log.e("Get All Students", "JSONException!", e);
		}
		
		return students;
	}
	
	public void addStudent(Student student) {
		try {
			JSONObject jsonStudent = new JsonStudentConverter().convertToJson(student);
			new HttpRestClient(HttpRequestMethod.POST, API_URI)
				.setContentTypeHeader(MediaType.JSON)
				.setEntity(jsonStudent.toString())
				.execute();
		} catch (JSONException e) {
			Log.e("Add Student", "JSONException!", e);
		}
	}

	public void removeStudent(Student student) {
		new HttpRestClient(HttpRequestMethod.DELETE, API_URI + "/" + student.getIdBooster())
			.execute();
	}

}
