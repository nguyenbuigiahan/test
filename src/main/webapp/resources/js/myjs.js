Utils = function(){
	
}
Utils.postToUrl = function(url,params,method){
	method = method || "POST";
	var form = document.createElement("form");
	form.setAttribute("method",method);
	form.setAttribute("action",url);
	for(var key in params){
		var hiddenField = document.createElement("input");
		hiddenField.setAttribute("type","hidden");
		hiddenField.setAttribute("name",key);
		hiddenField.setAttribute("value",params[key]);
		form.appendChild(hiddenField);
		
	}
	document.body.appendChild(form);
	form.submit();
}

Home = function(){
	
};
Home.prototype.init = function(){
	var self = this;
	//On post detail button click;
	$('.postToUrl').on('click',function(e){
		e.preventDefault();
		var url = $('input[name=postUrl]').val();
		var keyword = $('input[name=keyword').val();
		url = url+ keyword;
		window.location = url;
	});
	
};
Blog = function(){
	
};
Blog.prototype.init = function(){
	var self = this;
	//On post detail button click;
	$('.postToUrl').on('click',function(e){
		e.preventDefault();
		var url = $('input[name=postUrl]').val();
		var keyword = $('input[name=keyword').val();
		url = url+ keyword;
		window.location = url;
	});
	
	/* activate sidebar */
	$('#sidebar').affix({
	  offset: {
	    top: 100
	  }
	});

	/* activate scrollspy menu */
	var $body   = $(document.body);
	var navHeight = $('.navbar').outerHeight(true) + 10;

	$body.scrollspy({
		target: '#rightCol',
		offset: navHeight
	});

	/* smooth scrolling sections */
	$('a[href*=#]:not([href=#])').click(function() {
	    if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
	      var target = $(this.hash);
	      target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
	      if (target.length) {
	        $('html,body').animate({
	          scrollTop: target.offset().top - 50
	        }, 1000);
	        return false;
	      }
	    }
	});
	//facebook comment plugin
	
};
Error = function(){
	
};
Error.prototype.init = function(){
	var self = this;
	//On post detail button click;
	$('.postToUrl').on('click',function(e){
		e.preventDefault();
		var url = $('input[name=postUrl]').val();
		var keyword = $('input[name=keyword').val();
		url = url+ keyword;
		window.location = url;
	});
};