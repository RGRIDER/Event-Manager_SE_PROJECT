import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Home from "./pages/Home";
import HomeAdmin from "./pages/HomeAdmin";
import HomeOrganizer from "./pages/HomeOrganizer";
import HomeParticipant from "./pages/HomeParticipant";
import CreateEvent from "./pages/CreateEvent";
import ModifyEvent from "./pages/ModifyEvent";
import DeleteEvent from "./pages/DeleteEvent";
import EnrollEvent from "./pages/EnrollEvent";
import UnenrollEvent from "./pages/UnenrollEvent";
import ViewEvents from "./pages/ViewEvents";
import SendFeedback from "./pages/SendFeedback";
import FeedbackReportPage from "./pages/FeedbackReportPage";
import AddAnnouncement from "./pages/AddAnnouncement";
import NotificationsPage from "./pages/NotificationsPage";
import AdminFullReport from "./pages/AdminFullReport";
import EditEvent from './pages/EditEvent'; // or adjust the path if it's inside a pages/ folder


function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/home" element={<Home />} />
                <Route path="/home-admin" element={<HomeAdmin />} />
                <Route path="/home-organizer" element={<HomeOrganizer />} />
                <Route path="/home-participant" element={<HomeParticipant />} />
                <Route path="/create-event" element={<CreateEvent />} />
                <Route path="/modify-event" element={<ModifyEvent />} />
                <Route path="/delete-event" element={<DeleteEvent />} />
                <Route path="/enroll" element={<EnrollEvent />} />
                <Route path="/unenroll" element={<UnenrollEvent />} />
                <Route path="/view-events" element={<ViewEvents />} />
                <Route path="/send-feedback" element={<SendFeedback />} />
                <Route path="/feedback-report" element={<FeedbackReportPage />} />
                <Route path="/add-announcement" element={<AddAnnouncement />} />
                <Route path="/notifications" element={<NotificationsPage />} />
                <Route path="/admin-full-report" element={<AdminFullReport />} />
                <Route path="/modify-event/:eventId" element={<EditEvent />} />


            </Routes>
        </Router>
    );
}

export default App;
