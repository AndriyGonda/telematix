package org.telematix.services;

public class ItemNotFoundException extends ServiceException {
    public ItemNotFoundException (String message) {
        super(message);
    }
}
