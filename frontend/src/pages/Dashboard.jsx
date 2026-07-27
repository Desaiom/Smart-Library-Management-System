import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { dashboardApi } from '../api/dashboardApi';
import Loader from '../components/Loader';

const MONTHS = ['', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

function StatCard({ icon, label, value, color }) {
  return (
    <div className="col-6 col-md-3">
      <div className={`card stat-card text-white shadow-sm ${color}`}>
        <div className="card-body d-flex align-items-center justify-content-between">
          <div>
            <div className="h3 mb-0">{value}</div>
            <small>{label}</small>
          </div>
          <i className={`bi ${icon} stat-icon`}></i>
        </div>
      </div>
    </div>
  );
}

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dashboardApi.stats()
      .then(setStats)
      .catch((err) => toast.error(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Loader />;
  if (!stats) return <p>No data.</p>;

  const maxMonthly = Math.max(1, ...(stats.monthlyBorrowStatistics || []).map((m) => m.total));

  return (
    <div>
      <h3 className="mb-4">Dashboard</h3>
      <div className="row g-3 mb-4">
        <StatCard icon="bi-book" label="Total Books" value={stats.totalBooks} color="bg-primary" />
        <StatCard icon="bi-check-circle" label="Available Copies" value={stats.availableBooks} color="bg-success" />
        <StatCard icon="bi-journal-arrow-up" label="Active Borrows" value={stats.activeBorrowings} color="bg-warning" />
        <StatCard icon="bi-exclamation-triangle" label="Overdue" value={stats.overdueBorrowings} color="bg-danger" />
        <StatCard icon="bi-people" label="Users" value={stats.totalUsers} color="bg-info" />
        <StatCard icon="bi-tags" label="Categories" value={stats.totalCategories} color="bg-secondary" />
        <StatCard icon="bi-collection" label="Borrowed Books" value={stats.borrowedBooks} color="bg-dark" />
      </div>

      <div className="card">
        <div className="card-header">Monthly Borrow Statistics</div>
        <div className="card-body">
          {(!stats.monthlyBorrowStatistics || stats.monthlyBorrowStatistics.length === 0) ? (
            <p className="text-muted mb-0">No borrow activity yet.</p>
          ) : (
            stats.monthlyBorrowStatistics.map((m, idx) => (
              <div className="d-flex align-items-center mb-2" key={idx}>
                <div style={{ width: 90 }} className="small text-muted">
                  {MONTHS[m.month]} {m.year}
                </div>
                <div className="progress flex-grow-1" style={{ height: 20 }}>
                  <div
                    className="progress-bar bg-primary"
                    style={{ width: `${(m.total / maxMonthly) * 100}%` }}
                  >
                    {m.total}
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
