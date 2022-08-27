package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class UserLoginTest extends  UserClient {

    @Test
    @DisplayName("User valid login")
    @Description("To verify user login with valid credentials")
    public void userLoginTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста логина
        User user = new User(email, password, name);
        Response responseForTest = create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForTest.path("accessToken").toString();

        //проверка валидного логина
        Credentials userCredentials = new Credentials(email, password);
        Response response = loginUser(userCredentials);
        response.then().assertThat().statusCode(200).extract().response();
        String actual = response.path("success").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("User invalid login")
    @Description("To verify user login with invalid credentials")
    public void userInvalidLoginTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста логина
        User user = new User(email, password, name);
        Response responseForTest = create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForTest.path("accessToken").toString();

        //проверка невалидного логина
        Credentials userCredentials = new Credentials(email, null);
        Response response = loginUser(userCredentials);
        response.then().assertThat().statusCode(401).extract().response();
        String actual = response.path("message").toString();
        String expected = "email or password are incorrect";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        deleteUser(accessToken);
    }

}
