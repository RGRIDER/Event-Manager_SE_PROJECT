import React from "react";
import { Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function Home() {
    console.log("✅ Home.js is rendering!"); // Debugging message

    return (
        <div className="container text-center mt-5">
            <h1>Welcome to the Home Page</h1>
            <p>This is the main dashboard after login.</p>

            {/* Navigation Buttons */}
            <div className="mt-3">
                <Link to="/login" className="btn btn-primary mx-2">Login</Link>
                <Link to="/signup" className="btn btn-success mx-2">Signup</Link>
            </div>
        </div>
    );
}

export default Home;
