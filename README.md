# Documentation for the API

# Meeting Controller API Documentation

The Meeting Controller API provides endpoints for managing meetings between users.

## Base URL

The base URL for all API endpoints is `http://localhost:8080`.

## Endpoints

### Create Meeting

- **Endpoint**: `POST /meetings`
- **Description**: Creates a new meeting between two users.

### Get Meetings by Email

- **Endpoint**: `GET /meetings/{email}`
- **Description**: Retrieves all current and upcoming meetings associated with the specified email.

### Update Meeting

- **Endpoint**: `PUT /meetings/{meetingId}`
- **Description**: Updates the details of a specific meeting.

### Delete Meeting

- **Endpoint**: `DELETE /meetings/delete/{meetingId}`
- **Description**: Deletes a specific meeting.

# User Controller API Documentation

The User Controller API provides endpoints for managing user information and contacts.

## Endpoints

### Get All Users

- **Endpoint**: `GET /users`
- **Description**: Retrieves a list of all users.

### Get User by ID

- **Endpoint**: `GET /users/{id}`
- **Description**: Retrieves a user by their ID.

### Create User

- **Endpoint**: `POST /users`
- **Description**: Creates a new user.

### Login User

- **Endpoint**: `POST /users/login`
- **Description**: Logs in a user with their email and password.

### Update User

- **Endpoint**: `PUT /users/{id}`
- **Description**: Updates the details of a specific user.

### Delete User

- **Endpoint**: `DELETE /users/{id}`
- **Description**: Deletes a specific user.

### Get User Contacts

- **Endpoint**: `GET /users/{id}/contacts`
- **Description**: Retrieves the contacts of a specific user.

### Add Contact

- **Endpoint**: `POST /users/{userId}/contacts`
- **Description**: Adds a contact to a user's contact list.

### Remove Contact

- **Endpoint**: `DELETE /users/{userId}/contacts/{contactId}`
- **Description**: Removes a contact from a user's contact list.

