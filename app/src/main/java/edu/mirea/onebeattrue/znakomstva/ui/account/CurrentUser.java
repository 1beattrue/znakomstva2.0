package edu.mirea.onebeattrue.znakomstva.ui.account;

public class CurrentUser {
    private String email;
    private String userName;
    private String interests;

    public CurrentUser() {
    }

    public CurrentUser(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
}
