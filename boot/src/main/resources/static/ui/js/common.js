/**
 * 带加载层的ajax
 * 
 * @param obj
 */
$.ajaxLoad = function(obj) {
	if (typeof (obj) == 'object') {
		var async = (obj.async == null || obj.async == "" || typeof (obj.async) == "undefined") ? true : obj.async;
		var type = (obj.type == null || obj.type == "" || typeof (obj.type) == "undefined") ? "post" : obj.type;
		var dataType = (obj.dataType == null || obj.dataType == "" || typeof (obj.dataType) == "undefined") ? "json" : obj.dataType;
		var data = (obj.data == null || obj.data == "" || typeof (obj.data) == "undefined") ? {
			"date" : new Date().getTime()
		} : obj.data;
		var showLoading = (obj.showLoading == null || obj.showLoading == "" || typeof (obj.showLoading) == "undefined") ? true : obj.showLoading;
		var options = {
			type : type,
			async : async,
			data : data,
			url : obj.url,
			dataType : dataType,
			success : function(d) {
				if (showLoading) {
					try {
						window.parent.closeLoading();
					} catch (e) {
						closeLoading();
					}
				}
				if ($.isFunction(obj.success)) {
					obj.success(d);
				}
			},
			error : function(e) {
				if (showLoading) {
					try {
						window.parent.closeLoading();
					} catch (e) {
						closeLoading();
					}
				}
				if ($.isFunction(obj.error)) {
					obj.error(e);
				}
			}
		};
		if (dataType == 'jsonp') {
			options.jsonp = 'jsonpCallback';
		}
		if (showLoading) {
			try {
				window.parent.loading();
			} catch (e) {
				loading();
			}
		}
		$.ajax(options);
	}
};

function loading(obj) {
	var opt = $.extend({
		shade : [ 0.2, '#438EB9' ]
	// 0.3透明度的白色背景
	}, obj);
	// loading层
	var index = layer.load(2, opt);
	return index;
}

function closeLoading() {
	layer.close(loading());
}

// 根据laydate类自动加载laydate组件
if ($(".laydate") || $(".laydate-range"))
	layui.use([ 'laydate' ], function() {
		var laydate = layui.laydate;
		var form = layui.form;

		laydate.render({
			elem : '.laydate',
			trigger : 'click'
		});
		laydate.render({
			elem : '.laydate-range',
			trigger : 'click',
			range : '至'
		});
	});

// 返回页面的公共方法
function goBack(backUrl) {
	if (backUrl)
		location.href = backUrl;
	else
		history.back(-1);
}

// jquery扩展方法
(function($) {
	$ && $.fn.extend({
		serializeObject : function(form) {
			var o = {};
			$.each(form.serializeArray(), function(index) {
				if (this['value']) {
					var ov;
					if (ov = o[this['name']]) {
						if (typeof ov == 'string') {
							o[this['name']] = [ ov, this['value'] ];
						} else if (ov instanceof Array) {
							ov.push(this['value']);
							o[this['name']] = ov;
						}
					} else {
						o[this['name']] = this['value'];
					}
				}
			});
			return o;
		}
	});
})(jQuery || $);

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"H+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};
/**
 * 获取url参数
 * @param name
 * @returns
 */
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
}

function isEmptyOrNull(obj){
	return (obj === null || obj === undefined || obj === '');
}
/**
 * 对象深拷贝
 * @param source 原对象
 * @returns 拷贝后的对象 
 */
function deepCopy(source) { 
    var result={};
    for (var key in source) {
    	if (! isEmptyOrNull(source[key]))
    		result[key] = typeof source[key]==='object'? deepCopy(source[key]): source[key];
     }
     return result; 
}


/*$.ajaxSetup({
	complete:function(XMLHttpRequest){
		if (XMLHttpRequest.status == 500) {
			change("${path}/500.jsp");
		} else if(XMLHttpRequest.status == 404) {
			change("${path}/404.jsp");
		} else if (XMLHttpRequest.status == 0){
			//登陆超时或异地登陆处理，如果登陆超时跳转到登陆超时页面
			change("${path}/loginTimeout.jsp");
		}
	}
});*/

Number.prototype.add = function (arg) {
	return accAdd(arg, this);
};
//给Number类型增加一个sub方法，，使用时直接用 .sub 即可完成计算。
Number.prototype.sub = function (arg) {
	return Subtr(this, arg);
};
//给Number类型增加一个mul方法，使用时直接用 .mul 即可完成计算。
Number.prototype.mul = function (arg) {
	return accMul(arg, this);
};
//给Number类型增加一个div方法，，使用时直接用 .div 即可完成计算。
Number.prototype.div = function (arg) {
	return accDiv(this, arg);
};

/**
 * 解决float相加精度缺失的问题
 * @param arg1
 * @param arg2
 * @returns {number}
 */
function accAdd(arg1, arg2) {
	var r1, r2, m;
	try {
		r1 = arg1.toString().split(".")[1].length;
	}
	catch (e) {
		r1 = 0;
	}
	try {
		r2 = arg2.toString().split(".")[1].length;
	}
	catch (e) {
		r2 = 0;
	}
	m = Math.pow(10, Math.max(r1, r2));
	return (arg1 * m + arg2 * m) / m;
}
//减法函数
function Subtr(arg1, arg2) {
	var r1, r2, m, n;
	try {
		r1 = arg1.toString().split(".")[1].length;
	}
	catch (e) {
		r1 = 0;
	}
	try {
		r2 = arg2.toString().split(".")[1].length;
	}
	catch (e) {
		r2 = 0;
	}
	m = Math.pow(10, Math.max(r1, r2));
	//last modify by deeka
	//动态控制精度长度
	n = (r1 >= r2) ? r1 : r2;
	return ((arg1 * m - arg2 * m) / m).toFixed(n);
}

//乘法函数
function accMul(arg1, arg2) {
	var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	try {
		m += s1.split(".")[1].length;
	}
	catch (e) {
	}
	try {
		m += s2.split(".")[1].length;
	}
	catch (e) {
	}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
}

//除法函数
function accDiv(arg1, arg2) {
	var t1 = 0, t2 = 0, r1, r2;
	try {
		t1 = arg1.toString().split(".")[1].length;
	}
	catch (e) {
	}
	try {
		t2 = arg2.toString().split(".")[1].length;
	}
	catch (e) {
	}
	with (Math) {
		r1 = Number(arg1.toString().replace(".", ""));
		r2 = Number(arg2.toString().replace(".", ""));
		return (r1 / r2) * pow(10, t2 - t1);
	}
}

//验证规则
function verCodeF($dom,rules,obj){
	var _  = $dom;
	var focus = key = _blur = function(element) { $(element).valid(); };
	if(obj){
		var key = obj.keyup;
	}
	_.validate({
    	rules:rules.rule,
        messages:rules.msg,
       
        onfocusin: focus,
		onfocusout: _blur,
    	onkeyup:key,

    	focusInvalid:true,

    	highlight: function(element) {
    		$(element).addClass("error_msg")
    	},
    	unhighlight: function(element) {
    		$(element).removeClass("error_msg")
    	},
    	showErrors: function (errorMap, errorList) {
            var msg = "";
            $.each(errorList, function (i, v) {
                //在自定义layer的方法
                layer.tips(v.message, v.element, { time: 1000,tipsMore: true });
               // return false
            }); 
            this.defaultShowErrors();
        }
    });
};

/**
 * 比较两个日期的大小
 * @param startDate
 * @param endDate
 * @returns {Boolean}
 */
function compareDate(startDate,endDate){
	var start = new Date(startDate.replace("-", "/").replace("-", "/"));  
	var end = new Date(endDate.replace("-", "/").replace("-", "/"));  
	if(end<start){    
		return false;
	}else{
		return true;
	}
}

/**
 * 检查指定年月考勤是否被锁定
 * @param ctx
 * @param date
 * @returns {boolean}
 */
$.checkTimeLock=function(ctx,date,callback){
	var flag = false;
	$.ajax({
		url:ctx+'/cf/timeAutomated/checkTimeLock',
		async:false,
		data:{date:date},
		success:function (result) {
			flag = callback(result);
		}
	});
	return flag;
};


/**
 * 前端常量
 */
var dict = {};
/**
 * 普通审批状态：1通过，2拒绝，3撤销，4未审批
 */
dict.nomalApprStat = {
		"not_commit":{"desc":"未提交","val":"1"},
		"waiting_approve":{"desc":"待审批","val":"2"},
		"approve_pass":{"desc":"通过","val":"3"},
		"approve_refuse":{"desc":"拒绝","val":"4"}};

/**
 * 消分职级
 */
dict.cfRankType = {
		"sales_director":{"desc":"销管总监","val":"ZW82B43"},
		"city_manager":{"desc":"城市分中心经理","val":"ZW906E2"},
		"sales_manager":{"desc":"营业部经理","val":"ZW13FED"},
		"team_manager":{"desc":"团队经理","val":"ZW7A25B"},
		"customer_stand":{"desc":"客户代表","val":"ZW72510"},
		"admin_staff":{"desc":"行政专员","val":"ZW616F5"}};



