package my.edu.mmu.timetable;

import android.os.Parcel;
import android.os.Parcelable;


public class Item implements Parcelable {

	String day;
	String time;
	String subject;
	String venue;
	
	public Item(String day, String time, String subject, String venue) {
		super();
		this.day = day;
		this.time = time;
		this.subject = subject;
		this.venue = venue;
	}

	public String getDay() {
		return day;
	}
	
	public String getTime() {
		return time;
	}

	public String getSubject() {
		return subject;
	}

	public String getVenue() {
		return venue;
	}

	@Override
	public String toString() {
		return time + "\n" + subject + "\n" + venue;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(day);
		dest.writeString(time);
		dest.writeString(subject);
		dest.writeString(venue);
		
	}

	
	
}
