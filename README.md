# MoneyTransferAPI
Simple Spring boot application which provide RESTful API that performs finds transfer between two accounts with currency exchange.

### Prerequisite
- Maven
- JDK 11+
### Project Structure
```bash
MoneyTransferAPI
├── src
│   ├── main
│   │   ├── java\com\fundtransfer\fundtransferservice
│   │   └── resources
│   └── test
│       ├── java\com\fundtransfer\fundtransferservice
│       └── resources
├── .gitignore
├── pom.xml
└── README.md
```
### Packaging
```
mvn package
```
### Test
```
mvn test
```
### Running
```
java -jar fund-transfer-service-0.0.1-SNAPSHOT.jar
```
### Data
Initial data (src\main\resources\data.sql) will be loaded in the H2 database when application start.
> INSERT INTO ACCOUNT (account_id, account_balance, account_currency) VALUES
>(1, 100, 'EUR'),
>(2, 100, 'USD'),
>(3, 5000, 'INR'),
>(4, 500, 'EUR');
## Feature
This application is for demo only. It provides APIs for following 2 features
- Retrieve Account Balance
- Create Transaction
### Basic API Information
| Method | Path | Usage |
| --- | --- | --- |
| GET | /v1/accounts/{id}/balances | retrieve account balance |
| POST | /v1/transaction | create transaction |

### Currency exchange info fetch API
To get the exchange rate real time open API is used  [ExchageRate-api](https://www.exchangerate-api.com/docs/free)


### Swagger-UI
API Specification is provided in the [swagger-ui page](http://localhost:8080/swagger-ui/) after spring boot application start.
```
http://localhost:8080/swagger-ui/
```
### Error Code
| Code | Description |
| --- | --- |
| ERR_SYS_001 | used when internal server error happens |
| ERR_SYS_002 | used when gateway timeout happens (e.g. calling external services) |
| ERR_CLIENT_001 | used when error due to client's input or business logic |
| ERR_CLIENT_002 | used when error related to account logic |

### Library used
| Library | Usage                                                     |
| --- |-----------------------------------------------------------|
| spring boot | for spring boot application                               |
| spring data jpa | for ORM and DB operation purpose                          |
| H2 | lightweight database for demo purpose                     |
| springfox-boot-starter| generate swagger API specification from code and UI page  |
| spring-boot-starter-actuator |  HTTP endpoints to expose operational information about any running application     |
| spring-cloud-starter-openfeign | feign client for access the exchage rate                  |

