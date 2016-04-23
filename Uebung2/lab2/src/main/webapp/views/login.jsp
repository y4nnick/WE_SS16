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
    <title>BIG Bid - Anmelden</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../styles/style.css">
</head>
<body data-decimal-separator="," data-grouping-separator=".">

<a href="#productsheadline" class="accessibility">Zum Inhalt springen</a>

<jsp:include page="header.jsp" flush="true"/>
<div class="main-container">
    <main aria-labelledby="formheadline">
        <form class="form" method="post" action="">
            <h2 id="formheadline" class="registration-headline">Anmelden</h2>
            <div class="form-row">
                <label class="form-label" for="email-input">
                    Email
                </label>
                <input type="email" name="email" id="email-input" required class="form-input">
                <span id="email-error" class="error-text"></span>
            </div>
            <div class="form-row">
                <label class="form-label" for="password-input">
                    Passwort
                </label>
                <input type="password" name="password" id="password-input" required class="form-input" minlength="4"
                       maxlength="12">
                <span id="password-error" class="error-text"></span>
            </div>
            <div class="form-row form-row-center">
                <button class="button button-submit">
                    Anmelden
                </button>
            </div>
        </form>
    </main>
</div>
<jsp:include page="footer.jsp" flush="true"/>
<script src="/scripts/jquery.js"></script>
<script src="/scripts/framework.js"></script>
</body>
</html>