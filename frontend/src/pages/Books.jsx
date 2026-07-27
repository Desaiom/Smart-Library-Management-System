import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { bookApi } from '../api/bookApi';
import { categoryApi } from '../api/categoryApi';
import { useAuth } from '../context/AuthContext';
import Loader from '../components/Loader';
import EmptyState from '../components/EmptyState';
import Pagination from '../components/Pagination';
import RatingStars from '../components/RatingStars';

export default function Books() {
  const { isStaff } = useAuth();
  const [page, setPage] = useState(null);
  const [pageNum, setPageNum] = useState(0);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState([]);
  const [filters, setFilters] = useState({ keyword: '', categoryId: '', available: false });

  const load = async (p = 0) => {
    setLoading(true);
    try {
      let data;
      if (filters.keyword) {
        data = await bookApi.search({ keyword: filters.keyword, page: p, size: 8 });
      } else if (filters.categoryId || filters.available) {
        data = await bookApi.filter({
          categoryId: filters.categoryId || undefined,
          available: filters.available || undefined,
          page: p,
          size: 8,
        });
      } else {
        data = await bookApi.list({ page: p, size: 8, sortBy: 'title', direction: 'asc' });
      }
      setPage(data);
      setPageNum(p);
    } catch (err) {
      toast.error(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    categoryApi.list().then(setCategories).catch(() => {});
    load(0);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const applyFilters = (e) => {
    e.preventDefault();
    load(0);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this book?')) return;
    try {
      await bookApi.remove(id);
      toast.success('Book deleted');
      load(pageNum);
    } catch (err) {
      toast.error(err.message);
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h3 className="mb-0">Book Catalog</h3>
        {isStaff && (
          <Link to="/books/new" className="btn btn-primary">
            <i className="bi bi-plus-lg me-1"></i>Add Book
          </Link>
        )}
      </div>

      <form className="row g-2 mb-4" onSubmit={applyFilters}>
        <div className="col-md-5">
          <input
            className="form-control"
            placeholder="Search title, author or ISBN..."
            value={filters.keyword}
            onChange={(e) => setFilters({ ...filters, keyword: e.target.value })}
          />
        </div>
        <div className="col-md-3">
          <select
            className="form-select"
            value={filters.categoryId}
            onChange={(e) => setFilters({ ...filters, categoryId: e.target.value, keyword: '' })}
          >
            <option value="">All categories</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>
        </div>
        <div className="col-md-2 d-flex align-items-center">
          <div className="form-check">
            <input
              className="form-check-input"
              type="checkbox"
              id="availOnly"
              checked={filters.available}
              onChange={(e) => setFilters({ ...filters, available: e.target.checked, keyword: '' })}
            />
            <label className="form-check-label" htmlFor="availOnly">Available only</label>
          </div>
        </div>
        <div className="col-md-2">
          <button className="btn btn-outline-primary w-100">Filter</button>
        </div>
      </form>

      {loading ? (
        <Loader />
      ) : !page || page.content.length === 0 ? (
        <EmptyState icon="bi-book" title="No books found" message="Try different filters." />
      ) : (
        <>
          <div className="row g-3">
            {page.content.map((b) => (
              <div className="col-6 col-md-4 col-lg-3" key={b.id}>
                <div className="card book-card">
                  <Link to={`/books/${b.id}`}>
                    <img
                      className="card-img-top book-cover"
                      src={b.imageUrl || 'https://placehold.co/300x180?text=No+Cover'}
                      alt={b.title}
                    />
                  </Link>
                  <div className="card-body">
                    <h6 className="card-title text-truncate">{b.title}</h6>
                    <small className="text-muted d-block text-truncate">{b.author}</small>
                    <RatingStars value={b.averageRating} />
                    <div className="mt-2">
                      <span className={`badge ${b.availableQuantity > 0 ? 'bg-success' : 'bg-secondary'}`}>
                        {b.availableQuantity > 0 ? `${b.availableQuantity} available` : 'Out of stock'}
                      </span>
                    </div>
                  </div>
                  <div className="card-footer bg-white d-flex gap-2">
                    <Link to={`/books/${b.id}`} className="btn btn-sm btn-outline-primary flex-grow-1">
                      Details
                    </Link>
                    {isStaff && (
                      <>
                        <Link to={`/books/${b.id}/edit`} className="btn btn-sm btn-outline-secondary">
                          <i className="bi bi-pencil"></i>
                        </Link>
                        <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(b.id)}>
                          <i className="bi bi-trash"></i>
                        </button>
                      </>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
          <Pagination page={page.page} totalPages={page.totalPages} onChange={load} />
        </>
      )}
    </div>
  );
}
