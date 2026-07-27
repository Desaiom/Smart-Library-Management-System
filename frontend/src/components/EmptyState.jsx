export default function EmptyState({ icon = 'bi-inbox', title = 'Nothing here yet', message }) {
  return (
    <div className="text-center text-muted py-5">
      <i className={`bi ${icon}`} style={{ fontSize: '2.5rem' }}></i>
      <h5 className="mt-3">{title}</h5>
      {message && <p className="mb-0">{message}</p>}
    </div>
  );
}
