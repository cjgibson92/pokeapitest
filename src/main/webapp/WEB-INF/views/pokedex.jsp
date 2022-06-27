<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Spring Boot Form handling example</title>
</head>
<body>
<table>
<c:forEach items="${pokemonList}" var="pokemon"> 
 <tr> 
  	<td>${pokemon.id}</td>
    <td><a href="/getSpecifics?pokemonId=${pokemon.id}">${pokemon.name}</a></td>
    <td>${pokemon.weight}</td>
    <td>
    <ul>
    	<c:forEach items="${pokemon.types}" var="type">
    		<li>${type}</li>
    	</c:forEach>
    </ul>
    </td>
    <td>
    <ul>
    	<c:forEach items="${pokemon.abilities}" var="ability">
    		<li>${ability.name}</li>
    	</c:forEach>
    </ul>
    </td>
    <td><img src="${pokemon.sprite}"></td>
  </tr>
</c:forEach>
</table>
</body>
</html>