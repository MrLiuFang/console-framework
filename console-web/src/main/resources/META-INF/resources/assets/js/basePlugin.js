/**
 * 封装系统ui的一些常用插件
 */
var basePlugin = {

	/**
	 * 常用的弹出窗口，有取消和保存按钮。
	 * 
	 * @Param title
	 *            标题
	 * @Param id
	 *            窗口id
	 * @Param content
	 *            窗口内容
	 * @Param yesFn
	 *            保存方法
	 * @Param extOpt
	 *            插件的额外参数，如果额外参数和现有参数重复，额外参数会覆盖现有参数。
	 */
	win : function(title, id, content, yesFn, extOpt) {
		var defaultOpt = {
			type : 1,
			title : title,
			// 具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
			offset : 'auto',
			// 防止重复弹出
			id : id,
			content : content,
			btn : [ '保存', '取消' ],
			// 按钮居中
			btnAlign : 'c',
			// 不显示遮罩
			shade : 0,
			yes : function(index, layero) {
				var keepOpen;
				if (yesFn) {
					keepOpen = yesFn(index, layero);
				}
				if (!keepOpen) {
					layui.layer.close(index);
				}
			},
			btn2 : function() {
				layui.layer.close(index);
			}
		};
		if (extOpt) {
			$.extend(defaultOpt, extOpt);
		}
		var index = layui.layer.open(defaultOpt);
		return index;
	}
}