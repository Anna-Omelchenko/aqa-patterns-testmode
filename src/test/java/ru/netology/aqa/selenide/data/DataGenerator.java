package ru.netology.aqa.selenide.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final Faker FAKER_EN = new Faker(new Locale("en"));
    private static final String USER_STATUS_ACTIVE = "active";
    private static final String USER_STATUS_BLOCKED = "blocked";

    private static final RequestSpecification API_REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {}

    public static String generateUsername() {
        return FAKER_EN.name().username();
    }

    public static String generatePassword() {
        return FAKER_EN.internet().password();
    }

    public static UserCredentials generateActiveUser() {
        return new UserCredentials(
                generateUsername(),
                generatePassword(),
                USER_STATUS_ACTIVE
        );
    }

    public static UserCredentials generateBlockedUser() {
        return new UserCredentials(
                generateUsername(),
                generatePassword(),
                USER_STATUS_BLOCKED
        );
    }

    private static UserCredentials createOrUpdateUser(UserCredentials user) {
        given()
                .spec(API_REQUEST_SPEC)
                .body(user)
        .when()
                .post("/api/system/users")
        .then()
                .statusCode(HttpStatus.SC_OK);
        return user;
    }

    public static UserCredentials createActiveUser() {
        return createOrUpdateUser(generateActiveUser());
    }

    public static UserCredentials blockUser(UserCredentials user) {
        UserCredentials blockedUser = new UserCredentials(user.getLogin(), user.getPassword(), USER_STATUS_BLOCKED);
        return createOrUpdateUser(blockedUser);
    }
}
