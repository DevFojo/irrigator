## Irrigator

### Prerequisites
* Docker
* Any REST API client, preferably Postman

### Deploying using docker
To build the project, run the following command:
```shell
docker compose up
```
Then you can make a call to any of the endpoints.

### Endpoints
* Create land
    ```http request
    POST localhost:8080/api/lands
    Content-Type: application/json
  
    {
        "size": 123.4,
        "soilType": "SANDY"
    }
    ```
* Get land
    ```http request
    GET localhost:8080/api/lands/6d95ef93-7d22-48a2-9085-2a66394f0d88
    ```
* List lands
    ```http request
    GET localhost:8080/api/lands
    ```
* Update land
    ```http request
    PUT localhost:8080/api/lands/6d95ef93-7d22-48a2-9085-2a66394f0d88
    
    {
        "size": 123.4,
        "soilType": "SANDY"
    }
    ```
* Get schedule
    ```http request
    GET localhost:8080/api/schedules/6d95ef93-7d22-48a2-9085-2a66394f0d88
    ```
* List schedules
    ```http request
    GET localhost:8080/api/schedules
    ```
