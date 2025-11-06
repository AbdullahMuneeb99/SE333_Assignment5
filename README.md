# Assignment 5 – Unit, Mocking, and Integration Testing

## Build Badge
![Build Status](https://github.com/AbdullahMuneeb99/SE333_Assignment5/actions/workflows/SE333_CI.yml/badge.svg)

---

## Overview
In this project, I focus on automating testing and code quality checks using GitHub Actions, a continuous integration (CI) platform that runs builds and tests automatically.  
The goal is to maintain high code quality through unit testing, mocking, integration testing, and static analysis.

---

## Objectives
- Implement automated workflows with GitHub Actions
- Configure triggers (such as push or pull requests) that start workflows
- Define and organize jobs and steps within a CI pipeline
- Run tests automatically using Maven on Ubuntu runners
- Integrate Checkstyle for code style analysis and JaCoCo for test coverage reporting
- Upload analysis and coverage reports as GitHub Actions artifacts
- Maintain strong test coverage through specification-based and structural-based testing
- Include a workflow status badge in the README to visualize build status

---

## Part 1 – BarnesAndNoble Tests
- Implemented **specification-based** and **structural-based** tests for the `BarnesAndNoble` class.
- Used **Mockito** to mock dependencies (`BookDatabase`, `BuyBookProcess`).
- Verified correct price calculation, handling of unavailable books, and null inputs.
- Tests located in: `src/test/java/org/example/Barnes/BarnesAndNobleTest.java`

---

## Part 2 – GitHub Actions Workflow
- Added a CI workflow at `.github/workflows/SE333_CI.yml`
- Runs automatically on every push to the **main** branch.
- Performs:
    1. **Static analysis** with Maven Checkstyle (`validate` phase)
    2. **Unit testing** with JUnit and Mockito
    3. **Coverage reporting** with JaCoCo
- Uploads artifacts:
    - `checkstyle-result.xml`
    - `jacoco.xml`

---

## Part 3 – Amazon Tests
- Added **AmazonUnitTest.java** and **AmazonIntegrationTest.java** for the `org.example.Amazon` package.
- **Unit tests:** mock `ShoppingCart` and `PriceRule` to isolate `Amazon` logic.
- **Integration tests:** use a real in-memory **HSQLDB** via `ShoppingCartAdaptor` and `Database`.
- Verified:
    - Correct aggregation of pricing rules
    - Database reset behavior
    - Persistence of cart items

---

## Results
- All tests pass successfully (`mvn clean test`).
- **Checkstyle** and **JaCoCo** reports are generated automatically via GitHub Actions.
