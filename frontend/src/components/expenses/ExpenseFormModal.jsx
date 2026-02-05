import React, { useState, useEffect } from "react";

export default function ExpenseFormModal({ isOpen, onClose, onSubmit, expense }) {
  const [formData, setFormData] = useState({
    title: "",
    amount: "",
    category: "FOOD",
    date: "",
    description: "",
  });

  useEffect(() => {
    if (expense) {
      setFormData({
        title: expense.title,
        amount: expense.amount,
        category: expense.category,
        date: expense.date,
        description: expense.description,
      });
    } else {
      setFormData({
        title: "",
        amount: "",
        category: "FOOD",
        date: "",
        description: "",
      });
    }
  }, [expense]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-gray-900 p-6 rounded-xl w-full max-w-md relative">
        <h2 className="text-xl font-semibold mb-4">
          {expense ? "Edit Expense" : "Add Expense"}
        </h2>

        <form className="space-y-3" onSubmit={handleSubmit}>
          <input
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="Title"
            className="w-full px-3 py-2 rounded bg-gray-800 text-white"
            required
          />
          <input
            type="number"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            placeholder="Amount"
            className="w-full px-3 py-2 rounded bg-gray-800 text-white"
            required
          />
          <select
            name="category"
            value={formData.category}
            onChange={handleChange}
            className="w-full px-3 py-2 rounded bg-gray-800 text-white"
            required
          >
            <option value="FOOD">Food</option>
            <option value="TRAVEL">Travel</option>
            <option value="RENT">Rent</option>
            <option value="SHOPPING">Shopping</option>
            <option value="UTILITIES">Utilities</option>
            <option value="ENTERTAINMENT">Entertainment</option>
            <option value="HEALTH">Health</option>
            <option value="OTHER">Other</option>
          </select>
          <input
            type="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            className="w-full px-3 py-2 rounded bg-gray-800 text-white"
            required
          />
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Description"
            className="w-full px-3 py-2 rounded bg-gray-800 text-white"
          />
          <div className="flex justify-end gap-3 mt-3">
            <button
              type="button"
              onClick={onClose}
              className="bg-gray-600 px-4 py-2 rounded hover:bg-gray-700 transition"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="bg-indigo-600 px-4 py-2 rounded hover:bg-indigo-700 transition"
            >
              {expense ? "Update" : "Add"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
