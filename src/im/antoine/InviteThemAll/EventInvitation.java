package im.antoine.InviteThemAll;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class EventInvitation {

  private final String DOCUMENT_TYPE;
  private final String EVENT_NAME_FIELD;
  private final String EVENT_DATE_FIELD;
  private final String INVITE_SIGNATURE_FIELD;
  private final String DISPLAY_NAME_FIELD;
  private final String EMAIL_FIELD;
  private final String OPTIONAL_MESSAGE_FIELD;
  private Invitee invitee = null;
  private Event   event   = null;

  public EventInvitation(Context ctx, Event event, Invitee invitee) {
    DOCUMENT_TYPE = ctx.getString(R.string.event_document_type);
    EVENT_NAME_FIELD = ctx.getString(R.string.event_name_field);
    EVENT_DATE_FIELD = ctx.getString(R.string.event_date_field);
    INVITE_SIGNATURE_FIELD = ctx.getString(R.string.event_signature_field);
    DISPLAY_NAME_FIELD = ctx.getString(R.string.event_displayname_field);
    EMAIL_FIELD = ctx.getString(R.string.event_email_field);
    OPTIONAL_MESSAGE_FIELD = ctx.getString(R.string.event_optional_message_field);

    this.event = event;
    this.invitee = invitee;
  }

  public static List<EventInvitation> newInstance(Context ctx,
                                                  Event event,
                                                  List<Invitee> inviteeList) {

    List<EventInvitation> eventInvitationList = new ArrayList<>(inviteeList.size());
    for (Invitee i : inviteeList) {
      eventInvitationList.add(new EventInvitation(ctx, event, i));
    }
    return eventInvitationList;
  }

  public boolean isValid() {

    if (event == null) {
      return false;
    }

    if (!event.isValid()) {
      return false;
    }

    if (invitee == null) {
      return false;
    }

    if (!invitee.isValid()) {
      return false;
    }
    return true;
  }

  public String toJsonString() throws JSONException {
    return getJSON().toString();
  }

  private JSONObject getJSON() throws JSONException {
    final JSONObject filter = new JSONObject() {{
      accumulate("DocumentType", DOCUMENT_TYPE);
    }};

    final JSONObject fields = new JSONObject() {{
      accumulate(EVENT_NAME_FIELD, event.getName());
      accumulate(EVENT_DATE_FIELD, event.getDate());
      accumulate(INVITE_SIGNATURE_FIELD, event.getInviteSignature());
      accumulate(DISPLAY_NAME_FIELD, invitee.getDisplayName());
      accumulate(OPTIONAL_MESSAGE_FIELD, event.getOptionalMessage());
    }};

    final JSONObject actions = new JSONObject() {{
      accumulate("Type", "Email");
      accumulate("To", invitee.getEmail());
    }};

    final JSONObject request = new JSONObject() {{
      accumulate("filters", filter);
      accumulate("fields", fields);
      accumulate("actions", actions);
    }};

    return request;
  }

}
