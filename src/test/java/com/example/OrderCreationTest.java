package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OrderCreationTest extends OrderClient {

    @Test
    @DisplayName("Create order with auth")
    @Description("To verify order creation with auth")
    public void orderValidCreationTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста создания заказа
        User user = new User(email, password, name);
        Response responseForUser = UserClient.create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForUser.path("accessToken").toString();

        //получение ингредиентов для заказа
        List<String> listOfIngredients = getIngredientsData();

        //проверка создания заказа с авторизацией
        Order order = new Order(Arrays.asList(listOfIngredients.get(0), listOfIngredients.get(1)));
        Response response = createOrderWithAuth(order, accessToken).then().assertThat().statusCode(200).extract().response();
        String actual = response.path("success").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        UserClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create order without auth")
    @Description("To verify order creation without auth")
    public void orderInvalidCreationWithoutAuthTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста создания заказа
        User user = new User(email, password, name);
        Response responseForUser = UserClient.create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForUser.path("accessToken").toString();

        //получение ингредиентов для заказа
        List<String> listOfIngredients = getIngredientsData();

        //проверка создания заказа с авторизацией
        Order order = new Order(Arrays.asList(listOfIngredients.get(0), listOfIngredients.get(1)));
        Response response = createOrderWithoutAuth(order).then().assertThat().statusCode(200).extract().response();
        String actual = response.path("success").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        UserClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("To verify invalid order creation without ingredients")
    public void orderInvalidCreationWithoutIngredientsTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста создания заказа
        User user = new User(email, password, name);
        Response responseForUser = UserClient.create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForUser.path("accessToken").toString();

        //проверка создания заказа с авторизацией
        Order order = new Order(Arrays.asList(null, null));
        Response response = createOrderWithAuth(order, accessToken).then().assertThat().statusCode(400).extract().response();
        String actual = response.path("message").toString();
        String expected = "One or more ids provided are incorrect";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        UserClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create order with wrong ingredients' id")
    @Description("To verify invalid order creation with wrong ingredients' id")
    public void orderInvalidCreationWithWrongIngredientsIdTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста создания заказа
        User user = new User(email, password, name);
        Response responseForUser = UserClient.create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForUser.path("accessToken").toString();

        //проверка создания заказа с авторизацией
        Order order = new Order(Arrays.asList("007", "007"));
        createOrderWithAuth(order, accessToken).then().assertThat().statusCode(500);

        //удаление пользователя после теста
        UserClient.deleteUser(accessToken);
    }
}
