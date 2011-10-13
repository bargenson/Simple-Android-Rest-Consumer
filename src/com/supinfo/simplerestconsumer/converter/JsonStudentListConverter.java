package com.supinfo.simplerestconsumer.converter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.supinfo.simplerestconsumer.model.Student;

public class JsonStudentListConverter {
	
	private JsonStudentConverter jsonStudentConverter = new JsonStudentConverter();
	
	public List<Student> convertToStudentList(JSONObject object) throws JSONException {
		List<Student> students = new ArrayList<Student>();
		
		JSONArray jsonArray = object.getJSONArray("student");
		for (int i = 0; i < jsonArray.length(); i++) {
			students.add(jsonStudentConverter.convertToStudent(jsonArray.getJSONObject(i)));
		}
		
		return students;
	}
	
	public JSONObject convertToJson(List<Student> students) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("count", students.size());
		
		JSONArray jsonArray = new JSONArray();
		for (Student student : students) {
			jsonArray.put(jsonStudentConverter.convertToJson(student));
		}
		jsonObject.put("students", jsonArray);
		
		return jsonObject;
	}

}
