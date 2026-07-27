import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { userApi } from '../api/userApi';
import { useAuth } from '../context/AuthContext';
import Loader from '../components/Loader';

const ROLES = ['USER', 'LIBRARIAN', 'ADMIN'];

export default function Users() {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    setLoading(true);
    userApi.list()
      .then(setUsers)
      .catch((err) => toast.error(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const changeRole = async (id, role) => {
    try {
      await userApi.patch(id, { role });
      toast.success('Role updated');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this user?')) return;
    try {
      await userApi.remove(id);
      toast.success('User deleted');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  if (loading) return <Loader />;

  return (
    <div>
      <h3 className="mb-3">Users</h3>
      <div className="table-responsive">
        <table className="table align-middle">
          <thead>
            <tr><th>#</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th><th></th></tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.name}</td>
                <td>{u.email}</td>
                <td>{u.phone || '—'}</td>
                <td style={{ maxWidth: 140 }}>
                  <select
                    className="form-select form-select-sm"
                    value={u.role}
                    disabled={u.id === user?.userId}
                    onChange={(e) => changeRole(u.id, e.target.value)}
                  >
                    {ROLES.map((r) => <option key={r} value={r}>{r}</option>)}
                  </select>
                </td>
                <td>
                  <button
                    className="btn btn-sm btn-outline-danger"
                    disabled={u.id === user?.userId}
                    onClick={() => handleDelete(u.id)}
                  >
                    <i className="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
