<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Spring Boot Form handling example</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
    rel="stylesheet"
    integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
    crossorigin="anonymous">
</head>
<body>
<h3>${pokemon.name}</h3>
<div class="row">
<div class="col-md-2">
Types
</div>
<div class="col-md-2">
Weight
</div>
<div class="col-md-2">
Abilities
</div>
<div class="col-md-4">
Description
</div>

</div>
<div class="row">
<div class="col-md-2">
<ul>
    	<c:forEach items="${pokemon.types}" var="type">
    		<li>${type}</li>
    	</c:forEach>
    </ul>
</div>
<div class="col-md-2">
${pokemon.weight}
</div>
<div class="col-md-2">
 <ul>
    	<c:forEach items="${pokemon.abilities}" var="ability">
    		<li>${ability.name}</li>
    	</c:forEach>
    </ul>
</div>
<div class="col-md-4">
 ${pokemon.description}
</div>
<div class="col-md-2">
<img src="${pokemon.sprite}">
</div>
</div>
<div class="row">
<table>
 <tr> 
 		<c:forEach items="${pokemon.evolucionChain}" var="evolution">
    		<td><a href="/getSpecifics?pokemonId=${evolution.id}">${evolution.name}</td>
    	</c:forEach>
  </tr>
</table>
</div>
</body>
</html>