# Policy Planner: A Vehicle Insurance Calculator Application

# Table of Contents
- [Policy Planner: An Insurance Calculator Application](#policy-planner-an-insurance-calculator-application)
- [Table of Contents](#table-of-contents)
- [About](#about)
  - [Introduction](#introduction)
  - [Highlights](#highlights)
  - [Challenges](#challenges)
- [Running the Application](#running-the-application)
  - [Prerequisites](#prerequisites)
  - [Option 1: Docker (recommended)](#option-1-docker-recommended)
  - [Option 2: Locally](#option-2-locally)
- [Miscellaneous](#miscellaneous)
  - [Folder Structure](#folder-structure)
  - [Support](#support)

# About
This is a web application built with Spring Boot and Angular that provides an insurance premium calculation service. It allows users to input their vehicle and driving history information to get estimated annual and monthly insurance premiums.

## Introduction
I enjoyed working on this project over the past few days and it was a good refresher on some technologies I was already familiar with. I got a better understanding on designing better user experiences as well as functional but clean system design. My goal when working on this project was to focus on user experience and clean system design. Instead of implementing as many features as possible, I focused on optimizing the core features and improving ease of use. I believe I was able to reasonably achieve my goal for this project and expanded upon the original requirements in a meaningful way.

## Highlights
Apart from the core requirements, some of the additional enhancements include:
- Viewing previously created quotes by their reference id
- Printing a generated quote
- Refreshing the page without losing your generated quote
- Accessing quotes directly through a url
- Error handling with appropriate error messages shown
- Links to email specialists or support team for general inquiries
- Timestamp of when the quote was created
- Buttons to redirect you to the next appropriate location at every page
- Home, contact, and not found pages

## Challenges
The main challenge I faced was deciding the core structure of the Driver and Quote entities. Initially, I spent a lot of time experimenting with the idea of using a One to Many relationship between drivers and quotes. My main reasoning for this was that it would allow accessing all the quotes made by a driver. Although this sounded tempting as a feature, I ultimately decided to not go with this approach. The user would likely be confused why they have to provide both their license number and their driving record/history. Instead, I decided to maintain a slightly independent relationship between drivers and quotes. If the user can look up their quotes by reference id, that still largely accomplishes the same goal. I found that this was the best of both worlds where the user experience is streamlined and sensitive information is protected.

Another challenge was organizing lots of input fields on a single page. Since I had been using Angular Material for a long time, I decided to go with bootstrap for this project. Although bootstrap is great, I found that it isn't ideal for designing larger forms. My solution then, was to make better use of spacing by grouping up fields into categories, to better organize the long list of inputs.

# Running the Application
There are multiple options for running the application: using Docker or running it locally.

Note: the application can also be quickly accessed via https://policy-planner.vercel.app

## Prerequisites
Before running the application, make sure you have the following installed on your machine:
- [Java Development Kit (JDK) 17 or later](https://www.oracle.com/ca-en/java/technologies/downloads/#java17)
- [Node.js and npm (Node Package Manager)](https://nodejs.org/en)
- [Docker (to easily run the application)](https://www.docker.com/products/docker-desktop/)

## Option 1: Docker (recommended)
To run the application using Docker, follow these steps:

**1. Launch the Docker application**

**2. Clone the repository to your local machine:**
```
git clone https://github.com/hershk17/insurance-calculator.git
```

**3. Navigate to the project's root directory:**
```
cd insurance-calculator
```

**4. Build the Docker image:**
```
docker-compose up --build
```

**5. Access the application by opening a web browser and visiting http://localhost:4200**

## Option 2: Locally
To run the application locally, you need to start both the backend (Spring Boot) and frontend (Angular) applications.

**1. Clone the repository to your local machine:**
```
git clone https://github.com/hershk17/insurance-calculator.git
```

**2. Navigate to the backend directory:**
```
cd insurance-calculator/insurance-rest-service
```

**3. Build and run the Spring Boot application:**
```
./mvnw spring-boot:run
```
This will start the backend server on http://localhost:8080

**4. In a new terminal window, navigate to the frontend directory:**
```
cd insurance-calculator/insurance-web-app
```

**5. Install the necessary dependencies:**
```
npm install
```

**6. Start the Angular development server:**
```
ng serve
```
This will start the frontend application on http://localhost:4200

**7. Access the application by opening a web browser and visiting http://localhost:4200**

# Miscellaneous

## Folder Structure
Below is the breakdown of the project directory and where important folders and files can be found.
```
.insurance-calculator    // root directory
|
├── insurance-rest-service
│   ├── src
│   │   └── main
│   │       └── java/com/example/insurancerestservice
│   │           ├── controller     // rest controllers to handle api endpoints
│   │           ├── entity         // entities used for data handling
│   │           ├── repository     // Jpa repositories for entities
│   │           ├── service        // service layer for business logic
│   │           └── Application.java
│   ├── DockerFile
│   └── pom.xml
│
├── insurance-web-app
│   ├── src
│   │   ├── app
│   │   │   ├── components      // contains core components of the application
│   │   │   ├── models          // models/interfaces used for handling data
│   │   │   └── services        // services for making http requests
│   │   ├── assets
│   │   │   └── index.html
│   │   ├── app-routing.module.ts    // module which defines routes used
│   │   └── app.module.ts            // imports components and modules
│   ├── .gitignore
│   ├── Dockerfile       
│   └── package.json
│
├── docker-compose.yml   // docker compose file for containerizing applications
└── README.md            // contains project information and usage instructions
```

## Support
If you encounter any issues running this application, please reach out to me via email at hersh.k17@gmail.com. <br>
**Thank you!**
