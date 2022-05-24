package org.telematix.dto.user;

import org.telematix.models.User;

public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private boolean administrator;

    public boolean isAdministrator() {
        return administrator;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public UserUpdateDto() {
    }

    public User toModel() {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAvatarUrl(avatarUrl);
        user.setAdministrator(administrator);
        return user;
    }
}
