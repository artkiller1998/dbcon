$(function(){
	$('#item_list').hide()
});

function Color (elem, currentValue, max) {
	if (max == 10) {
		max = 9
		currentValue -= 1
	}
	else if (max == 5) {
		max = 4
		currentValue -= 1
		if (currentValue == 1) currentValue = 0
	}
	else if (max == 3) {
		max = 2
		if (currentValue == "-") currentValue = 0
		if (currentValue == "*") currentValue = 1
		if (currentValue == "+") currentValue = 2 
	}
	else if (max == 2) {
		if (currentValue == "-") currentValue = 0
		if (currentValue == "+") currentValue = 2 
	}

	//console.log('max',max)
	//console.log('currentValue',currentValue)

	if (currentValue > (max / 2)) {
		cur = currentValue - (max / 2)
	}
	else {
		cur = currentValue
	}

 	if (currentValue <= (max / 2)) {
 		changeColor(elem, cur, max / 2, [241,23,18], [227,200,0])
	}
	else {
		changeColor(elem, cur, max / 2, [160,121,67], [76,175,80])
	}	

}


function changeColor(elem, currentValue, max , start , end) {

	fromR = start[0];
	fromG = start[1];
	fromB = start[2];

	toR = end[0];
	toG = end[1];
	toB = end[2];

	deltaR = parseInt((toR - fromR) / max);
	deltaG = parseInt((toG - fromG) / max);
	deltaB = parseInt((toB - fromB) / max);

	R = fromR + currentValue * deltaR;
	G = fromG + currentValue * deltaG;
	B = fromB + currentValue * deltaB;

	if (!isNaN(R)) {
		$(elem).css('background-color', 'rgba('+R+','+G+','+B+',0.9)');
		var rgba='rgb('+R+','+G+','+B+')'
	}

}

$(document).keyup(function(e) {
	if (e.key === "Enter" || e.keyCode === 13) {
		var val = $('#inputM').val()	//получаем то, что в поле находится
		val = val.replace(/[^\w\sа-яёА-ЯЁ]/g, '')
		//console.log(val)
		//находим ячейку, опустошаем, вставляем значение из поля
		elem = $('#inputM').closest('td');
		elem.removeClass('p-0');
		//elem.attr('name', 'new');
		elem.html(val);
		
		saveMarks(elem)
		
	}
});

function editable(el, scale) {
	//ловим элемент, по которому кликнули

	e = $(el)
	//если это инпут - ничего не делаем

	//if(elm_name == 'input')	{return false;}
	var val = e.html();	//получаем значение ячейки
	//console.log($(val))
	if ($(val)[0] && $(val)[0].tagName == 'DIV')
		return false
	//console.log(val)
	//return
	code = '<div id="sel" class="form-label-group portlet no-height"><select id="select_option" class="form-control bs-select no-height" onchange="selectMark(e);">'
	onchange="selectMark(e);"

	if (scale == 5)
		code = code + '<option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option>'
	else if (scale == 10)
		code = code + '<option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option>'
	
	code = code + '</select></div>'

	if (scale == 100) {
		code = '<div id="sel" class="form-label-group portlet no-height"><input class="form-control no-height" type="text" id="inputM" value=""/></div>'
	}

	if (scale == 2 || scale == 3) {
		code = '<div id="sel" class="form-label-group portlet no-height"><input class="form-control no-height" value="" type="text" id="inputB"/></div>'
	}

	e.addClass('p-0')
	e.html(code)

	if (scale == 100) {
		$('#inputM').val(val)
		//устанавливаем фокус на свеженарисованное поле
		$('#inputM').focus();
		$('#inputM').blur(function()	{	//устанавливаем обработчик
			var val = $('#inputM').val()	//получаем то, что в поле находится
			val = val.replace(/[^\w\sа-яёА-ЯЁ]/g, '')
			//console.log(val)
			//находим ячейку, опустошаем, вставляем значение из поля
			e.html(val);
			e.removeClass('p-0');
			//e.attr('name', 'new')
			saveMarks(e)
		});
	}
	else if (scale == 2) {
		e.html(val);
		if (val == '')
			val = '+';
		else if (val == '-')
			val = '+'
		else
			val = '-'
		//$('#inputB').val(val)
		e.html('');
		e.append(val);
		e.removeClass('p-0');
		saveMarks(e)
	}
	else if (scale == 3) {
		e.html(val);
		if (val == '')
			val = '+';
		else if (val == '+')
			val = '*'
		else if (val == '-')
			val = '+'
		else
			val = '-'
		//$('#inputB').val(val)
		e.html('');
		e.append(val);
		e.removeClass('p-0');
		saveMarks(e)
	}
	else {
		$('#select_option').val(val)
		$('.filter-option-inner-inner').html(val)
		//устанавливаем фокус на свеженарисованное поле
		$('#select_option').focus();
		$('#select_option').blur(function()	{	//устанавливаем обработчик
			var val = $('#select_option option:selected').text()	//получаем то, что в поле находится
			//console.log(val)
			//находим ячейку, опустошаем, вставляем значение из поля
			e.html(val);
			e.removeClass('p-0');
			//e.attr('name', 'new')
			saveMarks(e)
		});
	}
}

function selectMark(e) {
	var val = $('#select_option option:selected').text()	//получаем то, что в поле находится
	//console.log(val)
	//находим ячейку, опустошаем, вставляем значение из поля
	$(e).closest('td').html(val);
	$(e).closest('td').removeClass('p-0');
	//$(e).closest('td').attr('name', 'new')
	saveMarks($(e).closest('td'))
}


function saveMarks(elem) {
	if ($('#showColor')[0].checked) {
		currentValue = elem.html()
		if (currentValue !== "") {
			Color(elem, currentValue, elem.attr('value'))	
		}
	}
	elem = $(elem)
	///////elem.attr('data-lesson'),elem.attr('data-name'),elem.attr('data-descr'), elem.html()
	groupFile = $('#group_number').val()

	subjectFile = $('#subject_config').val()
	//console.log($('#group_number'))
	//console.log(subjectFile)

	var token = $('#csrfToken').val();
	var header = $('#csrfHeader').val();

	subjectFile = subjectFile.toUpperCase()
	//jqXHR.setRequestHeader('CSRFToken', ACC.config.CSRFToken);
	$.ajax({
		headers : {
			Accept : "text/plain",
			"Content-Type" : "application/json"
		},
		url : '/dbconnector/teacher/'+subjectFile+"/"+groupFile+"/"+elem.attr('data-id'),
		method : "PUT",
		data : elem.html(),
		dataType : "text",
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			//console.log(data);
			//console.log("Instance has been saved!");
		},
		error : function() {
			//console.log("an error has occurred while putting response!");
		}
	});
}




function showTable() {

	//$('#item_list').show()

	groupFile = $('#group_number').val()
	groupFile = groupFile.toUpperCase()

	subjectFile = $('#subject_config').val()
	//console.log($('#group_number'))
	//console.log(subjectFile)

	subjectFile = subjectFile.toUpperCase()
	$.ajax({
        //cache: false,// ../../java/ru/web_marks/web/controllers/MarkController.java
        url: '/dbconnector/student/'+subjectFile+"/"+groupFile,  /*название файла, который занимается орабработкой запроса*/
        type: "GET",
        data: {
				// subject: subjectFile,
				// year_group: groupFile
			},
        //dataType: "json",//
        success: function(config) {
			if (config == "[]")
			{
				$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
			}
			else {
				$('#item_list').show()
				//console.log(config)
				config=JSON.parse(config)
				user_table = '<tr id="heightOneUser"><td name="user"></td></tr><tr id="heightTwoUser"><td name="user"></td></tr>'
				table = '<tr id="heightOne">'
				for (var i = 0; i < config[0]['tasks'].length; i++) {
					for (var j = 0; j < config[0]['tasks'][i]['marks'].length; j++) {
						if (j == 0) {
							table = table + '<td style="border-left: 2px solid #000000" colspan="'+config[0]['tasks'][i]['marks'].length+'">' + config[0]['tasks'][i]['lesson'] + '</td>'
						}
					}
				}
				table = table + '</tr><tr id="heightTwo">'
				countCol = 0
				for (var i = 0; i < config[0]['tasks'].length; i++) {
					for (var j = 0; j < config[0]['tasks'][i]['marks'].length; j++) {
						if (j == 0) {
							table = table + '<td style="border-left: 2px solid #000000">' + config[0]['tasks'][i]['marks'][j]['descr'] + '</td>'
						}
						else {
							table = table + '<td>' + config[0]['tasks'][i]['marks'][j]['descr'] + '</td>'
						}
						countCol++
					}
				}

				table = table + '</tr>'
				for (var i = 0; i < config.length; i++) {
					user_table = user_table + '<tr height="82px"><td name="user">' + config[i]['fname'] + '</td></tr>'
					table = table + '<tr height="82px">'
					for (var j = 0; j < config[i]['tasks'].length; j++) {
						for (var k = 0; k < config[i]['tasks'][j]['marks'].length; k++) {
							wid = 91 / countCol
							//"'+config[i]['tasks'][j]['marks'][k]['date']+'"

							////console.log(arrScale[j][1])
							///onmousedown="getDate(\''+config[i]['tasks'][j]['marks'][k]['date']+'\');"   
							table = table + '<td name="editable" data-id="'+config[i]['tasks'][j]['marks'][k]['mrk_id']+'" width="'+wid+'%" value="'+config[i]['tasks'][j]['marks'][k]['scale']+'" onclick="editable(this, '+config[i]['tasks'][j]['marks'][k]['scale']+');" data-toggle="tooltip" data-placement="top" title="Оценка поставлена: '+config[i]['tasks'][j]['marks'][k]['date']+'">'+config[i]['tasks'][j]['marks'][k]['mark']+'</td>'
							//data-name="'+config[i]['fname']+'" data-descr="'+config[i]['tasks'][j]['marks'][k]['descr']+'" data-lesson="'+config[i]['tasks'][j]['lesson']+'" 
						}
					}

					table = table + '</tr>'
				}
				$('#table').html(table)
				$('#user_table').html(user_table)
				$('#heightOneUser').height($('#heightOne').height())//
				$('#heightTwoUser').height($('#heightTwo').height())//

				$('#div2').doubleScroll({
					resetOnWindowResize: true
				});

				$('#backup_btn').removeAttr('hidden');
				$('#pills-backups-tab-item').removeAttr('hidden');


			}

			if ($('#showColor')[0].checked) {
				checkboxChange()
			}


        },
        error: function(config) {
			$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
		}
	});


}

function removeBackup(id) {
	$.ajax({
		//cache: false,// ../../java/ru/web_marks/web/controllers/MarkController.java
		url: '/dbconnector/teacher/delete/'+id,  /*название файла, который занимается орабработкой запроса*/
		type: "GET",
		cache: false,
		data: {
			// subject: subjectFile,
			// year_group: groupFile
		},
		//dataType: "json",//
		success: function(data) {
			// console.log(data);
			$('#pills-backups-div').html(data);
			// if (config != "success")
			// {
			// 	$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
			// }
			// else {
			// 	$.SOW.core.toast.show('success', '', "Резервная копия коллекции была создана успешно", 'bottom-right', 4000, true)
			// }
			//window.location.href='/dbconnector/teacher/backups/'+subjectFile+"/"+groupFile;

		},
		error: function(data) {
			$.SOW.core.toast.show('danger', '', "Что то пошло не так", 'bottom-right', 4000, true)
		}
	});
}

function restoreBackup(id) {
	$.ajax({
		//cache: false,// ../../java/ru/web_marks/web/controllers/MarkController.java
		url: '/dbconnector/teacher/restore/'+id,  /*название файла, который занимается орабработкой запроса*/
		type: "GET",
		cache: false,
		data: {
			// subject: subjectFile,
			// year_group: groupFile
		},
		//dataType: "json",//
		success: function(data) {
			// console.log(data);
			$('#pills-backups-div').html(data);
			$.SOW.core.toast.show('success', '', "Резервная копия коллекции была восстановлена успешно", 'bottom-right', 4000, true)
			// if (config != "success")
			// {
			// 	$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
			// }
			// else {
			// 	$.SOW.core.toast.show('success', '', "Резервная копия коллекции была создана успешно", 'bottom-right', 4000, true)
			// }
			//window.location.href='/dbconnector/teacher/backups/'+subjectFile+"/"+groupFile;

		},
		error: function(data) {
			$.SOW.core.toast.show('danger', '', "Что то пошло не так", 'bottom-right', 4000, true)
		}
	});
}

function getBackups() {
	groupFile = $('#group_number').val()
	groupFile = groupFile.toUpperCase()

	subjectFile = $('#subject_config').val()
	//console.log($('#group_number'))
	//console.log(subjectFile)

	subjectFile = subjectFile.toUpperCase()

	$.ajax({
		//cache: false,// ../../java/ru/web_marks/web/controllers/MarkController.java
		url: '/dbconnector/teacher/backups/'+subjectFile+"/"+groupFile,  /*название файла, который занимается орабработкой запроса*/
		type: "GET",
		cache: false,
		data: {
			// subject: subjectFile,
			// year_group: groupFile
		},
		//dataType: "json",//
		success: function(data) {
			// console.log(data);
			$('#pills-backups-div').html(data);
			// if (config != "success")
			// {
			// 	$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
			// }
			// else {
			// 	$.SOW.core.toast.show('success', '', "Резервная копия коллекции была создана успешно", 'bottom-right', 4000, true)
			// }
			//window.location.href='/dbconnector/teacher/backups/'+subjectFile+"/"+groupFile;

		},
		error: function(data) {
			$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
		}
	});
}


function backup() {

	//$('#item_list').show()

	groupFile = $('#group_number').val()
	groupFile = groupFile.toUpperCase()

	subjectFile = $('#subject_config').val()
	subjectFile = subjectFile.toUpperCase()

	$.ajax({
		//cache: false,// ../../java/ru/web_marks/web/controllers/MarkController.java
		url: '/dbconnector/teacher/backup/'+subjectFile+"/"+groupFile,  /*название файла, который занимается орабработкой запроса*/
		type: "GET",
		data: {
			// subject: subjectFile,
			// year_group: groupFile
		},
		//dataType: "json",//
		success: function(config) {
			if (config != "success")
			{
				$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
			}
			else {
				$.SOW.core.toast.show('success', '', "Резервная копия коллекции была создана успешно", 'bottom-right', 4000, true)
				getBackups();
			}
		},
		error: function(config) {
			$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true)
		}
	});

}

function getDate(date) {
	if (event.which == 2) {
		$.SOW.core.toast.show('primary', '', "Время выставления этой оценки: "+date, 'bottom-center', 3000, true)
	}
}

function checkboxChange() {

	//console.log($('#showColor')[0].checked)
	if ($('#showColor')[0].checked) {


		$('[name="editable"]').each(function(){
			elem = $(this)
			$(this).css('font-weight','600');
			currentValue = elem.html()
			if (currentValue !== "") {
				Color(elem, currentValue, elem.attr('value'))	
			}
		})
	}
	else {


		$('[name="editable"]').each(function(){
			$(this).css('background-color','#fff');
			$(this).css('font-weight','normal');
		})
	}
}


function delition() {
	subjectFile = $('#subject_config_del').val()
	subjectFile = subjectFile.toUpperCase()
	
	arrGroup = [subjectFile, $('#select_option option:selected').val()]
	
	resultInfoFromFiles = arrGroup
	//console.log(resultInfoFromFiles)
	
	$.ajax({
				headers : {
					Accept : "text/plain",
					"Content-Type" : "application/json"
				},
				url : '/dbconnector/administrator/delete',
				method : "PUT",
				data : JSON.stringify(resultInfoFromFiles),
				dataType : "text",
				contentType : "application/json; charset=utf-8",
				success : function(data) {
					$.SOW.core.toast.show('success', '', "Удаление прошло успешно", 'bottom-right', 4000, true)
				},
				error : function(data) {
					$.SOW.core.toast.show('danger', '', "Что то пошло не так, файл или коллекция не найдены!", 'bottom-right', 4000, true)
				}
			});
	
}


function upload() {
  var reader = new FileReader();
  tfile = $('input[name="groupFile"]')[0].files[0];
  reader.readAsText(tfile);
  reader.onload = function(e)
  {
    str = e.target.result;
	  arrGroup = [str, $('input[name="groupFile"]').val().match(/fakepath\\([\w\.]+)/)[1].toUpperCase(), $('input[name="groupFile"]').val().match(/\.(\w+)/)[1]]
      //resultInfoFromFiles.push("groupFile")
      var reader1 = new FileReader();
    tfile1 = $('input[name="configFile"]')[0].files[0];
    reader1.readAsText(tfile1);
    reader1.onload = function(el)
    {
      //console.log('3')
      str1 = el.target.result;
		arrConf = [str1, $('input[name="configFile"]').val().match(/fakepath\\([\w\.]+)/)[1].toUpperCase(), $('input[name="configFile"]').val().match(/\.(\w+)/)[1]]
        //resultInfoFromFiles.push("configFile")

      resultInfoFromFiles = [arrGroup,arrConf]
      //console.log(resultInfoFromFiles)

      //console.log(JSON.stringify(resultInfoFromFiles))

      $.ajax({
        headers : {
          Accept : "text/plain",
          "Content-Type" : "application/json"
        },
        url : "/dbconnector/administrator/load",
        method : "PUT",
        data : JSON.stringify(resultInfoFromFiles),
        dataType : "text",
        contentType : "application/json; charset=utf-8",
        success : function(data) {
			$.SOW.core.toast.show('success', '', "Добавление прошло успешно", 'bottom-right', 4000, true)
        },
        error : function(data) {
			if (data.responseText == "Collection exists")
			{
				$.SOW.core.toast.show('danger', '', "Что то пошло не так, коллекция уже существует!", 'bottom-right', 4000, true)
			}
			else {
				$.SOW.core.toast.show('danger', '', "Что то пошло не так, неверный формат входных данных!", 'bottom-right', 4000, true)
			}
        }
      });


    };
  };

}

