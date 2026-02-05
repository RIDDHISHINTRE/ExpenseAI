import { useState, useContext } from "react";
import { AuthContext } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";

export default function HomeAuth() {
  const [isLogin, setIsLogin] = useState(true);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const data = await res.json();

      if (!res.ok) {
        setError(data.message || "Invalid credentials");
      } else {
        login(data.user, data.token);
        navigate("/dashboard");
      }
    } catch {
      setError("Network error");
    }
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password }),
      });

      if (!res.ok) {
        const msg = await res.text();
        setError(msg);
      } else {
        setIsLogin(true);
        setError("Account created! Please login.");
      }
    } catch {
      setError("Network error");
    }
  };

  return (
    <div className="min-h-screen bg-black flex items-center">
      
      {/* Subtle background glow (no images, no text) */}
      <div className="absolute -top-40 -left-40 w-[500px] h-[500px] bg-indigo-600/20 rounded-full blur-3xl"></div>
      <div className="absolute bottom-0 right-0 w-[500px] h-[500px] bg-purple-600/20 rounded-full blur-3xl"></div>

      {/* Main container */}
      <div className="relative z-10 w-full max-w-7xl mx-auto px-10 grid grid-cols-1 md:grid-cols-2 gap-16">

        {/* LEFT – Branding */}
        <div className="flex flex-col justify-center space-y-6 text-white">
          <h1 className="text-5xl font-bold leading-tight">
            Expense<span className="text-indigo-500">AI</span>
          </h1>

          <p className="text-gray-300 max-w-lg text-lg">
            Smart expense tracking with AI-powered insights.  
            Track, analyze, and control your spending effortlessly
            with intelligent reports and predictions.
          </p>
        </div>

        {/* RIGHT – Auth box aligned to end */}
        <div className="flex justify-end items-center">
          <div className="w-full max-w-md bg-white rounded-xl shadow-2xl p-8">

            <h2 className="text-2xl font-bold text-center mb-6">
              {isLogin ? "Login" : "Create Account"}
            </h2>

            {error && (
              <p className="text-sm text-red-500 text-center mb-4">
                {error}
              </p>
            )}

            <form
              onSubmit={isLogin ? handleLogin : handleSignup}
              className="space-y-4"
            >
              {!isLogin && (
                <input
                  type="text"
                  placeholder="Name"
                  className="w-full border px-3 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                />
              )}

              <input
                type="email"
                placeholder="Email"
                className="w-full border px-3 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />

              <input
                type="password"
                placeholder="Password"
                className="w-full border px-3 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />

              <button className="w-full bg-indigo-600 text-white py-2 rounded-lg font-semibold hover:bg-indigo-700 transition">
                {isLogin ? "Login" : "Sign Up"}
              </button>
            </form>

            <p className="text-sm text-center mt-4">
              {isLogin ? "Don’t have an account?" : "Already have an account?"}{" "}
              <span
                className="text-indigo-600 font-medium cursor-pointer"
                onClick={() => setIsLogin(!isLogin)}
              >
                {isLogin ? "Sign up" : "Login"}
              </span>
            </p>

          </div>
        </div>

      </div>
    </div>
  );
}
