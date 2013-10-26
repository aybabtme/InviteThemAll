package im.antoine.InviteThemAll;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class InviteeFragment extends DialogFragment {

  private final String TAG = this.getClass().getCanonicalName();
  private final List<Invitee> inviteeList;
  // Widgets
  private       EditText      firstNameEditText;
  private       EditText      lastNameEditText;
  private       EditText      emailEditText;

  public InviteeFragment(List<Invitee> inviteeList) {
    this.inviteeList = inviteeList;
  }

  public static InviteeFragment newInstance(List<Invitee> inviteeList) {
    return new InviteeFragment(inviteeList);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    Log.d(TAG, "Inflating");
    View v = inflater.inflate(R.layout.fragment_event_invitee, container, false);

    firstNameEditText = (EditText) v.findViewById(R.id.inviteeFirstnameEditBox);
    lastNameEditText = (EditText) v.findViewById(R.id.inviteeLastnameEditText);
    emailEditText = (EditText) v.findViewById(R.id.inviteeEmailEditText);

    return v;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Log.d(TAG, "Creating dialog");
    return new AlertDialog.Builder(getActivity())
        .setTitle(getString(R.string.invitee_enter_manually_title))
        .setPositiveButton(android.R.string.ok,
                           getOkListener()
        )
        .setNegativeButton(android.R.string.cancel,
                           getCancelListener()
        )
        .create();
  }

  private DialogInterface.OnClickListener getOkListener() {
    return new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog,
                          int whichButton) {
        String givenName = firstNameEditText.getText().toString();
        String familyName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        Invitee inv = new Invitee(givenName + " " + familyName, email);

        if (!inv.isValid()) {
          Toast.makeText(getActivity(),
                         R.string.invitee_data_incomplete,
                         Toast.LENGTH_LONG).show();
          return;
        }

        Log.d(TAG, "Add "+email+" to list");

        inviteeList.add(inv);
        dismiss();
      }
    };
  }

  private DialogInterface.OnClickListener getCancelListener() {
    return new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog,
                          int whichButton) {
        dismiss();
      }
    };
  }
}