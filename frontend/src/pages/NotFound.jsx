import { Link } from 'react-router-dom';

export default function NotFound() {
  return (
    <div className="text-center py-5">
      <i className="bi bi-compass" style={{ fontSize: '4rem' }}></i>
      <h1 className="mt-3">404</h1>
      <p className="text-muted">The page you are looking for does not exist.</p>
      <Link to="/" className="btn btn-primary">
        <i className="bi bi-house me-1"></i>Back Home
      </Link>
    </div>
  );
}
