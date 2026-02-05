import { useContext } from "react";
import { AuthContext } from "../contexts/AuthContext";

export default function Navbar({ activeTab, setActiveTab }) {
  const { logout } = useContext(AuthContext);

  return (
    <div className="flex justify-between items-center mb-8">
      <h1 className="text-3xl font-bold text-white">
        Expense<span className="text-indigo-500">AI</span>
      </h1>

      <div className="flex gap-6 items-center">
        <button
          onClick={() => setActiveTab("EXPENSES")}
          className={`font-medium ${
            activeTab === "EXPENSES"
              ? "text-indigo-400"
              : "text-gray-400 hover:text-white"
          }`}
        >
          Expenses
        </button>

        <button
          onClick={() => setActiveTab("ANALYTICS")}
          className={`font-medium ${
            activeTab === "ANALYTICS"
              ? "text-indigo-400"
              : "text-gray-400 hover:text-white"
          }`}
        >
          Analytics
        </button>

        <button
          onClick={logout}
          className="bg-indigo-600 px-4 py-2 rounded-lg hover:bg-indigo-700 transition"
        >
          Logout
        </button>
      </div>
    </div>
  );
}
