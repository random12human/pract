# Node.js and Express Fundamentals

## Aim
Install Express, create a simple Node.js server, display a message in the browser, and create different routes.

## Requirements
- Node.js and npm
- Visual Studio Code

## 1. Terminal commands

Create a folder named `Node-Express-Routes`, open it in VS Code, and run:

```cmd
npm init -y
npm install express
```

## 2. File structure

```text
Node-Express-Routes/
├── node_modules/
├── index.js
├── package.json
└── package-lock.json
```

Create `index.js` in the main project folder.

## 3. Code

### `index.js`

```js
const express = require("express");
const app = express();
const PORT = 3000;

app.get("/", (req, res) => {
  console.log("Home route was accessed");
  res.send("<h1>Welcome to my Express Server</h1>");
});

app.get("/about", (req, res) => {
  console.log("About route was accessed");
  res.send("<h1>This is the about page</h1>");
});

app.get("/api/data", (req, res) => {
  console.log("API Data route was accessed");
  res.json({
    name: "Student",
    course: "Node.js and Express",
    status: "Learning Successful"
  });
});

app.get("/contact", (req, res) => {
  console.log("Contact route was accessed");
  res.send("Contact us at: example@gmail.com");
});

app.listen(PORT, () => {
  console.log(`Server is running successfully on http://localhost:${PORT}`);
});
```

## 4. Run and verify

In the terminal:

```cmd
node index.js
```

Keep the terminal open. Visit these URLs in the browser:

| URL | Expected response |
|---|---|
| `http://localhost:3000/` | Welcome to my Express Server |
| `http://localhost:3000/about` | This is the about page |
| `http://localhost:3000/api/data` | JSON data with name, course and status |
| `http://localhost:3000/contact` | Contact us at: example@gmail.com |

To stop the server, press `Ctrl + C` in the terminal.

**Remember:** Node.js runs JavaScript outside the browser. Express simplifies server and route creation. `app.get()` handles GET requests, `req` is the request, and `res` sends the response.
