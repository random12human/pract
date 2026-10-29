# Enterprise Java Practicals — Copy-and-Paste Code

> Based on the uploaded `Java.docx`. Only clear code errors and mismatches have been corrected; the original practicals and approach are retained.
> Environment: JDK 8, Java EE (`javax.*`), NetBeans, and GlassFish.

Open this README in a public GitHub repository to view and copy code directly in the browser.

## Module 1

## Practical 1A — Simple Calculator using Servlet

### `index.html`

```html
<!DOCTYPE html>
<html>
<head>
    <title>Simple Calculator</title>
</head>
<body>
    <form action="Calculator" method="get">
        Enter number 1:
        <input type="text" name="n1"><br><br>
        Enter number 2:
        <input type="text" name="n2"><br><br>
        Select Operator:
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

### `Calculator.java`

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
@WebServlet("/Calculator")
public class Calculator extends GenericServlet {
    public void service(ServletRequest req, ServletResponse res)throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        int a = Integer.parseInt(req.getParameter("n1"));
        int b = Integer.parseInt(req.getParameter("n2"));
        String op = req.getParameter("operator");
        if (op.equals("+")) {
            out.println("Addition is: " + (a + b));
        } else if (op.equals("-")) {
            out.println("Subtraction is: " + (a - b));
        } else if (op.equals("*")) {
            out.println("Multiplication is: " + (a * b));
        } else if (op.equals("/")) {
            if (b != 0) {
                out.println("Division is: " + ((double) a / b));
            } else {
                out.println("Cannot divide by zero");
            }
        }
    }
}
```

## Practical 1B — Login Servlet

### `index.html`

```html
<html>
<body>
    <h2>Login Page</h2>
    <form action="Demo" method="get">
        Username: <input type="text" name="n1"><br><br>
        Password: <input type="password" name="n2"><br><br>
        <input type="submit" value="Login">
    </form>
</body>
</html>
```

### `Demo.java`

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
@WebServlet("/Demo")
public class Demo extends GenericServlet {
    public void service(ServletRequest req, ServletResponse res)throws IOException {
        PrintWriter out = res.getWriter();
        String n1 = req.getParameter("n1");
        String n2 = req.getParameter("n2");
        if (n1.equals("Shubham") && n2.equals("115")) {
            out.println("Hello " + n1);
            out.println("Login Successful");
        } else {
            out.println("Login Failed");
        }
    }
}
```

## Practical 1C — Registration Servlet using JDBC

### `index.html`

```html
<html>
<body>
    <h2>Registration Form</h2>
    <form action="register" method="post">
        Username: <input type="text" name="n1"><br><br>
        Password: <input type="password" name="n2"><br><br>
        Email: <input type="text" name="n3"><br><br>
        Country: <input type="text" name="n4"><br><br>
        <input type="submit" value="Register">
        <input type="reset" value="Reset">
    </form>
</body>
</html>
```

### `register.java`

```java
import java.io.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/register")
public class register extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws IOException {
        PrintWriter out = res.getWriter();
        String s1 = req.getParameter("n1");
        String s2 = req.getParameter("n2");
        String s3 = req.getParameter("n3");
        String s4 = req.getParameter("n4");
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/register",
                "root", "root");
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO register VALUES (?, ?, ?, ?)");
            ps.setString(1, s1);
            ps.setString(2, s2);
            ps.setString(3, s3);
            ps.setString(4, s4);
            ps.executeUpdate();
            out.println("Data Inserted Successfully!");
            con.close();
        } catch (Exception e) {
            out.println(e);
        }
    }
}
```

## Practical 2A — Request Dispatcher / Password Validation

### `index.html`

```html
<html>
<body>
    <h2>Password Validation</h2>
    <form action="Demo" method="get">
        Username: <input type="text" name="n1"><br><br>
        Password: <input type="password" name="n2"><br><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
```

### `Demo.java`

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
@WebServlet("/Demo")
public class Demo extends GenericServlet {
    public void service(ServletRequest req, ServletResponse res)throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String n1 = req.getParameter("n1");
        String n2 = req.getParameter("n2");
        if (n2.equals("Servlet")) {
            req.setAttribute("user", n1);
            RequestDispatcher rd =
                req.getRequestDispatcher("welcome");
            rd.forward(req, res);
        } else {
            out.print("Password is invalid");
        }
    }
}
```

### `Welcome.java`

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
@WebServlet("/welcome")
public class Welcome extends GenericServlet {
    public void service(ServletRequest req, ServletResponse res)throws IOException, ServletException {
        String s = (String) req.getAttribute("user");
        PrintWriter out = res.getWriter();
        out.print("<h1>Hello " + s + "<br>You are Welcome here.</h1>");
    }
}
```

## Practical 2B — Cookie Visit Counter

### `index.html`

```html
<html>
<body>
    <h2>Visit Counter</h2>
    <form action="CookieServlet" method="get">
        <input type="submit" value="Visit Servlet">
    </form>
</body>
</html>
```

### `CookieServlet.java`

```java
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        int count = 0;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("visit")) {
                    count = Integer.parseInt(c.getValue());
                }
            }
        }
        count++;
        Cookie c = new Cookie("visit", String.valueOf(count));
        c.setMaxAge(60 * 60 * 24);
        res.addCookie(c);
        out.println("You visited this page "
                    + count + " times.");
    }
}
```

## Practical 3A — File Upload and Download

### `index.html`

```html
<html>
<body>
    <h2>File Upload</h2>
    <form action="FileUploadServlet" method="post"
          enctype="multipart/form-data">
        Select File:
        <input type="file" name="file"><br><br>
        <input type="submit" value="Upload">
    </form>
</body>
</html>
```

### `FileUploadServlet.java`

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet("/FileUploadServlet")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String path = "C:/Uploads";
        Part p = req.getPart("file");
        String name = p.getSubmittedFileName();
        InputStream is = p.getInputStream();
        OutputStream os = new FileOutputStream(
            new File(path + File.separator + name));
        int n;
        byte[] b = new byte[1024];
        while ((n = is.read(b)) != -1) {
            os.write(b, 0, n);
        }
        os.close();
        is.close();
        out.println("File uploaded successfully!");
        out.println("File name: " + name);
    }
}
```

### `download.html`

```html
<html>
<body>
    <h2>File Download Application</h2>
    <a href="DownloadServlet">Download File</a>
</body>
</html>
```

### `DownloadServlet.java`

```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException {
        String filename = "sample.txt";
        String filepath = "C:/Uploads/" + filename;
        File file = new File(filepath);
        if (!file.exists()) {
            res.getWriter().println("File not found!");
            return;
        }
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition",
                      "attachment; filename=\"" + filename + "\"");
        FileInputStream in = new FileInputStream(file);
        OutputStream out = res.getOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        out.close();
    }
}
```

## Practical 5A — JSP Directives and Implicit Objects

### `index.jsp`

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.Date"%>
<%@ include file="header.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>JSP Directives and Implicit Objects</title>
</head>
<body>
<h2>JSP Directives and Implicit Objects Demonstration</h2>
<h3>1. Request Object</h3>
<p>Request Method: <%= request.getMethod() %></p>
<p>Request URI: <%= request.getRequestURI() %></p>
<h3>2. Response Object</h3>
<p>Response Content Type: <%= response.getContentType() %></p>
<h3>3. Session Object</h3>
<p>Session ID: <%= session.getId() %></p>
<h3>4. Application Object</h3>
<p>Application Server Info:
    <%= application.getServerInfo() %>
</p>
<h3>5. Out Object</h3>
<%
    out.println("This message is displayed using the out implicit object.");
%>
<h3>6. Page Object</h3>
<p>Page Class:
    <%= page.getClass().getName() %>
</p>
<h3>7. PageContext Object</h3>
<p>Request Attribute Example:</p>
<%
    pageContext.setAttribute("studentName","Rahul");
%>
<p>Student Name:
    <%= pageContext.getAttribute("studentName") %>
</p>
<h3>8. Config Object</h3>
<p>Servlet Name:
    <%= config.getServletName() %>
</p>
<h3>Current Date</h3>
<p>
    <%= new Date() %>
</p>
</body>
</html>
```

### `header.jsp`

```jsp
<%@ page contentType="text/html; charset=UTF-8"%>
<div style="background-color:lightblue; padding:15px;">
    <h2>My JSP Application</h2>
    <hr>
</div>
```

### `footer.jsp`

```jsp
<div style="background-color:lightgray; padding:15px;">
    <hr>
    <p>Copyright © 2026 My JSP Application</p>
</div>
```

## Practical 6A — Update Employee Salary using JSP and JDBC

### `index.html`

```html
<html>
<body>
    <form action="UpdateEmp.jsp">
        Enter Employee No
        <input type="text" name="txtEno">
        <br><br>
        Enter Salary to update
        <input type="text" name="txtSal">
        <br>
        <input type="reset">
        <input type="submit">
    </form>
</body>
</html>
```

### `UpdateEmp.jsp`

```jsp
<%@ page contentType="text/html" import="java.sql.*" %>
<html>
    <body>
        <h1>Updating Employee Record</h1>
        <%
            String eno=request.getParameter("txtEno");
            String sal=request.getParameter("txtSal");
            try{
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection con =
                DriverManager.getConnection("jdbc:derby://localhost:1527/emp","root","root");
                PreparedStatement stmt = con.prepareStatement("select * from emp where eno=?");
                stmt.setString(1, eno);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    out.println("<h1>Employee"+rs.getString(2)+"Exist</h1>");
                    PreparedStatement pst= con.prepareStatement("update emp set sal=? where eno=?");
                            pst.setString(1, sal);
                            pst.setString(2, eno);
     pst.executeUpdate();
                            out.println("<h1>Employee Record updated !!!!</h1>");
                }
                else{
                    out.println("<h1>Employee Record not exist !!!!</h1>");
                }
            }
            catch(Exception e){out.println(e);}
            %>
    </body>
</html>
```

## Practical 6B — JSP Expression Language (EL)

### `index.jsp`

```jsp
<%@ page import="javax.servlet.http.Cookie" %>
<html>
<body>
    <h3>welcome to index page</h3>
    <%session.setAttribute("user","Admin");
    %>
    <%
        Cookie ck=new Cookie("name","mycookie");
        response.addCookie(ck);
        %>
        <form action="ExpressionLanguage.jsp">
              Enter Name: <input type="text" name="name"/><br/><br/>
            <input type="submit" value="Submit"/>
        </form>
</body>
</html>
```

### `ExpressionLanguage.jsp`

```jsp
<html>
<body>
    Welcome, ${param.name}
    Session Value is ${sessionScope.user}
    Cookie name is, ${cookie.name.value}
</body>
</html>
```

### `ELArithmeticOperator.jsp`

```jsp
<body>
    <%--arithmetic op--%>
    5*5+4:${5*5+4}<br>
    1.4E4+1.4:${1.4E4+1.4}<br>
    10 mod 4: ${10 mod 4}<br>
    15 div 3:${15 div 3}<br>
</body>
```

### `ELLogicalOperator.jsp`

```jsp
<body>
    <%--LogicalOperator--%>
    <h2>Logical Operator</h2>
    true and true:${true and true}<br>
    true && false: ${true && false}<br>
    true or true: ${true or true}<br>
    true || false: ${true ||false}<br>
    not true: ${not true}<br>
    !false: ${!false}<br>
</body>
```

### `ELRelationalOperator.jsp`

```jsp
<body>
    <%--RelationalOperator--%>
    <h2>Relational Operator</h2>
    10.0==10: ${10.0==10}<br>
    10.0 eq 10: ${10.0 eq 10}<br>
    ((20*10)!=200): ${((20*10)!=200)}<br>
    3 ne 3: ${3 ne 3}<br>
    3.2>=2: ${3.2>=2}<br>
    2<3: ${2<3}<br>
    4 lt 6: ${4 lt 6}<br>
    2 <= 4: ${2<= 4}<br>
    4 le 2: ${4 le 2}<br>
</body>
```

### `ELConditionalOperator.jsp`

```jsp
<body>
    <h2>Conditional Operator</h2>
    The result of 10>2 is:"${(10>2)?'greater':'lesser'}"
</body>
```

### `EmptyOperator.jsp`

```jsp
<html>
<body>
<h1>Empty Operator Example</h1>
The value for the operator is :: ${empty param.text}
</body>
</html>
```

## Practical 6C — JSP JSTL

### `index.html`

```html
<html>
<body>
    <a href="setDemo.jsp">SetDemo</a><br>
    <a href="Maxif.html">MaxIF</a><br>
    <a href="forEachDemo.jsp">ForEachDemo</a><br>
    <a href="outDemo.jsp">OutDemo</a><br>
    <a href="choose_when_otherwise.jsp">choose_when_otherwise</a>
</body>
</html>
```

### `setDemo.jsp`

```jsp
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle"scope="application"
       value="Dukes Soccer League: Registration"/>
${pageTitle}
```

### `Maxif.html`

```html
<form action="IFDemo.jsp">
    x=<input type="text"name="x"/><br>
    y=<input type="text"name="y"/><br>
    <input type="submit"value="Check Max"/>
</form>
```

### `IFDemo.jsp`

```jsp
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="x" value="${param.x}"/>
<c:set var="y" value="${param.y}"/>
<c:if test="${x>y}">
    <font color="blue"><h2>The Ans is:</h2></font>
    <c:out value="${x} is greater than ${y}"/>
</c:if>
    <c:if test="${y>x}">
        <font color="blue"><h2>The Ans is:</h2></font>
        <c:out value="${y} is greater than ${x}"/>
    </c:if>
```

### `forEachDemo.jsp`

```jsp
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach begin="1" end="10" var="i">
    The Square of<c:out value="${i}=${i*i}"/><br>
</c:forEach>
```

### `outDemo.jsp`

```jsp
<%@taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="name" value="John"/>
My name is:<c:out value="${name}"/>
```

### `URLDemo.jsp`

```jsp
<%@taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url value="index.html"/>
```

### `choose_when_otherwise.jsp`

```jsp
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="income" value="${5000*4}"/>
Your Income is:<c:out value="${income}"/>
<c:choose>
    <c:when test="${income<=1000}">
    Income is not good
</c:when>
<c:when test="${income>10000}">
    Income is Very good
</c:when>
<c:otherwise>
    Income is undermined
</c:otherwise>
</c:choose>
```

## Module 2

## Practical 7A — Currency Converter using EJB

### `index.html`

```html
<html>
<head>
    <title>Currency Converter</title>
</head>
<body>
    <form action="CCServlet">
        Enter Amount:
        <input type="text" name="amt"><br><br>
        Select Conversion Type:<br>
        <input type="radio" name="type" value="r2d">
        Rupees to Dollar<br>
        <input type="radio" name="type" value="d2r">
        Dollar to Rupees<br><br>
        <input type="reset">
        <input type="submit" value="CONVERT">
    </form>
</body>
</html>
```

### `CCBean.java`

```java
package mybeans;
import javax.ejb.Stateless;
@Stateless
public class CCBean implements CCBeanLocal {
    public double r2Dollar(double r) {
        return r / 95.16;
    }
    public double d2Rupees(double d) {
        return d * 95.16;
    }
}
```

### `CCBeanLocal.java`

```java
package mybeans;
import javax.ejb.Local;
@Local
public interface CCBeanLocal {
    public double r2Dollar(double r);
    public double d2Rupees(double d);
}
```

### `CCServlet.java`

```java
package mypack;
import java.io.*;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import mybeans.CCBeanLocal;
@WebServlet("/CCServlet")
public class CCServlet extends HttpServlet {
    @EJB
    CCBeanLocal obj;
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        double amt = Double.parseDouble(
            request.getParameter("amt"));
        String type = request.getParameter("type");
        if (type.equals("d2r")) {
            out.println("<h1>" + amt + " Dollars = "
                + obj.d2Rupees(amt) + " Rupees</h1>");
        } else if (type.equals("r2d")) {
            out.println("<h1>" + amt + " Rupees = "
                + obj.r2Dollar(amt) + " Dollars</h1>");
        }
    }
}
```

## Practical 8A — Servlet Hit Count using Singleton Session Bean

### `index.html`

```html
<html>
    <head>
        <title>Hit Button</title>
    </head>
    <body>
        <form action="ServletClient" method="post">
            <input type="submit" value="Take Hit Count">
        </form>
    </body>
</html>
```

### `CountServletHitsBean.java`

```java
package ejb;
import javax.ejb.Singleton;
@Singleton
public class CountServletHitsBean {
    private int hitCount;
    public synchronized int getCount()
    {
        return ++hitCount;
    }
}
```

### `ServletClient.java`

```java
package servlet;
import ejb.CountServletHitsBean;
import java.io.*;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet(name="ServletClient", urlPatterns={"/ServletClient"})
public class ServletClient extends HttpServlet {
    @EJB CountServletHitsBean obj;
    @Override
    protected void service (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        res.setContentType("text/html");
        PrintWriter out=res.getWriter();
        out.println("<b>Number of times this Servlet is accessed</b>:"+obj.getCount());
    }
}
```
