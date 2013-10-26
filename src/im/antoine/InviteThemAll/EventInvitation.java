package im.antoine.InviteThemAll;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class EventInvitation {

  private final String DOCUMENT_TYPE;

  private final String EVENT_NAME_FIELD;
  private final String EVENT_DATE_FIELD;
  private final String INVITE_SIGNATURE_FIELD;
  private final String FIRST_NAME_FIELD;
  private final String LAST_NAME_FIELD;
  private final String EMAIL_FIELD;
  private final String OPTIONAL_MESSAGE_FIELD;

  private String eventName       = "";
  private Date   eventDate       = Calendar.getInstance().getTime();
  private String inviteSignature = "";
  private String firstName       = "";
  private String lastName        = "";
  private String email           = "";
  private String optionalMessage = "";

  public EventInvitation(Context ctx) {
    DOCUMENT_TYPE = ctx.getString(R.string.event_document_type);
    EVENT_NAME_FIELD = ctx.getString(R.string.event_name_field);
    EVENT_DATE_FIELD = ctx.getString(R.string.event_date_field);
    INVITE_SIGNATURE_FIELD = ctx.getString(R.string.event_signature_field);
    FIRST_NAME_FIELD = ctx.getString(R.string.event_firstname_field);
    LAST_NAME_FIELD = ctx.getString(R.string.event_lastname_field);
    EMAIL_FIELD = ctx.getString(R.string.event_email_field);
    OPTIONAL_MESSAGE_FIELD = ctx.getString(R.string.event_optional_message_field);
  }

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

  public void setOptionalMessage(String optionalMessage) {
    this.optionalMessage = optionalMessage;
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

  public String toJsonString() throws JSONException {
    return getJSON().toString();
  }

  private JSONObject getJSON() throws JSONException {
    final JSONObject filter= new JSONObject() {{
      accumulate("DocumentType", DOCUMENT_TYPE);
    }};

    final JSONObject fields = new JSONObject() {{
      accumulate(EVENT_NAME_FIELD, eventName);
      accumulate(EVENT_DATE_FIELD, eventDate);
      accumulate(INVITE_SIGNATURE_FIELD, inviteSignature);
      accumulate(FIRST_NAME_FIELD, firstName);
      accumulate(LAST_NAME_FIELD, lastName);
      accumulate(EMAIL_FIELD, email);
      accumulate(OPTIONAL_MESSAGE_FIELD, optionalMessage);
    }};

    final JSONObject request = new JSONObject() {{
      accumulate("filters", filter);
      accumulate("fields", fields);
    }};

    return request;
  }

}
