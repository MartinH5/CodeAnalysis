<%-- 
    Document   : newjsp
    Created on : May 23, 2018, 1:15:09 PM
    Author     : Lasse
--%>

<%@page import="entities.AuthorBook"%>
<%@page import="java.util.ArrayList"%>
<%@page import="queries.QueryInterface"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World! <%= request.getParameter("city")%></h1>

        <div>
            <%
                ArrayList<AuthorBook> ret = QueryInterface.getMentioningBooksWithAuthors("city", QueryInterface.DBChoice.DB_MYSQL);
                
                out.write("Length: " + ret.size());
            %>
        </div>

    </body>
</html>
