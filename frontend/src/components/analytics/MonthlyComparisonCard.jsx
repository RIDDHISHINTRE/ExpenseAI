import { useEffect, useState } from "react";
import apiFetch from "../../utils/api";
import InsightCard from "./InsightCard";
import { SummaryCard } from "./SummaryCard";

export default function MonthlyComparisonCard({ month, year }) {
  const [data, setData] = useState(null);
  const [insight, setInsight] = useState("");
  const [loadingInsight, setLoadingInsight] = useState(false);

  useEffect(() => {
    apiFetch(
      `http://localhost:8080/insights/monthly-comparison?month=${month}&year=${year}`
    ).then(setData);
  }, [month, year]);

  const loadInsight = async () => {
    setLoadingInsight(true);
    const res = await apiFetch(
      `http://localhost:8080/insights/monthly-comparison/ai?month=${month}&year=${year}`
    );
    setInsight(res.insight);
    setLoadingInsight(false);
  };

  if (!data) return null;

  return (
    <InsightCard
      title="📈 Total Comparison With Previous Month"
      onInsight={loadInsight}
      insight={insight}
      loadingInsight={loadingInsight}
    >
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <SummaryCard title="Current Month" value={`₹ ${data.currentMonthTotal}`} />
        <SummaryCard title="Previous Month" value={`₹ ${data.previousMonthTotal}`} />
        <SummaryCard
          title="Change"
          value={`${data.percentageChange}%`}
          sub={data.trend}
        />
      </div>
    </InsightCard>
  );
}
