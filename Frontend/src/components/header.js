document.addEventListener("DOMContentLoaded", () => {
  const navLinks = document.querySelectorAll("nav a");

  navLinks.forEach((link) => {
    if (link.getAttribute("href") === window.location.pathname) {
      link.classList.add("bg-red-600");
    }
  });
});
