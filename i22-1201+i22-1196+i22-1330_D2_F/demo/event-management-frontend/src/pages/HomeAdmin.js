import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function HomeAdmin() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Admin") {
            navigate("/login");
        } else {
            setUser(storedUser);
        }
    }, [navigate]);

    const navigateToFullReport = () => {
        navigate("/admin-full-report");
    };

    return (
        <div
            className="d-flex flex-column min-vh-100"
            style={{
                backgroundImage: "url('/assets/background.png')",
                backgroundSize: "cover",
                backgroundPosition: "center",
                backgroundAttachment: "fixed",
                fontFamily: "Poppins, sans-serif",
                color: "#FFD700",
            }}
        >
            <Navbar />
            <div className="container text-center my-5">
                <div
                    style={{
                        backgroundColor: "rgba(0, 0, 0, 0.8)",
                        border: "2px solid #FFD700",
                        borderRadius: "10px",
                        padding: "15px",
                        marginBottom: "20px",
                        boxShadow: "0 0 15px #FFD700",
                        display: "inline-block",
                    }}
                >
                    <h1 style={{ color: "#FFD700", fontWeight: "bold", textShadow: "0 0 10px #FFD700" }}>
                        Welcome, Admin {user?.firstName}!
                    </h1>
                </div>
                <br />
                <div
                    style={{
                        backgroundColor: "rgba(0, 0, 0, 0.8)",
                        border: "2px solid #FFD700",
                        borderRadius: "10px",
                        padding: "10px",
                        boxShadow: "0 0 10px #FFD700",
                        display: "inline-block",
                    }}
                >
                    <p style={{ color: "#FFD700", fontWeight: "500", marginBottom: 0 }}>
                        This is your dashboard.
                    </p>
                </div>
            </div>

            <div className="container">
                <div className="row g-4 justify-content-center">
                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-warning shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-warning">Full Report</h5>
                                <p className="card-text text-light">View detailed reports of all events and participants.</p>
                                <button className="btn btn-warning mt-auto fw-bold" onClick={navigateToFullReport}>
                                    View Full Report
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <footer className="text-center mt-auto py-3" style={{ fontSize: "14px", color: "#FFD700" }}>
                © 2025 Event Management System
            </footer>
        </div>
    );
}

export default HomeAdmin;
