package im.antoine.InviteThemAll;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InviteActivity extends Activity {

  // Intent codes
  private static final int    PICK_CONTACT_REQUEST = 1;
  // Log
  private final        String TAG                  = this.getClass().getCanonicalName();
  // Widgets
  private EditText   eventNameEditText;
  private EditText   signatureEditText;
  private DatePicker eventDatePicker;
  private TimePicker eventTimePicker;
  // Buttons
  private Button     importContactButton;
  private Button     enterManuallyButton;
  private Button     sendInviteButton;
  // Populated by the views
  private Event      event;
  private List<Invitee> inviteeList = new ArrayList<>();
  // Static stuff
  private URL serverUrl;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
    signatureEditText = (EditText) findViewById(R.id.signatureEditText);
    eventDatePicker = (DatePicker) findViewById(R.id.eventDatePicker);
    eventTimePicker = (TimePicker) findViewById(R.id.eventTimePicker);

    sendInviteButton = (Button) findViewById(R.id.sendInviteButton);
    sendInviteButton.setOnClickListener(getSubmitClickListener());

    importContactButton = (Button) findViewById(R.id.importContactButton);
    importContactButton.setOnClickListener(getImportClickListener());

    enterManuallyButton = (Button) findViewById(R.id.enterManuallyButton);
    enterManuallyButton.setOnClickListener(getEnterClickListener());

    try {
      serverUrl = new URL(getString(R.string.serverUrl));
    } catch (MalformedURLException e) {
      Log.e(TAG, "R.string.serverUrl is messed up", e);
    }

  }

  private void enterInviteeDetailsDialog() {
    DialogFragment newFragment = InviteeFragment.newInstance(inviteeList);
    newFragment.show(getFragmentManager(), "dialog");
  }

  private void selectInviteeFromContact() {
    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

    startActivityForResult(intent, PICK_CONTACT_REQUEST);
  }

  private View.OnClickListener getEnterClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        enterInviteeDetailsDialog();
      }
    };
  }

  private View.OnClickListener getImportClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        selectInviteeFromContact();
      }
    };
  }

  private View.OnClickListener getSubmitClickListener() {
    final Activity that = this;
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d(TAG, "Clicked on " + ((Button) view).getText());

        event = new Event(
            eventNameEditText.getText().toString(),
            new Date(
                eventDatePicker.getYear(),
                eventDatePicker.getMonth(),
                eventDatePicker.getDayOfMonth(),
                eventTimePicker.getCurrentHour(),
                eventTimePicker.getCurrentMinute()
            ),
            signatureEditText.getText().toString()
        );

        if (inviteeList.isEmpty()) {
          Toast.makeText(that, R.string.nothing_to_send, Toast.LENGTH_LONG).show();
          return;
        }

        if (!event.isValid()) {
          Toast.makeText(that, R.string.event_data_incomplete, Toast.LENGTH_LONG).show();
          return;
        }

        AsyncTask<EventInvitation, Integer, Long> t = new InvitationSenderTask(serverUrl,
                                                                               getApplicationContext());

        List<EventInvitation> eventInviteList = EventInvitation.newInstance(that,
                                                                            event,
                                                                            inviteeList);

        t.execute(eventInviteList.toArray(new EventInvitation[eventInviteList.size()]));
      }
    };
  }

  @Override
  public void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    switch (reqCode) {
      case (PICK_CONTACT_REQUEST):
        handleContactIntentResult(resultCode, data);
        break;
    }
  }

  private void handleContactIntentResult(int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }

    ContentResolver cr = getContentResolver();

    Uri contactData = data.getData();
    Cursor c = managedQuery(contactData, null, null, null, null);

    while (c.moveToNext()) {

      String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
      String displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
      String email = getEmailForContactId(id);

      Log.d(TAG, "Adding " + email + " from contacts");
      inviteeList.add(new Invitee(displayName, email));
    }
  }

  private String getEmailForContactId(String id) {
    String email = null;
    ContentResolver cr = getContentResolver();
    Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                          null,
                          ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                          new String[]{id},
                          null);

    while (cur.moveToNext()) {
      int idx = cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
      email = cur.getString(idx);
    }
    cur.close();
    return email;
  }
}
