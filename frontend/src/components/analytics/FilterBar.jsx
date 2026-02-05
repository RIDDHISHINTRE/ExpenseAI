export default function FilterBar({
  month,
  year,
  predictionMonths,
  setMonth,
  setYear,
  setPredictionMonths
}) {
  return (
    <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 flex gap-6">
      
      <div className="flex flex-col gap-1 w-40">
        <label className="text-sm text-zinc-400">Month</label>
        <select
          value={month}
          onChange={(e) => setMonth(Number(e.target.value))}
          className="bg-black border border-zinc-700 rounded-lg px-3 py-2"
        >
          {[...Array(12)].map((_, i) => (
            <option key={i} value={i + 1}>
              {new Date(0, i).toLocaleString("default", { month: "long" })}
            </option>
          ))}
        </select>
      </div>

      <div className="flex flex-col gap-1 w-32">
        <label className="text-sm text-zinc-400">Year</label>
        <input
          type="number"
          value={year}
          onChange={(e) => setYear(Number(e.target.value))}
          className="bg-black border border-zinc-700 rounded-lg px-3 py-2"
        />
      </div>

      <div className="flex flex-col gap-1 w-48">
        <label className="text-sm text-zinc-400">Prediction Period</label>
        <select
          value={predictionMonths}
          onChange={(e) => setPredictionMonths(Number(e.target.value))}
          className="bg-black border border-zinc-700 rounded-lg px-3 py-2"
        >
          <option value={2}>2 months</option>
          <option value={3}>3 months</option>
          <option value={4}>4 months</option>
          <option value={5}>5 months</option>
          <option value={6}>6 months</option>
          <option value={7}>7 months</option>
          <option value={8}>8 months</option>
          <option value={9}>9 months</option>
          <option value={10}>10 months</option>
          <option value={11}>11 months</option>
          <option value={12}>12 months</option>
        </select>
      </div>
    </div>
  );
}
