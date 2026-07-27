export default function RatingStars({ value = 0 }) {
  const rounded = Math.round(value || 0);
  return (
    <span className="rating-stars" title={value ? value.toFixed(1) : 'No ratings'}>
      {[1, 2, 3, 4, 5].map((i) => (
        <i key={i} className={`bi ${i <= rounded ? 'bi-star-fill' : 'bi-star'}`}></i>
      ))}
    </span>
  );
}
