package com.supinfo.simplerestconsumer.validator;

import com.supinfo.simplerestconsumer.model.Student;

public class ValidationException extends Exception {

	public ValidationException(Student student) {
		super(student + " is not valid.");
	}

}
