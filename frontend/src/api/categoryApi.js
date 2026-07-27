import api from './axios';

export const categoryApi = {
  list: () => api.get('/categories').then((r) => r.data.data),
  getById: (id) => api.get(`/categories/${id}`).then((r) => r.data.data),
  create: (payload) => api.post('/categories', payload).then((r) => r.data.data),
  update: (id, payload) => api.put(`/categories/${id}`, payload).then((r) => r.data.data),
  remove: (id) => api.delete(`/categories/${id}`).then((r) => r.data),
};
