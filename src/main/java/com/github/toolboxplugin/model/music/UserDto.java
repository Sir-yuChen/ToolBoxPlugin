package com.github.toolboxplugin.model.music;


public class UserDto {
    private Boolean defaultAvatar;
    private String province;
    private String authStatus;
    private String followed;
    private String avatarUrl;
    private String accountStatus;
    private String gender;
    private String city;
    private String birthday;
    private String userId;
    private String userType;
    private String nickname;
    private String signature;
    private String description;
    private String detailDescription;
    private String avatarImgId;
    private String backgroundImgId;
    private String backgroundUrl;
    private String authority;
    private Boolean mutual;
    private String expertTags;
    private String experts;
    private String djStatus;
    private String vipType;
    private String remarkName;
    private String authenticationTypes;
    private avatarDetailDto avatarDetail;
    private String avatarImgIdStr;
    private String backgroundImgIdStr;
    private Boolean anchor;
    private String avatarImgId_str;

    public Boolean getDefaultAvatar() {
        return defaultAvatar;
    }

    public void setDefaultAvatar(Boolean defaultAvatar) {
        this.defaultAvatar = defaultAvatar;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public String getAvatarImgId() {
        return avatarImgId;
    }

    public void setAvatarImgId(String avatarImgId) {
        this.avatarImgId = avatarImgId;
    }

    public String getBackgroundImgId() {
        return backgroundImgId;
    }

    public void setBackgroundImgId(String backgroundImgId) {
        this.backgroundImgId = backgroundImgId;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Boolean getMutual() {
        return mutual;
    }

    public void setMutual(Boolean mutual) {
        this.mutual = mutual;
    }

    public String getExpertTags() {
        return expertTags;
    }

    public void setExpertTags(String expertTags) {
        this.expertTags = expertTags;
    }

    public String getExperts() {
        return experts;
    }

    public void setExperts(String experts) {
        this.experts = experts;
    }

    public String getDjStatus() {
        return djStatus;
    }

    public void setDjStatus(String djStatus) {
        this.djStatus = djStatus;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getAuthenticationTypes() {
        return authenticationTypes;
    }

    public void setAuthenticationTypes(String authenticationTypes) {
        this.authenticationTypes = authenticationTypes;
    }

    public avatarDetailDto getAvatarDetail() {
        return avatarDetail;
    }

    public void setAvatarDetail(avatarDetailDto avatarDetail) {
        this.avatarDetail = avatarDetail;
    }

    public String getAvatarImgIdStr() {
        return avatarImgIdStr;
    }

    public void setAvatarImgIdStr(String avatarImgIdStr) {
        this.avatarImgIdStr = avatarImgIdStr;
    }

    public String getBackgroundImgIdStr() {
        return backgroundImgIdStr;
    }

    public void setBackgroundImgIdStr(String backgroundImgIdStr) {
        this.backgroundImgIdStr = backgroundImgIdStr;
    }

    public Boolean getAnchor() {
        return anchor;
    }

    public void setAnchor(Boolean anchor) {
        this.anchor = anchor;
    }

    public String getAvatarImgId_str() {
        return avatarImgId_str;
    }

    public void setAvatarImgId_str(String avatarImgId_str) {
        this.avatarImgId_str = avatarImgId_str;
    }
}

class avatarDetailDto {
    private String userType;
    private String identityLevel;
    private String identityIconUrl;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIdentityLevel() {
        return identityLevel;
    }

    public void setIdentityLevel(String identityLevel) {
        this.identityLevel = identityLevel;
    }

    public String getIdentityIconUrl() {
        return identityIconUrl;
    }

    public void setIdentityIconUrl(String identityIconUrl) {
        this.identityIconUrl = identityIconUrl;
    }
}