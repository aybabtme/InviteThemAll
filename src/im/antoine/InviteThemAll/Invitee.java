package im.antoine.InviteThemAll;

/**
 * Project: InviteThemAll. Author: antoine. Date: 10/26/2013.
 */
public class Invitee {

  private String displayName = "";
  private String email       = "";

  public Invitee(String displayName, String email) {
    this.displayName = displayName;
    this.email = email;
  }

  public String getDisplayName() {
    return displayName;
  }


  public String getEmail() {
    return email;
  }

  public boolean isValid() {
    if (email == null || "".equals(email)) {
      return false;
    }

    if (displayName == null || "".equals(displayName)) {
      return false;
    }

    return true;
  }
}
