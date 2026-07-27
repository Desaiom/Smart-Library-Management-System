import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { categoryApi } from '../api/categoryApi';
import Loader from '../components/Loader';

const EMPTY = { name: '', description: '' };

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState(EMPTY);
  const [editingId, setEditingId] = useState(null);

  const load = () => {
    setLoading(true);
    categoryApi.list()
      .then(setCategories)
      .catch((err) => toast.error(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await categoryApi.update(editingId, form);
        toast.success('Category updated');
      } else {
        await categoryApi.create(form);
        toast.success('Category created');
      }
      setForm(EMPTY);
      setEditingId(null);
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  const startEdit = (c) => {
    setEditingId(c.id);
    setForm({ name: c.name, description: c.description || '' });
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this category?')) return;
    try {
      await categoryApi.remove(id);
      toast.success('Deleted');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  return (
    <div className="row g-4">
      <div className="col-md-4">
        <h5>{editingId ? 'Edit Category' : 'New Category'}</h5>
        <form className="card card-body" onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Name</label>
            <input className="form-control" value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          </div>
          <div className="mb-3">
            <label className="form-label">Description</label>
            <textarea className="form-control" rows="2" value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}></textarea>
          </div>
          <div className="d-flex gap-2">
            <button className="btn btn-primary">{editingId ? 'Update' : 'Create'}</button>
            {editingId && (
              <button type="button" className="btn btn-outline-secondary"
                onClick={() => { setEditingId(null); setForm(EMPTY); }}>
                Cancel
              </button>
            )}
          </div>
        </form>
      </div>
      <div className="col-md-8">
        <h5>Categories</h5>
        {loading ? (
          <Loader />
        ) : (
          <table className="table align-middle">
            <thead><tr><th>Name</th><th>Description</th><th>Books</th><th></th></tr></thead>
            <tbody>
              {categories.map((c) => (
                <tr key={c.id}>
                  <td>{c.name}</td>
                  <td className="small text-muted">{c.description}</td>
                  <td>{c.bookCount}</td>
                  <td className="text-end">
                    <button className="btn btn-sm btn-outline-secondary me-1" onClick={() => startEdit(c)}>
                      <i className="bi bi-pencil"></i>
                    </button>
                    <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(c.id)}>
                      <i className="bi bi-trash"></i>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
