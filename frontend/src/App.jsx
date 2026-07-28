import { Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
// import AdminRegister from "./pages/auth/AdminRegister";
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
          {/* <Route path="/register/admin" element={<AdminRegister />} /> */}
          {/* <Route path="/register/librarian" element={<LibrarianRegister />} /> */}
          <Route path="/books" element={<Books />} />
          <Route path="/books/:id" element={<BookDetails />} />

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
      <footer className="text-center text-muted py-4 small">
        Smart Library Management System &middot; Spring Boot + React portfolio project
      </footer>
    </>
  );
}
