package com.example;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDER_PATH = "/api/orders";
    private static final String INGREDIENT_PATH = "api/ingredients";
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Step("Создать заказ с авторизацией")
    public static Response createOrderWithAuth(Order order, String accessToken) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Создать заказ без авторизации")
    public static Response createOrderWithoutAuth(Order order) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получить заказы с авторизацией")
    public static Response getOrdersWithAuth(String accessToken) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("authorization", accessToken)
                .when()
                .get(ORDER_PATH);
    }

    @Step("Получить заказы без авторизации")
    public static Response getOrdersWithoutAuth() {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_PATH);
    }

    @Step("Получить данные ингредиентов")
    public static List<String> getIngredientsData() {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get(INGREDIENT_PATH)
                .then()
                .extract()
                .path("data._id");
    }
}
