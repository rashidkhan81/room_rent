package com.room_rent.Room_Rent_Application.exception.FileStorageException;

public class FileStorageException extends RuntimeException{

    public FileStorageException(String message) {
        super(message);
    }
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
