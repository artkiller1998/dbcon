$(".add").click(function() {
    $("form > p:first-child").clone(true).
                            find("input").val("").end()
                            .insertBefore("form > p:last-child");
    $('.task-remove-btn').attr('disabled',false);
    return false;
});

$(".remove").click(function() {
    if ($('#form-task > #task').length == 1) {
        $('.task-remove-btn').attr('disabled',true);
    }
    else {
        $('.task-remove-btn').attr('disabled',false);
        $(this).parent().remove();
    }
});
