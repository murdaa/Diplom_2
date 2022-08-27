package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GetListOfOrdersTest extends OrderClient {

    @Test
    @DisplayName("Get list of orders for user with auth")
    @Description("To verify getting a list of orders with auth")
    public void gettingListOfOrdersWithAuthTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста создания заказа
        User user = new User(email, password, name);
        Response responseForUser = UserClient.create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForUser.path("accessToken").toString();

        //получение ингредиентов для заказа и создание заказа
        List<String> listOfIngredients = getIngredientsData();
        Order order = new Order(Arrays.asList(listOfIngredients.get(0), listOfIngredients.get(1)));
        createOrderWithAuth(order, accessToken).then().assertThat().statusCode(200);

        //получение списка заказов для пользователя
        Response response = getOrdersWithAuth(accessToken).then().assertThat().statusCode(200).extract().response();
        String actual = response.path("success").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        UserClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Get list of orders for user without auth")
    @Description("To verify getting a list of orders without auth")
    public void gettingListOfOrdersWithoutAuthTest() {

        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(8);
        String name = RandomStringUtils.randomAlphanumeric(8);

        //создание пользователя для теста создания заказа
        User user = new User(email, password, name);
        Response responseForUser = UserClient.create(user).then().assertThat().statusCode(200).extract().response();
        String accessToken = responseForUser.path("accessToken").toString();

        //получение ингредиентов для заказа и создание заказа
        List<String> listOfIngredients = getIngredientsData();
        Order order = new Order(Arrays.asList(listOfIngredients.get(0), listOfIngredients.get(1)));
        createOrderWithAuth(order, accessToken).then().assertThat().statusCode(200);

        //получение списка заказов для пользователя
        Response response = getOrdersWithoutAuth().then().assertThat().statusCode(401).extract().response();
        String actual = response.path("message").toString();
        String expected = "You should be authorised";
        Assert.assertEquals(actual, expected);

        //удаление пользователя после теста
        UserClient.deleteUser(accessToken);
    }
}
