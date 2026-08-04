export default function Footer() {
  return (
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
  );
}
