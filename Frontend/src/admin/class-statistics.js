let downloadLink;
const jwt = localStorage.getItem("accessToken");
async function updateStatistics(date) {
  const dataDiv = document.getElementById("dataDiv");
  const url = `http://localhost:8080/api/statistics/lop-thi/show?date=${encodeURIComponent(
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
    data._embedded.lopThiStatisticsList.forEach((element) => {
      const table = document.createElement("table");
      table.classList.add("w-full");
      const header = document.createElement("tr");
      header.innerHTML = `<td>Mã lớp thi</td>
                        <td colspan ="2">Kíp thi</td>
                        <td>Phòng thi</td>
                        <td>Số SV</td>
                        <td>Giảng viên lý thuyết</td>`;
      table.appendChild(header);
      const listCT = element.listCT;
      for (let i = 0; i < listCT.length; i++) {
        const innerElement = listCT[i];
        const tr = document.createElement("tr");
        if (i == 0) {
          tr.innerHTML = `<td>${element.id}</td>`;
        } else {
          tr.innerHTML = `<td></td>`;
        }
        tr.innerHTML += `<td>${innerElement.gioThi}</td>
                        <td>${innerElement.kipThi}</td>
                        <td>${innerElement.phongThi}</td>
                        <td>${innerElement.soSV}</td>
                        <td></td>`;
        table.appendChild(tr);
      }
      dataDiv.appendChild(table);
      const br = document.createElement("br");
      dataDiv.appendChild(br);
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
  const dropdown = document.getElementById("dropdown");
  const ngayThiData = await fetchData(
    "http://localhost:8080/api/info/ngay-thi",
  );

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
    if (!response.ok) {
      throw new Error("Response was not OK");
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

    if (response.status == 403) {
      alert("Phiên làm việc đã hết hạn vui lòng đăng nhập lại");
      window.location.href = "../../login.html";
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
