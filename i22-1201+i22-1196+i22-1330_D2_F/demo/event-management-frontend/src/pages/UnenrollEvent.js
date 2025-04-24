import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function UnenrollEvent() {
    const [user, setUser] = useState(null);
    const [enrolledEvents, setEnrolledEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();

    // Fetch user and enrolled events
    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Participant") {
            navigate("/login");
        } else {
            setUser(storedUser);
            fetchEnrolledEvents(storedUser.email);
        }
    }, [navigate]);

    // Fetch events the participant is enrolled in
    const fetchEnrolledEvents = async (email) => {
        try {
            setLoading(true);
            console.log("🔍 Fetching enrolled events for:", email);

            const response = await fetch(`http://localhost:8080/api/participants/byUser/${email}`);

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();
            console.log("✅ Enrolled Events:", data);

            if (!Array.isArray(data)) {
                throw new Error("Unexpected response format from server.");
            }

            setEnrolledEvents(data);
        } catch (error) {
            console.error("⚠️ Error fetching enrolled events:", error.message);
            setError(error.message);
            setEnrolledEvents([]); // Reset list on failure
        } finally {
            setLoading(false);
        }
    };

    // Handle event selection for unenrollment
    const handleSelectEvent = (participant) => {
        setSelectedEvent(participant.event); // Store the event instead of participant object
        setError("");
        setSuccess("");
        console.log("🔹 Selected event:", participant.event);
    };

    // Handle Unenrollment
    const handleUnenroll = async () => {
        if (!selectedEvent || !user) {
            setError("No event selected.");
            return;
        }

        try {
            setLoading(true);
            console.log("🚀 Unenrolling from event ID:", selectedEvent.eventId);

            const response = await fetch(`http://localhost:8080/api/participants/unregister/${user.email}/${selectedEvent.eventId}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                const errorData = await response.text();
                throw new Error(errorData || "Failed to unenroll from event.");
            }

            setSuccess(`Successfully unenrolled from "${selectedEvent.title}"`);
            fetchEnrolledEvents(user.email);
            setSelectedEvent(null);
        } catch (error) {
            console.error("⚠️ Error unenrolling:", error.message);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <h2 className="text-center">Unenroll from Event</h2>

            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            {loading ? (
                <p className="text-center">Loading...</p>
            ) : enrolledEvents.length > 0 ? (
                <div className="table-responsive">
                    <table className="table table-striped">
                        <thead>
                        <tr>
                            <th>Title</th>
                            <th>Date</th>
                            <th>Location</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {enrolledEvents.map(participant => (
                            <tr key={participant.participantId}>
                                <td>{participant.event.title}</td>
                                <td>{new Date(participant.event.date).toLocaleDateString()}</td>
                                <td>{participant.event.location}</td>
                                <td>
                                    <button className="btn btn-danger btn-sm" onClick={() => handleSelectEvent(participant)}>
                                        Unenroll
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <p className="alert alert-info text-center">You are not enrolled in any events.</p>
            )}

            {selectedEvent && (
                <div className="text-center mt-4">
                    <h4>Are you sure you want to unenroll from "{selectedEvent.title}"?</h4>
                    <button className="btn btn-danger me-2" onClick={handleUnenroll} disabled={loading}>
                        {loading ? "Processing..." : "Yes, Unenroll"}
                    </button>
                    <button className="btn btn-secondary" onClick={() => setSelectedEvent(null)}>Cancel</button>
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

export default UnenrollEvent;
