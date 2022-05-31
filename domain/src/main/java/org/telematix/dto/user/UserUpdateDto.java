package org.telematix.dto.user;

import org.telematix.models.User;

public class UserUpdateDto {
    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public UserUpdateDto() {
    }

    public User toModel() {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
}
