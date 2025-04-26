import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "./Navbar";
import { useNavigate } from "react-router-dom";

function FeedbackReportPage() {
    const [events, setEvents] = useState([]);
    const [selectedEventId, setSelectedEventId] = useState("");
    const [feedbackReport, setFeedbackReport] = useState(null);
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem("user"));

    useEffect(() => {
        if (user && user.email) {
            axios.get(`http://localhost:8080/api/organizers/my-events/${user.email}`)
                .then(res => {
                    setEvents(res.data);
                })
                .catch(err => {
                    console.error("Error fetching events:", err);
                });
        }
    }, [user]);

    const handleFetchReport = () => {
        if (!selectedEventId) {
            alert("Please select an event!");
            return;
        }

        axios.get(`http://localhost:8080/api/organizers/feedback-report/${selectedEventId}`)
            .then(res => {
                setFeedbackReport(res.data);
            })
            .catch(err => {
                console.error("Error fetching feedback report:", err);
                alert("Failed to fetch feedback report.");
            });
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-4">
                <h2>Feedback Report</h2>
            </div>

            <div className="mb-4">
                <select
                    className="form-select"
                    value={selectedEventId}
                    onChange={(e) => setSelectedEventId(e.target.value)}
                >
                    <option value="">Select an Event</option>
                    {events.map((event) => (
                        <option key={event.eventId} value={event.eventId}>
                            {event.title}
                        </option>
                    ))}
                </select>
            </div>

            <div className="text-center mb-4">
                <button className="btn btn-primary" onClick={handleFetchReport}>
                    Get Feedback Report
                </button>
            </div>

            {feedbackReport && (
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">Average Rating: {feedbackReport.averageRating.toFixed(2)}</h5>
                        <p className="card-text">Remark: {feedbackReport.remark}</p>
                        <h6>Feedbacks:</h6>
                        <ul className="list-group">
                            {feedbackReport.feedbacks.map((fb, index) => (
                                <li key={index} className="list-group-item">
                                    {fb.content} — Rating: {fb.rating}
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            )}

            <div className="text-center mt-4">
                <button className="btn btn-secondary" onClick={() => navigate("/home-organizer")}>
                    Back to Dashboard
                </button>
            </div>
        </div>
    );
}

export default FeedbackReportPage;
