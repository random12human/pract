# Complete MERN Stack Integration

## Aim
Integrate React frontend with Node.js and Express backend, perform insert/update/delete operations through the UI, display database records dynamically, implement frontend validation, test APIs using Postman, and verify the complete frontend/backend/database system.

## Requirements
- Node.js and npm
- Visual Studio Code
- MongoDB Community Server running locally
- MongoDB Compass (optional)
- Postman
- React with Vite

## 1. Create project folders

Create a main folder named `MERN-Stack-Integration`. Inside it, create `backend` and `frontend`. Inside `backend`, create two folders named `models` and `routes`.

### Backend terminal commands

Open a terminal in `backend` and run:

```cmd
npm init -y
npm install express mongoose cors dotenv
```

### Frontend terminal commands

Open another terminal in `frontend` and run:

```cmd
npm create vite@latest . -- --template react
npm install
```

If asked to install `create-vite`, type `y`.

## 2. Complete file structure

```text
MERN-Stack-Integration/
├── backend/
│   ├── models/
│   │   └── Student.js
│   ├── routes/
│   │   └── studentRoutes.js
│   ├── .env
│   ├── server.js
│   ├── package.json
│   └── package-lock.json
└── frontend/
    ├── package.json
    └── src/
        ├── App.jsx
        ├── App.css
        └── main.jsx
```

Important: save the route file as **`studentRoutes.js`**, matching the import in `server.js`. The journal heading uses `studentsroutes.js`, but its server import uses `./routes/studentRoutes`; use one consistent filename to avoid a module-not-found error.

## 3. Backend code

### `backend/models/Student.js`

```js
const mongoose = require("mongoose");

const studentSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      trim: true
    },
    email: {
      type: String,
      required: true,
      unique: true,
      trim: true
    },
    age: {
      type: Number,
      required: true,
      min: 1
    }
  },
  {
    timestamps: true
  }
);

module.exports = mongoose.model("Student", studentSchema);
```

### `backend/routes/studentRoutes.js`

```js
const express = require("express");
const Student = require("../models/Student");

const router = express.Router();

router.get("/", async (req, res) => {
  try {
    const students = await Student.find();
    res.json(students);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.get("/:id", async (req, res) => {
  try {
    const student = await Student.findById(req.params.id);

    if (!student) {
      return res.status(404).json({ message: "Student not found" });
    }

    res.json(student);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

router.post("/", async (req, res) => {
  try {
    const { name, email, age } = req.body;
    const student = new Student({ name, email, age });
    const savedStudent = await student.save();
    res.status(201).json(savedStudent);
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

router.put("/:id", async (req, res) => {
  try {
    const updatedStudent = await Student.findByIdAndUpdate(
      req.params.id,
      req.body,
      {
        new: true,
        runValidators: true
      }
    );

    if (!updatedStudent) {
      return res.status(404).json({ message: "Student not found" });
    }

    res.json(updatedStudent);
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

router.delete("/:id", async (req, res) => {
  try {
    const deletedStudent = await Student.findByIdAndDelete(req.params.id);

    if (!deletedStudent) {
      return res.status(404).json({ message: "Student not found" });
    }

    res.json({ message: "Student deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

module.exports = router;
```

### `backend/server.js`

```js
const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
const dotenv = require("dotenv");

const studentRoutes = require("./routes/studentRoutes");

dotenv.config();

const app = express();

app.use(cors());
app.use(express.json());

app.use("/api/students", studentRoutes);

app.get("/", (req, res) => {
  res.send("Backend server is running");
});

mongoose
  .connect(process.env.MONGO_URI)
  .then(() => {
    console.log("MongoDB connected");

    app.listen(process.env.PORT, () => {
      console.log(`Server running on http://localhost:${process.env.PORT}`);
    });
  })
  .catch((error) => {
    console.error("MongoDB connection failed:", error);
  });
```

### `backend/.env`

```env
PORT=5000
MONGO_URI=mongodb://127.0.0.1:27017/studentdb
```

Do not put `.env` inside `src`, `models`, or `routes`. It must be directly inside `backend`.

## 4. Frontend code

### `frontend/src/App.jsx`

```jsx
import { useEffect, useState } from "react";
import "./App.css";

const API_URL = "http://localhost:5000/api/students";

function App() {
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState({
    name: "",
    email: "",
    age: ""
  });
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");

  const fetchStudents = async () => {
    try {
      const response = await fetch(API_URL);
      const data = await response.json();
      setStudents(data);
    } catch (error) {
      setError("Unable to fetch students");
    }
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const validateForm = () => {
    if (!form.name.trim()) {
      setError("Name is required");
      return false;
    }

    if (!form.email.trim()) {
      setError("Email is required");
      return false;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailPattern.test(form.email)) {
      setError("Enter a valid email address");
      return false;
    }

    if (!form.age) {
      setError("Age is required");
      return false;
    }

    if (Number(form.age) < 1) {
      setError("Age must be greater than 0");
      return false;
    }

    setError("");
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      const url = editingId ? `${API_URL}/${editingId}` : API_URL;
      const method = editingId ? "PUT" : "POST";

      const response = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          name: form.name,
          email: form.email,
          age: Number(form.age)
        })
      });

      const data = await response.json();

      if (!response.ok) {
        setError(data.message || "Operation failed");
        return;
      }

      setForm({
        name: "",
        email: "",
        age: ""
      });
      setEditingId(null);
      setError("");
      fetchStudents();
    } catch (error) {
      setError("Server error");
    }
  };

  const handleEdit = (student) => {
    setForm({
      name: student.name,
      email: student.email,
      age: student.age
    });
    setEditingId(student._id);
  };

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this student?"
    );

    if (!confirmDelete) {
      return;
    }

    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE"
      });

      const data = await response.json();

      if (!response.ok) {
        setError(data.message || "Delete failed");
        return;
      }

      fetchStudents();
    } catch (error) {
      setError("Server error");
    }
  };

  return (
    <div className="container">
      <h1>Student Management System</h1>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Enter name"
          value={form.name}
          onChange={handleChange}
        />

        <input
          type="email"
          name="email"
          placeholder="Enter email"
          value={form.email}
          onChange={handleChange}
        />

        <input
          type="number"
          name="age"
          placeholder="Enter age"
          value={form.age}
          onChange={handleChange}
        />

        <button type="submit">
          {editingId ? "Update Student" : "Add Student"}
        </button>
      </form>

      {error && <p className="error">{error}</p>}

      <h2>Student Records</h2>

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
          {students.map((student) => (
            <tr key={student._id}>
              <td>{student.name}</td>
              <td>{student.email}</td>
              <td>{student.age}</td>
              <td>
                <button onClick={() => handleEdit(student)}>Edit</button>
                <button onClick={() => handleDelete(student._id)}>
                  Delete
                </button>
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
  background: #f4f4f4;
}

.container {
  width: 800px;
  margin: 40px auto;
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
  margin-bottom: 20px;
}

input {
  padding: 10px;
  flex: 1;
}

button {
  padding: 10px 15px;
  border: none;
  background: #007bff;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

button:hover {
  background: #0056b3;
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

.error {
  color: red;
  text-align: center;
}
```

## 5. Run the complete practical

Make sure MongoDB Community Server is running.

**Terminal 1 — Backend**

```cmd
cd backend
node server.js
```

Expected messages: `MongoDB connected` and `Server running on http://localhost:5000`.

Check the backend by opening `http://localhost:5000/`.

**Terminal 2 — Frontend**

```cmd
cd frontend
npm run dev
```

Open the Vite URL shown in the terminal. Test adding a student, editing/updating a student, deleting a student, and the validation messages.

## 6. Test APIs using Postman

Base URL:

```text
http://localhost:5000/api/students
```

| Method | URL | Purpose |
|---|---|---|
| GET | `http://localhost:5000/api/students` | Get all students |
| GET | `http://localhost:5000/api/students/STUDENT_ID` | Get one student |
| POST | `http://localhost:5000/api/students` | Insert a student |
| PUT | `http://localhost:5000/api/students/STUDENT_ID` | Update a student |
| DELETE | `http://localhost:5000/api/students/STUDENT_ID` | Delete a student |

For POST, choose **Body → raw → JSON** and enter:

```json
{
  "name": "Ayush",
  "email": "ayush@example.com",
  "age": 22
}
```

For PUT, replace `STUDENT_ID` with the `_id` returned by the GET or POST request, then send the updated JSON. For DELETE, use the existing student ID in the URL.

The database is `studentdb`; the Mongoose collection is `students`.

**Remember:** Model defines the data structure, Routes handle API requests, Express runs the backend, React is the frontend, MongoDB stores records, and Postman tests APIs.
