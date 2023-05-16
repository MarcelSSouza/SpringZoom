import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import axios from "axios";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import LogoutButton from "./LogoutButton";


function generateRandomNumber() {
  const randomNumber = Math.random();
  const scaledNumber = randomNumber * (10000000 - 100000) + 100000;
  const finalNumber = Math.round(scaledNumber);
  return finalNumber;
}
const meetingId = generateRandomNumber();

function NewMeeting() {
  const [title, setTitle] = useState("");
  const [date, setDate] = useState("");
  const [hour, setHour] = useState("");
  const [yourEmail, setYourEmail] = useState("");
  const [otherUserEmail, setOtherUserEmail] = useState("");

  function handleSubmit(event) {
    event.preventDefault();
    const data = {
      title: title,
      meetingDate: date,
      meetingTime: hour,
      meetingId: meetingId,
      email1: yourEmail,
      email2: otherUserEmail,
    };
    axios
      .post("http://localhost:8080/meetings", data)
      .then((response) => {
        console.log(response);
        alert("Success");
        setTitle("");
        setDate("");
        setHour("");
        setYourEmail("");
        setOtherUserEmail("");
      })
      .catch((error) => {
        console.log(error);
        alert(error);
      });
  }



  return (
    <>
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="/home">SpringZomm</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="/home">Home</Nav.Link>
              <Nav.Link href="/contacts">Contacts</Nav.Link>
              <Nav.Link href="/newmeeting">Add Meeting</Nav.Link>
<LogoutButton />

            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container>
        <h1 className="text-center">New Meeting</h1>
        <br></br>
        <Form className="d-flex flex-column" onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="formBasicTitle">
            <Form.Label>Title</Form.Label>
            <Form.Control
              type="title"
              placeholder="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicDate">
            <Form.Label>Date</Form.Label>
            <Form.Control
              type="Date"
              placeholder="Date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicHour">
            <Form.Label>Hour</Form.Label>
            <Form.Control
              type="Time"
              placeholder="Hour"
              value={hour}
              onChange={(e) => setHour(e.target.value)}
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Your email</Form.Label>
            <Form.Control
              type="email"
              placeholder="Enter email"
              value={yourEmail}
              onChange={(e) => setYourEmail(e.target.value)}
            />
            <Form.Text className="text-muted"></Form.Text>
          </Form.Group>

          <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Email of the other user</Form.Label>
            <Form.Control
              type="email"
              placeholder="Enter email"
              value={otherUserEmail}
              onChange={(e) => setOtherUserEmail(e.target.value)}
            />
            <Form.Text className="text-muted"></Form.Text>
          </Form.Group>
          
          

          <h2 className="text-center">
            
            Your passcode for this meeting will be: {meetingId}
          </h2>

          <Button variant="primary" type="submit" onSubmit={handleSubmit}>
            Submit
          </Button>
        </Form>
      </Container>
    </>
  );
}

export default NewMeeting;
