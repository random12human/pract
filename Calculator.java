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
