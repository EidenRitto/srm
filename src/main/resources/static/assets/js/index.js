
function initpage() {
    //初始化表
    inittable('0');
}

function sub() {
    $.ajax({
        url:'/list/addsynctab',
        type:'POST',
        data:$('#form-sync').serialize(),
        async:false,
        success:function (data) {
            if (data == 'success'){
                window.location.reload();
            }
            alert('提交失败');
        }
    });
}

function clickbutton(id,synctabname) {
    $.ajax({
        url:'/list/synctab',
        type:'POST',
        data:{tabname:synctabname},
        async:false,
        success:function (data) {
            console.log("Sync ing.....");
        }
    });
    var jdt = 'jdt'+id;
    var jdtz = '#jdtz'+id;
     // $(".progress-bar").attr("style","width:0%");
     var go = setInterval(function() {
         $.ajax({
             url:'/list/jdt',
             type:'GET',
             success:function (data) {
                 var persent = data+"%";
                 document.getElementById(jdt).style.width = persent;
                 $(jdtz).attr('data-percent',persent);
                 if (data == '100'){
                     setTimeout(function() {
                         // content.innerHTML = '加载完成';
                         clearInterval(go);
                     }, 100);
                 }
             }
         });
    }, 1000);

    // for (var i = 0;i<100;i++){
    //     sleep(1);
    //     var j = "width:"+i+"%";
    //     $("#jdt").attr("style",j);
    // }
}

function sleep(numberMillis) {
    var now = new Date();
    var exitTime = now.getTime() + numberMillis;
    while (true) {
        now = new Date();
        if (now.getTime() > exitTime)
            return;
    }
}
//退出当前页面，回到登录页面
function exit() {
    self.location='/logout';
}

//添加同步表
function addsynctab() {
    $.ajax({
        url:'/list/findalltab',
        type:'POST',
        // data:{s:"gg"},
        success:function (data) {
            var opt_html ="";
            for ( var i = 0; i <data.length; i++){
                opt_html += "<option value='"+data[i]+"'>"+data[i]+"</option>";
            }
            layer.open({
                type: 1,
                title: '添加新的同步表',
                shadeClose: true,
                shade: 0.8,
                anim:4,
                area: ['680px', '300px'],
                content: '<form id="form-sync" class="form-horizontal" action="/list/addsynctab" method="POST" role="form" onsubmit="return sub()";>\n' +
                '\t\n' +
                '\t\t<div class="form-group">\n' +
                '\t\t\t\n' +
                '\t\t\t<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 需要同步的表 </label>\n' +
                '\n' +
                '\t\t\t<div class="col-sm-5">\n' +
                '\t\t\t\t<select name="syncTabName" class="form-control" id="table-select">\n' +
                '\t\t\t\t\t<option value="">&nbsp;</option>\n' +
                opt_html+
                '\t\t\t\t</select>\n' +
                '\t\t\t</div>\n' +
                '\t\t</div>\n' +
                '\n' +
                '\t\t<div class="space-4"></div>\n' +
                '\n' +
                '\t\t<div class="form-group">\n' +
                '\t\t\t<label class="col-sm-3 control-label no-padding-right" for="form-input-readonly"> 同步频率（小时/每次） </label>\n' +
                '\n' +
                '\t\t\t<div class="col-sm-8">\n' +
                '\t\t\t\t<input type="text" name="syncRateH" class="col-xs-10 col-sm-7" id="form-input-readonly" value="" />\n' +
                '\t\t\t</div>\n' +
                '\t\t</div>\n' +
                '\n' +
                '\t\t<div class="space-4"></div>\n' +
                '\n' +
                // '\t\t<div class="form-group">\n' +
                // '\t\t\t\n' +
                // '\t\t\t<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 同步单位 </label>\n' +
                // '\n' +
                // '\t\t\t<div class="col-sm-7">\n' +
                // '\t\t\t\t<select class="form-control" id="table-select">\n' +
                // '\t\t\t\t\t<option value="分钟">分钟</option>\n' +
                // '\t\t\t\t\t<option value="小时">小时</option>\n' +
                // '\t\t\t\t\t<option value="日">日</option>\n' +
                // '\t\t\t\t\t<option value="月">月</option>\n' +
                // '\t\t\t\t</select>\n' +
                // '\t\t\t</div>\n' +
                // '\t\t</div>\n' +
                // '\n' +
                //
                // '\t\t<div class="space-4"></div>\n' +
                // '\n' +
                '\t\t<div class="form-group">\n' +
                '\t\t\t<label class="col-sm-3 control-label no-padding-right">xxxx</label>\n' +
                '\n' +
                '\t\t\t<div class="col-sm-9">\n' +
                '\t\t\t\t<span class="input-icon">\n' +
                '\t\t\t\t\t<input type="text" id="form-field-icon-1" />\n' +
                '\t\t\t\t\t<i class="icon-leaf blue"></i>\n' +
                '\t\t\t\t</span>\n' +
                '\n' +
                '\t\t\t\t<span class="input-icon input-icon-right">\n' +
                '\t\t\t\t\t<input type="text" id="form-field-icon-2" />\n' +
                '\t\t\t\t\t<i class="icon-leaf green"></i>\n' +
                '\t\t\t\t</span>\n' +
                '\t\t\t</div>\n' +
                '\t\t</div>\n' +
                '\n' +
                '\t\t<div class="space-4"></div>\n' +
                '\n' +
                '\t\t\t<div class="col-md-offset-3 col-md-9">\n' +
                '\t\t\t\t<button class="btn btn-info" type="" onclick="sub()">\n' +
                '\t\t\t\t\t<i class="icon-ok bigger-110"></i>\n' +
                '\t\t\t\t\t提交\n' +
                '\t\t\t\t</button>\n' +
                '\n' +
                '\t\t\t\t&nbsp; &nbsp; &nbsp;\n' +
                '\t\t\t\t<button class="btn" type="reset">\n' +
                '\t\t\t\t\t<i class="icon-undo bigger-110"></i>\n' +
                '\t\t\t\t\t取消\n' +
                '\t\t\t\t</button>\n' +
                '\t\t\t</div>\n' +
                '\n' +
                '\t\t<div class="hr hr-24"></div>\n' +
                '\n' +
                '</form>'
            });
        }
    });
}


function inittable(data) {
    $.ajax({
        url:'/page/allpage',
        type:'POST',
        data:{page:data},
        success:function (data) {
            console.log(data);
            var html = '';
            for ( var i = 0; i <data.result.length; i++){
                var syncstate = '';
                if (data.result[i].lastSyncState == "已同步"){
                    syncstate ="<span class='label label-sm label-success'>已同步</span>";
                }
                if (data.result[i].lastSyncState == "同步失败"){
                    syncstate ="<span class='label label-sm label-warning'>同步失败</span>";
                }
                if (data.result[i].lastSyncState == "未同步"){
                    syncstate = "<span class='label label-sm label-inverse arrowed-in'>未同步</span>";
                }
                html += "<tr>\n" +
                    "<td class=\"center\">\n" +
                    "\t<label>\n" +
                    "\t\t<input type=\"checkbox\" class=\"ace\" name=\"items\" />\n" +
                    "\t\t<span class=\"lbl\"></span>\n" +
                    "\t</label>\n" +
                    "</td>\n" +
                    "<td>" + data.result[i].syncTabName + "</td>" +
                    "<td>" + data.result[i].lastSyncDate+ "</td>" +
                    "<td>" + data.result[i].syncRateH + "小时/次</td>" +
                    "<td>0</td>" +
                    "<td class=\"hidden-480\">" + syncstate + "</td>" +
                    "<td>\n" +
                    "<div class=\"visible-md visible-lg hidden-sm hidden-xs btn-group\">\n" +
                    "<button class=\"btn btn-xs btn-success\" onclick=\"clickbutton("+data.result[i].id+")\">\n" +
                    "<i class=\"icon-ok bigger-120\"></i>\n" +
                    "</button>\n" +
                    "<div class=\"col-xs-10\">\n" +
                    "<div id=\"jdtz"+data.result[i].id+"\" class=\"progress progress-striped active\" data-percent=\"0% \">\n" +
                    "<div id=\"jdt"+data.result[i].id+"\" class=\"progress-bar\" style=\"width: 0%;\"></div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</td></tr>";
            }
             html += "<tr align='center'>\n"
                    +"<td style='border-color: white;background: white'></td>\n"
                     +"<td style='border-color: white;background: white'></td>\n"
                     +"<td style='border-color: white;background: white'>\n"
                    +"第"+data.currentpage +"页&nbsp;&nbsp; &nbsp;&nbsp;共"+data.totalpages+"页\n"
                    +"</td>\n"
                    +"<td style='border-color: white;background: white'>\n"
                             +"<a href='#' onclick='searchpage('+1+')'> "+"首页"+"</a>\n"
                             + "<a href='#' onclick='searchpage("+(data.currentpage-1)+")'> "+"上一页"+"</a>\n"
                             +"<a href='#' onclick='searchpage("+(data.currentpage+1)+")'> "+"下一页"+"</a>\n"
                             +"<a href='#' onclick='searchpage("+data.totalpages+")'> "+"最后一页"+"</a>\n"
                     +"</td>\n"
                     +"<td style='border-color: white;background: white'>\n"
                     +"转到第："+"<input type='text' name='page' size='2'>"+"&nbsp;页&nbsp;&nbsp;"+"<input type='submit' value='GO' name='cndok'>\n"
                     +"</td>\n"
                    +"</tr>"

            $("#tbody").html(html);
        }
    });
}

function searchpage(data){
    inittable(data);
}