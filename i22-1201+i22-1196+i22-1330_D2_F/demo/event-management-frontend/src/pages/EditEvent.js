import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function EditEvent() {
    const { eventId } = useParams();
    const navigate = useNavigate();
    const [eventData, setEventData] = useState({
        title: "",
        description: "",
        date: "",
        location: "",
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Fetch existing event data
        axios.get(`http://localhost:8080/api/events/${eventId}`)
            .then(response => {
                setEventData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Failed to fetch event details:", error);
                setLoading(false);
            });
    }, [eventId]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEventData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        axios.put(`http://localhost:8080/api/events/update/${eventId}`, eventData)
            .then(() => {
                alert("Event updated successfully!");
                navigate("/modify-event");
            })
            .catch(error => {
                console.error("Error updating event:", error);
                alert("Failed to update event.");
            });
    };

    if (loading) {
        return <div className="container text-center mt-5"><h4>Loading event details...</h4></div>;
    }

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-5">
                <h2>Edit Event</h2>
            </div>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Title</label>
                    <input
                        type="text"
                        className="form-control"
                        name="title"
                        value={eventData.title}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Description</label>
                    <textarea
                        className="form-control"
                        name="description"
                        value={eventData.description}
                        onChange={handleChange}
                        rows="5"
                        required
                    ></textarea>
                </div>

                <div className="mb-3">
                    <label className="form-label">Date</label>
                    <input
                        type="date"
                        className="form-control"
                        name="date"
                        value={eventData.date}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Location</label>
                    <input
                        type="text"
                        className="form-control"
                        name="location"
                        value={eventData.location}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="d-flex justify-content-center">
                    <button type="submit" className="btn btn-success">Save Changes</button>
                </div>
            </form>
        </div>
    );
}

export default EditEvent;
