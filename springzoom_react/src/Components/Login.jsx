import React, { useState } from 'react';
import axios from 'axios';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = { email: email, password: password };
    axios.post('http://localhost:8080/users/login', data)
      .then(response => {
        alert("success");
        setEmail('');
        setPassword('');
        window.location.replace('/home');

      })
      .catch(error => {
        
      });
  };

  return (
    <>
      <Container className='d-flex flex-column align-items-center justify-content-center text-center '>
        <h1>Login</h1>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control type="email" placeholder="Enter email" value={email} onChange={(e) => setEmail(e.target.value)} />
            <Form.Text className="text-muted">
              We'll never share your email with anyone else.
            </Form.Text>
          </Form.Group>

          <Form.Group className="mb-3" controlId="formBasicPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
          </Form.Group>

          <Button variant="primary" type="submit" >
            Submit
          </Button>
        </Form>
        <Button variant="primary" className='mt-3' >
          <a href="/register" style={{textDecoration: "none", color: "white"}}>
            Register
          </a>
        </Button>
      </Container>
    </>
  );
}

export default Login;
