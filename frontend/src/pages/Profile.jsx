import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import api from '../api/axios';
import { userApi } from '../api/userApi';
import { useAuth } from '../context/AuthContext';
import Loader from '../components/Loader';

export default function Profile() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({ name: '', phone: '', address: '' });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    api.get('/users/me')
      .then((r) => {
        const data = r.data.data;
        setProfile(data);
        setForm({ name: data.name || '', phone: data.phone || '', address: data.address || '' });
      })
      .catch((err) => toast.error(err.message))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!profile) return;
    setSaving(true);
    try {
      const updated = await userApi.update(profile.id, form);
      setProfile(updated);
      toast.success('Profile updated');
    } catch (err) {
      toast.error(err.message);
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <Loader />;
  if (!profile) return <p>Unable to load profile.</p>;

  return (
    <div className="row justify-content-center">
      <div className="col-md-6">
        <h3 className="mb-3">My Profile</h3>
        <div className="card card-body">
          <p className="mb-1"><strong>Email:</strong> {profile.email}</p>
          <p><strong>Role:</strong> <span className="badge bg-secondary">{profile.role || user?.role}</span></p>
          <hr />
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Name</label>
              <input className="form-control" value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })} required />
            </div>
            <div className="mb-3">
              <label className="form-label">Phone</label>
              <input className="form-control" value={form.phone}
                onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            </div>
            <div className="mb-3">
              <label className="form-label">Address</label>
              <input className="form-control" value={form.address}
                onChange={(e) => setForm({ ...form, address: e.target.value })} />
            </div>
            <button className="btn btn-primary" disabled={saving}>
              {saving ? 'Saving...' : 'Save Changes'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
