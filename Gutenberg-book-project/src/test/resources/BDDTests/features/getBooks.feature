Feature: Can I get all the books?
  We want to know when we have the books

  Scenario: Find all books
    Given we want all the books
    When we get connection to the database
    Then I should be told "Got all the books from the database"
