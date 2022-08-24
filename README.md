# `salesforce-integration-service` Repository

[![pipeline status](https://gitlab.com/johnjvester/salesforce-integration-service/badges/master/pipeline.svg)](https://gitlab.com/johnjvester/salesforce-integration-service/commits/master)


> The `salesforce-integration-service` repository is very basic [Spring Boot](https://spring.io/projects/spring-boot) RESTful
> API which is designed to act as a middleware component to communicate with a [Salesforce](http://www.salesforce.com) instance 
> in order to retrieve (GET) and update (PATCH) `Contact` objects.

## Publications

This repository is related to an article published on DZone.com:

* TBD

To read more of my publications, please review one of the following URLs:

* https://dzone.com/users/1224939/johnjvester.html
* https://johnjvester.gitlab.io/dZoneStatistics/WebContent/#/stats?id=1224939

## Important Information

The following environment settings need to be configured:

* `${SF_USER_NAME}` (`salesforce.username`) - the username configured in Salesforce to utilize
* `${SF_PASSWORD}` (`salesforce.password`) - the password (excluding security token) for the username provided above
* `${SF_CONSUMER_KEY}` (`salesforce.consumer-key`) - the Salesforce Connected App consumer key value (see "Salesforce Connected App" section for more details)
* `${SF_CONSUMER_SECRET}` (`salesforce.consumer-secret`) - the Salesforce Connected App consumer secret value (see "Salesforce Connected App" section for more details)
* `${PORT}` (`server.port`) - port number for Spring Boot service

## Salesforce Connected App

A Connected App in Salesforce is required to allow access to the Salesforce instance.  Below, are instructions 
which were followed at the time this repository was created:

1. Create a free Developer account at https://developer.salesforce.com/signup
2. Navigate to the *Setup* link
3. Navigate to *Apps | Apps Manager* section on the left-hand menu
4. Select the *New Connected App* button
5. Populate the following properties:
   1. Connected App Name 
   2. API Name (computed value should be fine)
   3. Contact Email (your email address)
   4. API | Enable OAuth Settings = true
   5. Set callback URL to be "https://login.salesforce.com/"
   6. Use OAuth scopes "Access and manage your data (api)" and "Perform requests on your behalf at any time (refresh_token, offline_access)" (for now)
   7. Use "Relax IP restrictions" (for now)
   8. Use "Refresh token is valid until revoked" (for now)
6. Save the new Connected App
7. Navigate to *Security | Network Access*
8. Create a new Trusted IP Range which includes your current IP address

## The Contact API

For this example, only the `Contact` object in Salesforce will be utilized, which will focus on the following attributes:

```java
public class Contact {
    private String id;
    private String name;
    private String title;
    private String department;

    private SalesforceAttributes attributes;
}

public class SalesforceAttributes {
    private String type;
    private String url;
}
```

The following URIs exist with this service:

* `GET - /contacts` - returns a list of `Contact` objects from the connected Salesforce instance
* `GET - /contacts/{id}` - returns a single `Contact` object for the provided `id` from the connected Salesforce instance
* `PATCH - /contacts/{id}` - performs the necessary updates and returns the updated object for the following `PatchUpdates` object:

```java
public class PatchUpdates extends HashMap<String, String> { }
```

### GET Contacts

Below, is an example of a `GET` request to retrieve all `Contact` objects in Salesforce:

```shell
curl --location --request GET 'http://localhost:9999/contacts'
```

An HTTP `200` (OK)  response will be returned, along with a full list of `Contact` entries:

```json
[
    {
        "attributes": {
            "type": "Contact",
            "url": "/services/data/v52.0/sobjects/Contact/0035e000008eXq0AAE"
        },
        "id": "0035e000008eXq0AAE",
        "Name": "Rose Gonzalez",
        "Title": "SVP, Procurement",
        "Department": "Procurement"
    },
    // additional items here
    {
        "attributes": {
            "type": "Contact",
            "url": "/services/data/v52.0/sobjects/Contact/0035e000008eXqJAAU"
        },
        "id": "0035e000008eXqJAAU",
        "Name": "Jake Llorrac",
        "Title": null,
        "Department": null
    }
]
```

### GET Single Contact

Below, is an example of `GET` request for a single `Contact` (using values from the list above):

```shell
curl --location --request GET 'http://localhost:9999/contacts/0035e000008eXq0AAE'
```

An HTTP `200` (OK) response will be returned, along with the requested `Contact` entry:

```json
{
    "attributes": {
        "type": "Contact",
        "url": "/services/data/v52.0/sobjects/Contact/0035e000008eXq0AAE"
    },
    "id": "0035e000008eXq0AAE",
    "Name": "Rose Gonzalez",
    "Title": "SVP, Procurement",
    "Department": "Procurement"
}
```

### PATCH Contact

Below, is an example of a `PATCH` request using cURL:

```shell
curl --location --request PATCH 'http://localhost:9999/contacts/0035e000008eXq0AAE' \
--header 'Content-Type: application/json' \
--data-raw '{
    "Title": "SVP, Procurement 2"
}'
```

An HTTP `202` (Accepted) response will be returned, along with the updated `Contact` entry:

```json
{
    "attributes": {
        "type": "Contact",
        "url": "/services/data/v52.0/sobjects/Contact/0035e000008eXq0AAE"
    },
    "id": "0035e000008eXq0AAE",
    "Name": "Rose Gonzalez",
    "Title": "SVP, Procurement 2",
    "Department": "Procurement"
}
```

## Caching

This repository employs the very basic `spring-boot-starter-cache` in order to reduce the need to make API calls to Salesforce.

To keep things simple, when the `PATCH` URI (noted above) is called, the entire cache will be evicted.

## SSE Functionality

For those clients interested in keeping track of `Contact` updates as they are processed by this service, a contact events stream 
URI is available, which provides server-sent event (SSE) updates.  

To leverage this functionality, set up the appropriate SSE listener to listen for events at the following URI:

* `GET - /stream/{sessionId}` - where `sessionId` is a unique identifier for a client session accessing this API.

Below, is an example of a cURL command to initiate a new SSE listener:

```shell
curl -N --http2 -H "Accept:text/event-stream" http://localhost:9999/stream/cc2798a5-de4f-4916-83c3-52a8723b3c74
```

The Spring Boot service will respond with the following confirmation message in the logs:

```shell
2021-07-05 16:55:51.917 INFO 82905 --- [nio-9999-exec-1] c.g.j.s.controllers.StreamController     : Creating emitter for sessionId=cc2798a5-de4f-4916-83c3-52a8723b3c74
```

Once connected, `Contact` updates will be provided as they are patched to Salesforce.  Below, is an example from the same
`PATCH` event provided above:

```shell
data:{
data:  "attributes" : {
data:    "type" : "Contact",
data:    "url" : "/services/data/v52.0/sobjects/Contact/0035e000008eXq0AAE"
data:  },
data:  "id" : "0035e000008eXq0AAE",
data:  "Name" : "Rose Gonzalez",
data:  "Title" : "SVP, Procurement 2",
data:  "Department" : "Procurement"
data:}
```

## Additional Information

Made with <span style="color:red;">♥</span> &nbsp;by johnjvester@gmail.com, because I enjoy writing code.