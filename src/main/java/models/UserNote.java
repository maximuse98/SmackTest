package models;

public class UserNote {
    private String login;
    private String action;
    private String time;

    public UserNote(String login, String action, String time) {
        if(action==null) throw new NullPointerException() ;
        this.login = login;
        this.action = action;
        this.time = time;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAction() {
        return action;
    }

    public String getTime() {
        return time;
    }

}
