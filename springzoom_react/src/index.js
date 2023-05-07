import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import Login from './Components/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Register from './Components/Register';
ReactDOM.render(
  <BrowserRouter>
  <Routes>
  <Route path="/" element={<Login />} />
  <Route path="/register" element={<Register/>} />
  </Routes>
  </BrowserRouter>
  
  ,
  document.getElementById('root')
);

reportWebVitals();
