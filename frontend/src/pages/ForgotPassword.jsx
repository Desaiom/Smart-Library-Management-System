import { useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import { authApi } from "../api/authApi";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      await authApi.forgotPassword({ email });

      toast.success("Password reset link has been sent to your email.");

      setEmail("");
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

                <h3 className="text-center mb-3">
                  Forgot Password
                </h3>

                <p className="text-muted text-center mb-4">
                  Enter your email address and we'll send you a password reset link.
                </p>

                <form onSubmit={handleSubmit}>

                  <div className="mb-3">
                    <label className="form-label">
                      Email Address
                    </label>

                    <input
                      type="email"
                      className="form-control"
                      placeholder="Enter your email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                  </div>

                  <button
                    className="btn btn-primary w-100"
                    disabled={loading}
                  >
                    {loading ? "Sending..." : "Send Reset Link"}
                  </button>

                </form>

                <div className="text-center mt-3">
                  <Link to="/login">
                    Back to Login
                  </Link>
                </div>

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}