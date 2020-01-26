package Main;
import java.time.LocalDateTime;

public class Time extends Thread{

	public void run() {
		while(true) {
			int minute;
			int hour;
			String half;
			LocalDateTime now = LocalDateTime.now();  
			minute = now.getMinute();
			hour = now.getHour();
			half = "AM";
			if(hour >= 12) {
				if(hour != 12) {
					hour = hour - 12;
				}
				half = "PM";
			}
			if(hour == 0) {
				hour = 12;
			}
			MainBar.setTime(hour + ":" + String.format("%02d", minute) + " " + half);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
