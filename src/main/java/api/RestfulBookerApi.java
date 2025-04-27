package api;
// αντιγραφο του api/CwmApi.java ->

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Booking;
import dto.BookingDates;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import utils.Test;
import utils.enums.Application;
import utils.enums.User;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

public class RestfulBookerApi {
    private static final String PATH_AUTH = "/auth";
    private static final String PATH_BOOKING = "/booking";
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

    public void createBooking(DataTable bookingDetails) throws JsonProcessingException {

        String createBookingUrl = test.envDataConfig().getUrl(Application.RESTFULL_BOOKER) + PATH_BOOKING;

        Map<String, String> bookingMap = bookingDetails.asMap(String.class, String.class);

        BookingDates bookingDates = new BookingDates()
                .setCheckIn(bookingMap.get("Check-In"))
                .setCheckOut(bookingMap.get("Check-Out"));

        Booking booking = new Booking()
                .setFirstName(bookingMap.get("First Name"))
                .setLastName(bookingMap.get("Last Name"))
                .setTotalPrice(Double.parseDouble(bookingMap.get("Total Price")))
                .setDepositPaid(Boolean.parseBoolean(bookingMap.get("Deposit Paid")))
                .setBookingDates(bookingDates);

        if (bookingMap.containsKey("Additional Needs")) {
            String needs = bookingMap.get("Additional Needs");
            booking.setAdditionalNeeds(List.of(needs.split(",")));
        }

        Response response = test.api().restfulBookerApi().apiGenericPostJson(createBookingUrl, new ObjectMapper().writeValueAsString(booking), test.context().getAuthToken());
        response.then().statusCode(HttpStatus.SC_OK);
    }

    public void verifyExistingBooking(DataTable bookingDetails) throws JsonProcessingException {

//        Booking registeredBooking = test.context().getBooking();
//        Booking retrievedbooking = test.api().restfulBookerApi().getBookingByLastName();

//        assertThat(retrievedBooking) //AssertJ magic
//                .usingRecursiveComparison()
//                .ignoringFields("additionalNeeds")
//                .isEqualTo(registeredBooking);

        //        test.waitFor().expectedCondition(() ->
//                new HashSet<>(booking)
//                        .containsAll(dataTable.asList()));
//
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
