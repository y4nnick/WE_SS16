<%--
  Created by IntelliJ IDEA.
  User: mstrasser
  Date: 4/12/16
  Time: 12:31 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<jsp:useBean id="user" scope="session" class="at.ac.tuwien.big.we16.ue2.model.User"/>--%>
<aside class="sidebar" aria-labelledby="userinfoheadline">
    <div class="user-info-container">
        <h2 class="accessibility" id="userinfoheadline">Benutzerdaten</h2>
        <dl class="user-data properties">
            <dt class="accessibility">Name:</dt>
            <dd class="user-name">John Doe</dd>
            <dt>Kontostand:</dt>
            <dd>
                <%--<span class="balance"><%= user.getBalance() %></span>--%>
            </dd>
            <dt>Laufend:</dt>
            <dd>
                <span class="running-auctions-count">0</span>
                <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
            </dd>
            <dt>Gewonnen:</dt>
            <dd>
                <%--<span class="won-auctions-count"><%= user.getWonAuctions() %></span>--%>
                <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
            </dd>
            <dt>Verloren:</dt>
            <dd>
                <%--<span class="lost-auctions-count"><%= user.getLostAuctions() %></span>--%>
                <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
            </dd>
        </dl>
    </div>
    <div class="recently-viewed-container">
        <h3 class="recently-viewed-headline">Zuletzt angesehen</h3>
        <ul class="recently-viewed-list"></ul>
    </div>
</aside>