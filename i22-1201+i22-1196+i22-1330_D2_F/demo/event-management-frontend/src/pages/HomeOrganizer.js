import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function HomeOrganizer() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Organizer") {
            navigate("/login");
        } else {
            setUser(storedUser);
        }
    }, [navigate]);

    const navigateToCreateEvent = () => navigate("/create-event");
    const navigateToModifyEvent = () => navigate("/modify-event");
    const navigateToDeleteEvent = () => navigate("/delete-event");
    const navigateToFeedbackReport = () => navigate("/feedback-report");
    const navigateToAddAnnouncement = () => navigate("/add-announcement");

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
                        Welcome, Organizer {user?.firstName}!
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
                        This is your dashboard. Manage your events below.
                    </p>
                </div>
            </div>

            <div className="container">
                <div className="row g-4 justify-content-center">
                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-primary shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-primary">Create Event</h5>
                                <p className="card-text text-light">Create a new event with full details.</p>
                                <button className="btn btn-primary mt-auto fw-bold" onClick={navigateToCreateEvent}>
                                    Create New Event
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-warning shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-warning">Modify Event</h5>
                                <p className="card-text text-light">Update your event’s details.</p>
                                <button className="btn btn-warning mt-auto fw-bold" onClick={navigateToModifyEvent}>
                                    Modify Events
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-danger shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-danger">Delete Event</h5>
                                <p className="card-text text-light">Remove any unwanted events.</p>
                                <button className="btn btn-danger mt-auto fw-bold" onClick={navigateToDeleteEvent}>
                                    Delete Events
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-success shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-success">View Feedback Report</h5>
                                <p className="card-text text-light">Review attendee feedback & ratings.</p>
                                <button className="btn btn-success mt-auto fw-bold" onClick={navigateToFeedbackReport}>
                                    View Report
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-4">
                        <div className="card h-100 bg-dark border border-info shadow-lg">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-info">Add Announcement</h5>
                                <p className="card-text text-light">Notify participants with updates.</p>
                                <button className="btn btn-info mt-auto fw-bold" onClick={navigateToAddAnnouncement}>
                                    Add Announcement
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <footer className="text-center mt-auto py-3" style={{ fontSize: "14px", color: "#FFD700" }}>
                © 2025 SmartSphere Event Management
            </footer>
        </div>
    );
}

export default HomeOrganizer;
