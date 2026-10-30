# React Routing and Data Handling

## Aim
Install React Router, create multiple pages, navigate using `Link` and `Routes`, fetch public API data using Axios, display it in a table, and implement search.

## Requirements
- Node.js and npm
- Visual Studio Code
- Internet connection for the sample API

## 1. Terminal commands

Create a folder named `React-Router-Axios-API`, open it in VS Code, then run:

```cmd
npm create vite@latest . -- --template react
npm install
npm install react-router-dom
npm install axios
npm run dev
```

## 2. File structure

```text
React-Router-Axios-API/
├── index.html
├── package.json
└── src/
    ├── App.jsx
    ├── main.jsx
    └── pages/
        ├── Home.jsx
        ├── About.jsx
        └── Users.jsx
```

Create the `pages` folder inside `src`. Replace `App.jsx` and `main.jsx`. Create the three page files.

## 3. Code

### `src/App.jsx`

```jsx
import { Routes, Route, Link } from "react-router-dom";
import Home from "./pages/Home";
import About from "./pages/About";
import Users from "./pages/Users";

function App() {
  return (
    <div>
      <h1>React Routing Example</h1>
      <nav>
        <Link to="/">Home</Link> |{" "}
        <Link to="/about">About</Link> |{" "}
        <Link to="/users">Users</Link>
      </nav>
      <hr />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/users" element={<Users />} />
      </Routes>
    </div>
  );
}

export default App;
```

### `src/main.jsx`

```jsx
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);
```

### `src/pages/Home.jsx`

```jsx
function Home() {
  return (
    <div>
      <h2>Home Page</h2>
      <p>Welcome to the Home Page.</p>
    </div>
  );
}

export default Home;
```

### `src/pages/About.jsx`

```jsx
function About() {
  return (
    <div>
      <h2>About Page</h2>
      <p>This is the About Page.</p>
    </div>
  );
}

export default About;
```

### `src/pages/Users.jsx`

```jsx
import { useEffect, useState } from "react";
import axios from "axios";

function Users() {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");

  useEffect(() => {
    axios
      .get("https://jsonplaceholder.typicode.com/users")
      .then((response) => setUsers(response.data))
      .catch((error) => console.log("Error:", error));
  }, []);

  const filteredUsers = users.filter((user) =>
    user.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div>
      <h2>Users List</h2>
      <input
        type="text"
        placeholder="Search by Name..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />
      <br /><br />
      <table border="1" cellPadding="10">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Website</th>
          </tr>
        </thead>
        <tbody>
          {filteredUsers.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.name}</td>
              <td>{user.username}</td>
              <td>{user.email}</td>
              <td>{user.phone}</td>
              <td>{user.website}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Users;
```

## 4. Run and verify

```cmd
npm run dev
```

Open the Vite URL. Click Home, About, and Users. On the Users page, wait for API data to load and try searching by name.

**Remember:** `BrowserRouter` enables routing, `Link` navigates, `Routes` contains routes, `Route` maps a URL to a component, Axios makes HTTP requests, and `useEffect` loads data when the page opens.
