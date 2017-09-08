package net.ridhoperdana.asumu;

/**
 * Created by RIDHO on 9/8/2017.
 */

public class UserModel {
    String userName, userPassword, userIncome, userId, userFullName;

    public UserModel(String userName, String userIncome, String userId, String userFullName, String userPassword) {
        this.userName = userName;
        this.userIncome = userIncome;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserIncome() {
        return userIncome;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }
}
