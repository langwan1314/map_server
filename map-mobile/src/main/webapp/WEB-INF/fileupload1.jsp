<%--
  Created by IntelliJ IDEA.
  User: J
  Date: 2015/12/4
  Time: 10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传1</title>
</head>

<body>
<form action="/mobile/chat/image/fsend" method="post" enctype="multipart/form-data"
      accept-charset="UTF-8">
    <input type="text" name="receiverId"/>
    <input type="file" name="2_image1"/>
    <input type="submit" value="upload"/>
</form>
</body>
</html>
