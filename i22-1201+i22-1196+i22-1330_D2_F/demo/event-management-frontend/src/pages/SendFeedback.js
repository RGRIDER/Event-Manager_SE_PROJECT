import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function SendFeedback() {
    const [user, setUser] = useState(null);
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [feedback, setFeedback] = useState("");
    const [rating, setRating] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Participant") {
            navigate("/login");
        } else {
            setUser(storedUser);
            fetchEnrolledEvents(storedUser.email);
        }
    }, [navigate]);

    const fetchEnrolledEvents = async (email) => {
        try {
            const response = await fetch(`http://localhost:8080/api/participants/byUser/${email}`);
            const data = await response.json();
            setEvents(data.map(p => p.event));
        } catch (err) {
            console.error("Error fetching events:", err);
        }
    };

    const handleSubmitFeedback = async () => {
        const response = await fetch("http://localhost:8080/api/participants/feedback", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: user.email,
                eventId: selectedEvent.eventId,
                feedback: feedback,
                rating: rating
            })
        });

        if (response.ok) {
            alert("Feedback submitted successfully!");
            setSelectedEvent(null);
            setFeedback("");
            setRating(0);
        } else {
            alert("Error submitting feedback.");
        }
    };

    const renderStars = () => {
        return [...Array(5)].map((_, i) => (
            <span
                key={i}
                style={{ cursor: "pointer", color: i < rating ? "#ffc107" : "#e4e5e9", fontSize: "1.5rem" }}
                onClick={() => setRating(i + 1)}
            >
                ★
            </span>
        ));
    };

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Send Feedback</h2>

            {!selectedEvent ? (
                <>
                    <p>Select an event to provide feedback:</p>
                    <ul className="list-group">
                        {events.map((event) => (
                            <li key={event.eventId} className="list-group-item d-flex justify-content-between align-items-center">
                                <span>{event.title}</span>
                                <button className="btn btn-primary" onClick={() => setSelectedEvent(event)}>
                                    Give Feedback
                                </button>
                            </li>
                        ))}
                    </ul>
                </>
            ) : (
                <>
                    <h5>Feedback for: {selectedEvent.title}</h5>
                    <div className="mb-2">
                        <label className="form-label d-block">Rating:</label>
                        {renderStars()}
                    </div>
                    <textarea
                        className="form-control"
                        rows="4"
                        value={feedback}
                        onChange={(e) => setFeedback(e.target.value)}
                        placeholder="Write your feedback here..."
                    />
                    <div className="mt-3">
                        <button className="btn btn-success me-2" onClick={handleSubmitFeedback}>
                            Submit
                        </button>
                        <button className="btn btn-secondary" onClick={() => {
                            setSelectedEvent(null);
                            setFeedback("");
                            setRating(0);
                        }}>
                            Cancel
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

export default SendFeedback;
