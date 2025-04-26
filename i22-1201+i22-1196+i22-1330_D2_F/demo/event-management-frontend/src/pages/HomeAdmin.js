import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import "bootstrap/dist/css/bootstrap.min.css";

function HomeAdmin() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (!storedUser || storedUser.userType !== "Admin") {
            navigate("/login");
        } else {
            setUser(storedUser);
        }
    }, [navigate]);

    const navigateToFullReport = () => {
        navigate("/admin-full-report");
    };

    return (
        <div className="container mt-5">
            <Navbar />
            <div className="text-center mb-5">
                <h1>Welcome, Admin {user?.firstName}!</h1>
                <p>This is your dashboard.</p>
            </div>

            <div className="row mb-5 justify-content-center">
                <div className="col-md-4">
                    <div className="card h-100">
                        <div className="card-body d-flex flex-column">
                            <h5 className="card-title">Full Report</h5>
                            <p className="card-text">View detailed reports of all events and participants.</p>
                            <button className="btn btn-primary mt-auto" onClick={navigateToFullReport}>
                                View Full Report
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomeAdmin;
