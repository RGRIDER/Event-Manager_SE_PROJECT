import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function ModifyEvent() {
    const [user, setUser] = useState(null);
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        date: "",
        startTime: "09:00",
        endTime: "17:00",
        location: "",
        teamSize: 1,
        registrationFee: 0
    });

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

            setEvents(data.events);
        } catch (error) {
            console.error("⚠️ Error fetching events:", error.message);
            setError(error.message);
            setEvents([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSelectEvent = (event) => {
        setSelectedEvent(event);

        const descLines = event.description.split("\n");
        let startTime = "09:00";
        let endTime = "17:00";
        let teamSize = 1;
        let registrationFee = 0;

        descLines.forEach(line => {
            if (line.startsWith("Start Time:")) startTime = line.replace("Start Time:", "").trim();
            else if (line.startsWith("End Time:")) endTime = line.replace("End Time:", "").trim();
            else if (line.startsWith("Team Size:")) teamSize = parseInt(line.replace("Team Size:", "").trim());
            else if (line.startsWith("Registration Fee:")) registrationFee = parseFloat(line.replace("Registration Fee: $", "").trim());
        });

        setFormData({
            title: event.title,
            description: descLines[0] || "",
            date: new Date(event.date).toISOString().split("T")[0],
            startTime,
            endTime,
            location: event.location,
            teamSize,
            registrationFee
        });
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        try {
            setLoading(true);

            const eventData = {
                title: formData.title,
                description: `${formData.description}\nStart Time: ${formData.startTime}\nEnd Time: ${formData.endTime}\nTeam Size: ${formData.teamSize}\nRegistration Fee: $${formData.registrationFee}`,
                date: formData.date,
                location: formData.location
            };

            const response = await fetch(`http://localhost:8080/api/events/modify/${user.email}/${selectedEvent.eventId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(eventData)
            });

            if (response.ok) {
                setSuccess("Event updated successfully!");
                fetchOrganizerEvents(user.email);
                setTimeout(() => setSelectedEvent(null), 2000);
            } else {
                const errorData = await response.text();
                setError(errorData || "Failed to update event");
            }
        } catch (error) {
            setError("Error updating event: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <h2>Modify Event</h2>

            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            {loading ? <p>Loading...</p> : (
                events.length > 0 ? (
                    <ul className="list-group">
                        {events.map(event => (
                            <li
                                key={event.eventId}
                                className={`list-group-item ${selectedEvent?.eventId === event.eventId ? "active" : ""}`}
                                onClick={() => handleSelectEvent(event)}
                                style={{ cursor: "pointer" }}
                            >
                                {event.title} - {new Date(event.date).toLocaleDateString()}
                            </li>
                        ))}
                    </ul>
                ) : <p>No events found</p>
            )}

            {selectedEvent && (
                <div className="card mt-3 p-4">
                    <h4>Modify Event: <strong>{selectedEvent.title}</strong></h4>
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Title</label>
                            <input type="text" className="form-control" name="title" value={formData.title} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Description</label>
                            <textarea className="form-control" name="description" value={formData.description} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Date</label>
                            <input type="date" className="form-control" name="date" value={formData.date} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Start Time</label>
                            <input type="time" className="form-control" name="startTime" value={formData.startTime} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">End Time</label>
                            <input type="time" className="form-control" name="endTime" value={formData.endTime} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Location</label>
                            <input type="text" className="form-control" name="location" value={formData.location} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Team Size</label>
                            <input type="number" className="form-control" name="teamSize" value={formData.teamSize} onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Registration Fee ($)</label>
                            <input type="number" className="form-control" name="registrationFee" value={formData.registrationFee} onChange={handleChange} required />
                        </div>

                        <button type="submit" className="btn btn-primary me-2">Modify</button>
                        <button type="button" className="btn btn-secondary" onClick={() => setSelectedEvent(null)}>Cancel</button>
                    </form>
                </div>
            )}
        </div>
    );
}

export default ModifyEvent;
