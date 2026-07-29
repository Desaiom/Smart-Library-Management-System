import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";
import { authApi } from "../../api/authApi";
import SignupForm from "../../components/SignupForm";
import { useAuth } from "../../context/AuthContext";

export default function AdminRegister() {
  const navigate = useNavigate();
  const { registerAdmin } = useAuth();

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
    address: "",
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      await registerAdmin(form);

      toast.success("Admin account created");

      navigate("/");
    } catch (err) {
      toast.error(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <SignupForm
      title="Admin Signup"
      form={form}
      handleChange={handleChange}
      handleSubmit={handleSubmit}
      loading={loading}
    />
  );
}
