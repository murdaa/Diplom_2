package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class UserCreationTest extends UserClient {

    String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
    String password = RandomStringUtils.randomAlphanumeric(8);
    String name = RandomStringUtils.randomAlphanumeric(8);

    @Test
    @DisplayName("User valid creation")
    @Description("To verify user valid creation")
    public void userCreationTest() {

        User user = new User(email, password, name);
        Response response = create(user).then().assertThat().statusCode(200).extract().response();
        String actual = response.path("success").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //логин и удаление пользователя после теста
        String accessToken = response.path("accessToken").toString();
        loginUser(new Credentials(email, password));
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("User invalid creation")
    @Description("To verify user invalid creation if one required field is empty")
    public void userInvalidCreationTest() {
        User user = new User(null, password, name);
        Response response = create(user).then().assertThat().statusCode(403).extract().response();
        String actual = response.path("message").toString();
        String expected = "Email, password and name are required fields";
        Assert.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Another User invalid creation")
    @Description("To verify user invalid creation if this user is already created")
    public void userCreationTwiceTest() {
        //предварительное создание пользователя
        User user = new User(email, password, name);
        Response responseForDeletion = create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForDeletion.path("accessToken").toString();

        //проверка повторного создания пользователя
        Response response = create(user).then().assertThat().statusCode(403).extract().response();
        String actual = response.path("message").toString();
        String expected = "User already exists";
        Assert.assertEquals(actual, expected);

        //логин и удаление пользователя после теста
        loginUser(new Credentials(email, password));
        deleteUser(accessToken);
}

}
