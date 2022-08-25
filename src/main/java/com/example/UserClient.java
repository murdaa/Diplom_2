package com.example;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String USER_PATH = "/api/auth/register";
    private static final String USER_LOGIN_PATH = "/api/auth/login";
    private static final String USER_DELETE_OR_UPDATE_PATH = "/api/auth/user";
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Step("Создать пользователя")
    public Response create(User user) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(USER_PATH);
    }

    @Step("Авторизовать пользователя")
    public Response loginUser(Credentials credentials) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(USER_LOGIN_PATH);
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("authorization", accessToken)
                .when()
                .delete(USER_DELETE_OR_UPDATE_PATH)
                .then()
                .assertThat()
                .statusCode(202)
                .extract()
                .path("ok");
    }

    @Step("Обновить пользователя с авторизацией")
    public Response updateUser(UserInfo userInfo, String accessToken) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("authorization", accessToken)
                .body(userInfo)
                .when()
                .patch(USER_DELETE_OR_UPDATE_PATH);
    }

    @Step("Обновить пользователя без авторизации")
    public Response updateUserWithoutAuth(UserInfo userInfo) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(userInfo)
                .when()
                .patch(USER_DELETE_OR_UPDATE_PATH);
    }

}




