package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class UserUpdateTest extends UserClient {

    @Test
    @DisplayName("Valid update of username")
    @Description("To verify user's name update with valid authorisation")
    public void userValidNameUpdateTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста обновления
        User user = new User(email, password, name);
        Response responseForTest = create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForTest.path("accessToken").toString();

        //логин пользователя
        Credentials userCredentials = new Credentials(email, password);
        loginUser(userCredentials);

        //тест обновления данных
        UserInfo userInfo = new UserInfo(email, "updated name");
        Response response = updateUser(userInfo, accessToken).then().assertThat().statusCode(200).extract().response();
        String actual = response.path("success").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Valid update of user's email")
    @Description("To verify user's email update with valid authorisation")
    public void userValidEmailUpdateTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста обновления
        User user = new User(email, password, name);
        Response responseForTest = create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForTest.path("accessToken").toString();

        //логин пользователя
        Credentials userCredentials = new Credentials(email, password);
        loginUser(userCredentials);

        //тест обновления данных
        UserInfo userInfo = new UserInfo("updatedemail@yandex.ru", name);
        Response response = updateUser(userInfo, accessToken).then().assertThat().statusCode(200).extract().response();
        String actual = response.path("success").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Invalid update of user's email")
    @Description("To verify invalid user's email update due to lack of token")
    public void userInValidEmailUpdateWithoutAuthTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста обновления
        User user = new User(email, password, name);
        Response responseForTest = create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForTest.path("accessToken").toString();

        //логин пользователя
        Credentials userCredentials = new Credentials(email, password);
        loginUser(userCredentials);

        //тест обновления данных
        UserInfo userInfo = new UserInfo("updatedemail@yandex.ru", name);
        Response response = updateUserWithoutAuth(userInfo).then().assertThat().statusCode(401).extract().response();
        String actual = response.path("message").toString();
        String expected = "You should be authorised";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Invalid update of user's name")
    @Description("To verify invalid user's name update due to lack of token")
    public void userInValidNameUpdateWithoutAuthTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста обновления
        User user = new User(email, password, name);
        Response responseForTest = create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForTest.path("accessToken").toString();

        //логин пользователя
        Credentials userCredentials = new Credentials(email, password);
        loginUser(userCredentials);

        //тест обновления данных
        UserInfo userInfo = new UserInfo(email, "updated name");
        Response response = updateUserWithoutAuth(userInfo).then().assertThat().statusCode(401).extract().response();
        String actual = response.path("message").toString();
        String expected = "You should be authorised";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        deleteUser(accessToken);
    }

}
