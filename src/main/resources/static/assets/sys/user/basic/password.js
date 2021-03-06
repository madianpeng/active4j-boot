  layui.config({
     base: CXL.ctxPath + '/layuiadmin/' //静态资源所在路径
  }).extend({
    index: 'lib/index' //主入口模块
  }).use(['index', 'form', 'set'], function(){
	  var form = layui.form;
	  var admin = layui.admin;
	  var $ = layui.$;
	  //监听提交
	  form.on('submit(user_basic_save)', function(data){
		  var field = data.field; //获取提交的字段
	      
	      //提交 Ajax 成功后，关闭当前弹层并重载表格
	      $.ajax({
	             type: "post",
	             url: CXL.ctxPath + '/sys/user/savepwd',
	             data: field,
	             success: function(res) {
		    		 if(res.success) {
		    			 CXL.success(res.msg);
		    		 }else {
		    			 CXL.warn(res.msg);
		    		 }
		    	 }
	         });
     
	  });
	    
	    
  })