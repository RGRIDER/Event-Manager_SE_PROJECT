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
    const navigateToEnroll = () => {
        navigate("/enroll");
    };

    const navigateToUnenroll = () => {
        navigate("/unenroll");
    };

    const navigateToViewEvents = () => {
        navigate("/view-events");
    };

    const navigateToSendFeedback = () => {
        navigate("/send-feedback");
    };

    const navigateToNotifications = () => {
        navigate("/notifications");
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-5">
                <h1>Welcome, Participant {user?.firstName}!</h1>
                <p>This is your dashboard. Manage your event participation below.</p>
            </div>

            <div className="row mb-5">
                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Enroll in Event</h5>
                            <p className="card-text">Sign up for an event and participate in exciting activities.</p>
                            <button className="btn btn-primary mt-auto" onClick={navigateToEnroll}>
                                Enroll Now
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Unenroll from Event</h5>
                            <p className="card-text">Withdraw from an event you no longer wish to attend.</p>
                            <button className="btn btn-danger mt-auto" onClick={navigateToUnenroll}>
                                Unenroll
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">View Events</h5>
                            <p className="card-text">Browse through upcoming events and see what's happening.</p>
                            <button className="btn btn-success mt-auto" onClick={navigateToViewEvents}>
                                View Events
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Send Feedback</h5>
                            <p className="card-text">Provide feedback on events you attended to help improve future experiences.</p>
                            <button className="btn btn-warning mt-auto" onClick={navigateToSendFeedback}>
                                Send Feedback
                            </button>
                        </div>
                    </div>
                </div>

                <div className="col-md-3 mb-3">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">View Notifications</h5>
                            <p className="card-text">See all announcements made in events you’re part of.</p>
                            <button className="btn btn-info mt-auto" onClick={navigateToNotifications}>
                                View Notifications
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomeParticipant;
