import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { bookApi } from '../api/bookApi';
import { categoryApi } from '../api/categoryApi';
import Loader from '../components/Loader';

const EMPTY = {
  title: '', author: '', isbn: '', description: '', quantity: 1,
  price: '', publicationYear: '', imageUrl: '', categoryId: '',
};

export default function BookForm() {
  const { id } = useParams();
  const editing = Boolean(id);
  const navigate = useNavigate();
  const [form, setForm] = useState(EMPTY);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(editing);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    categoryApi.list().then(setCategories).catch(() => {});
    if (editing) {
      bookApi.getById(id)
        .then((b) => setForm({
          title: b.title || '', author: b.author || '', isbn: b.isbn || '',
          description: b.description || '', quantity: b.quantity ?? 1,
          price: b.price ?? '', publicationYear: b.publicationYear ?? '',
          imageUrl: b.imageUrl || '', categoryId: b.categoryId ?? '',
        }))
        .catch((err) => toast.error(err.message))
        .finally(() => setLoading(false));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    const payload = {
      ...form,
      quantity: Number(form.quantity),
      price: form.price === '' ? null : Number(form.price),
      publicationYear: form.publicationYear === '' ? null : Number(form.publicationYear),
      categoryId: form.categoryId === '' ? null : Number(form.categoryId),
    };
    try {
      if (editing) {
        await bookApi.update(id, payload);
        toast.success('Book updated');
      } else {
        await bookApi.create(payload);
        toast.success('Book created');
      }
      navigate('/books');
    } catch (err) {
      toast.error(err.message);
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <Loader />;

  return (
    <div className="row justify-content-center">
      <div className="col-md-8">
        <h3 className="mb-3">{editing ? 'Edit Book' : 'Add Book'}</h3>
        <form className="card card-body" onSubmit={handleSubmit}>
          <div className="row">
            <div className="col-md-8 mb-3">
              <label className="form-label">Title</label>
              <input name="title" className="form-control" value={form.title} onChange={handleChange} required />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">ISBN</label>
              <input name="isbn" className="form-control" value={form.isbn} onChange={handleChange} required />
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">Author</label>
            <input name="author" className="form-control" value={form.author} onChange={handleChange} required />
          </div>
          <div className="mb-3">
            <label className="form-label">Description</label>
            <textarea name="description" rows="3" className="form-control" value={form.description} onChange={handleChange}></textarea>
          </div>
          <div className="row">
            <div className="col-md-3 mb-3">
              <label className="form-label">Quantity</label>
              <input type="number" name="quantity" min="0" className="form-control" value={form.quantity} onChange={handleChange} required />
            </div>
            <div className="col-md-3 mb-3">
              <label className="form-label">Price</label>
              <input type="number" step="0.01" name="price" className="form-control" value={form.price} onChange={handleChange} />
            </div>
            <div className="col-md-3 mb-3">
              <label className="form-label">Year</label>
              <input type="number" name="publicationYear" className="form-control" value={form.publicationYear} onChange={handleChange} />
            </div>
            <div className="col-md-3 mb-3">
              <label className="form-label">Category</label>
              <select name="categoryId" className="form-select" value={form.categoryId} onChange={handleChange}>
                <option value="">None</option>
                {categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
              </select>
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">Cover Image URL</label>
            <input name="imageUrl" className="form-control" value={form.imageUrl} onChange={handleChange} />
          </div>
          <div className="d-flex gap-2">
            <button className="btn btn-primary" disabled={saving}>
              {saving ? 'Saving...' : 'Save'}
            </button>
            <button type="button" className="btn btn-outline-secondary" onClick={() => navigate('/books')}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
