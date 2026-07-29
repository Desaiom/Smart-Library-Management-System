import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { bookApi } from '../api/bookApi';
import { reviewApi } from '../api/reviewApi';
import { borrowApi } from '../api/borrowApi';
import { useAuth } from '../context/AuthContext';
import Loader from '../components/Loader';
import RatingStars from '../components/RatingStars';

export default function BookDetails() {
  const { id } = useParams();
  const { isAuthenticated, isStaff, user } = useAuth();
  const [book, setBook] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [reviewForm, setReviewForm] = useState({ rating: 5, review: '' });

  const load = async () => {
    setLoading(true);
    try {
      const [b, r] = await Promise.all([bookApi.getById(id), reviewApi.byBook(id)]);
      setBook(b);
      setReviews(r);
    } catch (err) {
      toast.error(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleBorrow = async () => {
    try {
      await borrowApi.borrow({ bookId: Number(id) });
      toast.success('Book borrowed!');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  const submitReview = async (e) => {
    e.preventDefault();
    try {
      await reviewApi.add({ bookId: Number(id), rating: Number(reviewForm.rating), review: reviewForm.review });
      toast.success('Review added');
      setReviewForm({ rating: 5, review: '' });
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  const deleteReview = async (rid) => {
    try {
      await reviewApi.remove(rid);
      toast.success('Review deleted');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  if (loading) return <Loader />;
  if (!book) return <p>Book not found.</p>;

  return (
    <div>
      <Link to="/books" className="btn btn-link px-0 mb-3">
        <i className="bi bi-arrow-left me-1"></i>Back to catalog
      </Link>

      <div className="row g-4">
        <div className="col-md-4">
          <img
            className="img-fluid rounded shadow-sm w-100 book-cover"
            style={{ height: 'auto', maxHeight: 420 }}
            src={`http://localhost:8080${book.imageUrl}`}
            alt={book.title}
          />
        </div>
        <div className="col-md-8">
          <h2>{book.title}</h2>
          <p className="text-muted mb-1">by {book.author}</p>
          <RatingStars value={book.averageRating} />
          <hr />
          <p>{book.description || 'No description available.'}</p>
          <ul className="list-unstyled small text-muted">
            <li><strong>ISBN:</strong> {book.isbn}</li>
            <li><strong>Category:</strong> {book.categoryName || 'Uncategorized'}</li>
            <li><strong>Published:</strong> {book.publicationYear || 'N/A'}</li>
            <li><strong>Price:</strong> {book.price != null ? `$${book.price}` : 'N/A'}</li>
            <li><strong>Availability:</strong> {book.availableQuantity} / {book.quantity}</li>
          </ul>
          {isAuthenticated && (
            <button className="btn btn-primary" onClick={handleBorrow} disabled={book.availableQuantity <= 0}>
              <i className="bi bi-journal-arrow-down me-1"></i>
              {book.availableQuantity > 0 ? 'Borrow this book' : 'Not available'}
            </button>
          )}
          {isStaff && (
            <Link to={`/books/${book.id}/edit`} className="btn btn-outline-secondary ms-2">
              <i className="bi bi-pencil me-1"></i>Edit
            </Link>
          )}
        </div>
      </div>

      <hr className="my-4" />
      <h4>Reviews ({reviews.length})</h4>

      {isAuthenticated && (
        <form className="card card-body mb-4" onSubmit={submitReview}>
          <div className="row g-2 align-items-end">
            <div className="col-md-2">
              <label className="form-label">Rating</label>
              <select
                className="form-select"
                value={reviewForm.rating}
                onChange={(e) => setReviewForm({ ...reviewForm, rating: e.target.value })}
              >
                {[5, 4, 3, 2, 1].map((n) => <option key={n} value={n}>{n} ★</option>)}
              </select>
            </div>
            <div className="col-md-8">
              <label className="form-label">Your review</label>
              <input
                className="form-control"
                value={reviewForm.review}
                onChange={(e) => setReviewForm({ ...reviewForm, review: e.target.value })}
                placeholder="Share your thoughts..."
              />
            </div>
            <div className="col-md-2">
              <button className="btn btn-primary w-100">Post</button>
            </div>
          </div>
        </form>
      )}

      {reviews.length === 0 ? (
        <p className="text-muted">No reviews yet.</p>
      ) : (
        reviews.map((r) => (
          <div className="card mb-2" key={r.id}>
            <div className="card-body py-2">
              <div className="d-flex justify-content-between">
                <div>
                  <strong>{r.userName}</strong> <RatingStars value={r.rating} />
                  <p className="mb-0 small">{r.review}</p>
                </div>
                {(isStaff || r.userId === user?.userId) && (
                  <button className="btn btn-sm btn-outline-danger" onClick={() => deleteReview(r.id)}>
                    <i className="bi bi-trash"></i>
                  </button>
                )}
              </div>
            </div>
          </div>
        ))
      )}
    </div>
  );
}
