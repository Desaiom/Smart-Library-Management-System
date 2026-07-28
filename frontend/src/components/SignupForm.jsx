import { Link } from 'react-router-dom';

export default function SignupForm({
  title,
  form,
  handleChange,
  handleSubmit,
  loading
}) {

  return (
    <div className="auth-wrapper">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-6">

            <div className="card shadow-sm">
              <div className="card-body p-4">

                <h4 className="mb-4 text-center">
                  <i className="bi bi-person-plus me-2"></i>
                  {title}
                </h4>


                <form onSubmit={handleSubmit}>

                  <div className="mb-3">
                    <label className="form-label">
                      Full Name
                    </label>

                    <input
                      name="name"
                      className="form-control"
                      value={form.name}
                      onChange={handleChange}
                      required
                    />
                  </div>


                  <div className="mb-3">
                    <label className="form-label">
                      Email
                    </label>

                    <input
                      type="email"
                      name="email"
                      className="form-control"
                      value={form.email}
                      onChange={handleChange}
                      required
                    />
                  </div>


                  <div className="mb-3">
                    <label className="form-label">
                      Password
                    </label>

                    <input
                      type="password"
                      name="password"
                      className="form-control"
                      value={form.password}
                      onChange={handleChange}
                      required
                      minLength={6}
                    />
                  </div>


                  <div className="row">

                    <div className="col-md-6 mb-3">
                      <label className="form-label">
                        Phone
                      </label>

                      <input
                        name="phone"
                        className="form-control"
                        value={form.phone}
                        onChange={handleChange}
                      />
                    </div>


                    <div className="col-md-6 mb-3">
                      <label className="form-label">
                        Address
                      </label>

                      <input
                        name="address"
                        className="form-control"
                        value={form.address}
                        onChange={handleChange}
                      />
                    </div>

                  </div>


                  <button
                    className="btn btn-primary w-100"
                    disabled={loading}
                  >
                    {loading
                      ? "Creating..."
                      : "Create Account"
                    }
                  </button>


                </form>


                <p className="text-center mt-3 mb-0 small">
                  Already registered?
                  <Link to="/login">
                    {" "}Login
                  </Link>
                </p>

              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  );
}