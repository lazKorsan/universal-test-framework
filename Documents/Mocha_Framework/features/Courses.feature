Feature: Courses API

  As a user, I want to interact with the courses endpoint to retrieve course information.

  Scenario: Successfully retrieve the list of all courses
    Given I am an authenticated user
    When I send a GET request to "/api/courses"
    Then the response status code should be 200
