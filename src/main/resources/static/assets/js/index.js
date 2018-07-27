
function initpage() {
    //初始化表
    inittable('0');
}
//重置表单
function reset() {
    document.getElementById("form-sync").reset();
}
//提交表单
function sub() {
    var selectvalue =$('#table-select').val().trim();
    var timevalue =$('#form-input-readonly').val().trim();
    if(selectvalue == "" ||timevalue == "") {
        if (selectvalue == ""){
            layer.tips('请先选择同步表！', '#table-select');
            return;
        }
        if (timevalue == ""){
            layer.tips('同步频率不能为空！', '#form-input-readonly');
            return;
        }
    }else {
        $.ajax({
            url:'/list/exitbytabname',
            type:'POST',
            data:{tabname:selectvalue},
            success:function (data) {
                if (data == 'success'){
                    layer.msg("同步表已存在，不能重复添加！");
                    return;
                }else {
                    $.ajax({
                        url:'/list/addsynctab',
                        type:'POST',
                        data:$('#form-sync').serialize(),
                        async:false,
                        success:function (data) {
                            if (data == 'success'){
                                layer.msg("新增同步表成功！");
                                window.location.reload();
                            }else {
                                layer.msg("新增失败，请联系管理员！");
                            }
                        }
                    });
                }
            }
        })
    }
}

//同步
function clickbutton(id,synctabname) {
    var jdt = 'jdt'+id;
    var jdtz = '#jdtz'+id;
    var st = '#st'+ id;
    $.ajax({
        url:'/list/synctab',
        type:'POST',
        data:{tabname:synctabname},
        async:false,
        success:function (data) {
            if(data == "exist"){
                layer.tips('同步进行中', jdtz,{tips: 3});
            }
            if(data == "success"){
                layer.tips('同步进行中', jdtz,{tips: 3});
            }
        }
    });
     // $(".progress-bar").attr("style","width:0%");
     var go = setInterval(function() {
         $.ajax({
             url:'/list/jdt',
             type:'GET',
             data:{"tabname":synctabname},
             success:function (data) {
                 var persent = data+"%";
                 document.getElementById(jdt).style.width = persent;
                 $(jdtz).attr('data-percent',persent);
                 if (data == '100'){
                     setTimeout(function() {
                         // content.innerHTML = '加载完成';
                         clearInterval(go);
                     }, 100);
                     setTimeout(function() {
                         // content.innerHTML = '加载完成';
                         //隐藏进度条
                         //$(jdtz).attr('hidden',"");
                         $(st).html("<span class='label label-sm label-success'>已同步</span>")
                     }, 3000);

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

//多选同步
function clickmorebutton() {
    var flag = false;
    var checkElements=document.getElementsByName('items');
    var ids = new Array();
    for (var i=0;i<checkElements.length;i++){
        //获取打勾的id
        var checkElement=checkElements[i];
        if(checkElement.checked){
            var id = checkElement.id;
            ids.push(id);
            flag = true;
        }
    }
    if (!flag){
        layer.msg("请先选择要同步的表！");
    }else{
        layer.confirm('确认开始同步吗？', {
            btn: ['同步','取消'] //按钮
        }, function() {
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


            if (data == 'success') {
                layer.msg("同步启动成功", {
                    skin: 'layui-layer-molv' //样式类名
                    , closeBtn: 0
                }, function () {
                    location.reload();
                });
            } else {
                layer.msg("同步失败，请联系管理员！");
            }

        })
    }
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
                area: ['500px', '300px'],
                content: '<form id="form-sync" class="form-horizontal" action="/list/addsynctab" method="POST" role="form" onsubmit="return sub()";>\n' +
                '\t\n' +
                '\t\t<div class="form-group" style="margin-top:40px;margin-left:60px"">\n' +
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
                '\t\t<div class="form-group" style="margin-left: 60px">\n' +
                '\t\t\t<label class="col-sm-3 control-label no-padding-right" for="form-input-readonly"> 同步频率(时/次)</label>\n' +
                '\n' +
                '\t\t\t<div class="col-sm-8">\n' +
                '\t\t\t\t<input type="text" name="syncRateH" class="col-xs-10 col-sm-7" id="form-input-readonly" value="" />\n' +
                '\t\t\t</div>\n' +
                '\t\t</div>\n' +
                '\n' +
                '\t\t<div class="space-4"></div>\n' +
                '\n' +
                '\t\t<div class="space-4"></div>\n' +
                '\n' +
                '\t\t\t<div class="col-md-offset-3 col-md-9">\n' +
                '\t\t\t\t<button class="btn btn-info btn-sm" type="button" onclick="sub()">\n' +
                '\t\t\t\t\t<i class="icon-ok bigger-110"></i>\n' +
                '\t\t\t\t\t提交\n' +
                '\t\t\t\t</button>\n' +
                '\n' +
                '\t\t\t\t&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;\n' +
                '\t\t\t\t<button class="btn btn-sm" type="reset" onclick="reset();">\n' +
                '\t\t\t\t\t<i class="icon-undo bigger-110"></i>\n' +
                '\t\t\t\t\t重置\n' +
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

//初始表格
function inittable(data) {
    var tabname;
    tabname = $("#searchtabname").val().trim();
    $.ajax({
        url:'/page/allpage',
        type:'POST',
        data:{'page':data,'tabname':tabname},
        success:function (data) {
            console.log(data);
            var html = '';
            for ( var i = 0; i <data.result.length; i++){
                var syncstate = '';
                if (data.result[i].lastSyncState == "已同步"){
                    syncstate ="<span class='label label-sm label-success'>已同步</span>";
                }
                if (data.result[i].lastSyncState == "同步失败"){
                    syncstate ="<span class='label label-sm label-inverse arrowed-in'>同步失败</span>";
                }
                if (data.result[i].lastSyncState == "未同步"){
                    syncstate = "<span class='label label-sm label-warning'>未同步</span>";
                }

                html += "<tr>\n" +
                    "<td class=\"center\">\n" +
                    "\t<label>\n" +
                    "\t\t<input type=\"checkbox\" class=\"ace\" name=\"items\" id='"+data.result[i].id +"' />\n" +
                    "\t\t<span class=\"lbl\"></span>\n" +
                    "\t</label>\n" +
                    "</td>\n" +
                    "<td>" + data.result[i].syncTabName + "</td>" +
                    "<td>" + data.result[i].lastSyncDate.substring(0, 10)+ "</td>\n" +
                    "<td align=\"right\">\n" +
                    + data.result[i].syncRateH + "小时/次&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n"+
                    "<button class=\"btn btn-primary no-radius btn-xs\"  style=\"width:28px\" onclick=\"edittime("+data.result[i].id+','+data.result[i].syncRateH+")\">\n" +
                    "<i class=\"icon-edit bigger-50\"></i>\n" +
                    "</button>"+
                    "</td>\n" +
                    // "<td>0</td>" +
                    "<td class=\"hidden-480\" id='st"+data.result[i].id+"'>" + syncstate + "</td>" +
                    "<td>\n" +
                    "<div class=\"visible-md visible-lg hidden-sm hidden-xs btn-group\">\n" +
                    "<button class=\"btn btn-danger btn-xs\" style=\"width:28px\"  onclick=\"deletetab("+data.result[i].id+")\">\n" +
                    "<i class=\"icon-trash bigger-50\"></i>\n" +
                    "</button>"+
                    "<button class=\"btn btn-xs btn-success\" onclick=\"clickbutton("+data.result[i].id+",'"+data.result[i].syncTabName+"')\">\n" +
                    "<i class=\"icon-ok bigger-120\"></i>\n" +
                    "</button>\n" +
                    "<div class=\"col-xs-9\">\n" +
                    "<div id=\"jdtz"+data.result[i].id+"\" class=\"progress progress-striped active\" data-percent=\"0% \">\n" +
                    "<div id=\"jdt"+data.result[i].id+"\" class=\"progress-bar\" style=\"width: 0%;\"></div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</td></tr>";
            }
             html += "<tr>\n"
                    +"<td style='border-color: white;background: white'></td>\n"
                     +"<td style='border-color: white;background: white'></td>\n"
                     +"<td style='border-color: white;background: white;valign=bottom;text-align-all: bottom'>\n"
                    +"第"+data.currentpage +"页&nbsp;&nbsp; &nbsp;&nbsp;共"+data.totalpages+"页\n"
                    +"</td>\n"
                    +"<td style='border-color: white;background: white;text-align-all: bottom'>\n"
                             +"<a href='#' onclick='searchpage(1)'> "+"首页"+"</a>\n"
                             + "<a href='#' onclick='searchpage("+(data.currentpage-1)+")'> "+"上一页"+"</a>\n"
                             +"<a href='#' onclick='searchpage("+(data.currentpage+1)+")'> "+"下一页"+"</a>\n"
                             +"<a href='#' onclick='searchpage("+data.totalpages+")'> "+"最后一页"+"</a>\n"
                     +"</td>\n"
                     +"<td style='border-color: white;background: white'>\n"
                     +"到第："+"<input type='text' name='page' style='width: 35px;height: 25px' id='num'>"+"&nbsp;&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;"+"<input type='button' onclick='jumppage()'value='跳转' name='cndok'>\n"
                     +"</td>\n"
                    +"</tr>"

            $("#tbody").html(html);
        }
    });
}
//分页
function searchpage(data){
    inittable(data);
}
//分页跳转
function jumppage(){
    var num = $('#num').val();
    inittable(num);
}

//删除同步表记录
function deletetab(id) {
    layer.confirm('确认删除同步表吗？', {
        btn: ['删除','取消'] //按钮
    }, function(){
        $.ajax({
            url:'/list/delettab',
            type:'post',
            data:{id:id},
            success:function (data) {
                if (data == 'success'){
                    layer.msg("删除成功", {
                        skin: 'layui-layer-molv' //样式类名
                        ,closeBtn: 0
                    }, function(){
                        location.reload();
                    });
                }else {
                    layer.msg("删除失败，请联系管理员！");
                }
            }
        });
    });
}

//多选删除
function deleteCheckedtab() {
    var flag = false;
    var checkElements=document.getElementsByName('items');
    var ids = new Array();
        for (var i=0;i<checkElements.length;i++){
            //获取打勾的id
            var checkElement=checkElements[i];
            if(checkElement.checked){
                var id = checkElement.id;
                ids.push(id);
                flag = true;
            }
        }
    if (!flag){
        layer.msg("请先选择要删除的表！");
    }else{
        layer.confirm('确认删除同步表吗？', {
            btn: ['删除','取消'] //按钮
        }, function() {
            $.ajax({
                url: '/list/deletselecttab',
                type: 'post',
                data: {"ids": ids},
                traditional: true,
                success: function (data) {
                    if (data == 'success') {
                        layer.msg("删除成功", {
                            skin: 'layui-layer-molv' //样式类名
                            , closeBtn: 0
                        }, function () {
                            location.reload();
                        });
                    } else {
                        layer.msg("删除失败，请联系管理员！");
                    }
                }
            });
        })
    }
}
//编辑同步频率
function edittime(id,time) {
            layer.open({
                type: 1,
                title: '修改同步时间',
                shadeClose: true,
                shade: 0.8,
                anim:4,
                area: ['400px', '250px'],
                content: '<form id="form-sync" class="form-horizontal" action="/list/addsynctab" method="POST" role="form" onsubmit="return subedit()";>\n' +
                '\t\t<div class="space-4"></div>\n' +
                '\n' +
                '\t\t<div class="form-group" style="margin-left: 50px;margin-top: 50px">\n' +
                '\t\t\t<label class="col-sm-3 control-label no-padding-right" for="form-input-readonly"> 同步频率(时/次)</label>\n' +
                '\n' +
                '\t\t\t<div class="col-sm-9">\n' +
                '\t\t\t\t<input type="text" name="syncRateH" class="col-xs-10 col-sm-7" id="edittime" value="'+time+'" />\n' +
                '\t\t\t</div>\n' +
                '\t\t</div>\n' +
                '\t\t\t\t<button class="btn btn-info btn-sm" type="button" onclick="subedit('+id+')" style="margin-left: 150px">\n' +
                '\t\t\t\t\t<i class="icon-ok bigger-110"></i>\n' +
                '\t\t\t\t\t提交\n' +
                '\t\t\t\t</button>\n' +
                '\n' +
                '</form>'
            });
}
//提交修改
function subedit(id) {
    var timevalue = $('#edittime').val().trim();
    $.ajax({
        url:'/list/updatetime',
        type:'POST',
        data:{id:id,time:timevalue},
        success:function (data) {
            if ( data = "success"){
                layer.msg("修改同步频率成功！");
                window.location.reload();
            }else {
                layer.msg("修改失败，请联系管理员！");
            }
        }
    })
}