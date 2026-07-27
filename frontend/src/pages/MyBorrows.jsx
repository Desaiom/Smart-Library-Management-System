import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { borrowApi } from '../api/borrowApi';
import Loader from '../components/Loader';
import EmptyState from '../components/EmptyState';

function statusBadge(status) {
  const map = { BORROWED: 'bg-primary', RETURNED: 'bg-success', OVERDUE: 'bg-danger' };
  return map[status] || 'bg-secondary';
}

export default function MyBorrows() {
  const [borrows, setBorrows] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    setLoading(true);
    borrowApi.myBorrows()
      .then(setBorrows)
      .catch((err) => toast.error(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const handleReturn = async (id) => {
    try {
      const res = await borrowApi.returnBook(id);
      toast.success(res.fine > 0 ? `Returned. Fine: $${res.fine}` : 'Returned!');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  if (loading) return <Loader />;

  return (
    <div>
      <h3 className="mb-3">My Borrows</h3>
      {borrows.length === 0 ? (
        <EmptyState icon="bi-journal" title="No borrows yet" message="Borrow a book from the catalog." />
      ) : (
        <div className="table-responsive">
          <table className="table align-middle">
            <thead>
              <tr>
                <th>Book</th><th>Borrowed</th><th>Due</th><th>Returned</th>
                <th>Fine</th><th>Status</th><th></th>
              </tr>
            </thead>
            <tbody>
              {borrows.map((b) => (
                <tr key={b.id}>
                  <td>{b.bookTitle}</td>
                  <td>{b.borrowDate}</td>
                  <td>{b.dueDate}</td>
                  <td>{b.returnDate || '—'}</td>
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
