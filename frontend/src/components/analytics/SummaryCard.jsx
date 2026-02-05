export function SummaryCard({ title, value, sub }) {
  return (
    <div className="bg-zinc-950 border border-zinc-800 rounded-xl p-4">
      <p className="text-sm text-zinc-400">{title}</p>
      <p className="text-2xl font-bold mt-1">{value}</p>
      {sub && <p className="text-sm text-zinc-400 mt-1">{sub}</p>}
    </div>
  );
}
