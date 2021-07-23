
package com.app.hmp.cognitive.afrocamgistchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{

    public User () {}

    public User (String id, String firstName, String lastName, String user_name, String email, String contactNumber, String registeredWith, int userId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user_name = user_name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.registeredWith = registeredWith;
        this.userId = userId;
        this.blockedByMe = false;
    }

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("registered_with")
    @Expose
    private String registeredWith;
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;
    @SerializedName("profile_cover_image")
    @Expose
    private String profileCoverUrl;
    @SerializedName("cover_image_url")
    @Expose
    private String coverImageUrl;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("last_active")
    @Expose
    private String lastActive;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("email_verified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("verification_token")
    @Expose
    private String verificationToken;
    @SerializedName("verification_otp")
    @Expose
    private String verificationOtp;
    @SerializedName("phone_verified")
    @Expose
    private Boolean phoneVerified;
    @SerializedName("introduced")
    @Expose
    private Boolean introduced;
    @SerializedName("last_login_ip")
    @Expose
    private String lastLoginIp;
    @SerializedName("career_interest")
    @Expose
    private String careerInterest;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("blocked_by_me")
    @Expose
    private Boolean blockedByMe;
    @SerializedName("sports_interests")
    @Expose
    private ArrayList<String> sportsInterests = null;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("following_ids")
    @Expose
    private ArrayList<Integer> followingIds = null;
    @SerializedName("friend_ids")
    @Expose
    private ArrayList<Integer> friendIds = null;
//    @SerializedName("followings_list")
//    @Expose
//    private ArrayList<Follow> followingsList = null;
    @SerializedName("followers_list")
    @Expose
    private ArrayList<Object> followersList = null;
//    @SerializedName("friends_list")
//    @Expose
//    private ArrayList<FriendsList> friendsList = null;
    @SerializedName("profile_strength")
    @Expose
    private Integer profileStrength;
    private final static long serialVersionUID = -4235442540076153281L;


    public String getUser_name() {
        user_name = "soul_mortal_92";
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Boolean getBlockedByMe() {
        return blockedByMe;
    }

    public void setBlockedByMe(Boolean blockedByMe) {
        this.blockedByMe = blockedByMe;
    }

    public String getId() {
        id = "5f4c926267f1fd465c1d21a0";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRegisteredWith() {
        return registeredWith;
    }

    public void setRegisteredWith(String registeredWith) {
        this.registeredWith = registeredWith;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getProfileCoverUrl() {
        return profileCoverUrl;
    }

    public void setProfileCoverUrl(String profileCoverUrl) {
        this.profileCoverUrl = profileCoverUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public Integer getUserId() {
        userId = 2149;
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getVerificationOtp() {
        return verificationOtp;
    }

    public void setVerificationOtp(String verificationOtp) {
        this.verificationOtp = verificationOtp;
    }

    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Boolean getIntroduced() {
        return introduced;
    }

    public void setIntroduced(Boolean introduced) {
        this.introduced = introduced;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getCareerInterest() {
        return careerInterest;
    }

    public void setCareerInterest(String careerInterest) {
        this.careerInterest = careerInterest;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public ArrayList<String> getSportsInterests() {
        return sportsInterests;
    }

    public void setSportsInterests(ArrayList<String> sportsInterests) {
        this.sportsInterests = sportsInterests;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ArrayList<Integer> getFollowingIds() {
        return followingIds;
    }

    public void setFollowingIds(ArrayList<Integer> followingIds) {
        this.followingIds = followingIds;
    }

    public ArrayList<Integer> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(ArrayList<Integer> friendIds) {
        this.friendIds = friendIds;
    }

//    public ArrayList<Follow> getFollowingsList() {
//        return followingsList;
//    }
//
//    public void setFollowingsList(ArrayList<Follow> followingsList) {
//        this.followingsList = followingsList;
//    }

    public ArrayList<Object> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(ArrayList<Object> followersList) {
        this.followersList = followersList;
    }

//    public ArrayList<FriendsList> getFriendsList() {
//        return friendsList;
//    }
//
//    public void setFriendsList(ArrayList<FriendsList> friendsList) {
//        this.friendsList = friendsList;
//    }

    public Integer getProfileStrength() {
        return profileStrength;
    }

    public void setProfileStrength(Integer profileStrength) {
        this.profileStrength = profileStrength;
    }

}
