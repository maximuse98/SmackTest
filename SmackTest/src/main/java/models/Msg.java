package models;

public class Msg {
    private String login;
    private String message;
    private String time;

    public Msg(String login, String message, String time) {
        this.login = login;
        this.message = message;
        this.time = time;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
