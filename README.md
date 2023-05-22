# SpringZoom

### Abstract

The SpringZoom Meeting Management Application is a web-based platform that allows users to schedule and manage meetings with other users. The application provides functionalities to create, update, and delete meetings, as well as manage user contacts for easy meeting invitations. The backend of the application is developed using Spring Boot, while the frontend is built using React.js.

The backend API consists of two main controllers: UserController and MeetingController. The UserController handles user-related operations such as user registration, login, user profile management, and managing user contacts. The MeetingController handles meeting-related operations such as creating new meetings, updating meeting details, retrieving meetings by user email, and deleting meetings.

The application uses a MySQL database to store user and meeting information. User and Meeting entities are defined and mapped to corresponding database tables using Spring Data JPA. The application ensures data integrity and validation through input validation and error handling.

The frontend of the application is developed using React.js and interacts with the backend API through RESTful API calls. The frontend provides a user-friendly interface for users to create meetings, view their upcoming meetings, update meeting details, and manage their contacts. The UI components are designed to be intuitive and responsive, providing a seamless user experience.

The project is containerized using Docker, allowing for easy deployment and scalability. The Docker Compose file is provided to set up the development environment, including the backend, frontend, and MySQL database containers.

The application can be deployed to AWS using services like Amazon ECS (Elastic Container Service) or Amazon EKS (Elastic Kubernetes Service) for container orchestration. Deployment pipelines can be set up using AWS CodePipeline and AWS CodeBuild to automate the build, test, and deployment processes.

The SpringZoom Meeting Management Application provides a reliable and efficient solution for scheduling and managing meetings, enhancing productivity and collaboration among users.

### Team

Aluno Marcel Santos Souza - 101043

### Project Bookmarks:

- CI (Github Actions): [https://github.com/MarcelSSouza/SpringZoom/actions]()
- SonarQube Static Code Analisis: **[https://sonarcloud.io/summary/overall?id=MarcelSSouza_SpringZoom]()**
- Documentation of the API: **[localhost:8080/swagger-ui/index.html]()**
- Docker Compose: Inside the project folder. All running inside docker.

### How to run?

**Just run docker-compose up and access the localhost:3000 (ReactJS)**
