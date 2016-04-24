/*
    Helper functions for the first exercise of the Web Engineering course
*/

/**
 * Gets the value of the given parameter
 * @param sParam the parameter
 * @returns {boolean} the value
 */
var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

var DEBUG = true;
var idparam = getUrlParameter("id");
var inDetailView = ((typeof idparam) !== 'undefined');

/*
    checks if native form validation is available.
    Source/Further informations: http://diveintohtml5.info/everything.html
*/
function hasFormValidation() {
    return 'noValidate' in document.createElement('form');
}

/*
    checks if native date input is available.
    Source/Further informations: http://diveintohtml5.info/everything.html
*/
function hasNativeDateInput() {
    var i = document.createElement('input');
    i.setAttribute('type', 'date');
    return i.type !== 'text';
}

var DATE_DELIMITERS = ['/','\\','-'];

/*
    returns the string representation of a date input field in the format dd.mm.yyyy.
    If the value of the input field can't be interpreted as a date, the original value is returned.
*/
function getNormalizedDateString(selector) {
    value = $(selector).val();

    // normalize delimiter to .
    for(var i = 0; i < DATE_DELIMITERS.length; i++)
        value = value.split(DATE_DELIMITERS[i]).join(".");

    // check if date might be reverse, i.e., yyyy.mm.dd
    rehtml5 = /^(\d{4})\.(\d{1,2})\.(\d{1,2})$/;
    if(regs = value.match(rehtml5))
        value = regs[3] + "." + regs[2] + "." + regs[1];

    // check if valid date string dd.mm.yyyy
    date = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
    if(value.match(date))
      return value;
    return $(selector).val();
}

/*
    returns the string representation of the given value (seconds) in the format mm:ss.
*/
function secToMMSS(value){
    var minutes = Math.floor(value / 60);
    var seconds = (value % 60);

    if(seconds < 10) {
        seconds = "0" + seconds;
    }
    if(minutes < 10) {
        minutes = "0" + minutes;
    }
    return minutes + ":" + seconds;
}

/*
  checks if native form validation is available.
  Source/Further informations: http://diveintohtml5.info/storage.html
*/
function supportsLocalStorage() {
    try {
        return 'localStorage' in window && window['localStorage'] !== null;
    } catch(e) {
        return false;
    }
}

function writeNewText(el, secs) {
    if (secs > 0) {
        secs--;
        el.html(secToMMSS(secs));
    }
    else {
        el.html(el.data("end-text"));
        el.parents(".product").addClass("expired");
    }
}
$(".js-time-left").each(function() {
    var endTime = $(this).data("end-time").split(",");
    endTime = new Date(endTime[0],endTime[1]-1,endTime[2],endTime[3],endTime[4],endTime[5],endTime[6]);
    var today = new Date();
    var diffS = Math.round((endTime - today) / 1000);
    var that = $(this);
    writeNewText(that, diffS);
    setInterval(function () {
        if (diffS > 0) {
            diffS--;
        }
        writeNewText(that, diffS);
    }, 1000);
});

function formatCurrency(x) {
    // regex from http://stackoverflow.com/a/2901298
    return x.toFixed(2).replace(".", $("body").data('decimal-separator')).replace(/\B(?=(\d{3})+(?!\d))/g, $("body").data('grouping-separator')) + "&nbsp;€";
}

MSG_TYPE = {
    NEW_BID : "NEW_BID",
    NEW_HIGHEST_BID : "NEW_HIGHEST_BID",
    AUCTION_EXPIRED : "AUCTION_EXPIRED"
}

// Depending on the setup of your server, servlet, and socket, you may have to
// change the URL.
var socket = new WebSocket("ws://localhost:8080/socket");
socket.onmessage = function (event) {

    var msg = jQuery.parseJSON(event.data);
    var msgType = msg.msgType;

    if(DEBUG)console.log("Message of type "+msgType+" received");
    if(DEBUG)console.log(msg);

    switch (msgType){
        case MSG_TYPE.NEW_BID:
            onNewBidMessage(msg);
            break;
        case MSG_TYPE.NEW_HIGHEST_BID:
            onNewHighestBid(msg);
            break;
        case MSG_TYPE.AUCTION_EXPIRED:
            onAuctionExpired(msg);
            break;
        default:
            console.log("Unknown message type: " + msgType);
            break;
    }
};

/**
 * Gets called when an auction is expired
 * @param msg the message with the new parameters for the user
 */
function onAuctionExpired(msg){

    updateSideBar(msg.balance,msg.running,msg.won,msg.lost);

    if(inDetailView && idparam == msg.product){
        $(".auction-expired-text").show();
        $(".detail-time").hide();
        $(".bid-form").hide();
    }
}

/**
 * Gets called when the user get surpassed on an auction
 * @param msg the message with the new balance for the user
 */
function onNewHighestBid(msg){
    updateSideBar(msg.balance,null,null,null);
}

/**
 * Gets called when a new bid is placed
 * @param msg the message with the parameters of the new bid
 */
function onNewBidMessage(msg){

    var newPrice = msg.price;
    var bidder = msg.bidder;
    var productID = msg.product;

    //Check if overview or details view
    if(!inDetailView){

        //Set Price and new highest bidder
        var productDiv = $('[data-product-id='+productID+']');
        productDiv.find('.product-price').text(newPrice + "€");
        productDiv.find('.product-highest').text(bidder);

        //Animate update
        productDiv.find(".product").addClass("highlightUpdate");
        setTimeout(function() {productDiv.find(".product").removeClass("highlightUpdate");}, 1200);

    }else if(idparam == productID){     //Check if details view of product
        //Set Price and new highest bidder
        $('.highest-bid').text(newPrice + "€");
        $('.highest-bidder').text(bidder);

        //Animate update
        $(".bid-form").addClass("highlightUpdate");
        setTimeout(function() {$(".bid-form").removeClass("highlightUpdate");}, 1200);
    }

}

/**
 * Updates the sidebar of the current user
 * @param balance the new balance or null if not updated
 * @param running the new running auctions count or null if not updated
 * @param won the new won auctions count or null if not updated
 * @param lost the new lost auctions count or null if not updated
 */
function updateSideBar(balance, running, won, lost){

    //Update values
    var update = (balance != null || running != null || won != null || lost != null);

    if(balance != null){
        $('.balance').text(balance + "€");
    }

    if(running != null){
        $('.running-auctions-count').text(running);
    }

    if(won != null){
        $('.won-auctions-count').text(won);
    }

    if(lost != null){
        $('.lost-auctions-count').text(lost);
    }

    //Animate update
    if(update){
        $(".user-info-container").addClass("highlightUpdate");
        setTimeout(function() {$(".user-info-container").removeClass("highlightUpdate");}, 1200);
    }
}


// Show visited Products
if(supportsLocalStorage()){

    var products = localStorage["visited"];
    if((typeof products) !== 'undefined') {

        //Remove trailing comma and split
        var products = products.replace(/(^,)|(,$)/g, "");
        var productsArray = products.split(',');

        console.log("loaded Products");
        console.log(productsArray);

        //Empty the list
        $(".recently-viewed-headline").show();
        var list = $(".recently-viewed-list");
        list.show();
        list.empty();

        productsArray.forEach(function(p){
            var values = p.split(":");
            list.append('<li><a href="/details?id='+values[0]+'">'+values[1]+'</a></li>');
        });

    }
}