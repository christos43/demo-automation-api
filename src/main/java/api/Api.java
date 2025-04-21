package api;
// αντιγραφο του api/CwmApi.java ->

import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Test;
import utils.enums.Application;
import utils.enums.User;

import java.util.Map;

public class Api {
    private static final String PATH_AUTH = "/auth";
    private static final String ACCESS_TOKEN_KEY = "token";

    final Test test;
    Response response;


    public Api(Test test) {
        this.test = test;
    }

    public String authenticateUser(User user) {
        String authUrl = test.domainConfig().getUrl(Application.RESTFULL_BOOKER) + PATH_AUTH;

        Map<String, String> credentials = Map.of(
                "username", test.domainConfig().getUsername(user),
                "password", test.domainConfig().getPassword(user));

        test.waitFor().expectedCondition(() -> {
            response = apiGenericPostJson(authUrl, credentials);
            return response.getStatusCode() == 200;
        });

        return response.jsonPath().getString(ACCESS_TOKEN_KEY);   //assumes all targeted domains return "token" as access token key
    }

    public Response apiGenericPostJson(String url, Map<String, String> params) {
        return RestAssured.given().relaxedHTTPSValidation().accept(ContentType.JSON).params(params)
                .log().all().post(url).then().log().all().extract().response();
    }

    public Response apiGenericPostJson(String url, String body, String token) {
        return RestAssured.given().auth().oauth2(token).relaxedHTTPSValidation().baseUri(url).contentType(ContentType.JSON)
                .body(body).log().all().post().then().log().all().extract().response();
    }

    public Response apiGenericGetJson(String url, Map<String, String> params, String token) {
        return RestAssured.given().auth().oauth2(token).relaxedHTTPSValidation().params(params).baseUri(url).contentType(ContentType.JSON)
                .log().all().post().then().log().all().extract().response();
    }


}
