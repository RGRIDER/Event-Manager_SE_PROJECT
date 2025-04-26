import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "./Navbar";
import { useNavigate } from "react-router-dom";

function AddAnnouncement() {
    const [events, setEvents] = useState([]);
    const [selectedEventId, setSelectedEventId] = useState("");
    const [message, setMessage] = useState("");
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

    const handleAddAnnouncement = () => {
        if (!selectedEventId || !message) {
            alert("Please select an event and enter a message.");
            return;
        }

        axios.post(`http://localhost:8080/api/organizers/add-announcement/${selectedEventId}`, message, {
            headers: {
                "Content-Type": "text/plain"
            }
        })
            .then(() => {
                alert("Announcement added successfully!");
                navigate("/home-organizer");
            })
            .catch(err => {
                console.error("Error adding announcement:", err);
                alert("Failed to add announcement.");
            });
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-4">
                <h2>Add Announcement</h2>
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

            <div className="mb-4">
                <textarea
                    className="form-control"
                    rows="3"
                    placeholder="Enter your announcement message here..."
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                ></textarea>
            </div>

            <div className="text-center">
                <button className="btn btn-success" onClick={handleAddAnnouncement}>
                    Add Announcement
                </button>
            </div>

            <div className="text-center mt-4">
                <button className="btn btn-secondary" onClick={() => navigate("/home-organizer")}>
                    Back to Dashboard
                </button>
            </div>
        </div>
    );
}

export default AddAnnouncement;
