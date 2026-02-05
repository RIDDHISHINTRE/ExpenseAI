import { useState } from "react";
import Navbar from "../components/Navbar";
import ExpenseCard from "../components/expenses/ExpenseCard";
import AnalyticsSection from "../components/analytics/AnalyticsSection";

export default function Dashboard() {
  const [activeTab, setActiveTab] = useState("EXPENSES");

  return (
    <div className="min-h-screen bg-black text-white px-10 py-8">
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />

      {/* CONTENT */}
      {activeTab === "EXPENSES" && (
        <ExpenseCard />
      )}

      {activeTab === "ANALYTICS" && (
        <AnalyticsSection />
      )}
    </div>
  );
}
