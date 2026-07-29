import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { authApi } from "../api/authApi";
import { TOKEN_KEY, USER_KEY } from "../api/axios";

const AuthContext = createContext(null);

/**
 * Global authentication state.
 *
 * Stores the JWT + user in localStorage so sessions survive refreshes, and
 * exposes login/register/logout plus role helpers to the whole app.
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  });
  const [loading, setLoading] = useState(false);

  const persist = (data) => {
    const { token, ...rest } = data;
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(rest));
    setUser(rest);
  };

  const login = async (credentials) => {
    setLoading(true);
    try {
      const data = await authApi.login(credentials);
      persist(data);
      return data;
    } finally {
      setLoading(false);
    }
  };

  const register = async (payload) => {
    setLoading(true);
    try {
      const data = await authApi.register(payload);
      persist(data);
      return data;
    } finally {
      setLoading(false);
    }
  };
  const registerAdmin = async (payload) => {
    setLoading(true);
    try {
      const data = await authApi.registerAdmin(payload);
      persist(data);
      return data;
    } finally {
      setLoading(false);
    }
  };

  const registerLibrarian = async (payload) => {
    setLoading(true);
    try {
      const data = await authApi.registerLibrarian(payload);
      persist(data);
      return data;
    } finally {
      setLoading(false);
    }
  };
  const logout = async () => {
    try {
      await authApi.logout();
    } catch {
      // ignore network errors on logout
    }
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setUser(null);
  };

  useEffect(() => {
    const onStorage = () => {
      const raw = localStorage.getItem(USER_KEY);
      setUser(raw ? JSON.parse(raw) : null);
    };
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
  }, []);

  const value = useMemo(() => {
    const role = user?.role;

    return {
      user,
      loading,
      login,
      register,
      registerAdmin,
      registerLibrarian,
      logout,
      isAuthenticated: !!user,
      isAdmin: role === "ADMIN",
      isLibrarian: role === "LIBRARIAN",
      isStaff: role === "ADMIN" || role === "LIBRARIAN",
    };
  }, [user, loading]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within an AuthProvider");
  return ctx;
}
