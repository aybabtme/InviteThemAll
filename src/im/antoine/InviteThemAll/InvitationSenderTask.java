package im.antoine.InviteThemAll;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class InvitationSenderTask extends AsyncTask<EventInvitation, Integer, Long> {

  private final String TAG = this.getClass().getCanonicalName();
  private Context context;
  private URL serverUrl;

  public InvitationSenderTask(URL serverUrl, Context context) {
    super();
    this.serverUrl = serverUrl;
    this.context = context;
  }

  @Override
  protected Long doInBackground(EventInvitation... event) {

    Log.d(TAG, "New task");

    if (event == null || event.length == 0) {
      Log.e(TAG, "Nothing to send");
      return -1l;
    }

    if (event.length != 1) {
      Log.w(TAG, String.format("Got %d invitation, sending only first",
                               event.length));
    }

    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(serverUrl.toString());

    Log.d(TAG, "Setting entity");

    try{
      post.setEntity(new StringEntity(event[0].toJsonString(), "UTF8"));
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "Setting JSON content", e);
      return -2l;
    }

    post.setHeader("Content-type", "application/json");

    HttpResponse resp;
    try {
      Log.d(TAG, "Executing post request");
      resp = client.execute(post);
    } catch (IOException e) {
      Log.e(TAG, "Post failed", e);
      return -3l;
    }

    Log.d(TAG, "Post done");

    int status = resp.getStatusLine().getStatusCode();
    if (status != HttpStatus.SC_OK) {
      Log.e(TAG, String.format("Request failed, code=%d, reason=%s",
                               status,
                               resp.getStatusLine().getReasonPhrase()));
      return -4l;
    }

    Log.d(TAG, "Success");

    return 0l;
  }
}
