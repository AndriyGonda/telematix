package org.telematix.dto.user;

import org.telematix.models.User;

public class ProfileUpdateDto {
    private String firstName;
    private String lastName;
    private String avatarUrl;

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public User toModel() {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAvatarUrl(avatarUrl);
        return user;
    }
}
