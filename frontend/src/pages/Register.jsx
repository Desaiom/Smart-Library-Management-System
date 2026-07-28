import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";
import { useAuth } from "../context/AuthContext";

export default function Register() {
  const { register, loading } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
    address: "",
  });

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await register(form);
      toast.success("Account created!");
      navigate("/");
    } catch (err) {
      toast.error(err.message);
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-6">
            <div className="card shadow-sm">
              <div className="card-body p-4">
                <h4 className="mb-4 text-center">
                  <i className="bi bi-person-plus me-2"></i>
                  User Signup
                </h4>

                <form onSubmit={handleSubmit}>
                  <Input label="Full Name" name="name" value={form.name} onChange={handleChange} />

                  <Input
                    label="Email"
                    name="email"
                    type="email"
                    value={form.email}
                    onChange={handleChange}
                  />

                  <Input
                    label="Password"
                    name="password"
                    type="password"
                    value={form.password}
                    onChange={handleChange}
                  />

                  <Input label="Phone" name="phone" value={form.phone} onChange={handleChange} />

                  <Input
                    label="Address"
                    name="address"
                    value={form.address}
                    onChange={handleChange}
                  />

                  <button className="btn btn-primary w-100" disabled={loading}>
                    {loading ? "Creating..." : "Create Account"}
                  </button>
                </form>

                <p className="text-center mt-3 small">
                  Already registered?
                  <Link to="/login"> Login</Link>
                </p>

                <hr />

                <div className="text-center small">
                  Staff?
                  <br />
                  <Link to="/register/admin">Admin Signup</Link>
                  {" | "}
                  <Link to="/register/librarian">Librarian Signup</Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

function Input({ label, name, type = "text", value, onChange }) {
  return (
    <div className="mb-3">
      <label className="form-label">{label}</label>

      <input
        type={type}
        name={name}
        className="form-control"
        value={value}
        onChange={onChange}
        required
      />
    </div>
  );
}
