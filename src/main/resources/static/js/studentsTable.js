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

	// hexR = Number(R).toString(16).padStart(2, '0');
	// hexG = Number(G).toString(16).padStart(2, '0');
	// hexB = Number(B).toString(16).padStart(2, '0');

	//$(elem).css('background-color','#'+hexR+hexG+hexB);
	if (!isNaN(R)) {
		//$(elem).css('background-color', 'rgba(' + R + ',' + G + ',' + B + ',' + 0.5 + ');');
		$(elem).css('background-color', 'rgba('+R+','+G+','+B+',0.9)');
		var rgba='rgb('+R+','+G+','+B+')'
	}
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
			subject: subjectFile,
			year_group: groupFile
		},
		//dataType: "json",//
		success: function(config) {
			if (config == "[]")
			{
				$.SOW.core.toast.show('danger', '', "Что то пошло не так, попробуйте загрузить файл с конфигурацией предмета", 'bottom-right', 4000, true);
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
							table = table + '<td colspan="'+config[0]['tasks'][i]['marks'].length+'">' + config[0]['tasks'][i]['lesson'] + '</td>'
						}
					}
				}
				table = table + '</tr><tr id="heightTwo">'
				countCol = 0
				for (var i = 0; i < config[0]['tasks'].length; i++) {
					for (var j = 0; j < config[0]['tasks'][i]['marks'].length; j++) {
						table = table + '<td>' + config[0]['tasks'][i]['marks'][j]['descr'] + '</td>'
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
