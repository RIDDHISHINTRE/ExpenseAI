import { useEffect, useState } from "react";
import ExpenseFormModal from "./ExpenseFormModal";
import ExpenseSearch from "./ExpenseSearch";
import ExpenseTable from "./ExpenseTable";
import ExpensePagination from "./ExpensePagination";
import SetBudgetModal from "./SetBudgetModal";
import apiFetch from "../../utils/api";

export default function ExpenseCard() {
  const [expenses, setExpenses] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [search, setSearch] = useState({ title: "", category: "" });

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isBudgetModalOpen, setIsBudgetModalOpen] = useState(false);
  const [budgetMode, setBudgetMode] = useState("SET"); // SET | EDIT
  const [currentBudget, setCurrentBudget] = useState(null);

  const [editingExpense, setEditingExpense] = useState(null);

  const loadExpenses = async (pageNumber = 0, filters = search) => {
    const url = new URL("http://localhost:8080/expenses/search");
    url.searchParams.append("page", pageNumber);
    url.searchParams.append("size", 5);
    if (filters.title) url.searchParams.append("title", filters.title);
    if (filters.category) url.searchParams.append("category", filters.category);

    const data = await apiFetch(url);
    setExpenses(data.content || []);
    setTotalPages(data.totalPages || 1);
    setPage(pageNumber);
  };

  const openBudgetModal = async () => {
    try {
      const data = await apiFetch(
        "http://localhost:8080/monthly-budget/current"
      );

      setBudgetMode("EDIT");
      setCurrentBudget(data.budgetAmount);
    } catch (err) {
      setBudgetMode("SET");
      setCurrentBudget(null);
    }

    setIsBudgetModalOpen(true);
  };


  useEffect(() => {
    loadExpenses();
  }, []);

 const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this expense?")) return;

    try {
      await apiFetch(`http://localhost:8080/expenses/delete/${id}`, {
        method: "DELETE",
      });

      alert("Expense deleted successfully ✅");
      loadExpenses(page);
    } catch (err) {
      alert("Failed to delete expense ❌");
      console.error(err);
    }
  };


  const handleSubmit = async (data) => {
    const url = editingExpense
      ? `http://localhost:8080/expenses/update/${editingExpense.id}`
      : `http://localhost:8080/expenses/add`;
    
    try{
      await apiFetch(url, {
        method: editingExpense ? "PUT" : "POST",
        body: JSON.stringify(data),
      });

      setIsModalOpen(false);
      loadExpenses(page);
    }catch(err){
       alert(err.message);
    }
  };

  return (
    <div className="bg-gray-900 rounded-xl p-6 shadow-md">
      <ExpenseSearch
        search={search}
        onSearch={setSearch}
        onSubmit={() => loadExpenses(0, search)}
        onAdd={() => {
          setEditingExpense(null);
          setIsModalOpen(true);
        }}
        onSetBudget={openBudgetModal}
      />

     <SetBudgetModal
        isOpen={isBudgetModalOpen}
        onClose={() => setIsBudgetModalOpen(false)}
        mode={budgetMode}
        existingAmount={currentBudget}
     />


      <ExpenseTable
        expenses={expenses}
        onEdit={(e) => {
          setEditingExpense(e);
          setIsModalOpen(true);
        }}
        onDelete={handleDelete}
      />

      <ExpensePagination
        page={page}
        totalPages={totalPages}
        onPrev={() => loadExpenses(page - 1)}
        onNext={() => loadExpenses(page + 1)}
      />

      <ExpenseFormModal
        isOpen={isModalOpen}
        expense={editingExpense}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleSubmit}
      />

      

    </div>
  );
}
