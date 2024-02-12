package ru.netology.aqa.selenide.ibank;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import ru.netology.aqa.selenide.data.DataGenerator;
import ru.netology.aqa.selenide.data.UserCredentials;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @Test
    void testMissingUsername() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=action-login]").click();
        SelenideElement usernameHint = form.$("[data-test-id='login'].input_invalid .input__sub");
        usernameHint.shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
        SelenideElement passwordHint = form.$("[data-test-id='password'].input_invalid .input__sub");
        passwordHint.shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void testMissingPassword() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(DataGenerator.generateUsername());
        form.$("[data-test-id=action-login]").click();
        SelenideElement passwordHint = form.$("[data-test-id='password'].input_invalid .input__sub");
        passwordHint.shouldBe(visible).shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void testNotExistingUser() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(DataGenerator.generateUsername());
        form.$("[data-test-id=password] input").setValue(DataGenerator.generatePassword());
        form.$("[data-test-id=action-login]").click();
        SelenideElement errorNotification = $("[data-test-id=error-notification]");
        errorNotification.shouldBe(visible);
        errorNotification.$(byClassName("notification__title")).shouldHave(exactText("Ошибка"));
        errorNotification.$(byClassName("notification__content"))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void testSuccess() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        UserCredentials user = DataGenerator.createActiveUser();
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("[data-test-id=action-login]").click();
        $("[id=root] h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    void testWrongPassword() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        UserCredentials user = DataGenerator.createActiveUser();
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(DataGenerator.generatePassword());
        form.$("[data-test-id=action-login]").click();
        SelenideElement errorNotification = $("[data-test-id=error-notification]");
        errorNotification.shouldBe(visible);
        errorNotification.$(byClassName("notification__title")).shouldHave(exactText("Ошибка"));
        errorNotification.$(byClassName("notification__content"))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void testBlockedUser() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        UserCredentials user = DataGenerator.createActiveUser();
        DataGenerator.blockUser(user);
        form.$("[data-test-id=login] input").setValue(user.getLogin());
        form.$("[data-test-id=password] input").setValue(user.getPassword());
        form.$("[data-test-id=action-login]").click();
        SelenideElement errorNotification = $("[data-test-id=error-notification]");
        errorNotification.shouldBe(visible);
        errorNotification.$(byClassName("notification__title")).shouldHave(exactText("Ошибка"));
        errorNotification.$(byClassName("notification__content"))
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }
}