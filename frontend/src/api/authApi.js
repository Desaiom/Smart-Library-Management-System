import api from "./axios";
export const authApi = {
  register: (payload) => api.post("/auth/register", payload).then((r) => r.data.data),

  registerAdmin: (payload) => api.post("/auth/register/admin", payload).then((r) => r.data.data),

  registerLibrarian: (payload) =>
    api.post("/auth/register/librarian", payload).then((r) => r.data.data),

  login: (payload) => api.post("/auth/login", payload).then((r) => r.data.data),

  logout: () => api.post("/auth/logout").then((r) => r.data),
};
