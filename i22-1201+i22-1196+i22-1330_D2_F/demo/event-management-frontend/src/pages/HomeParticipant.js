import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function HomeParticipant() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Participant") {
            navigate("/login");
        } else {
            setUser(storedUser);
        }
    }, [navigate]);

    // Navigation Handlers
    const navigateToEnroll = () => navigate("/enroll");
    const navigateToUnenroll = () => navigate("/unenroll");
    const navigateToViewEvents = () => navigate("/view-events");
    const navigateToSendFeedback = () => navigate("/send-feedback");
    const navigateToNotifications = () => navigate("/notifications");

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
                        marginBottom: "20px", // more space between strips
                        boxShadow: "0 0 15px #FFD700",
                        display: "inline-block",
                    }}
                >
                    <h1 style={{ color: "#FFD700", fontWeight: "bold", textShadow: "0 0 10px #FFD700" }}>
                        Welcome, Participant {user?.firstName}!
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
                        Manage your event participation below
                    </p>
                </div>
            </div>



            <div className="container">
                <div className="row g-4 justify-content-center">
                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-warning shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-warning">Enroll in Event</h5>
                                <p className="card-text text-light">Sign up for an exciting new event.</p>
                                <button className="btn btn-warning mt-auto fw-bold" onClick={navigateToEnroll}>
                                    Enroll Now
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-danger shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-danger">Unenroll from Event</h5>
                                <p className="card-text text-light">Withdraw from events you can't attend.</p>
                                <button className="btn btn-danger mt-auto fw-bold" onClick={navigateToUnenroll}>
                                    Unenroll
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-success shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-success">View Events</h5>
                                <p className="card-text text-light">Explore all upcoming events.</p>
                                <button className="btn btn-success mt-auto fw-bold" onClick={navigateToViewEvents}>
                                    View Events
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-primary shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-primary">Send Feedback</h5>
                                <p className="card-text text-light">Share your event experiences.</p>
                                <button className="btn btn-primary mt-auto fw-bold" onClick={navigateToSendFeedback}>
                                    Send Feedback
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-info shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-info">View Notifications</h5>
                                <p className="card-text text-light">Check latest event announcements.</p>
                                <button className="btn btn-info mt-auto fw-bold" onClick={navigateToNotifications}>
                                    View Notifications
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

export default HomeParticipant;
