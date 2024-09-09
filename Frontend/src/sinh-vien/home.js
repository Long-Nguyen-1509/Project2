document.addEventListener("DOMContentLoaded", async () => {
  const tableBody = document.querySelector("#data-table tbody");
  const nameData = document.querySelector("#name-data");
  const idData = document.querySelector("#id-data");
  const classData = document.querySelector("#class-data");
  const birthData = document.querySelector("#birth-data");
  const sexData = document.querySelector("#sex-data");
  const emailData = document.querySelector("#email-data");
  const button = document.querySelector("#download-button");
  const jwt = localStorage.getItem("accessToken");

  try {
    // Fetch data from the API endpoint
    const response = await fetch(
      "http://localhost:8080/api/sinh-vien/details/show",
      {
        method: "GET",
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
      },
    );
    const data = await response.json();
    nameData.textContent =
      data.hoTen != null ? data.hoTen : "Không có thông tin";
    idData.textContent = data.id != null ? data.id : "Không có thông tin";
    classData.textContent =
      data.lopSV != null ? data.lopSV : "Không có thông tin";
    birthData.textContent =
      data.ngaySinh != null ? data.ngaySinh : "Không có thông tin";
    sexData.textContent =
      data.gioiTinh != null ? data.gioiTinh : "Không có thông tin";
    emailData.textContent =
      data.email != null ? data.email : "Không có thông tin";
    localStorage.setItem("download", data._links.download.href);
    listData = data.lopThiOfSinhVien;
    let counter = 1;
    listData.forEach((item) => {
      const row = document.createElement("tr");
      const hpData = `${item.tenHocPhan} - ${item.hocPhanId} - ${item.lopHocId}`;
      row.innerHTML = `
                <td>${counter}</td>
                <td>${hpData}</td>
                <td>${item.id}</td>
                <td>${item.khoaHoc}</td>
                <td>${item.ngayThi}</td>
                <td>${item.kipThiId}</td>
                <td>${item.phongThi}</td>
            `;
      tableBody.appendChild(row);
      counter++;
    });
    document.getElementById("logout-button").addEventListener("click", () => {
      fetch("http://localhost:8080/api/auth/logout", {
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
      });
      localStorage.removeItem("accesToken");
      localStorage.removeItem("refreshToken");
      alert("Đăng xuất thành công trở lại trang đăng nhập");
      window.location.href = "../../login.html";
    });
  } catch (error) {
    console.error("Error fetching data:", error);
  }
});
