# API Design Assignment

## Project Name

*UFOAPI*

## Objective

Design and develop a robust, well-documented API (REST or GraphQL) that allows users to retrieve and manage information from a dataset of your choice. The API must include JWT authentication, automated testing via Postman/Newman in a CI/CD pipeline, and be publicly deployed.

Choose a dataset (10000+ data points) that interests you — it should include at least one primary CRUD resource and two additional read-only resources. Sources like [Kaggle](https://www.kaggle.com/datasets), public APIs, or CSV files work well. Pick something you find interesting, as you will reuse this API in the next assignment (WT dashboard).

## This API

The UFO Sightings API serves a dataset of reported UFO sightings, including details such as date, location, shape, duration, and comments.
Its main resources are:

**Sightings:** Records of individual UFO sightings, with full CRUD (Create, Read, Update, Delete) operations.

Users can filter, page, and retrieve detailed information about each sighting. Creating, updating, or deleting sightings requires authentication, but reading is public.

**Shapes:** Types of UFO shapes reported, available as a read-only resource.

**Locations:** Geographical locations associated with sightings, also read-only.

## Implementation Type

*REST*

## Links and Testing

| | URL / File                                          |
|---|-----------------------------------------------------|
| **Production API** | *https://ufoapi.andreaseriksson.me/*                |
| **API Documentation** | *https://ufoapi.andreaseriksson.me/swagger-ui.html* |
| **Postman Collection** | `postman/ufoapi.postman_collection.json`            |
| **Production Environment** | `postman/production.postman_environment.json`       |

**Examiner can verify tests in one of the following ways:**

1. **CI/CD pipeline** — check the pipeline output in GitHub for test results.
2. **Run manually** — no setup needed:
   ```
   npx newman run postman/ufoapi.postman_collection.json --environment postman/production.postman_environment.json
   ```

## Dataset

*Describe the dataset you chose:*

| Field | Description                                                 |
|---|-------------------------------------------------------------|
| **Dataset source** | *https://www.kaggle.com/datasets/NUFORC/ufo-sightings*                                                   |
| **Primary resource (CRUD)** | *Sightings (id, sightedAt, durationSeconds, durationText, comments, datePosted, city, state, countryCode, latitude, longitude, shapeId, locationId)* |
| **Secondary resource 1 (read-only)** | *Shapes (id, name, description)*                     |
| **Secondary resource 2 (read-only)** | *Locations (id, city, state, countryCode, latitude, longitude)*                            |


## Design Decisions

### Authentication

This API uses JWT (JSON Web Token) authentication: after login, the server issues a signed token that clients include in the Authorization header for protected requests.

This approach is stateless, scalable, and well-suited for REST APIs.

**Why JWT?**

Stateless (no server session storage needed).

Widely supported and easy to use across different clients.

Scalable for distributed/cloud deployments.

**Alternatives:**

Session-based auth: simpler for web apps, but not stateless or easily scalable.

OAuth2: more powerful and supports third-party logins, but more complex.

API keys: simple, but less secure and without user context.

JWT was chosen for its balance of security, scalability, and simplicity for modern APIs.

### API Design

HATEOAS (Hypermedia as the Engine of Application State) is implemented using Spring HATEOAS.

Each API response includes _links objects that provide URLs to related resources and actions.
For example, a sighting response includes links to itself, related shapes, and locations.

This allows clients to dynamically discover available actions and navigate the API without prior knowledge of its structure, making the API more self-descriptive and easier to use.

Resource URLs follow RESTful conventions:

- Main resources use plural nouns (e.g., /sightings, /shapes, /locations).

- Individual resources are accessed by ID (e.g., /sightings/{id}).

- Nested or related resources are linked via HATEOAS, not nested URLs.

HTTP methods are used according to REST best practices:

- GET for retrieving resources (200 OK, 404 Not Found if missing).

- POST for creating new resources (201 Created, with Location header).

- PUT for updating resources (200 OK or 204 No Content).

- DELETE for removing resources (204 No Content).

Proper status codes are returned for errors (e.g., 400 Bad Request, 401 Unauthorized, 403 Forbidden).

This structure ensures clarity, predictability, and adherence to REST principles.

### Error Handling

*How does your API handle errors? Describe the format and consistency of your error responses.*

The API returns errors in a consistent JSON format with fields like timestamp, status, error, and message.

HTTP status codes match the error type (e.g., 400, 401, 404).

All endpoints use this format, making it easy for clients to handle errors.

Example:

```java
{
  "timestamp": "2026-03-25T12:34:56.789+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Sighting not found"
}
```

## Core Technologies Used
**Spring Boot**

Java framework for building RESTful APIs. Chosen for its rapid development capabilities, strong ecosystem, and seamless integration with Spring Data, Security, and OpenAPI.

**Spring Data JPA**

Provides easy integration with relational databases using JPA (Java Persistence API). Chosen for its ability to simplify database access and reduce boilerplate code.

**Spring Security**

Handles authentication and authorization, including JWT-based security. Chosen for its flexibility, robustness, and deep integration with the Spring ecosystem.

**JWT (JSON Web Token)**

Used for stateless authentication. Chosen for its scalability, statelessness, and suitability for REST APIs.

**HATEOAS (Hypermedia as the Engine of Application State)**

API responses with navigational links. Chosen to make the API more discoverable and self-descriptive.

**OpenAPI (Swagger)**

Used for API documentation and interactive testing. Chosen for its ability to generate clear, interactive docs.

**Postman/Newman**

Used for API testing and automated test runs. Chosen for its popularity, ease of use, and integration with CI/CD pipelines.

**Docker**

Used for containerizing the application and its dependencies. Chosen for portability, consistency across environments, and ease of deployment.

## Reflection

**What was hard?**

Getting authentication and error handling right took some trial and error. Making sure the API was both secure and easy to use was a challenge.

**What did you learn?**

I learned how to use JWT for stateless authentication, how HATEOAS can make APIs easier to explore, and how important clear documentation and consistent error responses are.

**What would you do differently?**

Next time, I’d plan the error handling and documentation earlier, and set up more automated tests from the start. I might also look into OAuth2 if I needed more advanced authentication.
I would also separate the Swagger documentation from the Controller-classes so they get less bloated.

## Requirements

See [all requirements in Issues](../../issues/). Close issues as you implement them. Create additional issues for any custom functionality. See [TESTING.md](TESTING.md) for detailed testing requirements.

### Functional Requirements — Common

| Requirement | Issue | Status |
|---|---|---|
| Data acquisition — choose and document a dataset (1000+ data points) | [#1](../../issues/1) | ✅ |
| Full CRUD for primary resource, read-only for secondary resources | [#2](../../issues/2) | ✅ |
| JWT authentication for write operations | [#3](../../issues/3) | ✅ |
| Error handling (400, 401, 404 with consistent format) | [#4](../../issues/4) | ✅ |
| Filtering and pagination for large result sets | [#17](../../issues/17) | ✅|

### Functional Requirements — REST

| Requirement | Issue | Status |
|---|---|---|
| RESTful endpoints with proper HTTP methods and status codes | [#12](../../issues/12) | ✅ |
| HATEOAS (hypermedia links in responses) | [#13](../../issues/13) | ✅ |

### Non-Functional Requirements

| Requirement | Issue | Status |
|---|---|---|
| API documentation (Swagger/OpenAPI or Postman) | [#6](../../issues/6) | ✅ |
| Automated Postman tests (20+ test cases, success + failure) | [#7](../../issues/7) | ✅ |
| CI/CD pipeline running tests on every commit/MR | [#8](../../issues/8) | ✅ |
| Seed script for sample data | [#5](../../issues/5) | ✅ |
| Code quality (consistent standard, modular, documented) | [#10](../../issues/10) | ✅ |
| Deployed and publicly accessible | [#9](../../issues/9) | ✅ |
| Peer review reflection submitted on merge request | [#11](../../issues/11) |  |


