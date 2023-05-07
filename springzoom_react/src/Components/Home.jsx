import React from "react";
import { Link } from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
function Home() {
    return (
        <>    <Navbar bg="light" expand="lg">
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
      <Container className='d-flex flex-column align-items-center justify-content-center text-center '> 
        <h2>Welcome to SpringZoom</h2>
        <h3>SpringZoom is a web application that allows you to schedule meetings with your contacts.</h3>
    





    </Container>
        </>
    );
    }

export default Home;