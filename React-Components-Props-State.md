# React Components, Props and State

## Aim
Create multiple React components, pass data using props, use the `useState` hook, accept form input, display a list using `map()`, and create interactive counter and to-do components.

## Requirements
- Node.js and npm
- Visual Studio Code
- React with Vite

## 1. Terminal commands

Create a folder named `React-Components-Props-State`, open it in VS Code, then run:

```cmd
npm create vite@latest . -- --template react
npm install
npm run dev
```

## 2. File structure

```text
React-Components-Props-State/
├── index.html
├── package.json
└── src/
    ├── App.jsx
    ├── App.css
    └── main.jsx
```

Replace `src/App.jsx` and `src/App.css`. Keep the generated `main.jsx`.

## 3. Code

### `src/App.jsx`

```jsx
import { useState } from "react";
import "./App.css";

function Header(props) {
  return <h1>{props.title}</h1>;
}

function UserProfile({ name, age }) {
  return (
    <div>
      <h2>User Profile</h2>
      <p>Name: {name}</p>
      <p>Age: {age}</p>
    </div>
  );
}

function UserForm() {
  const [input, setInput] = useState("");
  const [displayName, setDisplayName] = useState("");

  function handleSubmit(e) {
    e.preventDefault();
    setDisplayName(input);
    setInput("");
  }

  return (
    <div>
      <h2>User Input Form</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Enter your name"
        />
        <button type="submit">Submit</button>
      </form>
      <p>Entered Name: <b>{displayName}</b></p>
    </div>
  );
}

function Counter() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <h2>Counter</h2>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Increase</button>
      <button onClick={() => setCount(count - 1)}>Decrease</button>
    </div>
  );
}

function TodoList() {
  const [items, setItems] = useState([
    "Learn React",
    "Practice Components",
    "Build Projects"
  ]);
  const [task, setTask] = useState("");

  function addTask() {
    if (task !== "") {
      setItems([...items, task]);
      setTask("");
    }
  }

  return (
    <div>
      <h2>Todo List</h2>
      <input
        value={task}
        onChange={(e) => setTask(e.target.value)}
        placeholder="Add task"
      />
      <button onClick={addTask}>Add</button>
      <ul>
        {items.map((item, index) => (
          <li key={index}>{item}</li>
        ))}
      </ul>
    </div>
  );
}

function App() {
  return (
    <div className="App">
      <Header title="React Components Demo" />
      <UserProfile name="Ayush" age="22" />
      <UserForm />
      <Counter />
      <TodoList />
    </div>
  );
}

export default App;
```

### `src/App.css`

```css
.App {
  text-align: center;
  font-family: Arial, sans-serif;
}

div {
  margin: 20px;
}

input {
  padding: 8px;
  margin: 5px;
}

button {
  padding: 8px 15px;
  margin: 5px;
  cursor: pointer;
}

li {
  list-style: none;
  margin: 5px;
}
```

## 4. Run and verify

```cmd
npm run dev
```

Open the Vite URL. Test the name form, Increase/Decrease buttons, and add a task to the to-do list.

**Remember:** Props pass data from parent to child. `useState` stores changing data. `map()` displays each item in an array.
