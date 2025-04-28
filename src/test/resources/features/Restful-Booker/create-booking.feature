Feature: User creates new booking

  Background:
    Given user logs in to Restful Booker Api

  Scenario: User creates new booking
    When they create a new booking with details
      | First Name       | James                |
      | Last Name        | Bach                 |
      | Total Price      | 540                  |
      | Deposit Paid     | true                 |
      | Check-In         | 2022-07-11           |
      | Check-Out        | 2022-07-15           |
      | Additional Needs | Breakfast,Daily Mail |
    Then they verify booking is created