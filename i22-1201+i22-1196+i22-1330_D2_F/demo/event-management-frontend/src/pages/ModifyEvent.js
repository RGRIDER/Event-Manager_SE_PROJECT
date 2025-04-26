import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";

function ModifyEvent() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const storedUser = JSON.parse(localStorage.getItem("user"));
                const response = await axios.get(`http://localhost:8080/api/events/my-events/${storedUser.email}`);
                setEvents(response.data || []);
            } catch (err) {
                console.error(err);
                setError("Failed to load events.");
            } finally {
                setLoading(false);
            }
        };
        fetchEvents();
    }, []);

    const handleModify = (eventId) => {
        navigate(`/modify-event/${eventId}`);
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-5">
                <h2>Modify Your Events</h2>
            </div>

            {loading ? (
                <div className="text-center">Loading...</div>
            ) : error ? (
                <div className="alert alert-danger text-center">{error}</div>
            ) : events.length === 0 ? (
                <div className="alert alert-info text-center">No events found.</div>
            ) : (
                <div className="row">
                    {events.map((event) => (
                        <div key={event.eventId} className="col-md-4 mb-4">
                            <div className="card h-100 shadow">
                                <div className="card-body d-flex flex-column">
                                    <h5 className="card-title">{event.title}</h5>
                                    <p className="card-text">{event.location}</p>
                                    <p className="card-text">
                                        {event.date}
                                    </p>
                                    <button
                                        className="btn btn-warning mt-auto"
                                        onClick={() => handleModify(event.eventId)}
                                    >
                                        Modify Event
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default ModifyEvent;
