# User_Story_Api

This is a sample project for managing transactions.

## Description

The project provides REST APIs for managing transactions. Users can create, read, update and delete transactions through the provided endpoints. Each transaction has a client associated with it, and clients can be managed through another set of endpoints.

## Getting Started
To get started with this project, follow these steps:

1. Clone the repository to your local machine.
2. Install `Java` and `Gradle` if you haven't already.
3. Configure the database in `application.properties` with your preferred database provider.
4. Open a terminal and navigate to the root directory of the project.
5. Run the command `gradle build` to build the project.
6. Run the command `gradle run` to start the application.

## Usage

The API has the following endpoints:

### Clients

* `GET /clients`: Retrieves a list of all clients.
* `GET /clients/{id}`: Retrieves a single client by ID.
* `POST /clients`: Creates a new client.
* `PUT /clients/{id}`: Updates an existing client.
* `DELETE /clients/{id}`: Deletes an existing client.

### Transactions
* `GET /transactions/{id}`: Retrieves a single transaction by ID.
* `GET /transactions/client/{clientId}`: Retrieves a list of transactions of a specific client.
* `POST /transactions`: Creates a new transaction.
* `PUT /transactions/{id}`: Updates an existing transaction.
* `DELETE /transactions/{id}`: Deletes an existing transaction.

### Calculations
* `GET /calculation/calculateFee/{transactionId}`: Calculates the fee for a single transaction.
* `GET /calculation/calculateFee/calculateTotalFee/client/{clientId}`: Calculates the fee for all transactions of a particular client.

## Request/Response Formats

The APIs expect and return data in JSON format. The following is an example of the request body for creating a new client:
```shell
{
  "name": "John Doe",
  "email": "johndoe@example.com"
}
```
And this is an example of the response body for retrieving a client by ID:

```shell
{
  "id": 1,
            "createDate": "2023-04-18T19:37:30.380+00:00",
            "lastModifiedDate": "2023-04-18T19:37:30.381+00:00",
            "version": 0,
            "name": "John Doe",
            "email": "johndoe@example.com",
            "transactions": [
                {
                    "id": 1,
                    "createDate": "2023-04-18T20:17:03.570+00:00",
                    "lastModifiedDate": "2023-04-18T20:17:03.570+00:00",
                    "version": 0,
                    "type": "TRANSFER",
                    "description": "Something for something",
                    "amount": 100.0
                }
            ]
}
```


## Testing

To run the tests for this project, use the command `gradle test` in the root directory of the project. This will execute all the test cases and output the results to the console.