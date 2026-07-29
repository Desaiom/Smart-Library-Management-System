import { Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import AdminRegister from "./pages/auth/AdminRegister";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import LibrarianRegister from "./pages/auth/LibrarianRegister";
import Books from "./pages/Books";
import BookDetails from "./pages/BookDetails";
import BookForm from "./pages/BookForm";
import MyBorrows from "./pages/MyBorrows";
import ManageBorrows from "./pages/ManageBorrows";
import Categories from "./pages/Categories";
import Users from "./pages/Users";
import Dashboard from "./pages/Dashboard";
import Profile from "./pages/Profile";
import NotFound from "./pages/NotFound";

const STAFF = ["ADMIN", "LIBRARIAN"];

export default function App() {
  return (
    <>
      <Navbar />
      <main className="container py-4 page-min">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/register/admin" element={<AdminRegister />} />
          <Route path="/register/librarian" element={<LibrarianRegister />} />
          <Route path="/books" element={<Books />} />
          <Route path="/books/:id" element={<BookDetails />} />

          <Route path="/forgot-password" element={<ForgotPassword />} />

          <Route path="/reset-password" element={<ResetPassword />} />
          <Route
            path="/books/new"
            element={
              <ProtectedRoute roles={STAFF}>
                <BookForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/books/:id/edit"
            element={
              <ProtectedRoute roles={STAFF}>
                <BookForm />
              </ProtectedRoute>
            }
          />

          <Route
            path="/my-borrows"
            element={
              <ProtectedRoute>
                <MyBorrows />
              </ProtectedRoute>
            }
          />
          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            }
          />

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute roles={STAFF}>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/categories"
            element={
              <ProtectedRoute roles={STAFF}>
                <Categories />
              </ProtectedRoute>
            }
          />
          <Route
            path="/manage-borrows"
            element={
              <ProtectedRoute roles={STAFF}>
                <ManageBorrows />
              </ProtectedRoute>
            }
          />
          <Route
            path="/users"
            element={
              <ProtectedRoute roles={["ADMIN"]}>
                <Users />
              </ProtectedRoute>
            }
          />

          <Route path="/404" element={<NotFound />} />
          <Route path="*" element={<Navigate to="/404" replace />} />
        </Routes>
      </main>
      <footer
        style={{
          background: "linear-gradient(90deg, #0A2A66, #103E8A)",
          color: "#fff",
        }}
        className="mt-auto py-4"
      >
        <div className="container text-center">
          <h6 className="fw-semibold mb-2 text-white">📚 Smart Library Management System</h6>

          <p className="mb-2 text-white">
            A full-stack Library Management System built with{" "}
            <span className="fw-semibold text-success">Spring Boot</span>,{" "}
            <span className="fw-semibold text-info">React</span>, and{" "}
            <span className="fw-semibold text-warning">MySQL</span>.
          </p>

          <small className="text-white-50">
            © {new Date().getFullYear()} Smart Library Management System • Developed as a Full-Stack
            Portfolio Project
          </small>
        </div>
      </footer>
    </>
  );
}
