import api from './axios';

export const reviewApi = {
  byBook: (bookId) => api.get(`/reviews/book/${bookId}`).then((r) => r.data.data),
  add: (payload) => api.post('/reviews', payload).then((r) => r.data.data),
  remove: (id) => api.delete(`/reviews/${id}`).then((r) => r.data),
};
