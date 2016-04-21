<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--

@@ -0,0 +1,85 @@
  Created by IntelliJ IDEA.
  User: mstrasser
  Date: 4/12/16
  Time: 12:05 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="user" scope="session" class="at.ac.tuwien.big.we16.ue2.model.User"/>
<!doctype html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <title>BIG Bid - Produkte</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body data-decimal-separator="," data-grouping-separator=".">

<a href="#productsheadline" class="accessibility">Zum Inhalt springen</a>

<jsp:include page="header.jsp" flush="true"/>
<div class="main-container">
    <aside class="sidebar" aria-labelledby="userinfoheadline">
        <div class="user-info-container">
            <h2 class="accessibility" id="userinfoheadline">Benutzerdaten</h2>
            <dl class="user-data properties">
                <dt class="accessibility">Name:</dt>
                <dd class="user-name">John Doe</dd>
                <dt>Kontostand:</dt>
                <dd>
                    <span class="balance"><%= user.getBalance() %></span>
                </dd>
                <dt>Laufend:</dt>
                <dd>
                    <span class="running-auctions-count">0</span>
                    <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
                </dd>
                <dt>Gewonnen:</dt>
                <dd>
                    <span class="won-auctions-count"><%= user.getWonAuctions() %></span>
                    <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
                </dd>
                <dt>Verloren:</dt>
                <dd>
                    <span class="lost-auctions-count"><%= user.getLostAuctions() %></span>
                    <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
                </dd>
            </dl>
        </div>
        <div class="recently-viewed-container">
            <h3 class="recently-viewed-headline">Zuletzt angesehen</h3>
            <ul class="recently-viewed-list"></ul>
        </div>
    </aside>
    <main aria-labelledby="productsheadline">
        <h2 class="main-headline" id="productsheadline">Produkte</h2>
        <div class="products">
            <c:forEach items="${products}" var="product">
                <div class="product-outer" data-product-id="${product.getID()}">
                    <a href="" class="product expired "
                       title="Mehr Informationen zu ${product.getName()}">
                        <img class="product-image" src="images/${product.getImg()}" alt="">
                        <dl class="product-properties properties">
                            <dt>Bezeichnung</dt>
                            <dd class="product-name">${product.getName()}</dd>
                            <dt>Preis</dt>
                            <dd class="product-price">
                                    ${product.getPrice()}
                            </dd>
                            <dt>Verbleibende Zeit</dt>
                            <dd data-end-time="2016,03,14,14,30,23,288" data-end-text="abgelaufen"
                                class="product-time js-time-left"></dd>
                            <dt>Höchstbietende/r</dt>
                            <dd class="product-highest">Jane Doe</dd>
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
