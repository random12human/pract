# Enterprise Java Practicals — Copy-and-Paste Code

> Environment: JDK 8, Java EE 8 (`javax.*`), GlassFish 5.1, NetBeans.
> Servlet/JSP programs should be deployed as a web application; EJB programs require an EJB-capable server such as GlassFish.

Use the file headings below to find each practical. Open this README on GitHub to select and copy code directly in your browser.

Enterprise Java Practicals — Copyable Code and Outputs

**Environment:** JDK 8, Java EE 8 (`javax.*`), GlassFish 5.1, NetBeans. Deploy servlet/JSP programs as a web application. Deploy EJB programs to an EJB-capable server such as GlassFish.

## Practical 1A — Simple Calculator using Servlet

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Simple Calculator</title></head>
<body>
<form action="Calculator" method="get">
  Number 1: <input type="number" name="n1" required><br><br>
  Number 2: <input type="number" name="n2" required><br><br>
  Operator:
  <select name="operator">
    <option value="+">+</option>
    <option value="-">-</option>
    <option value="*">*</option>
    <option value="/">/</option>
  </select>
  <input type="submit" value="Calculate">
  <input type="reset" value="Reset">
</form>
</body>
</html>

```

### Calculator.java

```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Calculator")
public class Calculator extends GenericServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {
            int a = Integer.parseInt(req.getParameter("n1"));
            int b = Integer.parseInt(req.getParameter("n2"));
            String op = req.getParameter("operator");

            if ("+".equals(op)) {
                out.println("Addition is: " + (a + b));
            } else if ("-".equals(op)) {
                out.println("Subtraction is: " + (a - b));
            } else if ("*".equals(op)) {
                out.println("Multiplication is: " + (a * b));
            } else if ("/".equals(op)) {
                if (b == 0) {
                    out.println("Cannot divide by zero.");
                } else {
                    out.println("Division is: " + ((double) a / b));
                }
            } else {
                out.println("Invalid operator.");
            }
        } catch (NumberFormatException e) {
            out.println("Please enter valid integers.");
        }
    }
}

```

Sample output: `Addition is: 30` (for 10 + 20).

## Practical 1B — Login Servlet

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Login Page</title></head>
<body>
<h2>Login Page</h2>
<form action="Demo" method="get">
  Username: <input type="text" name="n1" required><br><br>
  Password: <input type="password" name="n2" required><br><br>
  <input type="submit" value="Login">
</form>
</body>
</html>

```

### Demo.java

```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Demo")
public class Demo extends GenericServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String username = req.getParameter("n1");
        String password = req.getParameter("n2");

        if ("Shubham".equals(username) && "115".equals(password)) {
            out.println("Hello " + username);
            out.println("<br>Login Successful");
        } else {
            out.println("Login Failed");
        }
    }
}

```

Sample output: Correct credentials: `Hello Shubham` / `Login Successful`. Incorrect credentials: `Login Failed`.

## Practical 1C — Registration Servlet using JDBC

Create a Derby database named `register` and table:

CREATE TABLE register (

username VARCHAR(50),

password VARCHAR(100),

email VARCHAR(100),

country VARCHAR(50)

);

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Registration</title></head>
<body>
<h2>Registration Form</h2>
<form action="register" method="post">
  Username: <input type="text" name="n1" required><br><br>
  Password: <input type="password" name="n2" required><br><br>
  Email: <input type="email" name="n3" required><br><br>
  Country: <input type="text" name="n4" required><br><br>
  <input type="submit" value="Register">
  <input type="reset" value="Reset">
</form>
</body>
</html>

```

### register.java

```java
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        String sql = "INSERT INTO register (username, password, email, country) VALUES (?, ?, ?, ?)";

        try (PrintWriter out = res.getWriter();
             Connection con = DriverManager.getConnection(
                     "jdbc:derby://localhost:1527/register", "root", "root");
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, req.getParameter("n1"));
            ps.setString(2, req.getParameter("n2"));
            ps.setString(3, req.getParameter("n3"));
            ps.setString(4, req.getParameter("n4"));
            ps.executeUpdate();
            out.println("Data Inserted Successfully!");
        } catch (Exception e) {
            throw new ServletException("Registration failed. Check Derby server, credentials, and table.", e);
        }
    }
}

```

Sample output: `Data Inserted Successfully!`

## Practical 2A — Request Dispatcher / Password Validation

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Password Validation</title></head>
<body>
<form action="Demo" method="get">
  Username: <input type="text" name="n1" required><br><br>
  Password: <input type="password" name="n2" required><br><br>
  <input type="submit" value="Submit">
</form>
</body>
</html>

```

### Demo.java

```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Demo")
public class Demo extends GenericServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        String username = req.getParameter("n1");
        String password = req.getParameter("n2");

        if ("Servlet".equals(password)) {
            req.setAttribute("user", username);
            RequestDispatcher rd = req.getRequestDispatcher("/welcome");
            rd.forward(req, res);
        } else {
            res.setContentType("text/html;charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("Password is invalid. <a href='index.html'>Try again</a>");
        }
    }
}

```

### welcome.java

```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/welcome")
public class welcome extends GenericServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        String username = (String) req.getAttribute("user");
        PrintWriter out = res.getWriter();
        out.println("<h1>Hello " + username + "<br>You are welcome here.</h1>");
    }
}

```

Sample output: Password `Servlet` forwards to `Hello [username]`. Any other password displays `Password is invalid`.

## Practical 2B — Cookie Visit Counter

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Visit Counter</title></head>
<body>
<form action="CookieServlet" method="get">
  <input type="submit" value="Visit Servlet">
</form>
</body>
</html>

```

### CookieServlet.java

```java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int count = 0;
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("visit".equals(cookie.getName())) {
                    try {
                        count = Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException ignored) {
                        count = 0;
                    }
                    break;
                }
            }
        }

        count++;
        Cookie cookie = new Cookie("visit", String.valueOf(count));
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        res.addCookie(cookie);
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("You visited this page " + count + " times.");
    }
}

```

Sample output: `You visited this page 1 times.` The next visit displays `2`.

## Practical 3A — File Upload and Download

Create the folder `C:/Uploads` on the server machine before running these examples.

### Upload — `index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>File Upload</title></head>
<body>
<h2>File Upload</h2>
<form action="FileUploadServlet" method="post" enctype="multipart/form-data">
  Select file: <input type="file" name="file" required><br><br>
  <input type="submit" value="Upload">
</form>
</body>
</html>

```

### FileUploadServlet.java

```java
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        Part part = req.getPart("file");

        if (part == null || part.getSize() == 0) {
            res.getWriter().println("Please select a file.");
            return;
        }

        String name = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        Path folder = Paths.get("C:/Uploads");
        Files.createDirectories(folder);
        Path target = folder.resolve(name).normalize();

        if (!target.getParent().equals(folder.toAbsolutePath().normalize())
                && !target.getParent().equals(folder.normalize())) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file name.");
            return;
        }

        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        res.getWriter().println("File uploaded successfully!<br>File name: " + name);
    }
}

```

### Download — `download.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>File Download</title></head>
<body>
<h2>File Download Application</h2>
<a href="DownloadServlet">Download sample.txt</a>
</body>
</html>

```

### DownloadServlet.java

```java
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String filename = "sample.txt";
        Path folder = Paths.get("C:/Uploads").toAbsolutePath().normalize();
        Path file = folder.resolve(filename).normalize();

        if (!file.startsWith(folder) || !Files.isRegularFile(file)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found. Upload or place sample.txt in C:/Uploads.");
            return;
        }

        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        res.setContentLengthLong(Files.size(file));

        try (InputStream in = Files.newInputStream(file);
             OutputStream out = res.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
        }
    }
}

```

Sample output: Upload: `File uploaded successfully!`. Download: browser downloads `sample.txt`.

## Practical 5A — JSP Directives and Implicit Objects

### index.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>JSP Directives and Implicit Objects</title></head>
<body>
<%@ include file="header.jsp" %>
<h2>JSP Directives and Implicit Objects Demonstration</h2>
<h3>1. Request Object</h3>
<p>Request Method: <%= request.getMethod() %></p>
<p>Request URI: <%= request.getRequestURI() %></p>
<h3>2. Response Object</h3>
<p>Response Content Type: <%= response.getContentType() %></p>
<h3>3. Session Object</h3>
<p>Session ID: <%= session.getId() %></p>
<h3>4. Application Object</h3>
<p>Application Server Info: <%= application.getServerInfo() %></p>
<h3>5. Out Object</h3>
<% out.println("This message is displayed using the out implicit object."); %>
<h3>6. Page Object</h3>
<p>Page Class: <%= page.getClass().getName() %></p>
<h3>7. PageContext Object</h3>
<% pageContext.setAttribute("studentName", "Rahul"); %>
<p>Student Name: <%= pageContext.getAttribute("studentName") %></p>
<h3>8. Config Object</h3>
<p>Servlet Name: <%= config.getServletName() %></p>
<h3>Current Date</h3>
<p><%= new Date() %></p>
<%@ include file="footer.jsp" %>
</body>
</html>

```

### header.jsp

```jsp
<div style="background-color:lightblue;padding:15px;">
  <h2>My JSP Application</h2>
  <hr>
</div>

```

### footer.jsp

```jsp
<div style="background-color:lightgray;padding:15px;">
  <hr>
  <p>Copyright &copy; 2026 My JSP Application</p>
</div>

```

Sample output: The page displays request method/URI, response content type, session ID, server information, student name `Rahul`, servlet name, and current date.

## Practical 6A — Update Employee Salary using JSP and JDBC

Create a Derby database `emp` with this table:

CREATE TABLE emp (

eno INT PRIMARY KEY,

name VARCHAR(80),

age INT,

desg VARCHAR(80),

sal DECIMAL(10,2)

);

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Update Employee</title></head>
<body>
<form action="UpdateEmp.jsp" method="post">
  Employee No: <input type="number" name="txtEno" required><br><br>
  New Salary: <input type="number" name="txtSal" step="0.01" min="0" required><br><br>
  <input type="reset" value="Reset">
  <input type="submit" value="Update">
</form>
</body>
</html>

```

### UpdateEmp.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Update Employee</title></head>
<body>
<h1>Updating Employee Record</h1>
<%
String eno = request.getParameter("txtEno");
String sal = request.getParameter("txtSal");

try {
    int employeeNo = Integer.parseInt(eno);
    double salary = Double.parseDouble(sal);
    if (salary < 0) {
        out.println("<h3>Salary cannot be negative.</h3>");
    } else {
        try (Connection con = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/emp", "root", "root");
             PreparedStatement ps = con.prepareStatement(
                "UPDATE emp SET sal = ? WHERE eno = ?")) {
            ps.setDouble(1, salary);
            ps.setInt(2, employeeNo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.println("<h3>Employee record updated successfully.</h3>");
            } else {
                out.println("<h3>Employee record does not exist.</h3>");
            }
        }
    }
} catch (NumberFormatException e) {
    out.println("<h3>Enter a valid employee number and salary.</h3>");
} catch (SQLException e) {
    application.log("Employee update failed", e);
    out.println("<h3>Database error. Check the Derby server, credentials, and table.</h3>");
}
%>
</body>
</html>

```

Sample output: `Employee record updated successfully.` if the employee number exists.

### Insert employee record — `InsertEmp.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.*" %>
<%
try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/emp", "root", "root");
     PreparedStatement ps = con.prepareStatement(
       "INSERT INTO emp (eno, name, age, desg, sal) VALUES (?, ?, ?, ?, ?)")) {
    ps.setInt(1, Integer.parseInt(request.getParameter("eno")));
    ps.setString(2, request.getParameter("name"));
    ps.setInt(3, Integer.parseInt(request.getParameter("age")));
    ps.setString(4, request.getParameter("desg"));
    ps.setDouble(5, Double.parseDouble(request.getParameter("sal")));
    ps.executeUpdate();
    out.println("Employee inserted successfully.");
} catch (Exception e) {
    application.log("Employee insert failed", e);
    out.println("Insert failed. Check inputs, database, and duplicate employee number.");
}
%>

```

### Delete employee record — `DeleteEmp.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.*" %>
<%
try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/emp", "root", "root");
     PreparedStatement ps = con.prepareStatement("DELETE FROM emp WHERE eno = ?")) {
    ps.setInt(1, Integer.parseInt(request.getParameter("eno")));
    int rows = ps.executeUpdate();
    out.println(rows > 0 ? "Employee deleted successfully." : "Employee record does not exist.");
} catch (Exception e) {
    application.log("Employee delete failed", e);
    out.println("Delete failed. Check inputs and database.");
}
%>

```

## Practical 6B — JSP Expression Language (EL)

### index.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="javax.servlet.http.Cookie" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>EL Demo</title></head>
<body>
<h3>Welcome to index page</h3>
<% session.setAttribute("user", "Admin"); %>
<% response.addCookie(new Cookie("name", "mycookie")); %>
<form action="ExpressionLanguage.jsp" method="get">
  Enter Name: <input type="text" name="name" required>
  <input type="submit" value="Submit">
</form>
</body>
</html>

```

### ExpressionLanguage.jsp

```jsp
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Expression Language</title></head>
<body>
Welcome, ${param.name}<br>
Session value is ${sessionScope.user}<br>
Cookie value is ${cookie.name.value}<br>
</body>
</html>

```

### ELArithmeticOperator.jsp

```jsp
<!DOCTYPE html>
<html><body>
5 * 5 + 4 = ${5 * 5 + 4}<br>
1.4E4 + 1.4 = ${1.4E4 + 1.4}<br>
10 mod 4 = ${10 mod 4}<br>
15 div 3 = ${15 div 3}<br>
</body></html>

```

### ELLogicalOperator.jsp

```jsp
<!DOCTYPE html>
<html><body>
<h2>Logical Operators</h2>
true and true: ${true and true}<br>
true and false: ${true and false}<br>
true or false: ${true or false}<br>
not true: ${not true}<br>
</body></html>

```

### ELRelationalOperator.jsp

```jsp
<!DOCTYPE html>
<html><body>
<h2>Relational Operators</h2>
10.0 == 10: ${10.0 == 10}<br>
10.0 eq 10: ${10.0 eq 10}<br>
(20 * 10) != 200: ${(20 * 10) != 200}<br>
3 ne 3: ${3 ne 3}<br>
3.2 >= 2: ${3.2 >= 2}<br>
2 lt 3: ${2 lt 3}<br>
2 le 4: ${2 le 4}<br>
4 le 2: ${4 le 2}<br>
</body></html>

```

### ELConditionalOperator.jsp

```jsp
<!DOCTYPE html>
<html><body>
<h2>Conditional Operator</h2>
The result of 10 &gt; 2 is: ${10 > 2 ? 'greater' : 'lesser'}
</body></html>

```

### EmptyOperator.jsp

```jsp
<!DOCTYPE html>
<html><body>
<h2>Empty Operator Example</h2>
Is text empty? ${empty param.text}
</body></html>

```

Sample output: With name `Ayush`, the EL page displays `Welcome, Ayush`, session value `Admin`, and cookie value `mycookie`.

## Practical 6C — JSP JSTL

**Requirement:** Add JSTL 1.2 library to the web application. These examples use the Java EE 8 core tag URI.

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>JSTL Demo</title></head>
<body>
<a href="setDemo.jsp">Set Demo</a><br>
<a href="Maxif.html">Maximum</a><br>
<a href="forEachDemo.jsp">ForEach Demo</a><br>
<a href="outDemo.jsp">Out Demo</a><br>
<a href="URLDemo.jsp">URL Demo</a><br>
<a href="choose_when_otherwise.jsp">Choose/When/Otherwise Demo</a>
</body>
</html>

```

### setDemo.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" scope="application" value="Dukes Soccer League: Registration" />
${applicationScope.pageTitle}

```

### Maxif.html

```html
<!DOCTYPE html>
<html><body>
<form action="IFDemo.jsp" method="get">
  x = <input type="number" name="x" required><br>
  y = <input type="number" name="y" required><br>
  <input type="submit" value="Check Maximum">
</form>
</body></html>

```

### IFDemo.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
  <c:when test="${param.x > param.y}">
    <h2>The answer is:</h2>
    <c:out value="${param.x}" /> is greater than <c:out value="${param.y}" />.
  </c:when>
  <c:when test="${param.y > param.x}">
    <h2>The answer is:</h2>
    <c:out value="${param.y}" /> is greater than <c:out value="${param.x}" />.
  </c:when>
  <c:otherwise><h2>Both values are equal.</h2></c:otherwise>
</c:choose>

```

### forEachDemo.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach begin="1" end="10" var="i">
  The square of <c:out value="${i}" /> is <c:out value="${i * i}" />.<br>
</c:forEach>

```

### outDemo.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="name" value="John" />
My name is: <c:out value="${name}" />

```

### URLDemo.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="homeUrl" value="/index.html" />
<a href="${homeUrl}">Open index page</a>

```

### choose_when_otherwise.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="income" value="${5000 * 4}" />
Your income is: <c:out value="${income}" /><br>
<c:choose>
  <c:when test="${income <= 1000}">Income is not good.</c:when>
  <c:when test="${income > 10000}">Income is very good.</c:when>
  <c:otherwise>Income is moderate.</c:otherwise>
</c:choose>

```

Sample output: The ForEach page displays squares from 1 to 100; the maximum page identifies the larger number or reports equality.

## Practical 7A — Currency Converter using EJB

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Currency Converter</title></head>
<body>
<form action="CCServlet" method="get">
  Amount: <input type="number" name="amt" min="0" step="any" required><br><br>
  <label><input type="radio" name="type" value="r2d" required> Rupees to Dollar</label><br>
  <label><input type="radio" name="type" value="d2r"> Dollar to Rupees</label><br><br>
  <input type="reset" value="Reset">
  <input type="submit" value="Convert">
</form>
</body>
</html>

```

### CCBeanLocal.java

```java
package mybeans;

import javax.ejb.Local;

@Local
public interface CCBeanLocal {
    double r2Dollar(double rupees);
    double d2Rupees(double dollars);
}

```

### CCBean.java

```java
package mybeans;

import javax.ejb.Stateless;

@Stateless
public class CCBean implements CCBeanLocal {
    private static final double RATE = 95.16;

    @Override
    public double r2Dollar(double rupees) {
        return rupees / RATE;
    }

    @Override
    public double d2Rupees(double dollars) {
        return dollars * RATE;
    }
}

```

### CCServlet.java

```java
package mypack;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mybeans.CCBeanLocal;

@WebServlet("/CCServlet")
public class CCServlet extends HttpServlet {
    @EJB
    private CCBeanLocal obj;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            double amount = Double.parseDouble(request.getParameter("amt"));
            String type = request.getParameter("type");
            if (amount < 0) {
                out.println("Amount cannot be negative.");
            } else if ("d2r".equals(type)) {
                out.println("<h1>" + amount + " Dollars = " + obj.d2Rupees(amount) + " Rupees</h1>");
            } else if ("r2d".equals(type)) {
                out.println("<h1>" + amount + " Rupees = " + obj.r2Dollar(amount) + " Dollars</h1>");
            } else {
                out.println("Please select a conversion type.");
            }
        } catch (NumberFormatException e) {
            out.println("Please enter a valid amount.");
        }
    }
}

```

Sample output: At the fixed demonstration rate 95.16, `951.6 Rupees = 10.0 Dollars`.

## Practical 8A — Servlet Hit Counter using Singleton Session Bean

### index.html

```html
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Hit Counter</title></head>
<body>
<form action="ServletClient" method="post">
  <input type="submit" value="Take Hit Count">
</form>
</body>
</html>

```

### CountServletHitsBean.java

```java
package ejb;

import javax.ejb.Singleton;

@Singleton
public class CountServletHitsBean {
    private int hitCount = 0;

    public synchronized int getCount() {
        hitCount++;
        return hitCount;
    }
}

```

### ServletClient.java

```java
package servlet;

import ejb.CountServletHitsBean;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletClient", urlPatterns = {"/ServletClient"})
public class ServletClient extends HttpServlet {
    @EJB
    private CountServletHitsBean obj;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<b>Number of times this Servlet is accessed:</b> " + obj.getCount());
    }
}

```

Sample output: First click: `Number of times this Servlet is accessed: 1`; second click: `2`.

## Setup notes

- The code uses `javax.*` APIs and is intended for Java EE 8 / GlassFish 5.1, not Jakarta EE 9+.
- For Derby examples, start the Derby Network Server and confirm database names, tables, and credentials. If your local Derby setup does not use `root` / `root`, change the connection credentials to match your database.
- For JSTL, add the JSTL 1.2 library to the web application.
- For upload/download, create `C:/Uploads` and ensure the application server process has permission to write there.
- These code listings are formatted for copying. Runtime success depends on matching server, libraries, database setup, and file paths.
