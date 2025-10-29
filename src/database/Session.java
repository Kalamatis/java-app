package database;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Session {
OffsetDateTime odt;
int id;
String date;
String time;

	public Session(int id, String datetime){
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(datetime, inputFormat);

        this.id = id;
        this.date = ldt.toLocalDate().toString(); // 2025-10-28
        this.time = ldt.format(DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH));
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
