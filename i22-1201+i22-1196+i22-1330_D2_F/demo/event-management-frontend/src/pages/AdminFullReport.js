import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function AdminFullReport() {
    const navigate = useNavigate();
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [feedbacks, setFeedbacks] = useState([]);
    const [participants, setParticipants] = useState([]);
    const [selectedParticipants, setSelectedParticipants] = useState([]);
    const [averageRating, setAverageRating] = useState(0);
    const [remark, setRemark] = useState("");
    const [showFeedbacks, setShowFeedbacks] = useState(false);

    useEffect(() => {
        axios.get("http://localhost:8080/api/events/all")
            .then(response => {
                setEvents(response.data);
            })
            .catch(error => {
                console.error("Error fetching events:", error);
            });
    }, []);

    const handleEventClick = (event) => {
        setSelectedEvent(event);
        fetchFeedbacks(event.eventId);
        fetchParticipants(event.eventId);
        setShowFeedbacks(false);
    };

    const fetchFeedbacks = (eventId) => {
        axios.get(`http://localhost:8080/api/organizers/feedback-report/${eventId}`)
            .then(response => {
                setFeedbacks(response.data.feedbacks || []);
                setAverageRating(response.data.averageRating || 0);
                setRemark(response.data.remark || "No Remark");
            })
            .catch(error => {
                console.error("Error fetching feedbacks:", error);
            });
    };

    const fetchParticipants = (eventId) => {
        axios.get(`http://localhost:8080/api/participants/byEvent/${eventId}`)
            .then(response => {
                setParticipants(response.data || []);
            })
            .catch(error => {
                console.error("Error fetching participants:", error);
            });
    };

    const toggleParticipantSelection = (participantId) => {
        setSelectedParticipants(prev =>
            prev.includes(participantId)
                ? prev.filter(id => id !== participantId)
                : [...prev, participantId]
        );
    };

    const cancelEnrollments = () => {
        const requests = selectedParticipants.map(participantId => {
            const participant = participants.find(p => p.participantId === participantId);
            if (participant) {
                const userEmail = participant.user.email;
                return axios.delete(`http://localhost:8080/api/participants/unregister/${userEmail}/${selectedEvent.eventId}`);
            }
            return null;
        });

        Promise.all(requests)
            .then(() => {
                alert("Selected participants have been removed.");
                setSelectedParticipants([]);
                fetchParticipants(selectedEvent.eventId);
            })
            .catch(error => {
                console.error("Error cancelling enrollments:", error);
                alert("An error occurred while removing participants.");
            });
    };

    const calculateStars = (rating) => {
        const stars = [];
        for (let i = 1; i <= 5; i++) {
            if (rating >= i) {
                stars.push("★");
            } else {
                stars.push("☆");
            }
        }
        return stars.join(" ");
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-5">
                <h1>Admin Full Report</h1>
                <p>Review all events, feedback, and manage participants.</p>
            </div>

            <div className="row mb-4">
                <div className="col-md-4">
                    <h4>All Events</h4>
                    {events.length > 0 ? (
                        events.map(event => (
                            <div key={event.eventId} className="card mb-2" onClick={() => handleEventClick(event)} style={{ cursor: "pointer" }}>
                                <div className="card-body">
                                    <h5 className="card-title">{event.title}</h5>
                                    <p className="card-text">{event.location}</p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>No events found.</p>
                    )}
                </div>

                <div className="col-md-8">
                    {selectedEvent && (
                        <>
                            <h4>Event Details</h4>
                            <div className="card mb-4">
                                <div className="card-body">
                                    <h5>{selectedEvent.title}</h5>
                                    <p><strong>Date:</strong> {selectedEvent.date}</p>
                                    <p><strong>Location:</strong> {selectedEvent.location}</p>
                                    <p><strong>Description:</strong> {selectedEvent.description}</p>
                                </div>
                            </div>

                            {/* Feedback Section */}
                            <h5>Average Feedback</h5>
                            {feedbacks.length > 0 ? (
                                <>
                                    <div className="mb-2">
                                        <strong>Rating:</strong> {calculateStars(averageRating)} ({averageRating.toFixed(1)})
                                        <br />
                                        <strong>Remark:</strong> {remark}
                                    </div>

                                    <button
                                        className="btn btn-outline-primary mb-3"
                                        onClick={() => setShowFeedbacks(!showFeedbacks)}
                                    >
                                        {showFeedbacks ? "Hide Feedbacks" : "View All Feedbacks"}
                                    </button>

                                    {showFeedbacks && (
                                        <div>
                                            <ul className="list-group">
                                                {feedbacks.map(fb => (
                                                    <li key={fb.id} className="list-group-item">
                                                        {fb.content} ({fb.rating} ★)
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                    )}
                                </>
                            ) : (
                                <p>No feedbacks for this event yet.</p>
                            )}

                            {/* Participant Section */}
                            <h5 className="mt-4">Participants</h5>
                            {participants.length > 0 ? (
                                <>
                                    <ul className="list-group">
                                        {participants.map(p => (
                                            <li key={p.participantId} className="list-group-item d-flex justify-content-between align-items-center">
                                                {p.user?.firstName} {p.user?.lastName} ({p.user?.email})
                                                <input
                                                    type="checkbox"
                                                    onChange={() => toggleParticipantSelection(p.participantId)}
                                                    checked={selectedParticipants.includes(p.participantId)}
                                                />
                                            </li>
                                        ))}
                                    </ul>
                                    {selectedParticipants.length > 0 && (
                                        <button className="btn btn-danger mt-3" onClick={cancelEnrollments}>
                                            Cancel Enrollment for Selected
                                        </button>
                                    )}
                                </>
                            ) : (
                                <p>No participants enrolled yet.</p>
                            )}
                        </>
                    )}
                </div>
            </div>

            <div className="text-center">
                <button className="btn btn-secondary" onClick={() => navigate("/home-admin")}>
                    Back to Dashboard
                </button>
            </div>
        </div>
    );
}

export default AdminFullReport;
