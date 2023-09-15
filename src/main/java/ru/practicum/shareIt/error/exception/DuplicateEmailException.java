package ru.practicum.shareIt.error.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String s) {
        super(s);
    }
}
