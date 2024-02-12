package ru.netology.aqa.selenide.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserCredentials {
    private final String login;
    private final String password;
    private final String status;
}
