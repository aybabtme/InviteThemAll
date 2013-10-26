package im.antoine.InviteThemAll;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

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
  private URL             serverUrl;

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

    invitation = new EventInvitation(this);
  }

  private View.OnClickListener getSubmitClickListener() {
    final Activity that = this;
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d(TAG, "Clicked on " + view.getId());


        if (!invitation.isComplete()) {
          Toast.makeText(that, R.string.incomplete_details, Toast.LENGTH_LONG).show();
          return;
        }

        new InvitationSenderTask(serverUrl,
                                 getApplicationContext())
            .execute(invitation);
      }
    };
  }

  private TimePicker.OnTimeChangedListener getEventTimeChangedListener() {
    return new TimePicker.OnTimeChangedListener() {
      @Override
      public void onTimeChanged(TimePicker view, int i, int i2) {
        Log.d(TAG, "Event on " + view.getId());
        invitation.setEventTime(view.getCurrentHour(),
                                view.getCurrentMinute());
      }
    };
  }

  private DatePicker.OnDateChangedListener getEventDateChangedListener() {
    return new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker view,
                                int year,
                                int monthOfYear,
                                int dayOfMonth) {
        Log.d(TAG, "Event on " + view.getId());
        invitation.setEventDate(year, monthOfYear, dayOfMonth);
      }
    };
  }

  private TextWatcher getEventNameEditTextListener() {
    return new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "Set even name");
        invitation.setEventName(s.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {}
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
  }

  private TextWatcher getEmailEditTextListener() {
    return new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "Set invitee email");
        invitation.setEmail(s.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {}
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
  }

  private TextWatcher getSignatureEditTextListener() {
    return new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "Set invite signature");
        invitation.setInviteSignature(s.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {}
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
  }

  private TextWatcher getLastNameEditTextListener() {
    return new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "Set invitee lastname");
        invitation.setLastName(s.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {}
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
  }

  private TextWatcher getFirstNameEditTextListener() {
    return new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "Set invitee firstname");
        invitation.setFirstName(s.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {}
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
  }
}
