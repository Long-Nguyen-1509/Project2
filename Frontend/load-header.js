// load-header.js
document.addEventListener("DOMContentLoaded", () => {
  // Get the current page URL
  const currentPage = window.location.pathname;

  const headerTemplates = {
    "/index.html": "src/components/test.html",
    "/admin/import.html": "src/components/import.html",
    "/admin/info/html": "src/components/info.html",
    "/admin/statistics/html": "src/components/statistics.html",
  };

  const headerFileName = headerTemplates[currentPage];
  fetch(headerFileName)
    .then((response) => response.text())
    .then((data) => {
      const headerContainer = document.getElementById("full-page");
      headerContainer.innerHTML = data;
    })
    .catch((error) => {
      console.error("Error loading header:", error);
    });
});
