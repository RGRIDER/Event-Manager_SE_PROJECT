import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";

function ViewEvents() {
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem("user"));

    useEffect(() => {
        if (user?.email) {
            axios.get(`http://localhost:8080/api/participants/byUser/${user.email}`)
                .then((res) => {
                    const eventList = res.data.map((p) => p.event);
                    setEvents(eventList);
                    setLoading(false);
                })
                .catch((err) => {
                    console.error("Error fetching events:", err);
                    setLoading(false);
                });
        }
    }, [user?.email]);

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-4">
                <h2>Your Enrolled Events</h2>
            </div>

            {loading ? (
                <p>Loading your events...</p>
            ) : events.length === 0 ? (
                <p>You are not enrolled in any events.</p>
            ) : (
                <>
                    <ul className="list-group mb-4">
                        {events.map((event) => (
                            <li
                                key={event.eventId}
                                className={`list-group-item ${selectedEvent?.eventId === event.eventId ? "active" : ""}`}
                                onClick={() => setSelectedEvent(event)}
                                style={{ cursor: "pointer" }}
                            >
                                {event.title}
                            </li>
                        ))}
                    </ul>

                    {selectedEvent && (
                        <div className="card p-4 shadow-sm">
                            <h4>{selectedEvent.title}</h4>
                            <p><strong>Description:</strong> {selectedEvent.description}</p>
                            <p><strong>Date:</strong> {selectedEvent.date}</p>
                            <p><strong>Location:</strong> {selectedEvent.location}</p>
                        </div>
                    )}
                </>
            )}

            <div className="text-center mt-4">
                <button className="btn btn-secondary" onClick={() => navigate("/home-participant")}>
                    Back to Dashboard
                </button>
            </div>
        </div>
    );
}

export default ViewEvents;
