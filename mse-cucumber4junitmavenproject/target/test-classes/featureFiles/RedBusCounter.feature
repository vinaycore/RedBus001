Feature: Number of Bus availability between two cities 

Scenario: This is first scenario
    Given User opens the RebBus homePage Url
    When User enters SourceLocation,DestinationLocation,DateOfJourney and Hits Search button
    Then User is navigated to BusAvailability Page
    