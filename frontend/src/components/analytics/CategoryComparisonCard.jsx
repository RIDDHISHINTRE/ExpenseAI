import { useEffect, useState } from "react";
import apiFetch from "../../utils/api";
import InsightCard from "./InsightCard";
import { SummaryCard } from "./SummaryCard";

export default function CategoryComparisonCard({ month, year }) {
  const [data, setData] = useState(null);
  const [insight, setInsight] = useState("");
  const [loadingInsight, setLoadingInsight] = useState(false);

  useEffect(() => {
    apiFetch(
      `http://localhost:8080/insights/category-comparison?month=${month}&year=${year}`
    ).then(setData);
  }, [month, year]);

  const loadInsight = async () => {
    setLoadingInsight(true);
    const res = await apiFetch(
      `http://localhost:8080/insights/category-comparison/ai?month=${month}&year=${year}`
    );
    setInsight(res.insight);
    setLoadingInsight(false);
  };

  if (!data) return null;

  return (
    <InsightCard
      title="📂 Category Comparison With Previous Month"
      onInsight={loadInsight}
      insight={insight}
      loadingInsight={loadingInsight}
    >
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {data.comparisons.map((c) => (
          <SummaryCard
            key={c.category}
            title={c.category}
            value={`₹ ${c.currentMonthAmount}`}
            sub={c.trend}
          />
        ))}
      </div>
    </InsightCard>
  );
}
