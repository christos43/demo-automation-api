package api;
// αντιγραφο του api/CwmApi.java ->

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import utils.Test;
import utils.enums.Application;
import utils.enums.User;

import java.util.Map;

public class RestfulBookerApi {
    private static final String PATH_AUTH = "/auth";
    private static final String ACCESS_TOKEN_KEY = "token";

    final Test test;
    Response response;


    public RestfulBookerApi(Test test) {
        this.test = test;
    }

    public String authenticateUser(User user) throws JsonProcessingException {
        String authUrl = test.envDataConfig().getUrl(Application.RESTFULL_BOOKER) + PATH_AUTH;

        Map<String, String> credentials = Map.of(
                "username", test.envDataConfig().getUsername(user),
                "password", test.envDataConfig().getPassword(user));

        String body = new ObjectMapper().writeValueAsString(credentials);

        test.waitFor().expectedCondition(() -> {
            response = apiGenericPostJson(authUrl, body);
            return response.getStatusCode() == 200;
        });

        String token = response.jsonPath().getString(ACCESS_TOKEN_KEY);
        test.context().setAuthToken(token); //needs lombok plugin to work, not just the dependency in pom

        return token;
    }

    public void createBooking(DataTable bookingDetails) {
    }

    public Response apiGenericPostJson(String url, String body) {
        return RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body(body)
                .log().all().post(url).then().log().all().extract().response();
    }

    public Response apiGenericPostJson(String url, String body, String token) {
        return RestAssured.given().auth().oauth2(token).relaxedHTTPSValidation().contentType(ContentType.JSON)
                .body(body).log().all().post(url).then().log().all().extract().response();
    }

    public Response apiGenericGetJson(String url, Map<String, String> params, String token) {
        return RestAssured.given().auth().oauth2(token).relaxedHTTPSValidation().params(params).baseUri(url).contentType(ContentType.JSON)
                .log().all().post().then().log().all().extract().response();
    }


}
