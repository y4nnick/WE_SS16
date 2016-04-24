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
    <title>BIG Bid - ${product.getName()}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../styles/style.css">
</head>
<body data-decimal-separator="," data-grouping-separator=".">

<a href="#productsheadline" class="accessibility">Zum Inhalt springen</a>

<jsp:include page="header.jsp" flush="true"/>
<div class="main-container">
    <jsp:include page="sidebar.jsp" flush="true"/>
    <main aria-labelledby="productheadline" class="details-container">
        <div class="details-image-container">
            <img class="details-image" src="../images/${product.getImg()}" alt="">
        </div>
        <div data-product-id="ce510a73-408f-489c-87f9-94817d845773" class="details-data">
            <h2 class="main-headline" id="productheadline">${product.getName()}</h2>

            <div class="auction-expired-text" style="display:${(product.isRunning())?'none':'block'}">
                <p>
                    Diese Auktion ist bereits abgelaufen.
                    Das Produkt wurde um
                    <span class="highest-bid">${product.getPrice()} €</span> an
                    <span class="highest-bidder">${product.getHighestBidName()}</span> verkauft.
                </p>
            </div>
            <p class="detail-time" style="display:${(!product.isRunning())?'none':'block'}">Restzeit: <span data-end-time="${product.getAuctionEndString()}" class="detail-rest-time js-time-left"></span>
            </p>
            <form class="bid-form" method="post" action="" style="display:${(!product.isRunning())?'none':'flex'}">

                <!--<label class="bid-form-field" id="highest-price"></label>-->
                <label class="bid-form-field" id="highest-price">
                    <span class="highest-bid">${product.getPrice()} €</span>
                    <span class="highest-bidder">${product.getHighestBidName()}</span>
                </label>

                <label class="accessibility" for="new-price"></label>
                <input type="number" step="0.01" min="0" id="new-price" class="bid-form-field form-input"
                       name="new-price" required>
                <p class="bid-error">Es gibt bereits ein höheres Gebot oder der Kontostand ist zu niedrig.</p>
                <input type="submit" id="submit-price" class="bid-form-field button" name="submit-price" value="Bieten">
            </form>
        </div>
    </main>
</div>
<jsp:include page="footer.jsp" flush="true"/>
<script src="/scripts/jquery.js"></script>
<script src="/scripts/framework.js"></script>
<script src="/scripts/BiddingEventInit.js"></script>
<script src="/scripts/storeVisit.js"></script>
</body>
</html>