export default function ExpensePagination({ page, totalPages, onPrev, onNext }) {
  return (
    <div className="flex justify-center gap-4 mt-4 text-gray-300">
      <button
        disabled={page === 0}
        onClick={onPrev}
        className="px-3 py-1 rounded bg-gray-700 disabled:opacity-50"
      >
        Prev
      </button>

      <span>
        Page {page + 1} of {totalPages}
      </span>

      <button
        disabled={page + 1 >= totalPages}
        onClick={onNext}
        className="px-3 py-1 rounded bg-gray-700 disabled:opacity-50"
      >
        Next
      </button>
    </div>
  );
}
