package com.supinfo.simplerestconsumer.validator;

import com.supinfo.simplerestconsumer.model.Student;

public class StudentValidator {
	
	public void validate(Student student) throws ValidationException {
		if(!(student.getIdBooster() != null
				&& isNotEmpty(student.getFirstName())
				&& isNotEmpty(student.getLastName())
				&& student.getBirthDate() != null)) {
			
			throw new ValidationException(student);
		}
	}
	
	private boolean isNotEmpty(String value) {
		return value != null && !value.equals("");
	}

}
