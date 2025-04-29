import React from "react";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function Navbar() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("user");
        navigate("/login");
    };

    return (
        <nav
            className="navbar p-3"
            style={{
                backgroundColor: "#FFD700",
                fontFamily: "Poppins, sans-serif",
                fontWeight: "bold",
                boxShadow: "0 0 15px #FFD700",
            }}
        >
            <div className="container d-flex justify-content-between align-items-center">
                <Link
                    to="#"
                    className="navbar-brand mb-0 h1"
                    style={{
                        color: "#111",
                        fontSize: "28px",
                        textShadow: "0 0 10px #FFD700",
                    }}
                >
                    SmartSphere
                </Link>
                <button
                    className="btn"
                    style={{
                        backgroundColor: "#111",
                        color: "#FFD700",
                        fontWeight: "bold",
                        border: "none",
                        padding: "8px 16px",
                        boxShadow: "0 0 10px #111",
                    }}
                    onClick={handleLogout}
                >
                    Logout
                </button>
            </div>
        </nav>
    );
}

export default Navbar;
