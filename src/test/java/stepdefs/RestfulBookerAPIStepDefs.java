package stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.Test;
import utils.enums.User;


public class RestfulBookerAPIStepDefs {

    Test test;

    public RestfulBookerAPIStepDefs(Test test) {
        this.test = test;
    }

    @Given("user logs in to Restful Booker Api")
    public void userLogin() throws JsonProcessingException {
        test.api().restfulBookerApi().authenticateUser(User.ADMIN);
    }

    @When("they create a new booking with details")
    public void userCreatesBooking(DataTable bookingDetails) throws JsonProcessingException {
        test.api().restfulBookerApi().createBooking(bookingDetails);
    }

    @Then("they verify booking is created")
    public void verifyBooking() {
        test.api().restfulBookerApi().verifyBookingDetails();
    }

}