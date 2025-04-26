import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "./Navbar";

function NotificationsPage() {
    const [announcements, setAnnouncements] = useState([]);
    const [loading, setLoading] = useState(true);
    const user = JSON.parse(localStorage.getItem("user"));

    useEffect(() => {
        if (user?.email) {
            axios.get(`http://localhost:8080/api/participants/announcements/${user.email}`)
                .then(res => {
                    setAnnouncements(res.data);
                    setLoading(false);
                })
                .catch(err => {
                    console.error("Error fetching announcements:", err);
                    setLoading(false);
                });
        }
    }, [user?.email]);

    return (
        <div className="container mt-5">
            <Navbar />
            <h2 className="mb-4 text-center">Your Notifications</h2>
            {loading ? (
                <p>Loading announcements...</p>
            ) : announcements.length === 0 ? (
                <p>No announcements found.</p>
            ) : (
                <ul className="list-group">
                    {announcements.map((announcement, index) => (
                        <li key={index} className="list-group-item">
                            <strong>{announcement.event.title}</strong><br />
                            {announcement.message}<br />
                            <small className="text-muted">{new Date(announcement.timestamp).toLocaleString()}</small>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default NotificationsPage;
