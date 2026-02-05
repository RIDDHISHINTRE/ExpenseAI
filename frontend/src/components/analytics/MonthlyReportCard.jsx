import { useEffect, useState } from "react";
import apiFetch from "../../utils/api";
import InsightCard from "./InsightCard";
import { SummaryCard } from "./SummaryCard";

export default function MonthlyReportCard({ month, year }) {
  const [data, setData] = useState(null);
  const [insight, setInsight] = useState("");
  const [loadingInsight, setLoadingInsight] = useState(false);

  useEffect(() => {
    apiFetch(
      `http://localhost:8080/insights/monthly-report?month=${month}&year=${year}`
    ).then(setData);
  }, [month, year]);

  const loadInsight = async () => {
    setLoadingInsight(true);
    const res = await apiFetch(
      `http://localhost:8080/insights/monthly-report/ai?month=${month}&year=${year}`
    );
    setInsight(res.insight);
    setLoadingInsight(false);
  };

  if (!data) return null;

  const highestExpense = data.highestSingleExpense;

  return (
    <InsightCard
      title="📊 Monthly Report"
      onInsight={loadInsight}
      insight={insight}
      loadingInsight={loadingInsight}
    >
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <SummaryCard
          title="Total Expenses"
          value={`₹ ${data.totalExpense}`}
          sub={`${month}/${year}`}
        />
        <SummaryCard
          title="Transactions"
          value={data.transactionCount}
          sub="Total count"
        />
        <SummaryCard
          title="Highest Category"
          value={data.highestSpendingCategory ?? "N/A"}
        />
        <SummaryCard
          title="Biggest Expense"
          value={`₹ ${highestExpense?.amount ?? 0}`}
          sub={highestExpense?.category ?? "No expenses"}
        />
      </div>
    </InsightCard>
  );
}
