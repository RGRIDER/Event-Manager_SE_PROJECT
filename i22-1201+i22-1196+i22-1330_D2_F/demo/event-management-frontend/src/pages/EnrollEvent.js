import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function EnrollEvent() {
    const [user, setUser] = useState(null);
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Participant") {
            navigate("/login");
        } else {
            setUser(storedUser);
            fetchEvents();
        }
    }, [navigate]);

    const fetchEvents = async () => {
        try {
            setLoading(true);
            const response = await fetch("http://localhost:8080/api/events/all");

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();
            setEvents(data);
        } catch (error) {
            setError("Error fetching events: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleEnroll = async (eventId) => {
        try {
            setLoading(true);
            const response = await fetch(`http://localhost:8080/api/participants/register/${user.email}/${eventId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                }
            });

            const message = await response.text();
            if (response.ok) {
                setSuccess(`Successfully enrolled in the event!`);
                fetchEvents(); // Refresh event list
            } else {
                setError(message || "Failed to enroll.");
            }
        } catch (error) {
            setError("Error enrolling in event: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <h2 className="text-center">Enroll in an Event</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            {loading ? (
                <div className="text-center">
                    <div className="spinner-border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                </div>
            ) : (
                <div className="row">
                    {events.length > 0 ? (
                        events.map((event) => (
                            <div key={event.eventId} className="col-md-4 mb-3">
                                <div className="card h-100">
                                    <div className="card-body">
                                        <h5 className="card-title">{event.title}</h5>
                                        <p className="card-text"><strong>Date:</strong> {new Date(event.date).toLocaleDateString()}</p>
                                        <p className="card-text"><strong>Location:</strong> {event.location}</p>
                                        <p className="card-text"><strong>Description:</strong> {event.description}</p>
                                        <button
                                            className="btn btn-primary w-100"
                                            onClick={() => handleEnroll(event.eventId)}
                                        >
                                            Enroll
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className="alert alert-info text-center">No events available for enrollment.</div>
                    )}
                </div>
            )}

            <div className="text-center mt-4">
                <button className="btn btn-secondary" onClick={() => navigate("/home-participant")}>
                    Back to Dashboard
                </button>
            </div>
        </div>
    );
}

export default EnrollEvent;
