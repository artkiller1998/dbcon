$('body').on('click', '.password-control', function(){
    if ($(this).parent().find('.password-input').attr('type') == 'password'){

        $(this).addClass('view');

        $(this).parent().find('.password-input').attr('type', 'text');

    } else {

        $(this).removeClass('view');

        $(this).parent().find('.password-input').attr('type', 'password');

    }

    return false;

});

$(document).ready(function(){
    var passOne = $("#passOne").val();
    var passTwo = $("#passTwo").val();

    $("#checkText").html("");
    $("#changeButton").prop('disabled', true);

    var checkAndChange = function()
    {
        if(passOne.length < 1){
            if($("#check").hasClass("correct")){
                $("#check").removeClass("correct").addClass("incorrect");
                $("#checkText").html("");
            }else{
                $("#checkText").html("");
                $("#changeButton").prop('disabled', true);
            }
        }
        else if($("#check").hasClass("incorrect"))
        {
            if(passOne == passTwo){
                $("#check").removeClass("incorrect").addClass("correct");
                $("#checkText").html("Пароли совпадают");
                $("#changeButton").prop('disabled', false);
            }
            else {
                $("#checkText").html("Пароли не совпадают");
                $("#changeButton").prop('disabled', true);
            }
        }
        else
        {
            if(passOne != passTwo){
                $("#check").removeClass("correct").addClass("incorrect");
                $("#checkText").html("Пароли не совпадают");
                $("#changeButton").prop('disabled', true);
            }
        }
    }



    $("input").keyup(function(){
        var newPassOne = $("#passOne").val();
        var newPassTwo = $("#passTwo").val();

        passOne = newPassOne;
        passTwo = newPassTwo;

        checkAndChange();
    });
});