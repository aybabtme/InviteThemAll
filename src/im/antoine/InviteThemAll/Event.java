package im.antoine.InviteThemAll;

import java.util.Calendar;
import java.util.Date;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class Event {

  private String name            = "";
  private Date   date            = Calendar.getInstance().getTime();
  private String inviteSignature = "";
  private String optionalMessage = "";

  public Event(String name,
               Date date,
               String inviteSignature) {
    this.name = name;
    this.date = date;
    this.inviteSignature = inviteSignature;
  }

  public String getName() {
    return name;
  }

  public Date getDate() {
    return date;
  }

  public String getInviteSignature() {
    return inviteSignature;
  }

  public String getOptionalMessage() {
    return optionalMessage;
  }

  public void setOptionalMessage(String optionalMessage) {
    this.inviteSignature = inviteSignature;
  }

  public boolean isValid() {

    if ("".equals(name)) {
      return false;
    }
    if ("".equals(inviteSignature)) {
      return false;
    }

    Calendar nextMinute = Calendar.getInstance();
    nextMinute.add(Calendar.MINUTE, 1);
    if (date.before(nextMinute.getTime())) {
      return false;
    }
    return true;
  }

}
