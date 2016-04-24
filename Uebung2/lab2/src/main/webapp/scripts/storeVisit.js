/**
 * Created by Butterkeks on 23/04/16.
 */

$(document).ready(function () {

    if(supportsLocalStorage()){
        var idparam = getUrlParameter("id");
        var idDefined = ((typeof idparam) !== 'undefined');

        if(idDefined){

            var productName = $(".main-headline").text();
            var storeValue = idparam+":"+productName;

            if(!((typeof localStorage["visited"]) !== 'undefined')){
                localStorage["visited"] = "";
            }

            if (localStorage["visited"].indexOf(storeValue+",") < 0){
                localStorage["visited"] += (storeValue+",");
            }
        }else{
            console.log("No product ID in details view");
        }
    }else{
        $(".recently-viewed-headline").hide();
        $(".recently-viewed-list").hide();
    }
    
});