import React from "react";
import { useNavigate } from "react-router-dom";

function SendFeedback() {
    const navigate = useNavigate();

    return (
        <div className="container text-center mt-5">
            <h2>Send Feedback</h2>
            <p>Page content goes here...</p>
            <button className="btn btn-secondary" onClick={() => navigate("/home-participant")}>
                Back to Dashboard
            </button>
        </div>
    );
}

export default SendFeedback;
