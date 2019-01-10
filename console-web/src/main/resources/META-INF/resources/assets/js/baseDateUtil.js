window.baseDateUtil = {};

/**
 * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
 * 例子：   
 * baseDateUtil.format(new Date(),"yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
 * baseDateUtil.format(new Date(),"yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
 * 参数:
 * date：Date类型的变量。
 * formatStr：格式化字符串。
 */
baseDateUtil.format = function(date, formatStr) {
	var o = {
		"d+" : date.getDate(), // 日
		"h+" : date.getHours(), // 小时
		"m+" : date.getMinutes(), // 分
		"s+" : date.getSeconds(), // 秒
		"q+" : Math.floor((date.getMonth() + 3) / 3), // 季度
		"S" : date.getMilliseconds(), // 毫秒
		"M+" : date.getMonth() + 1
	// 月份
	}, monthname = {
		1 : 'Jan',
		2 : 'Feb',
		3 : 'Mar',
		4 : 'Apr',
		5 : 'May',
		6 : 'Jun',
		7 : 'Jul',
		8 : 'Aug',
		9 : 'Sep',
		10 : 'Oct',
		11 : 'Nov',
		12 : 'Dec'
	};
	if (/(y+)/.test(formatStr)) {
		formatStr = formatStr.replace(RegExp.$1, (date.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(formatStr)) {
			if (k === 'M+' && RegExp.$1.length == 3) {
				formatStr = formatStr.replace(RegExp.$1, monthname[o[k]]);
				continue;
			}
			formatStr = formatStr.replace(RegExp.$1,
					(RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k])
							.substr(("" + o[k]).length)));
		}
	}
	return formatStr;
};

baseUtil.strToDate = function(dataStr) {
	var splitTime = $.trim(dataStr).split(/[^0-9]+/);
	var d = new Date();
	d.setFullYear(splitTime[0]);
	d.setMonth(splitTime[1] - 1);
	d.setDate(splitTime[2]);
	d.setHours(splitTime[3]);
	d.setMinutes(splitTime[4]);
	d.setSeconds(splitTime[5]);
	d.setMilliseconds(splitTime[6]);
	return d;
};
