document.addEventListener("DOMContentLoaded", () => {
  const button = document.querySelector("#download-button");
  button.addEventListener("click", async () => {
    const downloadUrl = localStorage.getItem("download");
    const jwt = localStorage.getItem("accessToken");
    try {
      const response = await fetch(downloadUrl, {
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
  });
});
