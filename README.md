
# Event Management System

A full-stack **Event Management System** designed to manage and organize events, users, tickets, feedback, and announcements. Built using **Java**, **Spring Boot**, **Hibernate**, **React**, and **MySQL**. This application allows users to register for events, purchase tickets, provide feedback, and allows event organizers to manage and announce details about their events.

## Table of Contents

- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
  - [Frontend Setup](#frontend-setup)
  - [Backend Setup](#backend-setup)
- [API Endpoints](#api-endpoints)
- [Running the Application](#running-the-application)
- [Testing](#testing)
  - [Unit Tests](#unit-tests)
  - [Black-box Testing (Selenium)](#black-box-testing-selenium)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

This application is designed to provide a platform for users to interact with events. Users can:

- Register for events.
- Purchase tickets.
- Provide feedback on events.
- View and access announcements for specific events.

Organizers can:

- Create and manage events.
- View registered participants and tickets sold.
- Post announcements for their events.

## Technologies Used

- **Frontend**:
  - **React**: For building the dynamic, responsive user interface.
  - **Bootstrap**: For UI components and styling.
  
- **Backend**:
  - **Java**: Core programming language for backend development.
  - **Spring Boot**: Used for building the backend APIs and handling the application's business logic.
  - **Hibernate**: For object-relational mapping and interaction with MySQL.
  - **MySQL**: For managing relational data.

- **Testing**:
  - **JUnit**: For writing unit tests for backend logic.
  - **Selenium**: For black-box testing of the frontend application.

## Setup Instructions

Follow the steps below to get the project up and running on your local machine.

### Frontend Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/event-management-system.git
   cd event-management-system/frontend
   ```

2. **Install dependencies:**
   Make sure you have **Node.js** and **npm** installed on your machine. If not, download and install them from [Node.js official website](https://nodejs.org/).

   Run the following command to install the required dependencies:
   ```bash
   npm install
   ```

3. **Run the development server:**
   Start the frontend development server by running:
   ```bash
   npm start
   ```

   This will run the app in development mode and open it on [http://localhost:3000](http://localhost:3000).

### Backend Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/event-management-system.git
   cd event-management-system/backend
   ```

2. **Install dependencies:**
   Make sure you have **Java 17+** installed. If not, download and install it from [AdoptOpenJDK](https://adoptopenjdk.net/).

   To install Maven (for dependency management), follow the instructions from the official [Maven website](https://maven.apache.org/install.html).

   Run the following command to install dependencies:
   ```bash
   mvn install
   ```

3. **Setup the database:**
   - Make sure **MySQL** is installed on your machine.
   - Create a database in MySQL (e.g., `event_management_db`).
   - Update the `application.properties` file located in `src/main/resources` with your MySQL connection details.

4. **Run the application:**
   You can now start the backend by running:
   ```bash
   mvn spring-boot:run
   ```

   The backend will be available at [http://localhost:8080](http://localhost:8080).

## API Endpoints

Here is a summary of the key API endpoints in the system:

### User Endpoints
- **POST /users/register**: Register a new user.
- **POST /users/login**: Log in with user credentials.
  
### Event Endpoints
- **POST /events**: Create a new event (Organizers only).
- **GET /events**: Fetch all events.
- **GET /events/{id}**: Fetch details of a specific event.
  
### Ticket Endpoints
- **POST /tickets**: Buy a ticket for an event.
- **GET /tickets/{id}**: Fetch details of a specific ticket.

### Feedback Endpoints
- **POST /feedback**: Submit feedback for an event.

### Announcement Endpoints
- **POST /announcements**: Post an announcement for an event.

## Running the Application

After setting up the backend and frontend:

1. **Start the backend**:
   - Use the `mvn spring-boot:run` command in the `backend` folder.
   
2. **Start the frontend**:
   - Use `npm start` in the `frontend` folder.

Once both the backend and frontend are running, you can access the application at `http://localhost:3000` for the frontend and `http://localhost:8080` for the backend API.

## Testing

### Unit Tests

Unit tests for the backend are written using **JUnit**. To run the tests, use the following Maven command in the `backend` folder:

```bash
mvn test
```

### Black-box Testing (Selenium)

We are using **Selenium** for black-box testing the frontend. Make sure to install **Selenium** and related dependencies on your machine. If you’re unfamiliar with Selenium, follow this guide to get started:

1. **Install dependencies**:
   ```bash
   npm install selenium-webdriver
   ```

2. **Run Selenium tests**:
   - Set up your tests in the `tests/` folder inside the frontend directory.
   - Execute the test scripts using your chosen test runner (e.g., Jest or Mocha).

## Contributing

Contributions are welcome! Please fork the repository, create a feature branch, and submit a pull request. Ensure your code is properly tested before submitting. If you encounter any issues, feel free to open an issue in the GitHub repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
