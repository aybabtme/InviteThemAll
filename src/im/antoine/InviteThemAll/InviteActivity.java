package im.antoine.InviteThemAll;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class InviteActivity extends Activity {

  private final String TAG = this.getClass().getCanonicalName();

  private EditText        firstNameEditText;
  private EditText        lastNameEditText;
  private EditText        emailEditText;
  private EditText        eventNameEditText;
  private EditText        signatureEditText;
  private DatePicker      eventDatePicker;
  private TimePicker      eventTimePicker;
  private Button          sendInviteButton;

  // Populated by the views
  private EventInvitation invitation;
  private URL serverUrl;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    firstNameEditText = (EditText) findViewById(R.id.inviteeFirstnameEditBox);
    lastNameEditText = (EditText) findViewById(R.id.inviteeLastnameEditText);
    emailEditText = (EditText) findViewById(R.id.inviteeEmailEditText);
    eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
    signatureEditText = (EditText) findViewById(R.id.signatureEditText);
    eventDatePicker = (DatePicker) findViewById(R.id.eventDatePicker);
    eventTimePicker = (TimePicker) findViewById(R.id.eventTimePicker);
    sendInviteButton = (Button) findViewById(R.id.sendInviteButton);

    firstNameEditText.addTextChangedListener(getFirstNameEditTextListener());
    lastNameEditText.addTextChangedListener(getLastNameEditTextListener());
    emailEditText.addTextChangedListener(getEmailEditTextListener());
    eventNameEditText.addTextChangedListener(getEventNameEditTextListener());
    signatureEditText.addTextChangedListener(getSignatureEditTextListener());

    Calendar today = Calendar.getInstance();
    eventDatePicker.init(today.get(Calendar.YEAR),
                         today.get(Calendar.MONTH),
                         today.get(Calendar.DAY_OF_MONTH),
                         getEventDateChangedListener());

    eventTimePicker.setOnTimeChangedListener(getEventTimeChangedListener());

    sendInviteButton.setOnClickListener(getSubmitClickListener());

    try {
      serverUrl = new URL(getString(R.string.serverUrl));
    } catch (MalformedURLException e) {
      Log.e(TAG, "R.string.serverUrl is messed up", e);
    }

    invitation = new EventInvitation();
  }

  private View.OnClickListener getSubmitClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d(TAG, "Clicked on " + view.getId());
        new InvitationSenderTask(serverUrl,
                                 getApplicationContext())
            .execute(invitation);
      }
    };
  }

  private TimePicker.OnTimeChangedListener getEventTimeChangedListener() {
    return new TimePicker.OnTimeChangedListener() {
      @Override
      public void onTimeChanged(TimePicker timePicker, int i, int i2) {
        Log.d(TAG, "Event on " + timePicker.getId());
        invitation.setEventTime(timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());
      }
    };
  }

  private DatePicker.OnDateChangedListener getEventDateChangedListener() {
    return null;  //To change body of created methods use File | Settings | File Templates.
  }

  private TextWatcher getEventNameEditTextListener() {
    return null;
  }

  private TextWatcher getEmailEditTextListener() {
    return null;  //To change body of created methods use File | Settings | File Templates.
  }

  private TextWatcher getSignatureEditTextListener() {
    return null;  //To change body of created methods use File | Settings | File Templates.
  }

  private TextWatcher getLastNameEditTextListener() {
    return null;  //To change body of created methods use File | Settings | File Templates.
  }

  private TextWatcher getFirstNameEditTextListener() {
    return null;
  }
}
