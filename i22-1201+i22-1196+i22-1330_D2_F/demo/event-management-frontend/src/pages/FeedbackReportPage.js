import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "./Navbar";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function FeedbackReportPage() {
    const [user, setUser] = useState(null);
    const [events, setEvents] = useState([]);
    const [selectedEventId, setSelectedEventId] = useState(null);
    const [feedbackReport, setFeedbackReport] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Organizer") {
            navigate("/login");
        } else {
            setUser(storedUser);
            fetchEvents(storedUser.email);
        }
    }, [navigate]);

    const fetchEvents = async (email) => {
        try {
            const res = await axios.get(`http://localhost:8080/api/events/my-events/${email}`);
            const data = res.data;
            if (data.events) {
                setEvents(data.events);
            } else {
                setEvents([]);
            }
        } catch (err) {
            console.error("Error fetching events:", err);
        }
    };

    const handleSelectEvent = async (eventId) => {
        setSelectedEventId(eventId);
        try {
            const res = await axios.get(`http://localhost:8080/api/organizers/feedback-report/${eventId}`);
            setFeedbackReport(res.data);
        } catch (err) {
            console.error("Error fetching feedback report:", err);
        }
    };

    const renderStars = (avgRating) => {
        const fullStars = Math.floor(avgRating);
        const halfStar = avgRating % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

        return (
            <span className="text-warning">
                {"★".repeat(fullStars)}
                {halfStar && "½"}
                {"☆".repeat(emptyStars)}
            </span>
        );
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <h2 className="mb-4">Feedback Reports</h2>

            <div className="mb-4">
                <h5>Select Event</h5>
                <select
                    className="form-select"
                    onChange={(e) => handleSelectEvent(e.target.value)}
                    value={selectedEventId || ""}
                >
                    <option value="" disabled>Select an event</option>
                    {events.map((event) => (
                        <option key={event.eventId} value={event.eventId}>
                            {event.title}
                        </option>
                    ))}
                </select>
            </div>

            {feedbackReport && (
                <div className="card p-4">
                    <h4 className="mb-3">Summary</h4>
                    <p><strong>Average Rating:</strong> {renderStars(feedbackReport.averageRating)} ({feedbackReport.averageRating.toFixed(1)})</p>
                    <p><strong>Remark:</strong> {feedbackReport.remark}</p>

                    <h5 className="mt-4">Feedbacks:</h5>
                    {feedbackReport.feedbacks.length > 0 ? (
                        <ul className="list-group">
                            {feedbackReport.feedbacks.map((fb, index) => (
                                <li key={index} className="list-group-item">
                                    <strong>Rating:</strong> {renderStars(fb.rating)} ({fb.rating})<br />
                                    <strong>Comment:</strong> {fb.content}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="text-muted">No feedbacks available for this event.</p>
                    )}
                </div>
            )}
        </div>
    );
}

export default FeedbackReportPage;
