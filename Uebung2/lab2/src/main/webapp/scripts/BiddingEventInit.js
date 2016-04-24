/**
 * Created by Butterkeks on 23/04/16.
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
            success: function (response) {
                var data = JSON.parse(response);
                $(".bid-error").hide();
                $('.highest-bid').text(data.price + "€");
                $('.highest-bidder').text(data.bidder);
                $(".bid-form-field").val("");

                $(".balance").html(data.balance + "€");
                $(".running-auctions-count").html(data.running);
            },
            error: function (error) {
                $(".bid-error").show();
            }
        });

        return false;
    });


});