package im.antoine.InviteThemAll;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class InvitationSenderTask extends AsyncTask<EventInvitation, Integer, Long> {

  private final String TAG = this.getClass().getCanonicalName();
  private Context context;
  private String     serverUrl;

  public InvitationSenderTask(String serverUrl, Context context) {
    super();
    this.serverUrl = serverUrl;
    this.context = context;
  }

  @Override
  protected Long doInBackground(EventInvitation... eventInvitations) {

    Log.d(TAG, "New task");

    if (eventInvitations == null || eventInvitations.length == 0) {
      Log.w(TAG, "Nothing to send");
      return -1l;
    }

    HttpClient client = new DefaultHttpClient();

    List<EventInvitation> failedInvites = new ArrayList<>();

    for (EventInvitation invitation : eventInvitations) {
      postInvitation(client, failedInvites, invitation);
    }

    List<EventInvitation> retryFailed = new ArrayList<>();

    for (EventInvitation invitation : failedInvites) {
      postInvitation(client, retryFailed, invitation);
    }

    return 0l;
  }

  private void postInvitation(HttpClient client,
                              List<EventInvitation> failures,
                              EventInvitation invitation) {
    HttpPost post = null;
    try {
      post = prepareHttpPost(invitation);
    } catch (JSONException | UnsupportedEncodingException e) {
      // Don't retry it if the data is just wrong
      logE("Post preparation failed", e);
      return;
    }

    HttpResponse resp = null;
    try {
      Log.d(TAG, "Executing post request");
      resp = client.execute(post);
    } catch (IOException e) {
      logE("Post failed", e);
      failures.add(invitation);
      return;
    }

    Log.d(TAG, "Post done");

    int status = resp.getStatusLine().getStatusCode();
    Log.d(TAG, "HTTP " + status);

    if (status != HttpStatus.SC_OK) {
      Log.e(TAG, String.format("Request failed, code=%d, reason=%s",
                               status,
                               resp.getStatusLine().getReasonPhrase()));
      if (status >= 400 && status < 500) {
        failures.add(invitation);
      }
    }

    InputStream body = null;
    BufferedReader bodyReader = null;
    try {
      body = resp.getEntity().getContent();

      bodyReader = new BufferedReader(new InputStreamReader(body));

      StringBuffer bf = new StringBuffer();
      String aux;
      while ((aux = bodyReader.readLine()) != null) {
        bf.append(aux + "\n");
      }

      Log.d(TAG, bf.toString());
    } catch (Exception e) {
      // Don't care
    } finally {
      close(bodyReader);
      close(body);
    }

  }

  private HttpPost prepareHttpPost(EventInvitation invitation)
      throws JSONException, UnsupportedEncodingException {
    String jsonPayload = null;
    try {
      jsonPayload = invitation.toJsonString();
      Log.d(TAG, "Payload is: " + jsonPayload);
    } catch (JSONException e) {
      logE("JSONizing invitation", e);
      throw e;

    }

    Log.d(TAG, "Setting values");

    HttpPost post = new HttpPost(serverUrl.toString());


    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    nameValuePairs.add(new BasicNameValuePair("payload", jsonPayload));
    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//    post.setHeader("Content-type", "application/json");

    return post;
  }

  private void close(Closeable c) {
    if (c == null) {
      return;
    }
    try {
      c.close();
    } catch (IOException e) {
      // Don't care
    }
  }

  private void logE(String context, Throwable t) {
    Log.e(TAG, String.format("%s, cause : %s", context, t.getMessage()), t);
  }
}
