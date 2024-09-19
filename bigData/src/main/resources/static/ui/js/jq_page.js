/**
 * 用于分页的控件
 * 依赖于jQuery。
 */
;(function($){
	$.fn.extend({
		makePage:function(pageInfo,refleshFunction){
		  var pageNo = pageInfo.pageNo;
		  var pageSize = pageInfo.pageSize;
		  var totalCount = pageInfo.totalCount;
		  var totalPages = pageInfo.totalPages;
		  var pageContext = '';
		  if(pageNo>1){
		    pageContext += ' <a href="javascript:void(0)" class="page" pageNo="1">首页</a> ';
		    pageContext += ' <a href="javascript:void(0)" class="page" pageNo="'+(pageNo-1)+'">上一页</a> ';
		  }
		  pageContext += ' 第'+pageNo+'页/共'+totalPages+'页 ';
	      if(pageNo<totalPages){
		    pageContext += ' <a href="javascript:void(0)" class="page" pageNo="'+(pageNo+1)+'">下一页</a> ';
			pageContext += ' <a href="javascript:void(0)" class="page" pageNo="'+totalPages+'">尾页</a> ';
		  }
		  $(this).empty().append(pageContext);
		  
		  if(typeof(refleshFunction)=='function'){
			  $(this).find('.page').click(refleshFunction);
		  }
		}
	});
})(jQuery);