import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { bookApi } from '../api/bookApi';
import { useAuth } from '../context/AuthContext';
import RatingStars from '../components/RatingStars';

export default function Home() {
  const { isAuthenticated } = useAuth();
  const [latest, setLatest] = useState([]);

  useEffect(() => {
    bookApi.list({ page: 0, size: 4, sortBy: 'id', direction: 'desc' })
      .then((p) => setLatest(p.content || []))
      .catch(() => setLatest([]));
  }, []);

  return (
    <div>
      <div className="sl-hero p-5 mb-5">
        <div className="row align-items-center">
          <div className="col-md-8">
            <h1 className="fw-bold">Smart Library Management System</h1>
            <p className="lead mb-4">
              Browse the catalog, borrow books, review your reads and manage the
              library — all in one place.
            </p>
            <Link to="/books" className="btn btn-light btn-lg me-2">
              <i className="bi bi-search me-2"></i>Browse Books
            </Link>
            {!isAuthenticated && (
              <Link to="/register" className="btn btn-outline-light btn-lg">
                Get Started
              </Link>
            )}
          </div>
          <div className="col-md-4 text-center d-none d-md-block">
            <i className="bi bi-journal-bookmark-fill" style={{ fontSize: '8rem', opacity: 0.85 }}></i>
          </div>
        </div>
      </div>

      <h4 className="mb-3">Recently Added</h4>
      <div className="row g-3">
        {latest.map((b) => (
          <div className="col-6 col-md-3" key={b.id}>
            <Link to={`/books/${b.id}`} className="text-decoration-none text-dark">
              <div className="card book-card">
                <img
                  className="card-img-top book-cover"
                  src={b.imageUrl || 'https://placehold.co/300x180?text=No+Cover'}
                  alt={b.title}
                />
                <div className="card-body">
                  <h6 className="card-title text-truncate">{b.title}</h6>
                  <small className="text-muted d-block text-truncate">{b.author}</small>
                  <RatingStars value={b.averageRating} />
                </div>
              </div>
            </Link>
          </div>
        ))}
        {latest.length === 0 && <p className="text-muted">No books yet.</p>}
      </div>
    </div>
  );
}
