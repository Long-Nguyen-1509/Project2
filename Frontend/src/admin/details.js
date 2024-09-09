let allContentDivs = [];
let allButtons = [];
let downloadLink;
const jwt = localStorage.getItem("accessToken");
function getIdFromURL() {
  const urlSearchParams = new URLSearchParams(window.location.search);
  const idParam = urlSearchParams.get("id");
  return idParam;
}

async function fetchData() {
  const id = getIdFromURL();
  const url = `http://localhost:8080/api/info/lop-thi/${id}`;
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
  const buttonsContainer = document.getElementById("buttons");
  const container = document.getElementById("dataContainer");
  const data = await response.json();
  console.log(data);
  downloadLink = data._links.download.href;
  const details = document.getElementById("detailsList");
  details.innerHTML = `
                  <li>${data.id}</li>
                  <li>${data.maLopHoc}</li>
                  <li>${data.maHocPhan}</li>
                  <li>${data.tenHocPhan}</li>
                  <li>${data.nhomThi}</li>
                  <li>${data.kiHoc}</li>
                  <li>${data.khoaHoc}</li>
                  <li>${data.ngayThi}</li>
                `;
  details.classList.add("text-center", "font-bold", "grow");
  const listCaThi = data.listCT;
  for (let i = 0; i < listCaThi.length; i++) {
    const element = listCaThi[i];
    const div = document.createElement("div");
    div.id = `div${i}`;
    div.classList.add("absolute", "bottom-2", "left-2", "right-2", "top-2");
    div.innerHTML = `<ul class="text-xl">
                  <li>Kíp thi: ${element.kipThi}</li>
                  <li>Phòng thi: ${element.phongThi}</li>
                  <li>Số sinh viên: ${element.soSV}</li>
                  <li>Danh sách sinh viên:</li>
                </ul>`;
    const table = document.createElement("table");
    table.classList.add("w-full");
    table.innerHTML = `<thead class="text-left">
                  <th>STT</th>
                  <th>MSSV</th>
                  <th>Họ tên</th>
                  <th>Ngày sinh</th>
                  <th>Lớp sinh viên</th>
                </thead>`;
    const tbody = document.createElement("tbody");
    for (let j = 0; j < element.listSV.length; j++) {
      const sv = element.listSV[j];
      const tr = document.createElement("tr");
      tr.innerHTML = `<td>${sv.stt}</td>
                  <td>${sv.id}</td>
                  <td>${sv.hoTen}</td>
                  <td>${sv.ngaySinh}</td>
                  <td>${sv.lopSV}</td>`;
      tbody.appendChild(tr);
    }
    const style = document.createElement("style");
    style.innerHTML = `td,
                  th {
                    padding: 4px;
                    border: 1px solid black;
                  }`;
    table.appendChild(tbody, style);
    div.appendChild(table);
    container.appendChild(div);

    const button = document.createElement("button");
    button.classList.add(
      "w-6",
      "rounded-md",
      "border-2",
      "border-solid",
      "border-gray-300",
      "hover:bg-gray-300",
    );
    if (i == 0) {
      button.classList.add("bg-gray-300");
    } else {
      div.classList.add("hidden");
    }
    button.id = `button${i}`;
    button.textContent = `${i + 1}`;

    allContentDivs.push(div.id);
    allButtons.push(button.id);
    button.addEventListener("click", () => {
      allContentDivs.forEach((id) => {
        const tempDiv = document.getElementById(id);
        if (id !== div.id) {
          tempDiv.classList.add("hidden");
        }
      });

      div.classList.remove("hidden");

      allButtons.forEach((id) => {
        const tempButton = document.getElementById(id);
        if (id != button.id) {
          tempButton.classList.remove("bg-gray-300");
        }
      });
      button.classList.add("bg-gray-300");
    });
    buttonsContainer.appendChild(button);
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
  fetchData();
  const downloadButton = document.getElementById("download");
  downloadButton.addEventListener("click", download);
});
