$(function () {
    var validateMandatory = function () {
        var elem = $(this);
        var val = elem.val();

        elem.find("~ .error-mandatory").hide();

        if (val == null || val == "") {
            elem.find("~ .error-mandatory").show();
            return false;
        }
    };

    $("#fname").on('focus keyup', validateMandatory);
    $("#lname").on('focus keyup', validateMandatory);
    $("#bday").on('focus keyup', validateMandatory);
    $("#password").on('focus keyup', validateMandatory);
    $("#email").on('focus keyup', validateMandatory);


    function isMinAge(normalizedBirthday) {
        var minAge = 18;
        var tempDate = new Date(normalizedBirthday.getFullYear() + minAge, normalizedBirthday.getMonth(), normalizedBirthday.getDate());
        return (tempDate <= new Date());
    }

    function isValidDate(normalizedBirthday) {
        return !isNaN(normalizedBirthday.getTime());
    }

    var validateBirthday = function () {
        var normalizedDate = getNormalizedDateString("#bday").split(".");
        var normalizedBirthday = new Date(normalizedDate[2], normalizedDate[1] - 1, normalizedDate[0]);
        var elem = $(this);

        elem.find("~ .error-invalid-date").hide();
        elem.find("~ .error-not-born-yet").hide();
        elem.find("~ .error-too-young").hide();

        if (!isValidDate(normalizedBirthday)) {
            elem.find("~ .error-invalid-date").show();
            return false;
        }

        if (normalizedBirthday > new Date()) {
            elem.find("~ .error-not-born-yet").show();
            return false;
        }

        if (!isMinAge(normalizedBirthday)) {
            elem.find("~ .error-too-young").show();
            return false;
        }
    };

    $("#bday").on('change', validateBirthday);

    var validateEmail = function () {
        var elem = $(this);
        var regex = /^\S+@\S+\.\S+$/;

        elem.find("~ .error-invalid").hide();

        if (!regex.test(elem.val())) {
            elem.find("~ .error-invalid").show();
            return false;
        }
    };

    $("#email").on('change', validateEmail);

    var validatePassword = function () {
        var elem = $(this);
        var val = elem.val();

        elem.find("~ .error-too-short").hide();
        elem.find("~ .error-too-long").hide();

        if (val.length < 4) {
            elem.find("~ .error-too-short").show();
            return false;
        }

        if (val.length > 8) {
            elem.find("~ .error-too-long").show();
            return false;
        }
    };

    $('#password').on('change', validatePassword);

    var validateAGBCheckBox = function () {
        var elem = $(this);
        var val = elem.val();

        elem.find("~ .error-not-checked").hide();

        if(!val.checked){
            elem.find("~ .error-not-checked").show();
            return false;
        }
    }

    $('#AGB').on('change', validateAGBCheckBox);

});
