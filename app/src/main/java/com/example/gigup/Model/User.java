package com.example.gigup.Model;

public class User {
    String userName, universityName, profilePic, eId;

    public User(String userName, String universityName, String eId, String profilePic) {
        this.userName = userName;
        this.universityName = universityName;
        this.profilePic = profilePic;
        this.eId = eId;
    }
    public User(){

    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
