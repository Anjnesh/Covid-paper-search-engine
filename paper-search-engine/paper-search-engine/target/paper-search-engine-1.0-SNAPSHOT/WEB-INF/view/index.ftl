
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
    <title>Covid-Paper Search Engine</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/homepage_style.css">
</head>
<body>
<div class="header">
    <h1>Covid-Paper Search</h1>

    <div class="form_box">

        <form:form action="processQuery" modelAttribute="query">

            <td><form:input path="queryContent" name="q" class="search-field-content"
                            placeholder="paper name, authors, publication type.."/></td>

            <td><form:select path="queryType" class="search-field-type">
                    <form:options items="${query.typeOptions}" />
                </form:select></td>

            <td><input type="submit" value="Search" class="search-btn"/>
        </form:form></td>
    </div>
</div>





</body>
</html>
