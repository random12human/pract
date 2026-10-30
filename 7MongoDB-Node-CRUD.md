# MongoDB Connection and CRUD Operations

## Aim
Install and start MongoDB, create a database and collection, insert records, view records, update and delete records, and connect MongoDB with Node.js.

## Requirements
- Node.js and npm
- Visual Studio Code
- MongoDB Community Server running locally
- MongoDB Compass (optional, to view data)

> MongoDB Compass is only a GUI. The MongoDB Community Server must be installed and running for this local connection.

## 1. Terminal commands

Create a folder named `MongoDB-Node-CRUD`, open it in VS Code, and run:

```cmd
npm init -y
npm install mongodb
```

## 2. File structure

```text
MongoDB-Node-CRUD/
├── node_modules/
├── index.js
├── package.json
└── package-lock.json
```

Create `index.js` in the main folder.

## 3. Code

### `index.js`

```js
const { MongoClient } = require("mongodb");

const url = "mongodb://localhost:27017/";
const dbName = "Practical7";

async function main() {
  const client = new MongoClient(url);

  try {
    await client.connect();
    console.log("Connected successfully to MongoDB");

    const db = client.db(dbName);
    const collection = db.collection("students");

    const insertResult = await collection.insertOne({
      name: "Ayush",
      course: "BSc IT",
      marks: 92
    });
    console.log("Inserted record:", insertResult.insertedId);

    const allStudents = await collection.find({}).toArray();
    console.log("All students:", allStudents);

    const updateResult = await collection.updateOne(
      { name: "Ayush" },
      { $set: { marks: 90 } }
    );
    console.log("Updated count:", updateResult.modifiedCount);

    const deleteResult = await collection.deleteOne({ name: "Ayush" });
    console.log("Deleted count:", deleteResult.deletedCount);

    const finalStudents = await collection.find({}).toArray();
    console.log("Final students list:", finalStudents);

  } catch (err) {
    console.error("Error:", err);
  } finally {
    await client.close();
    console.log("Connection closed");
  }
}

main();
```

## 4. Run and verify

First make sure the MongoDB Community Server is running. Then run:

```cmd
node index.js
```

The terminal should show a successful connection, inserted record ID, records retrieved, update count, delete count, final list, and connection closed.

The database `Practical7` and collection `students` are created automatically when the first record is inserted. The final list is normally empty because this program deletes the inserted record.

**Remember:** CRUD means Create, Read, Update, Delete. `insertOne()` inserts, `find()` reads, `updateOne()` updates, and `deleteOne()` deletes.
