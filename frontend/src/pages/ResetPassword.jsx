import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { authApi } from "../api/authApi";

export default function ResetPassword() {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const location = useLocation();

  const token = new URLSearchParams(location.search).get("token");

  const [form, setForm] = useState({
    password: "",
    confirmPassword: "",
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!token) {
      toast.error("Invalid password reset link.");
      return;
    }

    if (form.password !== form.confirmPassword) {
      toast.error("Passwords do not match.");
      return;
    }

    try {
      setLoading(true);

      await authApi.resetPassword({
        token,
        password: form.password,
      });

      toast.success("Password reset successfully.");

      navigate("/login");
    } catch (err) {
      toast.error(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-5">
            <div className="card shadow-sm">
              <div className="card-body p-4">
                <h3 className="text-center mb-4">Reset Password</h3>

                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label className="form-label">New Password</label>

                    <div className="position-relative">
                      <input
                        type={showPassword ? "text" : "password"}
                        className="form-control pe-5"
                        name="password"
                        value={form.password}
                        onChange={handleChange}
                        required
                      />

                      <i
                        className={`bi ${showPassword ? "bi-eye-slash-fill" : "bi-eye-fill"}`}
                        onClick={() => setShowPassword(!showPassword)}
                        style={{
                          position: "absolute",
                          right: "15px",
                          top: "50%",
                          transform: "translateY(-50%)",
                          cursor: "pointer",
                          fontSize: "18px",
                        }}
                      />
                    </div>
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Confirm Password</label>

                    <div className="position-relative">
                      <input
                        type={showConfirmPassword ? "text" : "password"}
                        className="form-control pe-5"
                        name="confirmPassword"
                        value={form.confirmPassword}
                        onChange={handleChange}
                        required
                      />

                      <i
                        className={`bi ${showConfirmPassword ? "bi-eye-slash-fill" : "bi-eye-fill"}`}
                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                        style={{
                          position: "absolute",
                          right: "15px",
                          top: "50%",
                          transform: "translateY(-50%)",
                          cursor: "pointer",
                          fontSize: "18px",
                        }}
                      />
                    </div>
                  </div>

                  <button className="btn btn-success w-100" disabled={loading}>
                    {loading ? "Updating..." : "Reset Password"}
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
