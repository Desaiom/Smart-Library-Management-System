import axios from 'axios';
import { toast } from 'react-toastify';

/**
 * Central Axios instance.
 *
 * - Base URL comes from VITE_API_BASE_URL (defaults to /api via the Vite proxy).
 * - A request interceptor attaches the JWT from localStorage.
 * - A response interceptor surfaces API errors and handles 401 by logging out.
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  headers: { 'Content-Type': 'application/json' },
});

export const TOKEN_KEY = 'sl_token';
export const USER_KEY = 'sl_user';

api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const message =
      error.response?.data?.message ||
      error.response?.data?.error ||
      error.message ||
      'Unexpected error';

    if (status === 401) {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
      if (!window.location.pathname.startsWith('/login')) {
        toast.error('Session expired. Please log in again.');
        window.location.href = '/login';
      }
    } else if (status === 403) {
      toast.error('You do not have permission to perform this action.');
    }

    return Promise.reject(new Error(message));
  }
);

export default api;
