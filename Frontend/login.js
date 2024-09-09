const loginForm = document.getElementById("loginForm");

loginForm.addEventListener("submit", (event) => {
  event.preventDefault(); // Prevent form submission

  const usernameInput = document.getElementById("username");
  const passwordInput = document.getElementById("password");

  const usernameValue = usernameInput.value;
  const passwordValue = passwordInput.value;

  const loginDTO = {
    username: usernameValue,
    password: passwordValue,
  };

  const loginUrl = "http://localhost:8080/api/auth/authenticate";

  fetch(loginUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(loginDTO),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Login failed: An error occurred");
      }
      return response.json();
    })
    .then((data) => {
      const accessToken = data.access_token;
      const refreshToken = data.refresh_token;
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);

      const base64Url = accessToken.split(".")[1];
      const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
      const payload = JSON.parse(atob(base64));

      console.log(payload);
      const jwtData = payload.data;
      const sub = payload.sub;
      const iat = payload.iat;
      const exp = payload.exp;
      const role = payload.roles[0].authority;
      localStorage.setItem("accessTokenExpiration", exp);
      console.log(sub);
      console.log(iat);
      console.log(exp);
      console.log(role);
      if (role === "ROLE_ADMIN") {
        window.location.href = "src/admin/import.html";
      } else if (role === "ROLE_SV") {
        window.location.href = "src/sinh-vien/home.html";
      } else {
        console.log("Unknown role:", role);
      }
      alert("Login successful!");
      console.log("User data:", data);
    })
    .catch((error) => {
      alert("Login failed: " + error.message);
    });
});
