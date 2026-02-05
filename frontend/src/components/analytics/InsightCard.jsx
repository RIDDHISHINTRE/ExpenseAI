export default function InsightCard({
  title,
  children,
  onInsight,
  insight,
  loadingInsight
}) {
  return (
    <div className="bg-zinc-900 border border-zinc-800 rounded-xl p-6 space-y-4">
      
      {/* Header */}
      <h2 className="text-xl font-semibold flex items-center gap-2">
        {title}
      </h2>

      {/* Content */}
      <div className="space-y-2 text-sm text-zinc-200">
        {children}
      </div>

      {/* AI Button (ALWAYS AVAILABLE) */}
      {onInsight && (
        <button
          onClick={onInsight}
          disabled={loadingInsight}
          className={`px-5 py-2 rounded-lg text-sm transition
            ${loadingInsight
              ? "bg-zinc-700 cursor-not-allowed"
              : "bg-indigo-600 hover:bg-indigo-700"}
          `}
        >
          {loadingInsight
            ? "🤖 Thinking..."
            : insight
              ? "🔄 Regenerate Insight"
              : "🤖 Get AI Insight"}
        </button>
      )}

      {/* Insight */}
      {insight && (
        <div className="bg-indigo-600/10 border border-indigo-600 rounded-xl p-4 flex gap-3">
          <span className="text-indigo-400 mt-1">✨</span>
          <p className="text-sm text-indigo-300">{insight}</p>
        </div>
      )}
    </div>
  );
}
