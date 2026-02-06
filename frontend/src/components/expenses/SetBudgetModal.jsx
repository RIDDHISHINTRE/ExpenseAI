import { useEffect, useState } from "react";
import apiFetch from "../../utils/api";

export default function SetBudgetModal({
  isOpen,
  onClose,
  mode,
  existingAmount,
}) {
  const [amount, setAmount] = useState("");

  useEffect(() => {
    if (mode === "EDIT") {
      setAmount(existingAmount);
    } else {
      setAmount("");
    }
  }, [mode, existingAmount]);

  if (!isOpen) return null;

  const now = new Date();
  const month = now.getMonth() + 1;
  const year = now.getFullYear();

  const handleSubmit = async (e) => {
    e.preventDefault();

    const url =
      mode === "EDIT"
        ? "http://localhost:8080/monthly-budget/edit"
        : "http://localhost:8080/monthly-budget/set";

    try {
      await apiFetch(url, {
        method: mode === "EDIT" ? "PUT" : "POST",
        body: JSON.stringify({
          month,
          year,
          budgetAmount: amount,
        }),
      });

      alert(
        mode === "EDIT"
          ? "Budget updated successfully ✅"
          : "Budget set successfully ✅"
      );
      onClose();
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-gray-900 p-6 rounded-xl w-full max-w-sm">
        <h2 className="text-xl font-semibold mb-4">
          {mode === "EDIT" ? "Edit Monthly Budget" : "Set Monthly Budget"}
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="Enter budget amount"
            className="w-full px-3 py-2 rounded bg-gray-800 text-white"
            required
          />

          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="bg-gray-600 px-4 py-2 rounded"
            >
              Cancel
            </button>

            <button
              type="submit"
              className="bg-green-600 px-4 py-2 rounded"
            >
              {mode === "EDIT" ? "Update" : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
