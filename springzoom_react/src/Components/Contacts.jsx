import React from "react";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Table from "react-bootstrap/Table";
import Navbar from "react-bootstrap/Navbar";
import Button from "react-bootstrap/Button";
import axios from "axios";
import { useState } from "react";
import Form from "react-bootstrap/Form";
function Contacts() {
  function deletar() {
    axios.delete("http://localhost:8080/users/delete/1").then((response) => {
      alert("success");
      window.location.replace("/contacts");
    });
  }


  const [email, setEmail] = useState('');
  const [name, setName] = useState('');
  const [myId, setMyId] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = { name: name, email: email};
    axios.post(`http://localhost:8080/users/${myId}/add-contact`, data)
      .then(response => {
        console.log(response);
        alert("Contact added successfully!");
        setEmail('');
        setName('');
      })
      .catch(error => {
        console.log(error);
        alert("Failed to add contact: " + error.response.data.message);
      });
  };
  return (
    <>
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home">SpringZomm</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="/home">Home</Nav.Link>
              <Nav.Link href="/contacts">Contacts</Nav.Link>
              <Nav.Link href="/newmeeting">Add Meeting</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container className="d-flex flex-column align-items-center justify-content-center text-center ">
        <h1>Contacts</h1>
        <h3>Here you can see your contacts</h3>
      </Container>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>Mark</td>
            <td>Otto</td>
            <td>
              <button onClick={deletar}> Delete Contact </button>
            </td>
          </tr>
        </tbody>
      </Table>
    <br /> 
    <br /> 
      <h3 className="text-center">Add a new contact</h3>
      <Form onSubmit={handleSubmit} className="d-flex flex-column text-center align-items-center justify-content-center">
          <Form.Group className="mb-3" controlId="formBasicName"  >
            <Form.Label>Name</Form.Label>
            <Form.Control type="name" placeholder="Name" value={name} onChange={(event) => setName(event.target.value)} />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control type="email" placeholder="Enter email" value={email} onChange={(event) => setEmail(event.target.value)} />
            <Form.Text className="text-muted">
              We'll never share your email with anyone else.
            </Form.Text>
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicName"  >
            <Form.Label>Your ID</Form.Label>
            <Form.Control  placeholder="ID" value={myId} onChange={(event) => setMyId(event.target.value)} />
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
    </>
  );
}

export default Contacts;
