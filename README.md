This  application demonstrates how to  write unit tests for
spring boot rest API.
Here for mocking objects mockito has been used.
While  testing service layer, we have mocked repository layer since 
we only need to test service layer logic and not the repository layer logic.
For testing repository layer we are using H2 database.