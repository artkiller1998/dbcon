$(".add").click(function() {
    $("form > p:first-child").clone(true).
                            find("input").val("").end()
                            .insertBefore("form > p:last-child");
    $('.task-remove-btn').attr('disabled',false);
    return false;
});

function handleSubmit(event) {
    event.preventDefault();

    const data = new FormData(event.target);

    const value = Object.fromEntries(data.entries());

    task_names = data.getAll("task_name");

    console.log(JSON.stringify($('form').serializeArray()));
    console.log(($('form').serializeArray()));
    console.log({ task_names });
    console.log({ value });
}

const form = document.querySelector('form');
form.addEventListener('submit', handleSubmit);


// var shipments = [],
//     task  = {},
//     _id = {},
//     mark = {};
// $(".task").each(function() {
//     var task = {},
//         boxes = {};
//     $(this).children().each(function(){
//         var boxdata = $(this).data();
//         mark = {};
//         $(this).children().each(function(){
//             var candydata = $(this).data();
//             mark[candydata["candyid"]] = {
//                 scale: candydata["color"],
//                 descr: candydata["flavor"]
//             };
//             boxes[boxdata["boxid"]] = candy;
//         });
//         //console.log(JSON.stringify(boxes)); // works
//     });
//     shipment = {shipment: boxes};
//     shipments.push(shipment); // for multiples
// });
//
// console.log(JSON.stringify(shipments[0]));
// console.log(shipments.length);



$(".add_mark").click(function() {
    $("#mark").clone(true).
    find("input").val("").end()
        .insertAfter($(this).parent());
    //$('.task-remove-btn').attr('disabled',false);
    return false;
});

// $('#add_mark').on('click', function() {
//     // while ($(this).val() != $('#form-task > #mark').length) {
//     //     if ($(this).val() > $('#form-task > #mark').length) {
//     //         add_mark();
//     //     }
//     //     else {
//     //         remove_mark();
//     //     }
//     // }
//
//      add_mark();
//     $(this).val() // get the current value of the input field.
// });



$(".remove_mark").click(function() {
    $(this).parent().remove();
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
/*
$(window).on("wheel", function(e) {
    focusedEl = document.activeElement;
    if(focusedEl === excludedEl){
        //Exclude one specific element for UI comparison
        return;
    }
    if (focusedEl.nodeName='input' && focusedEl.type && focusedEl.type.match(/number/i)){
        e.preventDefault();
        var max=null;
        var min=null;
        if(focusedEl.hasAttribute('max')){
            max = focusedEl.getAttribute('max');
        }
        if(focusedEl.hasAttribute('min')){
            min = focusedEl.getAttribute('min');
        }
        var value = parseInt(focusedEl.value, 10);
        if (e.originalEvent.deltaY < 0) {
            value++;
            if (max !== null && value > max) {
                value = max;
            }
        } else {
            value--;
            if (min !== null && value < min) {
                value = min;
            }
        }
        focusedEl.value = value;
    }
});*/

