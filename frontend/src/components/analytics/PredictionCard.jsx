import { useEffect, useState } from "react";
import apiFetch from "../../utils/api";
import InsightCard from "./InsightCard";
import { SummaryCard } from "./SummaryCard";

export default function PredictionCard({ months }) {
  const [data, setData] = useState(null);
  const [insight, setInsight] = useState("");
  const [loadingInsight, setLoadingInsight] = useState(false);

  useEffect(() => {
    apiFetch(
      `http://localhost:8080/insights/prediction?months=${months}`
    ).then(setData);
  }, [months]);

  const loadInsight = async () => {
    setLoadingInsight(true);
    const res = await apiFetch(
      `http://localhost:8080/insights/prediction/ai?months=${months}`
    );
    setInsight(res.insight);
    setLoadingInsight(false);
  };

  if (!data) return null;

  return (
    <InsightCard
      title="🔮 Expense Prediction based on Last N months"
      onInsight={loadInsight}
      insight={insight}
      loadingInsight={loadingInsight}
    >
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <SummaryCard title="Months Used" value={data.monthsUsed} />
        <SummaryCard title="Predicted Expense" value={`₹ ${data.predictedAmount}`} />
        <SummaryCard title="Trend" value={data.trend} />
      </div>
    </InsightCard>
  );
}
