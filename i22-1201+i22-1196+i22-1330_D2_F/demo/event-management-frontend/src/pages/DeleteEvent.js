import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function DeleteEvent() {
    const [user, setUser] = useState(null);
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [selectedEvent, setSelectedEvent] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Organizer") {
            navigate("/login");
        } else {
            setUser(storedUser);
            fetchOrganizerEvents(storedUser.email);
        }
    }, [navigate]);

    const fetchOrganizerEvents = async (email) => {
        try {
            setLoading(true);
            setError("");
            setSuccess("");

            const response = await fetch(`http://localhost:8080/api/events/my-events/${email}`);

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const text = await response.text();
            console.log("📢 Raw API Response:", text);

            let data;
            try {
                data = JSON.parse(text);
            } catch (error) {
                throw new Error("Failed to parse JSON response.");
            }

            console.log("✅ Parsed JSON:", data);

            if (!data.events || !Array.isArray(data.events)) {
                throw new Error("Unexpected response format from server (events key missing or not an array).");
            }

            setEvents(data.events);  // ✅ Extracting correct array
        } catch (error) {
            console.error("⚠️ Error fetching events:", error.message);
            setError(error.message);
            setEvents([]); // Ensure events is an empty array on failure
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async () => {
        if (!selectedEvent) return;

        const confirmDelete = window.confirm(`Are you sure you want to delete "${selectedEvent.title}"?`);
        if (!confirmDelete) return;

        try {
            setLoading(true);
            setError("");
            setSuccess("");

            const response = await fetch(`http://localhost:8080/api/events/delete/${user.email}/${selectedEvent.eventId}`, {
                method: "DELETE"
            });

            if (response.ok) {
                setSuccess(`Event "${selectedEvent.title}" has been deleted successfully!`);
                setEvents(events.filter(event => event.eventId !== selectedEvent.eventId));
                setSelectedEvent(null);
            } else {
                const errorData = await response.text();
                setError(errorData || "Failed to delete event");
            }
        } catch (error) {
            setError("Error deleting event: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <h2>Delete Event</h2>

            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            {loading ? <p>Loading...</p> : (
                events.length > 0 ? (
                    <ul className="list-group">
                        {events.map(event => (
                            <li
                                key={event.eventId}
                                className={`list-group-item ${selectedEvent?.eventId === event.eventId ? "active" : ""}`}
                                onClick={() => setSelectedEvent(event)}
                                style={{ cursor: "pointer" }}
                            >
                                {event.title} - {new Date(event.date).toLocaleDateString()}
                            </li>
                        ))}
                    </ul>
                ) : <p>No events found</p>
            )}

            {selectedEvent && (
                <div className="mt-3">
                    <h4>Are you sure you want to delete <strong>{selectedEvent.title}</strong>?</h4>
                    <button onClick={handleDelete} className="btn btn-danger me-2">Delete</button>
                    <button onClick={() => setSelectedEvent(null)} className="btn btn-secondary">Cancel</button>
                </div>
            )}
        </div>
    );
}

export default DeleteEvent;
