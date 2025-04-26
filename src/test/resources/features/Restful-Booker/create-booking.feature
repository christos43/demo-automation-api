Feature: User creates new booking

  Background:

  Scenario: User creates new booking
    Given user logs in to Restful Booker Api
    When they create a new booking with details
      | First Name | Last Name | Total Price | Check-In   | Check-Out  | Additional Needs      |
      | James      | Bach      | 540         | 2022-07-11 | 2022-07-15 | Breakfast, Daily Mail |
    Then they verify ?