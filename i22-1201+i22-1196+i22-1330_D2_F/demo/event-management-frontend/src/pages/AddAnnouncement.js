import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "./Navbar";

function AddAnnouncement() {
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState("");
    const [message, setMessage] = useState("");
    const [status, setStatus] = useState("");

    const user = JSON.parse(localStorage.getItem("user"));

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const res = await axios.get(`http://localhost:8080/api/events/my-events/${user.email}`);
                setEvents(res.data.events || []);
            } catch (err) {
                console.error(err);
                setStatus("Failed to load events.");
            }
        };
        fetchEvents();
    }, [user.email]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!selectedEvent || !message.trim()) {
            setStatus("Event and message are required.");
            return;
        }

        try {
            await axios.post(`http://localhost:8080/api/events/announce/${user.email}/${selectedEvent}`, {
                message
            });
            setStatus("Announcement sent successfully!");
            setMessage("");
        } catch (err) {
            console.error(err);
            setStatus("Failed to send announcement.");
        }
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <h2>Add Announcement</h2>
            {status && <div className="alert alert-info mt-3">{status}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group mt-3">
                    <label>Select Event</label>
                    <select
                        className="form-control"
                        value={selectedEvent}
                        onChange={(e) => setSelectedEvent(e.target.value)}
                    >
                        <option value="">-- Select an Event --</option>
                        {events.map((event) => (
                            <option key={event.eventId} value={event.eventId}>
                                {event.title}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group mt-3">
                    <label>Announcement Message</label>
                    <textarea
                        className="form-control"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        rows={4}
                    ></textarea>
                </div>
                <button type="submit" className="btn btn-info mt-3">
                    Submit Announcement
                </button>
            </form>
        </div>
    );
}

export default AddAnnouncement;
