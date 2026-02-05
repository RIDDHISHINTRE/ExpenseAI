export default function ExpenseTable({ expenses, onEdit, onDelete }) {
  return (
    <div className="overflow-x-auto">
      <table className="w-full border border-gray-700">
        <thead className="bg-gray-800 text-gray-300">
          <tr>
            <th className="px-4 py-2">Title</th>
            <th className="px-4 py-2">Amount</th>
            <th className="px-4 py-2">Category</th>
            <th className="px-4 py-2">Date</th>
            <th className="px-4 py-2">Description</th>
            <th className="px-4 py-2">Actions</th>
          </tr>
        </thead>

        <tbody>
          {expenses.length === 0 ? (
            <tr>
              <td colSpan="6" className="text-center py-4 text-gray-400">
                No expenses found
              </td>
            </tr>
          ) : (
            expenses.map((e) => (
              <tr key={e.id} className="border-t border-gray-700">
                <td className="px-4 py-2 text-center">{e.title}</td>
                <td className="px-4 py-2 text-center">₹{e.amount}</td>
                <td className="px-4 py-2 text-center">{e.category}</td>
                <td className="px-4 py-2 text-center">
                  {new Date(e.date).toLocaleDateString()}
                </td>
                <td className="px-4 py-2 text-center">{e.description}</td>
                <td className="px-4 py-2 flex gap-2 text-center">
                  <button
                    onClick={() => onEdit(e)}
                    className="text-sm px-3 py-1 rounded bg-gray-700 hover:bg-gray-600 text-center"
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => onDelete(e.id)}
                    className="text-sm px-3 py-1 rounded bg-gray-700 hover:bg-red-600 text-center"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
