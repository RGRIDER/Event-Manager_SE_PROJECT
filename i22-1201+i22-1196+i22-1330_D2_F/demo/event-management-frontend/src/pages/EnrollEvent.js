import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function EnrollEvent() {
    const [user, setUser] = useState(null);
    const [events, setEvents] = useState([]);
    const [searchTitle, setSearchTitle] = useState("");
    const [searchOrganizer, setSearchOrganizer] = useState("");
    const [searchLocation, setSearchLocation] = useState("");
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

    const fetchEvents = async (filters = {}) => {
        try {
            setLoading(true);
            let url = "http://localhost:8080/api/events/search";

            const params = new URLSearchParams();
            if (filters.title) params.append("title", filters.title);
            if (filters.organizer) params.append("organizer", filters.organizer);
            if (filters.location) params.append("location", filters.location);

            if (Array.from(params).length > 0) {
                url += `?${params.toString()}`;
            }

            const response = await fetch(url);

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
                setSuccess("Successfully enrolled in the event!");
                fetchEvents();
            } else {
                setError(message || "Failed to enroll.");
            }
        } catch (error) {
            setError("Error enrolling in event: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        fetchEvents({
            title: searchTitle.trim(),
            organizer: searchOrganizer.trim(),
            location: searchLocation.trim(),
        });
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <h2 className="text-center mb-4">Enroll in an Event</h2>

            <form onSubmit={handleSearch} className="mb-5">
                <div className="row g-2">
                    <div className="col-md-4">
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Search by Event Title"
                            value={searchTitle}
                            onChange={(e) => setSearchTitle(e.target.value)}
                        />
                    </div>
                    <div className="col-md-4">
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Search by Organizer Name"
                            value={searchOrganizer}
                            onChange={(e) => setSearchOrganizer(e.target.value)}
                        />
                    </div>
                    <div className="col-md-4">
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Search by Location"
                            value={searchLocation}
                            onChange={(e) => setSearchLocation(e.target.value)}
                        />
                    </div>
                    <div className="col-12 text-center">
                        <button type="submit" className="btn btn-primary mt-3">Search</button>
                    </div>
                </div>
            </form>

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
                            <div key={event.eventId} className="col-md-4 mb-4">
                                <div className="card h-100 shadow-sm">
                                    <div className="card-body">
                                        <h5 className="card-title">{event.title}</h5>
                                        <p className="card-text"><strong>Date:</strong> {new Date(event.date).toLocaleDateString()}</p>
                                        <p className="card-text"><strong>Location:</strong> {event.location}</p>
                                        <p className="card-text"><strong>Description:</strong> {event.description}</p>
                                        <p className="card-text"><strong>Organizer:</strong> {event.organizerName}</p>
                                        <button
                                            className="btn btn-success w-100"
                                            onClick={() => handleEnroll(event.eventId)}
                                        >
                                            Enroll Now
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className="alert alert-info text-center">No events found matching the criteria.</div>
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
