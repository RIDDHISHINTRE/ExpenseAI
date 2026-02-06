// export default async function apiFetch(url, options = {}) {
//   const token = localStorage.getItem("token");

//   const headers = {
//     "Content-Type": "application/json",
//     ...(token && { Authorization: `Bearer ${token}` }),
//     ...options.headers,
//   };

//   const response = await fetch(url, { 
//     ...options,
//     headers,
//   });

//   if (!response.ok) {
//     const errorText = await response.text();
//     throw new Error(errorText || "Request failed");
//   }

//   return response.json();
// }
export default async function apiFetch(url, options = {}) {
  const token = localStorage.getItem("token");

  const headers = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  };

  const response = await fetch(url, {
    ...options,
    headers,
  });

  if (!response.ok) {
    let errorMessage = "Request failed";

    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorMessage;
    } catch (e) {
      // fallback if response is not JSON
    }

    throw new Error(errorMessage);
  }

  return response.json();
}
