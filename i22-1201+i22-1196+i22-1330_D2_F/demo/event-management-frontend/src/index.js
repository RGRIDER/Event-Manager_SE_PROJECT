import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App'; // ⬅️ Import App instead of Home
import reportWebVitals from './reportWebVitals';
import "bootstrap/dist/css/bootstrap.min.css";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <App /> {/* ⬅️ Render App.js instead of Home.js */}
    </React.StrictMode>
);

reportWebVitals();
