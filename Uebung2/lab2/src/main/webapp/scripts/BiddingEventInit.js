/**
 * Created by Butterkeks on 23/04/16.
 */

/*TODO:
 * Formular auf Detailseite mit Ajax abschicken -check
 * Validierung am Server, ob Gebot höher als aktuelles Gebot ist
 *     if false --> Fehlermeldung auf Detailseite -check
 *     if true ---> Kontostand und Aktionszähler aktualisiert & mit
 *         mit neuem Gebot am Server gespeichert
 * AKTIONSSZÄHLER: nur bei der ersten Teilnahme erhöht, nach
 *     Ablauf der Aktion Aktionszähler verringert & Gewinn-/Verlustzähler
 *     erhöht
 * Ajax-Response: Anzahl laufender Aktionen, Kontostand
 * Aktualisierung der Daten auf der Detailseite
 * WEBSOCKET KOMPONENTE:
 *     + Name des Höchstbietenden & sein Gebot an ALLE Clients
 *     + Wird die Person überboten --> Gutschreibung des Betrags
 *         am Server & Aktualiserung der Sidebar
 */


$(document).ready(function () {

    var $newPrice = $('#new-price');
    var $highestPrice = $('#highest-price');

    $('#submit-price').on('click', function (e) {
        e.preventDefault();

        var newPrice = $newPrice.val();
        var data = {
            id: window.location.search.match(/id=(\d+)/)[1],
            newPrice: newPrice
        };

        $.ajax({
            type: 'POST',
            url: '/bidding',
            data: JSON.stringify(data),
            success: function (newPrice) {
                $(".bid-error").hide();
                $highestPrice.html('<label> Highest:' + newPrice + '</label>');
            },
            error: function (error) {
                $(".bid-error").show();
            }
        });

        return false;
    });
});