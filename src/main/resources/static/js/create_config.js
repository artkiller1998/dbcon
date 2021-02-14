$(".add").click(function() {
    $("form > p:first-child").clone(true).
                            find("input").val("").end()
                            .insertBefore("form > p:last-child");
    $('.task-remove-btn').attr('disabled',false);

    return false;
});

$(".add_mark").click(function() {
    if ($(this).parent().next(".mark_t").length == 0)
        $("#mark").clone(true).
        find("input").val("").end()
            .insertAfter($(this).parent());
    else
        $("#mark").clone(true).
        find("input").val("").end()
            .insertAfter($(this).parent().next(".mark_t")); //$(this).parent()
    //$('.task-remove-btn').attr('disabled',false);
    return false;
});

$(".remove_mark").click(function() {
    if ($('#form-task > #mark').length == 1) {
        $('.mark-remove-btn').attr('disabled',true);
    }
    else {
        $(this).parent().remove();
        $('.mark-remove-btn').attr('disabled',false);
    }
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

function handleSubmit(event) {
    event.preventDefault();

    const data = new FormData(event.target);

    // const value = Object.fromEntries(data.entries());
    //
    // task_names = data.getAll("task_name");

    console.log(JSON.stringify($('form').serializeArray()));
    fields = $('form').serializeArray();
    console.log(fields + fields.length);
    // $( "#results" ).empty();
    tasks = [];
    task = {};
    marks = [];
    mark = {};
    jQuery.each( fields, function( it, field ) {
        // console.log((field.value));
        // console.log(JSON.stringify(tasks));
        // console.log((field.name))
        // console.log(($('form').serializeArray()));
        // console.log({ task_names });
        // console.log({ value });
        let i;
        if (field.name == 'task_name') {
            task = {};
            marks = [];
            task._id = field.value;
            i = it;
            i++;
            while (fields[i].name != 'task_name') {
                mark = {};
                if (fields[i].name == 'mark_descr') {
                    mark.descr = fields[i].value;
                    i++;
                }
                if (fields[i].name == 'mark_scale') {
                    mark.scale = fields[i].value;
                    i++;
                }
                marks.push(mark);
                if (i >= fields.length)
                    break;
            }
            task.mark = marks;
            tasks.push(task);
        }


        // $( "#results" ).append( task.value + " " );
    });
    console.log(JSON.stringify(tasks));


    var json = JSON.stringify(tasks);
    var blob = new Blob([json], {type: "application/json"});
    var url  = URL.createObjectURL(blob);

    var a = document.createElement('a');
    a.download    = "enter_subject_id.json";
    a.href        = url;
    a.click();
    //window.location.href = url;
    //a.textContent = "Загрузка файла.json";

    document.getElementById('download_button').appendChild(a);
}

const form = document.querySelector('form');
form.addEventListener('submit', handleSubmit);



