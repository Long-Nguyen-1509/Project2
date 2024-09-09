import page from "./node_modules/page/page.mjs";

const routes = new Map([
  [
    "/",
    {
      html: "src/content/home.html",
      script: "src/content/home.js",
    },
  ],
  [
    "/about",
    {
      html: "src/content/about.html",
      script: "src/content/about.js",
    },
  ],
  [
    "/contact",
    {
      html: "src/content/contact.html",
      script: "src/content/contact.js",
    },
  ],
  [
    "/admin-import",
    {
      html: "src/admin/import.html",
      script: "src/admin/import.js",
    },
  ],
]);

async function renderPage(route) {
  const appDiv = document.getElementById("app");
  const routeInfo = routes.get(route);

  if (routeInfo) {
    try {
      const loadedHTML = await loadHTML(routeInfo.html);
      appDiv.innerHTML = loadedHTML;
      await loadScript(routeInfo.script);
    } catch (error) {
      console.error("Error:", error);
      appDiv.innerHTML = "<h1>Failed to load content</h1>";
    }
  } else {
    appDiv.innerHTML = "<h1>Page not found</h1>";
  }
}

page("/", () => renderPage("/"));
page("/about", () => renderPage("/about"));
page("/contact", () => renderPage("/contact"));
page("*", () => renderPage("*"));

page();

async function loadHTML(url) {
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  return response.text();
}

function loadScript(url) {
  return new Promise((resolve, reject) => {
    const script = document.createElement("script");
    script.src = url;
    script.defer = true;
    script.onload = resolve;
    script.onerror = reject;
    document.head.appendChild(script);
  });
}
