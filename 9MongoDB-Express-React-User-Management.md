# User Data Management using MongoDB, Express and React

## Aim
Create an API to add user details using Express and MongoDB, design a React form, retrieve and display users, update existing records, delete selected records, and display updated records dynamically.

## Requirements
- Node.js and npm
- Visual Studio Code
- MongoDB Community Server running locally
- React with Vite

## 1. Create project folders

Create the main folder `MongoDB-Express-React-User-Management`. Inside it, create two folders named `backend` and `frontend`.

### Backend terminal commands

Open the `backend` folder in a terminal and run:

```cmd
npm init -y
npm install express mongoose cors
```

### Frontend terminal commands

Open a second terminal, move into the `frontend` folder and run:

```cmd
npm create vite@latest . -- --template react
npm install
```

If asked to install `create-vite`, type `y`.

## 2. File structure

```text
MongoDB-Express-React-User-Management/
├── backend/
│   ├── node_modules/
│   ├── server.js
│   ├── package.json
│   └── package-lock.json
└── frontend/
    ├── node_modules/
    ├── package.json
    └── src/
        ├── App.jsx
        ├── App.css
        └── main.jsx
```

## 3. Backend code

### `backend/server.js`

```js
const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");

const app = express();

app.use(cors());
app.use(express.json());

mongoose
  .connect("mongodb://127.0.0.1:27017/user_management")
  .then(() => console.log("MongoDB Connected"))
  .catch((err) => console.log(err));

const userSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true
  },
  email: {
    type: String,
    required: true
  },
  age: {
    type: Number,
    required: true
  }
});

const User = mongoose.model("User", userSchema);

app.post("/users", async (req, res) => {
  try {
    const user = new User(req.body);
    await user.save();
    res.status(201).json(user);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.get("/users", async (req, res) => {
  try {
    const users = await User.find();
    res.json(users);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.put("/users/:id", async (req, res) => {
  try {
    const updatedUser = await User.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true }
    );
    res.json(updatedUser);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.delete("/users/:id", async (req, res) => {
  try {
    await User.findByIdAndDelete(req.params.id);
    res.json({ message: "User deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

app.listen(5000, () => {
  console.log("Server running on port 5000");
});
```

## 4. Frontend code

### `frontend/src/App.jsx`

```jsx
import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [users, setUsers] = useState([]);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    age: ""
  });
  const [editingId, setEditingId] = useState(null);

  const fetchUsers = async () => {
    const response = await fetch("http://localhost:5000/users");
    const data = await response.json();
    setUsers(data);
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const url = editingId
      ? `http://localhost:5000/users/${editingId}`
      : "http://localhost:5000/users";

    const method = editingId ? "PUT" : "POST";

    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        ...formData,
        age: Number(formData.age)
      })
    });

    if (response.ok) {
      setFormData({
        name: "",
        email: "",
        age: ""
      });
      setEditingId(null);
      fetchUsers();
    }
  };

  const handleEdit = (user) => {
    setFormData({
      name: user.name,
      email: user.email,
      age: user.age
    });
    setEditingId(user._id);
  };

  const handleDelete = async (id) => {
    await fetch(`http://localhost:5000/users/${id}`, {
      method: "DELETE"
    });
    fetchUsers();
  };

  return (
    <div className="container">
      <h1>User Management</h1>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Enter name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Enter email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="number"
          name="age"
          placeholder="Enter age"
          value={formData.age}
          onChange={handleChange}
          required
        />
        <button type="submit">
          {editingId ? "Update User" : "Add User"}
        </button>
      </form>

      <h2>Users</h2>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Age</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user._id}>
              <td>{user.name}</td>
              <td>{user.email}</td>
              <td>{user.age}</td>
              <td>
                <button onClick={() => handleEdit(user)}>Edit</button>
                <button onClick={() => handleDelete(user._id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
```

### `frontend/src/App.css`

```css
body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
}

.container {
  width: 800px;
  margin: 50px auto;
  background: white;
  padding: 30px;
  border-radius: 10px;
}

h1,
h2 {
  text-align: center;
}

form {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
}

input {
  padding: 10px;
  flex: 1;
}

button {
  padding: 10px 15px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

button:hover {
  background-color: #0056b3;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 12px;
  border: 1px solid #ddd;
  text-align: center;
}

th {
  background-color: #007bff;
  color: white;
}

td button {
  margin: 3px;
}
```

## 5. Run the practical

Make sure MongoDB is running.

**Terminal 1 — Backend**

```cmd
cd backend
node server.js
```

Expected: `MongoDB Connected` and `Server running on port 5000`.

**Terminal 2 — Frontend**

```cmd
cd frontend
npm run dev
```

Open the Vite URL shown in the terminal. Add a user, edit and update a record, and delete a record. The list should refresh dynamically.

The database is `user_management` and the collection created by Mongoose is `users`.

**Remember:** Express creates the API, Mongoose connects Node.js to MongoDB, React displays the form and records, and `fetch()` sends requests to the backend.
