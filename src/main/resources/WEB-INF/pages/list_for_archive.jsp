<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Prog.kiev.ua</title>
  </head>
  <body>
     <div align="center">
         <form action="/archive_all_checked" method="POST">
             <table>
                 <tr>
                     <td>ID</td>
                     <td>Photo</td>
                     <td></td>
                 </tr>

                 <c:forEach var="photo" items="${listForArchive}">
                 <tr>
                     <td>${photo.key}</td>
                     <td><img src="/photo/${photo.key}" width="100px" /></td>
                     <td><input type="checkbox" name = "checked_photos" value="${photo.key}"></td>
                 </tr>
                 </c:forEach>
             </table>
             <input type="submit" value="Archive photo"/>
         </form>
      </div>
  </body>
</html>
