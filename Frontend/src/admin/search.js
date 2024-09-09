const jwt = localStorage.getItem("accessToken");
async function fetchDataWithParams(param1, param2, param3, param4, page) {
  const url = `http://localhost:8080/api/admin/lop-thi/search?idLopThi=${encodeURIComponent(
    param1,
  )}&idLopHoc=${encodeURIComponent(param2)}&idHocPhan=${encodeURIComponent(
    param3,
  )}&tenHocPhan=${encodeURIComponent(param4)}&page=${encodeURIComponent(page)}`;
  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${jwt}`,
      },
    });
    if (response.status == 403) {
      alert("Phiên làm việc đã hết hạn vui lòng đăng nhập lại");
      window.location.href = "../../login.html";
    }
    const data = await response.json();
    console.log(data);
    const dataBody = document.getElementById("dataBody");
    dataBody.innerHTML = "";
    const list = data._embedded.lopThiCompactList;
    list.forEach((element) => {
      console.log(element);
      const row = document.createElement("tr");
      row.innerHTML = `
      <td>${element.idLopThi}</td>
      <td>${element.idLopHoc}</td>
      <td>${element.idHocPhan}</td>
      <td>${element.tenHocPhan}</td>
      <td>${element.kiHoc}</td>
      <td>${element.khoaHoc}</td>
      <td>${element.nhomThi}</td>
      <td>${element.ngayThi}</td>
      <td>${element.soSV}</td>
    `;
      row.addEventListener("click", openDetailsPage);
      dataBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
}

// Function to update form fields based on URL parameters
function updateFormWithParams(params) {
  const param1 = params.get("idLopThi") || "";
  const param2 = params.get("idLopHoc") || "";
  const param3 = params.get("idHocPhan") || "";
  const param4 = params.get("tenHocPhan") || "";
  const page = params.get("page") || "1";

  document.getElementById("idLopThi").value = param1;
  document.getElementById("idLopHoc").value = param2;
  document.getElementById("idHocPhan").value = param3;
  document.getElementById("tenHocPhan").value = param4;
  document.getElementById("page").value = page;
  fetchDataWithParams(param1, param2, param3, param4, page);
}

// Function to handle form submission
function handleFormSubmit(event) {
  event.preventDefault(); // Prevent page reload

  const formData = new FormData(event.target);
  const param1 = formData.get("idLopThi");
  const param2 = formData.get("idLopHoc");
  const param3 = formData.get("idHocPhan");
  const param4 = formData.get("tenHocPhan");
  const page = parseInt(formData.get("page"));

  fetchDataWithParams(param1, param2, param3, param4, page);

  const urlParams = new URLSearchParams(formData);
  const newURL = `?${urlParams.toString()}`;
  window.history.pushState(null, "", newURL);
}

// Function to handle pagination buttons
function handlePaginationButtonClick(event) {
  const urlParams = new URLSearchParams(window.location.search);
  console.log(urlParams.get("page"));
  let currentPage = parseInt(urlParams.get("page"));
  const page = document.getElementById("page");
  if (event.target.id === "prevPage") {
    currentPage = Math.max(1, currentPage - 1);
  } else if (event.target.id === "nextPage") {
    currentPage++;
  }
  page.value = currentPage.toString();
  document.getElementById("searchForm").submit();
}

function openDetailsPage(event) {
  const targetRow = event.target.closest("tr");
  if (!targetRow || targetRow.tagName !== "TR") return;

  const id = targetRow.cells[0].textContent;

  window.location.href = `details.html?id=${encodeURIComponent(id)}`;
}

window.addEventListener("DOMContentLoaded", () => {
  const urlParams = new URLSearchParams(window.location.search);
  updateFormWithParams(urlParams);
  isFirstLoad = false;
  console.log("repeat");

  document
    .getElementById("prevPage")
    .addEventListener("click", handlePaginationButtonClick);
  document
    .getElementById("nextPage")
    .addEventListener("click", handlePaginationButtonClick);
  document
    .getElementById("searchForm")
    .addEventListener("submit", handleFormSubmit);
});
