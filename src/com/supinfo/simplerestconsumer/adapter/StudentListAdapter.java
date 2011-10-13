package com.supinfo.simplerestconsumer.adapter;

import java.text.DateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.supinfo.simplerestconsumer.R;
import com.supinfo.simplerestconsumer.model.Student;

public class StudentListAdapter extends BaseAdapter {

	private Context context;
	private List<Student> students;
	private LayoutInflater inflater;
	private DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);


	public StudentListAdapter(Context context, List<Student> students) {
		this.context = context;
		this.students = students;
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return students.size();
	}

	public Student getItem(int position) {
		return students.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.student_item, null);
			holder = new ViewHolder();
			holder.idBooster = (TextView) convertView.findViewById(R.id.student_idBooster);
			holder.name = (TextView) convertView.findViewById(R.id.student_name);
			holder.dateOfBirth = (TextView) convertView.findViewById(R.id.student_dateOfBirth);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Student student = getItem(position);
		holder.idBooster.setText(student.getIdBooster().toString());
		holder.name.setText(student.getFullName());
		holder.dateOfBirth.setText(dateFormat.format(student.getBirthDate()));

		if(position % 2 == 0) {
			convertView.setBackgroundResource(R.color.dark);
		} else {
			convertView.setBackgroundResource(R.color.light);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView idBooster;
		TextView name;
		TextView dateOfBirth;
	}

}
