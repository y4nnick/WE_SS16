<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--

@@ -0,0 +1,85 @@
  Created by IntelliJ IDEA.
  User: mstrasser
  Date: 4/12/16
  Time: 12:05 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <title>BIG Bid - Produkte</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery-ui.css">
    <script src="../scripts/jquery.js"></script>
    <script src="../scripts/jquery-ui.js"></script>
    <script src="../scripts/framework.js"></script>
</head>
<body data-decimal-separator="," data-grouping-separator=".">

<a href="#productsheadline" class="accessibility">Zum Inhalt springen</a>

<jsp:include page="header.jsp" flush="true"/>
<div class="main-container">
    <jsp:include page="sidebar.jsp" flush="true"/>
    <main aria-labelledby="productsheadline">
        <h2 class="main-headline" id="productsheadline">Produkte</h2>
        <div class="products">
            <c:forEach items="${products.values()}" var="product">
                <div class="product-outer transition" data-product-id="${product.getID()}">
                    <a href="details?id=${product.getID()}" class="product transition ${(product.isRunning())?"":"expired"} <%--expired--%> " title="Mehr Informationen zu ${product.getName()}">
                        <img class="product-image" src="images/${product.getImg()}" alt="product.getName()">
                        <dl class="product-properties properties">
                            <dt>Bezeichnung</dt>
                            <dd class="product-name">${product.getName()}</dd>
                            <dt>Preis</dt>
                            <dd class="product-price">
                                    ${product.getPrice()} €
                            </dd>
                            <dt>Verbleibende Zeit</dt>
                            <dd data-end-time="${product.getAuctionEndString()}" data-end-text="abgelaufen"
                                class="product-time js-time-left"></dd>
                            <dt>Höchstbietende/r</dt>
                            <dd class="product-highest">${product.getHighestBidName()}</dd>
                        </dl>
                    </a>
                </div>
            </c:forEach>
        </div>
    </main>
</div>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
