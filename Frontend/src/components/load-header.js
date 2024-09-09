function loadComponent() {
  fetch("../components/header.html")
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to load component");
      }
      return response.text();
    })
    .then((html) => {
      const container = document.getElementById("header");
      container.innerHTML = html;
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
    })
    .catch((error) => {
      console.error(error);
    });
}
window.addEventListener("load", loadComponent);
