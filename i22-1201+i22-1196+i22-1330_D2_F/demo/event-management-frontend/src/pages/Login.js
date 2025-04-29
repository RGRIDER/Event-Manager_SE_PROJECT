import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

function Login() {
    const [credentials, setCredentials] = useState({
        email: "",
        password: "",
        userType: "Participant",
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
        <div
            className="d-flex flex-column justify-content-center align-items-center vh-100"
            style={{
                backgroundImage: "url(/assets/background.png)",
                backgroundSize: "cover",
                backgroundPosition: "center",
                backgroundRepeat: "no-repeat",
                fontFamily: "Poppins, sans-serif",
            }}
        >
            {/* Big SmartSphere Title */}
            <h1
                style={{
                    fontSize: "128px",
                    backgroundColor: "rgba(17, 17, 17, 0.8)", // Semi-transparent black
                    color: "#FFD700",
                    fontWeight: "bold",
                    textShadow: "0 0 15px #FFD700",
                    padding: "10px 40px",
                    border: "2px solid #FFD700",
                    borderRadius: "12px",
                    marginBottom: "60px",
                    textAlign: "center",
                }}
            >
                SmartSphere
            </h1>

            {/* Login Card */}
            <div
                className="p-5 rounded"
                style={{
                    backgroundColor: "rgba(17, 17, 17, 0.95)",
                    boxShadow: "0 0 20px rgba(255, 215, 0, 0.7)",
                    width: "400px",
                    border: "2px solid #FFD700",
                }}
            >
                <h2
                    className="text-center mb-4"
                    style={{ color: "#FFD700", fontWeight: "bold", textShadow: "0 0 10px #FFD700" }}
                >
                    Login
                </h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: "#FFD700" }}>
                            Email
                        </label>
                        <input
                            type="email"
                            className="form-control"
                            name="email"
                            placeholder="Enter your email"
                            value={credentials.email}
                            onChange={handleChange}
                            required
                            style={{ backgroundColor: "#222", color: "#FFD700", border: "1px solid #FFD700" }}
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: "#FFD700" }}>
                            Password
                        </label>
                        <input
                            type="password"
                            className="form-control"
                            name="password"
                            placeholder="Enter your password"
                            value={credentials.password}
                            onChange={handleChange}
                            required
                            style={{ backgroundColor: "#222", color: "#FFD700", border: "1px solid #FFD700" }}
                        />
                    </div>
                    <div className="mb-4">
                        <label className="form-label" style={{ color: "#FFD700" }}>
                            User Type
                        </label>
                        <select
                            className="form-control"
                            name="userType"
                            value={credentials.userType}
                            onChange={handleChange}
                            style={{ backgroundColor: "#222", color: "#FFD700", border: "1px solid #FFD700" }}
                        >
                            <option value="Participant">Participant</option>
                            <option value="Admin">Admin</option>
                            <option value="Organizer">Organizer</option>
                        </select>
                    </div>
                    <button
                        type="submit"
                        className="btn w-100"
                        style={{
                            backgroundColor: "#FFD700",
                            border: "none",
                            color: "#000",
                            fontWeight: "bold",
                            boxShadow: "0 0 10px #FFD700",
                        }}
                    >
                        Login
                    </button>
                </form>
                <p className="mt-3 text-center" style={{ color: "#FFD700" }}>
                    Don't have an account?{" "}
                    <Link to="/signup" style={{ color: "#FFD700", textDecoration: "underline" }}>
                        Sign Up
                    </Link>
                </p>
            </div>
        </div>
    );
}

export default Login;
