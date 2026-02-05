export default function ExpenseSearch({ search, onSearch, onSubmit, onAdd }) {
  return (
    <>
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-semibold">All Expenses</h2>
        <button
          onClick={onAdd}
          className="bg-indigo-600 px-4 py-2 rounded-lg hover:bg-indigo-700"
        >
          Add Expense
        </button>
      </div>

      <div className="flex gap-4 mb-4">
        <input
          placeholder="Search title"
          value={search.title}
          onChange={(e) => onSearch({ ...search, title: e.target.value })}
          className="flex-1 px-4 py-2 rounded bg-gray-800"
        />

        <select
          value={search.category}
          onChange={(e) => onSearch({ ...search, category: e.target.value })}
          className="px-4 py-2 rounded bg-gray-800"
        >
          <option value="">All</option>
          <option value="FOOD">Food</option>
          <option value="TRAVEL">Travel</option>
          <option value="RENT">Rent</option>
          <option value="SHOPPING">Shopping</option>
          <option value="UTILITIES">Utilities</option>
          <option value="ENTERTAINMENT">Entertainment</option>
          <option value="HEALTH">Health</option>
          <option value="OTHER">Other</option>
        </select>

        <button
          onClick={onSubmit}
          className="bg-indigo-600 px-4 py-2 rounded hover:bg-indigo-700"
        >
          Search
        </button>
      </div>
    </>
  );
}
