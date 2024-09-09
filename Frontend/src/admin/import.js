const jwt = localStorage.getItem("accessToken");
function toggleVisibility(buttonId, divId) {
  const button = document.getElementById(buttonId);
  const contentDiv = document.getElementById(divId);

  button.addEventListener("click", () => {
    const allContentDivs = ["contentDiv1", "contentDiv2"];
    allContentDivs.forEach((name) => {
      const div = document.getElementById(name);
      if (name !== divId) {
        div.classList.add("hidden");
      }
    });
    contentDiv.classList.remove("hidden");
    const allButtons = ["toggleButton1", "toggleButton2"];
    allButtons.forEach((name) => {
      const but = document.getElementById(name);
      if (name != buttonId) {
        but.classList.remove("bg-blue-700");
      }
    });
    button.classList.add("bg-blue-700");
  });
}

function initializeFileUpload(
  fileDropAreaId,
  fileInputId,
  chooseFileButtonId,
  url,
) {
  const fileDropArea = document.getElementById(fileDropAreaId);
  const fileInput = document.getElementById(fileInputId);
  const chooseFileButton = document.getElementById(chooseFileButtonId);

  fileDropArea.addEventListener("dragenter", (event) => {
    event.preventDefault();
    fileDropArea.style.border = "2px dashed #666";
  });

  fileDropArea.addEventListener("dragleave", (event) => {
    event.preventDefault();
    fileDropArea.style.border = "2px dashed #ccc";
  });

  fileDropArea.addEventListener("dragover", (event) => {
    event.preventDefault();
  });

  fileDropArea.addEventListener("drop", (event) => {
    event.preventDefault();
    fileDropArea.style.border = "2px dashed #ccc";
    const selectedFile = event.dataTransfer.files[0];
    confirmFileUpload(selectedFile, url);
  });

  fileInput.addEventListener("change", (event) => {
    const selectedFile = event.target.files[0];
    confirmFileUpload(selectedFile, url);
  });

  chooseFileButton.addEventListener("click", () => {
    fileInput.click();
  });

  const form = document.createElement("form");
  form.style.display = "none";
  document.body.appendChild(form);

  form.addEventListener("submit", (event) => {
    event.preventDefault();
    console.log("Form submitted!");
  });
}

async function confirmFileUpload(selectedFile, url) {
  const confirmed = confirm(
    `Are you sure you want to upload ${selectedFile.name}?`,
  );
  if (confirmed) {
    try {
      const formData = new FormData();
      formData.append("file", selectedFile);

      const response = await fetch(url, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
        body: formData,
      });

      if (response.status == 403) {
        alert("Phiên làm việc đã hết hạn vui lòng đăng nhập lại");
        window.location.href = "../../login.html";
      }
      console.log("Uploading file:", selectedFile);
    } catch (error) {
      console.error("Error:", error);
    }
  }
}

async function download() {
  const downloadLink = "http://localhost:8080/api/info/lop-thi/download-all";
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

document.addEventListener("DOMContentLoaded", () => {
  toggleVisibility("toggleButton1", "contentDiv1");
  toggleVisibility("toggleButton2", "contentDiv2");
  const url1 = "http://localhost:8080/api/admin/danh-sach-thi/upload";
  const url2 = "http://localhost:8080/api/admin/thong-tin/upload";
  initializeFileUpload(
    "fileDropArea1",
    "fileInput1",
    "chooseFileButton1",
    url1,
  );
  initializeFileUpload(
    "fileDropArea2",
    "fileInput2",
    "chooseFileButton2",
    url2,
  );
  document.getElementById("download").addEventListener("click", download);
});
