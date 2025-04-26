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
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-5">
                <h1>Welcome, Organizer {user?.firstName}!</h1>
                <p>This is your dashboard. Manage your events below.</p>
            </div>

            <div className="row mb-5">
                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Create Event</h5>
                            <p className="card-text">Create a new event with details like title, description, date, location, and more.</p>
                            <button className="btn btn-primary mt-auto" onClick={navigateToCreateEvent}>
                                Create New Event
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Modify Event</h5>
                            <p className="card-text">Update details of your existing events including time, location, description, etc.</p>
                            <button className="btn btn-warning mt-auto" onClick={navigateToModifyEvent}>
                                Modify Events
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Delete Event</h5>
                            <p className="card-text">Remove events that are no longer needed or have been cancelled.</p>
                            <button className="btn btn-danger mt-auto" onClick={navigateToDeleteEvent}>
                                Delete Events
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">View Feedback Report</h5>
                            <p className="card-text">See average rating and reviews of your hosted events.</p>
                            <button className="btn btn-success mt-auto" onClick={navigateToFeedbackReport}>
                                View Report
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Add Announcement</h5>
                            <p className="card-text">Send a new announcement for any event you’re organizing.</p>
                            <button className="btn btn-info mt-auto" onClick={navigateToAddAnnouncement}>
                                Add Announcement
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomeOrganizer;
