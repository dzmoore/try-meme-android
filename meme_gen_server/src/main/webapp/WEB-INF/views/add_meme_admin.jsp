<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Add New Meme</title>
</head>
<body>
<h1> 
    Add New Meme
</h1>
<form action="add_meme_admin" method="post">
<table>
    <tr>
        <td>Background File Name:</td>
        <td><input type="text" name="backgroundFileName" value=""  /></td>
    </tr>
    <tr>
        <td>Meme Title:</td>
        <td><input type="text" name="memeTitle" value=""  /></td>
    </tr>
    
    <tr>
        <td>Sample 1 Top Text:</td>
        <td><input type="text" name="sample1TopText" value=""  /></td>
    </tr>
    <tr>
        <td>Sample 1 Bottom Text:</td>
        <td><input type="text" name="sample1BottomText" value=""  /></td>
    </tr> 
    
    
    <tr>
        <td>Sample 2 Top Text:</td>
        <td><input type="text" name="sample2TopText" value=""  /></td>
    </tr>
    <tr>
        <td>Sample 2 Bottom Text:</td>
        <td><input type="text" name="sample2BottomText" value=""  /></td>
    </tr> 
    
    
    <tr>
        <td>Sample 3 Top Text:</td>
        <td><input type="text" name="sample3TopText" value=""  /></td>
    </tr>
    <tr>
        <td>Sample 3 Bottom Text:</td>
        <td><input type="text" name="sample3BottomText" value=""  /></td>
    </tr> 
</table>
<input value="Add" type="submit" />
</form>
</body>
</html>
