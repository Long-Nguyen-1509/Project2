let downloadLink;
const jwt = localStorage.getItem("accessToken");
async function updateStatistics(date) {
  const table = document.getElementById("dataTable");
  const url = `http://localhost:8080/api/statistics/phong-thi/show?date=${encodeURIComponent(
    date,
  )}`;
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
    let i = 1;
    let j = 2;
    data._embedded.stringList.forEach((element) => {
      if (!document.getElementById(`cell${i}${j}`)) {
        i++;
        j = 2;
      }
      cell = document.getElementById(`cell${i}${j}`);
      cell.textContent = element;
      j++;
    });
    downloadLink = data._links.download.href;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
}

function updateChoice(params) {
  const date = params.get("date");
  if (date) {
    document.getElementById(`${date}`).selected = true;
    updateStatistics(date);
  }
}

function handleSubmit(event) {
  event.preventDefault();
  const choices = document.getElementById("dropdown");
  const date = choices.value;
  updateStatistics(date);
  const params = new URLSearchParams(window.location.search);
  params.set("date", `${date}`);
  const newUrl = `${window.location.pathname}?${params.toString()}`;
  window.history.pushState({}, "", newUrl);
}
async function fillStuffs() {
  const table = document.getElementById("dataTable");
  const dropdown = document.getElementById("dropdown");
  const kipThiData = await fetchData("http://localhost:8080/api/info/kip-thi");
  const phongThiData = await fetchData(
    "http://localhost:8080/api/info/phong-thi",
  );
  const ngayThiData = await fetchData(
    "http://localhost:8080/api/info/ngay-thi",
  );
  console.log(kipThiData);
  console.log(phongThiData);
  console.log(ngayThiData);
  const rows = kipThiData.length + 1;
  const columns = phongThiData.length + 2;
  for (let i = 0; i < rows; i++) {
    const row = document.createElement("tr");
    for (let j = 0; j < columns; j++) {
      const cell = document.createElement("td");
      cell.setAttribute("id", `cell${i}${j}`);
      row.appendChild(cell);
    }
    table.appendChild(row);
  }
  document.getElementById("cell00").textContent = "Kíp thi";
  document.getElementById("cell01").textContent = "STT kíp";
  for (let i = 0; i < phongThiData.length; i++) {
    document.getElementById(`cell0${i + 2}`).textContent = phongThiData[i];
  }
  for (let i = 0; i < kipThiData.length; i++) {
    document.getElementById(`cell${i + 1}0`).textContent = kipThiData[i];
    document.getElementById(`cell${i + 1}1`).textContent = i + 1;
  }

  ngayThiData.forEach((element) => {
    const option = document.createElement("option");
    option.value = element;
    option.id = element;
    option.textContent = element;
    dropdown.appendChild(option);
  });
}

async function fetchData(url) {
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
    return data;
  } catch (error) {
    console.log(error);
  }
}

async function download() {
  // const jwt = localStorage.getItem("accessToken");
  try {
    const response = await fetch(downloadLink, {
      method: "GET",
      headers: {
        "Content-type": "application/octet-stream",
        Authorization: `Bearer ${jwt}`,
      },
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    const filenameHeader = response.headers.get("Content-Disposition");
    const regex = /filename="([^"]+)"/;
    const matches = filenameHeader.match(regex);

    // Check if the file name is found and extract it
    let filename = null;
    if (matches && matches.length > 1) {
      filename = matches[1];
    }

    const blob = await response.blob();

    const objectURL = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = objectURL;
    link.download = filename;
    link.click();
  } catch (error) {
    console.error("Error fetching the API:", error);
  }
}

window.addEventListener("DOMContentLoaded", async () => {
  await fillStuffs();
  const urlParams = new URLSearchParams(window.location.search);
  updateChoice(urlParams);
  document.getElementById("submit").addEventListener("click", handleSubmit);
  document.getElementById("download").addEventListener("click", download);
});
