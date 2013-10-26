package im.antoine.InviteThemAll;

import java.util.Calendar;
import java.util.Date;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class EventInvitation {

  private String eventName       = "";
  private Date   eventDate       = Calendar.getInstance().getTime();
  private String inviteSignature = "";
  private String firstName       = "";
  private String lastName        = "";
  private String email           = "";

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public void setEventDate(int year, int month, int dayOfMonth) {
    eventDate.setYear(year);
    eventDate.setMonth(month);
    eventDate.setDate(dayOfMonth);
  }

  public void setEventTime(int hour, int minute) {
    eventDate.setHours(hour);
    eventDate.setMinutes(minute);
  }

  public void setInviteSignature(String inviteSignature) {
    this.inviteSignature = inviteSignature;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isComplete() {

    if ("".equals(eventName)) {
      return false;
    }
    if ("".equals(inviteSignature)) {
      return false;
    }

    if ("".equals(firstName)) {
      return false;
    }
    if ("".equals(lastName)) {
      return false;
    }

    if ("".equals(email)) {
      return false;
    }

    Calendar nextMinute = Calendar.getInstance();
    nextMinute.add(Calendar.MINUTE, 1);
    if (eventDate.before(nextMinute.getTime())) {
      return false;
    }

    return true;
  }

  public String toJsonString() {
    return "0xDEADBEEF";
  }

}
