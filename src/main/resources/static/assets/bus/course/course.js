  layui.config({
     base: CXL.ctxPath + '/layuiadmin/' //静态资源所在路径
  }).extend({
    index: 'lib/index' //主入口模块
  }).use(['index', 'form','upload'], function(){
	  var form = layui.form;
	  var admin = layui.admin;
	  var $ = layui.$;
	  var upload = layui.upload;

	  //监听提交
	  form.on('submit(form-btn-save)', function(data){
		  var field = data.field; //获取提交的字段
	      var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
	      
	      //提交 Ajax 成功后，关闭当前弹层并重载表格
	      $.ajax({
	             type: "post",
	             url: CXL.ctxPath + '/bus/course/save',
	             data: field,
	             success: function(res) {
		    		 if(res.success) {
		    			 CXL.success(res.msg);
		    			 parent.layui.table.reload('sys-user-table'); //重载表格
		    		     parent.layer.close(index); //再执行关闭 
		    		 }else {
		    			 CXL.warn(res.msg);
		    		 }
		    	 }
	         });
	      
	     
     
	  });

	  //多文件列表示例
	  var demoListView = $('#demoList')
		  ,uploadListIns = upload.render({
		  elem: '#testList'
		  ,url: CXL.ctxPath + '/func/upload/localupload?linkId='+$("#linkId").val()
		  ,accept: 'video'
		  ,multiple: true
		  ,auto: false
		  ,bindAction: '#testListAction'
		  ,choose: function(obj){
			  var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
			  //读取本地文件
			  obj.preview(function(index, file, result){
				  var tr = $(['<tr id="upload-'+ index +'">'
					  ,'<td>'+ file.name +'</td>'
					  ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
					  ,'<td>等待上传</td>'
					  ,'<td>'
					  ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
					  ,'</td>'
					  ,'</tr>'].join(''));

				  //单个重传
				  tr.find('.demo-reload').on('click', function(){
					  obj.upload(index, file);
				  });

				  //删除
				  tr.find('.demo-delete').on('click', function(){
					  delete files[index]; //删除对应的文件
					  var val = tr.attr("val");
					  if (val != undefined && val!=null){
						  $.ajax({
							  type: "post",
							  url: CXL.ctxPath + '/func/upload/delete',
							  data: {
							  	"filePath":val
							  },
							  success: function(res) {
								  if(res.success) {
									  CXL.success(res.msg);
								  }else {
									  CXL.warn(res.msg);
								  }
							  }
						  });
					  }
					  tr.remove();
					  uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
				  });

				  demoListView.append(tr);
			  });
		  }
		  ,done: function(res, index, upload){
			  if(res.success){ //上传成功
				  var key = res.attributes.key;
				  var tr = demoListView.find('tr#upload-'+ index)
					  ,tds = tr.children();
				  tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
				  tr.attr("val",key);
				  return delete this.files[index]; //删除文件队列已经上传成功的文件
			  }
			  this.error(index, upload);
		  }
		  ,error: function(index, upload){
			  var tr = demoListView.find('tr#upload-'+ index)
				  ,tds = tr.children();
			  tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
			  tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
		  }
	  });

	  $('.demo-delete').on('click', function (data) {
		  var val = $(this).attr("val");
		  var path = $(this).attr("path");
		  if (path != undefined && path!=null){
			  $.ajax({
				  type: "post",
				  url: CXL.ctxPath + '/func/upload/delete',
				  data: {
					  "filePath":path
				  },
				  success: function(res) {
					  if(res.success) {
						  CXL.success(res.msg);
					  }else {
						  CXL.warn(res.msg);
					  }
				  }
			  });
		  }
		  $("#upload-"+val).remove()
	  });
	    
  })