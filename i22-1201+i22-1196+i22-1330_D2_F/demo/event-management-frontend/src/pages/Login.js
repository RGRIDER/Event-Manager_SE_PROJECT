import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

function Login() {
    const [credentials, setCredentials] = useState({
        email: "",
        password: "",
        userType: "Participant"
    });

    const navigate = useNavigate();

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/users/login", credentials);

            console.log("Login Response:", response.data);

            if (response.data.userType !== credentials.userType) {
                alert(`Invalid user type selected. You are a ${response.data.userType}.`);
                return;
            }

            alert("Login successful!");
            localStorage.setItem("user", JSON.stringify(response.data));

            if (response.data.userType === "Participant") {
                navigate("/home-participant");
            } else if (response.data.userType === "Organizer") {
                navigate("/home-organizer");
            } else {
                navigate("/home-admin");
            }

        } catch (error) {
            console.error("Login Error:", error.response ? error.response.data : error.message);
            alert("Invalid email, password, or user type.");
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4 shadow" style={{ width: "400px" }}>
                <h2 className="text-center">Login</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Email</label>
                        <input
                            type="email"
                            className="form-control"
                            name="email"
                            placeholder="Enter your email"
                            value={credentials.email}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Password</label>
                        <input
                            type="password"
                            className="form-control"
                            name="password"
                            placeholder="Enter your password"
                            value={credentials.password}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">User Type</label>
                        <select
                            className="form-control"
                            name="userType"
                            value={credentials.userType}
                            onChange={handleChange}
                        >
                            <option value="Participant">Participant</option>
                            <option value="Admin">Admin</option>
                            <option value="Organizer">Organizer</option>
                        </select>
                    </div>
                    <button type="submit" className="btn btn-primary w-100">Login</button>
                </form>
                <p className="mt-3 text-center">
                    Don't have an account? <Link to="/signup">Sign Up</Link>
                </p>
            </div>
        </div>
    );
}

export default Login;
