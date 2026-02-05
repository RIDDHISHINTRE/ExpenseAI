import { useState } from "react";
import FilterBar from "./FilterBar";
import MonthlyComparisonCard from "./MonthlyComparisonCard";
import CategoryComparisonCard from "./CategoryComparisonCard";
import PredictionCard from "./PredictionCard";
import MonthlyReportCard from "./MonthlyReportCard"

export default function AnalyticsSection() {
  const now = new Date();

  const [month, setMonth] = useState(now.getMonth() + 1);
  const [year, setYear] = useState(now.getFullYear());
  const [predictionMonths, setPredictionMonths] = useState(3);

  return (
    <div className="flex flex-col gap-8 w-full mt-6">
      <h1 className="text-2xl font-bold">Analytics & Insights</h1>

      <FilterBar
        month={month}
        year={year}
        predictionMonths={predictionMonths}
        setMonth={setMonth}
        setYear={setYear}
        setPredictionMonths={setPredictionMonths}
      />

      <MonthlyReportCard month={month} year={year} />

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <MonthlyComparisonCard month={month} year={year} />
        <CategoryComparisonCard month={month} year={year} />
      </div>

      <PredictionCard months={predictionMonths} />
    </div>
  );
}

