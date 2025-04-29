import React, { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

const Signup = () => {
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        userType: "Participant",
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const validateForm = () => {
        const nameRegex = /^[A-Za-z]+$/;
        const emailRegex = /[a-zA-Z]/;
        const passwordLength = formData.password.length;
        const passwordUppercase = /[A-Z]/.test(formData.password);
        const passwordNumber = /\d/.test(formData.password);

        if (!nameRegex.test(formData.firstName)) {
            alert("First name should not contain numbers or special characters.");
            return false;
        }
        if (!nameRegex.test(formData.lastName)) {
            alert("Last name should not contain numbers or special characters.");
            return false;
        }
        if (!emailRegex.test(formData.email)) {
            alert("Email must contain alphabetic characters.");
            return false;
        }
        if (passwordLength < 8 || passwordLength > 15) {
            alert("Password must be between 8 and 15 characters.");
            return false;
        }
        if (!passwordUppercase) {
            alert("Password must contain at least one uppercase letter.");
            return false;
        }
        if (!passwordNumber) {
            alert("Password must contain at least one number.");
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        try {
            const response = await axios.post("http://localhost:8080/api/users/signup", formData);
            alert("Signup successful!");
            console.log(response.data);
        } catch (error) {
            alert("Signup failed. Please try again.");
            console.error(error.response ? error.response.data : error.message);
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
                    backgroundColor: "rgba(17, 17, 17, 0.8)",  // Semi-transparent black (matches your form)
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



            {/* Signup Card */}
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
                    Sign Up
                </h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: "#FFD700" }}>
                            First Name
                        </label>
                        <input
                            type="text"
                            className="form-control"
                            name="firstName"
                            value={formData.firstName}
                            onChange={handleChange}
                            required
                            style={{ backgroundColor: "#222", color: "#FFD700", border: "1px solid #FFD700" }}
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: "#FFD700" }}>
                            Last Name
                        </label>
                        <input
                            type="text"
                            className="form-control"
                            name="lastName"
                            value={formData.lastName}
                            onChange={handleChange}
                            required
                            style={{ backgroundColor: "#222", color: "#FFD700", border: "1px solid #FFD700" }}
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: "#FFD700" }}>
                            Email
                        </label>
                        <input
                            type="email"
                            className="form-control"
                            name="email"
                            value={formData.email}
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
                            value={formData.password}
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
                            value={formData.userType}
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
                        Sign Up
                    </button>
                </form>
                <p className="mt-3 text-center" style={{ color: "#FFD700" }}>
                    Already have an account?{" "}
                    <Link to="/login" style={{ color: "#FFD700", textDecoration: "underline" }}>
                        Login
                    </Link>
                </p>
            </div>
        </div>
    );
};

export default Signup;
