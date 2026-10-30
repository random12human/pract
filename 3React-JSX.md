# React Introduction and JSX

## Aim
Create a React application using Vite, display dynamic data with JSX, calculate the sum of two numbers, apply CSS, create functional components, and display the current date and time.

## Requirements
- Node.js and npm
- Visual Studio Code
- React with Vite

## 1. Terminal commands

Create a folder named `React-JSX`, open it in VS Code, then open Terminal → New Terminal (CMD) and run:

```cmd
npm create vite@latest . -- --template react
npm install
npm run dev
```

If asked to install `create-vite`, type `y`. Open the local URL shown in the terminal, usually `http://localhost:5173/`.

## 2. File structure

```text
React-JSX/
├── index.html
├── package.json
└── src/
    ├── App.jsx
    ├── App.css
    ├── Greeting.jsx
    ├── DateTime.jsx
    └── main.jsx
```

Create `Greeting.jsx`, `DateTime.jsx`, and `App.css` inside `src`. Replace the contents of `src/App.jsx`. Keep the Vite-generated `main.jsx`.

## 3. Code

### `src/Greeting.jsx`

```jsx
function Greeting() {
  return (
    <div>
      <h2>Hello from Greeting Component!</h2>
    </div>
  );
}

export default Greeting;
```

### `src/DateTime.jsx`

```jsx
function DateTime() {
  const currentDate = new Date();

  return (
    <div>
      <h3>Current Date and Time</h3>
      <p>{currentDate.toLocaleString()}</p>
    </div>
  );
}

export default DateTime;
```

### `src/App.jsx`

```jsx
import "./App.css";
import Greeting from "./Greeting";
import DateTime from "./DateTime";

function App() {
  const name = "Student";
  const num1 = 15;
  const num2 = 25;

  function calculateSum(a, b) {
    return a + b;
  }

  return (
    <div className="container">
      <h1>Welcome to MERN Stack Development</h1>
      <h2>Hello, {name}!</h2>
      <p>Sum of {num1} and {num2} = {calculateSum(num1, num2)}</p>
      <Greeting />
      <DateTime />
    </div>
  );
}

export default App;
```

### `src/App.css`

```css
.container {
  text-align: center;
  margin-top: 50px;
  font-family: Arial, sans-serif;
}

h1 {
  color: darkblue;
}

h2 {
  color: green;
}

p {
  font-size: 18px;
  color: #333;
  background-color: #f2f2f2;
  padding: 10px;
  border-radius: 5px;
}
```

## 4. Run and verify

```cmd
npm run dev
```

Open the URL shown in the terminal. The page should display the welcome message, `Hello, Student!`, the sum `40`, the Greeting component, and the current date and time.

**Remember:** JSX displays JavaScript values inside `{ }`. A component is a reusable function that returns JSX.
