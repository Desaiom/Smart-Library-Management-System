import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { borrowApi } from '../api/borrowApi';
import Loader from '../components/Loader';
import EmptyState from '../components/EmptyState';

function statusBadge(status) {
  const map = { BORROWED: 'bg-primary', RETURNED: 'bg-success', OVERDUE: 'bg-danger' };
  return map[status] || 'bg-secondary';
}

export default function ManageBorrows() {
  const [borrows, setBorrows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState('all');

  const load = (which = tab) => {
    setLoading(true);
    const fetcher = which === 'overdue' ? borrowApi.overdue() : borrowApi.all();
    fetcher
      .then(setBorrows)
      .catch((err) => toast.error(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => load(tab), [tab]);

  const handleReturn = async (id) => {
    try {
      await borrowApi.returnBook(id);
      toast.success('Marked as returned');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  return (
    <div>
      <h3 className="mb-3">All Borrows</h3>
      <ul className="nav nav-pills mb-3">
        <li className="nav-item">
          <button className={`nav-link ${tab === 'all' ? 'active' : ''}`} onClick={() => setTab('all')}>All</button>
        </li>
        <li className="nav-item">
          <button className={`nav-link ${tab === 'overdue' ? 'active' : ''}`} onClick={() => setTab('overdue')}>Overdue</button>
        </li>
      </ul>

      {loading ? (
        <Loader />
      ) : borrows.length === 0 ? (
        <EmptyState icon="bi-journal-check" title="Nothing to show" />
      ) : (
        <div className="table-responsive">
          <table className="table align-middle">
            <thead>
              <tr>
                <th>#</th><th>Member</th><th>Book</th><th>Borrowed</th>
                <th>Due</th><th>Fine</th><th>Status</th><th></th>
              </tr>
            </thead>
            <tbody>
              {borrows.map((b) => (
                <tr key={b.id}>
                  <td>{b.id}</td>
                  <td>{b.userName}</td>
                  <td>{b.bookTitle}</td>
                  <td>{b.borrowDate}</td>
                  <td>{b.dueDate}</td>
                  <td>{b.fine > 0 ? `$${b.fine}` : '—'}</td>
                  <td><span className={`badge ${statusBadge(b.status)}`}>{b.status}</span></td>
                  <td>
                    {b.status !== 'RETURNED' && (
                      <button className="btn btn-sm btn-outline-success" onClick={() => handleReturn(b.id)}>
                        Return
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
