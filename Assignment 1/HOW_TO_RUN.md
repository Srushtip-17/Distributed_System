# 🖥️ How to Run the RMI Arithmetic Program — Step by Step

---

## 📁 STEP 0 — Keep All Files in ONE Folder

Make sure all 4 files are in the **same folder**:
```
ArithmeticProject/
├── ArithmeticService.java
├── ArithmeticServiceImpl.java
├── ArithmeticServer.java
└── ArithmeticClient.java
```
> ⚠️ If they are in different folders, compilation will FAIL.

---

## 🔨 STEP 1 — Open Terminal / Command Prompt

- **Windows**: Press `Win + R` → type `cmd` → press Enter
- **Mac/Linux**: Open Terminal

Then navigate to your folder using `cd`:
```bash
cd path/to/ArithmeticProject
```
**Example:**
```bash
cd C:\Users\YourName\Desktop\ArithmeticProject     # Windows
cd /home/yourname/Desktop/ArithmeticProject        # Linux/Mac
```

---

## ⚙️ STEP 2 — Compile All Java Files

Run this ONE command to compile all 4 files at once:
```bash
javac *.java
```

### ✅ What you should see:
- No output = SUCCESS (Java is silent on success)
- New `.class` files will appear in the folder

### ❌ If you get errors:
- `cannot find symbol` → make sure all 4 files are in the same folder
- `javac not found` → Java is not installed or not in PATH

### After compiling, your folder should look like:
```
ArithmeticProject/
├── ArithmeticService.java
├── ArithmeticService.class        ← NEW
├── ArithmeticServiceImpl.java
├── ArithmeticServiceImpl.class    ← NEW
├── ArithmeticServer.java
├── ArithmeticServer.class         ← NEW
├── ArithmeticClient.java
└── ArithmeticClient.class         ← NEW
```

---

## 🖥️ STEP 3 — Open TWO Terminal Windows

You need **2 separate terminal windows** open at the same time:
- **Terminal 1** → for the Server
- **Terminal 2** → for the Client

In BOTH terminals, navigate to the same folder:
```bash
cd path/to/ArithmeticProject
```

---

## 🚀 STEP 4 — Run the SERVER (Terminal 1)

In **Terminal 1**, type:
```bash
java ArithmeticServer
```

### ✅ You should see:
```
Arithmetic Server is ready...
```
> This means the server started the RMI Registry on port 1099 and is now WAITING for clients.
> 
> ⚠️ **DO NOT close this terminal!** The server must keep running.

### ❌ If you get an error:
- `Address already in use: 1099` → Port 1099 is busy. Kill the old process or restart your PC.
- `ClassNotFoundException` → You forgot to compile. Go back to Step 2.

---

## 👤 STEP 5 — Run the CLIENT (Terminal 2)

In **Terminal 2**, type:
```bash
java ArithmeticClient
```

### ✅ You should see:
```
Enter the first number: 
```

Now interact with the program:
```
Enter the first number: 10
Enter the second number: 5

Choose an operation:
1. Add
2. Subtract
3. Multiply
4. Divide
Enter your choice (1-4): 1

Result: 15.0
```

---

## 🔄 Full Flow Recap (What Happens Internally)

```
[CLIENT]                              [SERVER]
   |                                     |
   |--- Naming.lookup("...Service") ---> |  Client finds the service in registry
   |                                     |
   |--- arithmeticService.add(10, 5) --> |  Call travels OVER NETWORK to server
   |                                     |  Server computes: 10 + 5 = 15
   |<----------- returns 15.0 ---------- |  Result comes back over network
   |                                     |
   Prints: Result: 15.0
```

---

## 🧪 Test All 4 Operations

| Choice | Operation | Example Input | Expected Result |
|--------|-----------|---------------|-----------------|
| 1 | Add | 10, 5 | 15.0 |
| 2 | Subtract | 10, 5 | 5.0 |
| 3 | Multiply | 10, 5 | 50.0 |
| 4 | Divide | 10, 5 | 2.0 |
| 4 | Divide by zero | 10, 0 | Error (RemoteException) |

---

## 🛑 How to Stop the Server

Go to **Terminal 1** (where server is running) and press:
```
Ctrl + C
```
This kills the server process.

---

## ⚠️ Common Mistakes & Fixes

| Problem | Cause | Fix |
|--------|-------|-----|
| `Connection refused` | Server not running | Start server FIRST, then client |
| `Address already in use` | Port 1099 taken | Restart PC or kill old Java process |
| `ClassNotFoundException` | .class files missing | Run `javac *.java` again |
| `Cannot divide by zero` | Entered 0 as divisor | Expected behavior — server sends error |
| Client shows nothing | Wrong folder in terminal | `cd` to correct folder |

---

## 📝 Quick Command Summary (For Exam)

```bash
# 1. Go to project folder
cd path/to/folder

# 2. Compile everything
javac *.java

# 3. Terminal 1 — Start Server
java ArithmeticServer

# 4. Terminal 2 — Start Client
java ArithmeticClient
```

> 🧠 **Remember for exam**: Server ALWAYS starts first. Client connects to it. Both run at the same time in separate terminals.
