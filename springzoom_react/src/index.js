import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import Login from './Components/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Register from './Components/Register';
import Home from './Components/Home';
import NewMeeting from './Components/NewMeeting';
import Contacts from './Components/Contacts';	
import { UserProvider } from './Components/UserContext';

ReactDOM.render(
  <BrowserRouter>
  <UserProvider>
  <Routes>

  <Route path="/" element={<Login />} />
  <Route path="/register" element={<Register/>} />
  <Route path="/home" element={<Home/>} />
  <Route path="/contacts" element={<Contacts/>} />
  <Route path="/newmeeting" element={<NewMeeting/>} />
  </Routes>
  </UserProvider>
  </BrowserRouter>
  
  ,
  document.getElementById('root')
);

reportWebVitals();
