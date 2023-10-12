package ru.practicum.shareIt.error.exception;

public class ItemRequestNotFoundException extends RuntimeException{
        public ItemRequestNotFoundException(String s) {
            super(s);
        }
}
