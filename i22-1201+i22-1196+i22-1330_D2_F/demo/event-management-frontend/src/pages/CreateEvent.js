import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function CreateEvent() {
    const [user, setUser] = useState(null);
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
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Organizer") {
            navigate("/login");
        } else {
            setUser(storedUser);
        }
    }, [navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const validateForm = () => {
        if (!formData.title || !formData.description || !formData.date || !formData.location) {
            setError("All fields are required");
            return false;
        }

        const selectedDate = new Date(formData.date);
        const today = new Date();
        if (selectedDate < today) {
            setError("Event date must be in the future");
            return false;
        }

        const startTime = formData.startTime.split(":");
        const endTime = formData.endTime.split(":");
        const startHour = parseInt(startTime[0]);
        const endHour = parseInt(endTime[0]);

        if (startHour < 8 || endHour > 22) {
            setError("Events must be scheduled between 8:00 AM and 10:00 PM");
            return false;
        }

        if (startHour > endHour) {
            setError("End time must be after start time");
            return false;
        }

        if (formData.teamSize < 1) {
            setError("Team size must be at least 1");
            return false;
        }

        if (formData.registrationFee < 0) {
            setError("Registration fee cannot be negative");
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        if (!validateForm()) {
            return;
        }

        try {
            setLoading(true);
            const eventData = {
                title: formData.title,
                description: `${formData.description}\nStart Time: ${formData.startTime}\nEnd Time: ${formData.endTime}\nTeam Size: ${formData.teamSize}\nRegistration Fee: $${formData.registrationFee}`,
                date: formData.date,
                location: formData.location,
            };

            const response = await fetch(`http://localhost:8080/api/events/create/${user.email}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(eventData),
            });

            const responseText = await response.text();
            let data;
            try {
                data = JSON.parse(responseText);
            } catch (error) {
                data = responseText;
            }

            if (response.ok) {
                setSuccess("Event created successfully!");
                setFormData({
                    title: "",
                    description: "",
                    date: "",
                    startTime: "09:00",
                    endTime: "17:00",
                    location: "",
                    teamSize: 1,
                    registrationFee: 0
                });
                setTimeout(() => {
                    navigate("/home-organizer");
                }, 2000);
            } else {
                setError(data || "Failed to create event");
            }
        } catch (error) {
            setError("Error creating event: " + error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="card">
                        <div className="card-header bg-primary text-white">
                            <h3 className="mb-0">Create New Event</h3>
                        </div>
                        <div className="card-body">
                            {error && <div className="alert alert-danger">{error}</div>}
                            {success && <div className="alert alert-success">{success}</div>}

                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label className="form-label">Event Title</label>
                                    <input type="text" className="form-control" name="title" value={formData.title} onChange={handleChange} required />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Description</label>
                                    <textarea className="form-control" name="description" rows="4" value={formData.description} onChange={handleChange} required></textarea>
                                </div>

                                <div className="row mb-3">
                                    <div className="col-md-6">
                                        <label className="form-label">Date</label>
                                        <input type="date" className="form-control" name="date" value={formData.date} onChange={handleChange} required />
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label">Location</label>
                                        <input type="text" className="form-control" name="location" value={formData.location} onChange={handleChange} required />
                                    </div>
                                </div>

                                <div className="d-flex justify-content-between mt-4">
                                    <button type="button" className="btn btn-secondary" onClick={() => navigate("/home-organizer")}>Cancel</button>
                                    <button type="submit" className="btn btn-primary" disabled={loading}>
                                        {loading ? <> <span className="spinner-border spinner-border-sm me-2"></span> Creating... </> : "Create Event"}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CreateEvent;
