package ru.yandex.practicum.filmorate.model;

public class ExceptionDTO {
    private final String massage;

    public ExceptionDTO(String massage) {
        this.massage = massage;
    }

    public String getMassage() {
        return massage;
    }
}
