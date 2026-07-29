import api from "./axios";

export const bookApi = {
  list: (params) => api.get("/books", { params }).then((r) => r.data.data),
  search: (params) => api.get("/books/search", { params }).then((r) => r.data.data),
  filter: (params) => api.get("/books/filter", { params }).then((r) => r.data.data),
  getById: (id) => api.get(`/books/${id}`).then((r) => r.data.data),
  topBorrowed: (limit = 5) =>
    api.get("/books/top-borrowed", { params: { limit } }).then((r) => r.data.data),
  create: (formData) => api.post("/books", formData).then((r) => r.data.data),

  update: (id, formData) => api.put(`/books/${id}`, formData).then((r) => r.data.data),
  patch: (id, payload) => api.patch(`/books/${id}`, payload).then((r) => r.data.data),
  remove: (id) => api.delete(`/books/${id}`).then((r) => r.data),
};
