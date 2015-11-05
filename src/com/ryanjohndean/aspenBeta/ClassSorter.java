package com.ryanjohndean.aspenBeta;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ClassSorter {
	
	private String[] titles;
	private String[] teachers;
	private String[] grades;
	
	public ClassSorter() {
		teachers = new String[7];
		titles = new String[7];
		grades = new String[7];
	}
	
	/* PHYSICS a11 then a += 2
	 */
	
	public void sortAndHandle(Document doc) {
		Elements rows = doc.getElementsByClass("listcell");
		Elements items = rows.select("td");
		for (int i = 0; i < 7; i++) {
			titles[i] = items.get(i * 11 + 1).text();
			teachers[i] = items.get(i * 11 + 4).text();
			grades[i] = items.get(i * 11 + 7).text();
		}


		
	}
	
	public String getTitle(int period) {
		return titles[period-1];
	}
	public String getTeacher(int period) {
		return teachers[period-1];
	}
	public String getGrade(int period) {
		return grades[period-1];
	}
	public int getNumGrade(int period) {
		String str = "";
		for (int i = 0; i < 3; i++) {
			char c = grades[period-1].charAt(i);
			if (c != '.')
				str+=c;
			else
				break;
		}
		return Integer.valueOf(str);
	}
}
