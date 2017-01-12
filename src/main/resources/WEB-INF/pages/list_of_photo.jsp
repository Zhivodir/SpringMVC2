<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Prog.kiev.ua</title>
  </head>
  <body>
     <div align="center">
         <form action="/delete_all_checked" method="POST">
             <table>
                 <tr>
                     <td></td>
                     <td>Photo</td>
                     <td>ID</td>
                 </tr>

                 <c:forEach var="photo" items="${listOfPhotos}">
                 <tr>
                     <td><input type="checkbox" name = "checked_photos" value="${photo.key}"></td>
                     <td>${photo.key}</td>
                     <td><img src="/photo/${photo.key}" width="100px" /></td>
                 </tr>
                 </c:forEach>
             </table>
             <input type="submit" value="Delete Photo"/>
         </form>
      </div>
  </body>
</html>
