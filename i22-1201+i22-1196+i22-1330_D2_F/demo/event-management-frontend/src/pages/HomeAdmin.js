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

    return (
        <div className="container text-center mt-5">
            <Navbar />
            <h1>Welcome, Admin {user?.firstName}!</h1>
            <p>This is your dashboard.</p>
        </div>
    );
}

export default HomeAdmin;
