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
import org.testng.Assert;
import utils.Test;
import utils.enums.Application;
import utils.enums.User;

import java.util.List;
import java.util.Map;


public class RestfulBookerApi {
    private static final String PATH_AUTH = "/auth";
    private static final String PATH_BOOKING = "/booking";
    private static final String PATH_BOOKING_ID = "/booking/.*";
    private static final String ACCESS_TOKEN_KEY = "token";
    private static final String PATH_REGEX = ".*";

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
        test.context().setAuthToken(token);   //needs lombok plugin to work, not just the dependency in pom

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

        Response response = test.api().restfulBookerApi().apiGenericPostJson(createBookingUrl, new ObjectMapper()
                .writeValueAsString(booking), test.context().getAuthToken());
        response.then().statusCode(HttpStatus.SC_OK);

        test.context().setBooking(booking);
    }

    public void verifyBookingDetails() {

        Booking registeredBooking = test.context().getBooking();
        int bookingId = test.api().restfulBookerApi().getBookingIdsByLastName(registeredBooking.getLastName()).jsonPath()
                .getInt("[0].bookingid");

        Booking retrievedBooking = test.api().restfulBookerApi().getSingleBookingById(bookingId);
        Assert.assertEquals(registeredBooking, retrievedBooking, "Booking details retrieved do not match registered instance");
    }

    private Booking getSingleBookingById(int bookingId) {

        return test.api().restfulBookerApi().apiGenericGetJson(test.envDataConfig().getUrl(Application.RESTFULL_BOOKER)
                + PATH_BOOKING_ID.replace(PATH_REGEX, Integer.toString(bookingId)), test.context().getAuthToken()).as(Booking.class);
    }

    private Response getBookingIdsByLastName(String lastName) {  // might return more than one booking
        Map<String, String> params = Map.of("lastname", lastName);

        return test.api().restfulBookerApi().apiGenericGetJson(test.envDataConfig().getUrl(Application.RESTFULL_BOOKER)
                        + PATH_BOOKING, params, test.context().getAuthToken());
    }

    public Response apiGenericPostJson(String url, String body) {
        return RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body(body)
                .log().all().post(url).then().log().all().extract().response();
    }

    public Response apiGenericPostJson(String url, String body, String token) {
        return RestAssured.given().auth().oauth2(token).relaxedHTTPSValidation().contentType(ContentType.JSON)
                .body(body).log().all().post(url).then().log().all().extract().response();
    }

    public Response apiGenericGetJson(String url, String token) {
        return RestAssured.given().auth().oauth2(token).relaxedHTTPSValidation().baseUri(url).contentType(ContentType.JSON)
                .log().all().get().then().log().all().extract().response();
    }
    public Response apiGenericGetJson(String url, Map<String, String> params, String token) {
        return RestAssured.given().auth().oauth2(token).relaxedHTTPSValidation().params(params).baseUri(url).contentType(ContentType.JSON)
                .log().all().get().then().log().all().extract().response();
    }


}
