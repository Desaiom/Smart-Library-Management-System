import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { authApi } from "../../api/authApi";
import { useAuth } from "../../context/AuthContext";
import SignupForm from "../../components/SignupForm";

export default function LibrarianRegister() {
  const navigate = useNavigate();

  const { registerLibrarian } = useAuth();
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

      await registerLibrarian(form);

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
      title="Librarian Signup"
      form={form}
      handleChange={handleChange}
      handleSubmit={handleSubmit}
      loading={loading}
    />
  );
}
