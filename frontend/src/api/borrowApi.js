import api from './axios';

export const borrowApi = {
  borrow: (payload) => api.post('/borrows', payload).then((r) => r.data.data),
  returnBook: (id) => api.put(`/borrows/${id}/return`).then((r) => r.data.data),
  myBorrows: () => api.get('/borrows/me').then((r) => r.data.data),
  byUser: (userId) => api.get(`/borrows/user/${userId}`).then((r) => r.data.data),
  all: () => api.get('/borrows').then((r) => r.data.data),
  overdue: () => api.get('/borrows/overdue').then((r) => r.data.data),
};
