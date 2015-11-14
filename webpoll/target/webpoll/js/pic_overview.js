$.ajax({
			type : "post",
			url : "listalljson.do",
			dataType : "json",
			success : function(data) {

				var $tbody = $('#picoverview_row6');
				$.each(
								data.data,
								function(n, value) {

									var tr = "<div class=\"col-xs-4 col-xs-offset-1 placeholder\">"
											+ "<a id=\"atag" + value.idpic +"\" href=\"#\" class=\"thumbnail\" data-toggle=\"modal\""
											+ "data-target=\"#mydivid"
											+ value.idpic
											+ "\"> <img src=\""
											+ value.picUrl
											+ "\""
											+ "alt=\"Generic placeholder thumbnail\">"
											+ "</a>"
											+ "<h4>"
											+ value.pictitle
											+ " <span class=\"badge\" id=\"mycount"+value.idpic+"\">"
											+ value.picCount
											+ "</span>"
											+ "</h4>"
											//+ "<span class=\"text-muted\">Description</span>"
											+ "</div>";
									$tbody.append(tr);
									
									var $tbigpic = $('#bigpic');
									$("a[id='atag"+value.idpic+"']").click(function(){
												//ajax post start
												$.ajax({
													type : "post",
													url : "findbyid.do?id="+value.idpic,
													dataType : "json",
													success : function(resimgdata) {
														//alert(resimgdata.data);
														var imgUrlW, imgUrlH;
														var img=new Image();
														//img.style.visibility = "show";
											            img.src=resimgdata.data.picUrl;
														var imgwidth = img.width;  
													    var imgheight = img.height; 
													    //alert(img.src + ":"+ imgwidth + ":" + imgheight);
													    //if (imgwidth > imgheight){
													    var ratio=0.5;
													    if (imgwidth>imgheight){
													    	if(imgwidth>1000 && imgwidth<2000){
													    		ratio = 0.5;
													    	}
													    	else if(imgwidth>2000 && imgwidth<3000){
													    		ratio = 0.3;
													    	}
													    	else if(imgwidth>3000 && imgwidth<4000){
													    		ratio = 0.15;
													    	}
													    	else if(imgwidth>0 && imgwidth<=1000){
													    		ratio = 0.8;
													    	}
													    	else{
													    		ratio = 0.12;
													    	}
													    }
													    if (imgwidth<imgheight){
													    	if(imgheight>1000 && imgheight<2000){
													    		ratio = 0.5;
													    	}
													    	else if(imgheight>2000 && imgheight<3000){
													    		ratio = 0.3;
													    	}
													    	else if(imgheight>3000 && imgheight<4000){
													    		ratio = 0.15;
													    	}else if(imgwidth>0 && imgwidth<=1000){
													    		ratio = 0.8;
													    	}
													    	else{
													    		ratio = 0.12;
													    	}
													    }
												    	imgUrlW = imgwidth*ratio;
												    	imgUrlH = imgheight*ratio;
														var divs = "<div class=\"modal fade\" id=\"mydivid"+resimgdata.data.idpic+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\"><div class=\"modal-dialog\" role=\"document\"><div class=\"modal-content\">"
															+ "<div class=\"modal-header\"><button type=\"button\" class=\"close\" data-dismiss=\"modal\"	aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span>"
															+ "</button><h4 class=\"modal-title\" id=\"myModalLabel\">"
															+ resimgdata.data.pictitle
															+ "</h4></div>	<div class=\"modal-body\"><div class=\"row placeholders\">"
															+ "<a href=\""+ resimgdata.data.picUrl +"\"><img	src=\""
															+ resimgdata.data.picUrl
															+ "\" class=\"img-rounded\" alt=\"Generic placeholder thumbnail\" width="+imgUrlW+" height="+imgUrlH+"></a>"
															+ "</div><dl class=\"dl-horizontal\"><dt>Title</dt><dd>"
															+ resimgdata.data.pictitle
															+ "</dd>"
															+ "	</dl></div>"
															+ "<div class=\"modal-footer\">"
															+ "<button id=\"firstPicVoteBtn"
															+ resimgdata.data.idpic
															+ "\" type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\">Like</button>"
															+ "</div>" + "</div></div></div>"

													$tbigpic.append(divs);
														
														$("button[id='firstPicVoteBtn"
																+ resimgdata.data.idpic + "']").click(function(){
																	$('#myWarningMsg').modal('show');
																});
														$("button[id='buttonyes']").click(function(){
															//ajax post start
															$
															.ajax({
																type : "post",
																url : "votebyidwitherror.do?id="+resimgdata.data.idpic,
																dataType : "json",
																success : function(resdata) {
																	if (resdata.error.code != 0){
																		$('#myErrorMsg').modal('show');
																	}else{
																		resimgdata.data.picCount+=1;
																		$("#mycount"+resimgdata.data.idpic).html(resimgdata.data.picCount);
																	}
															
																}});
															//ajax post end
														});
														$("button[id='buttonno']").click(function(){
															$('#myWarningMsg').modal('hide');
														});
														$('#mydivid'+resimgdata.data.idpic).modal('show');
													}});
												//ajax post end
											});
									
								});
				
			}

		});






var $loading = $('#loadingspinner').hide();
$(document)
  .ajaxSend(function () {
    $loading.show();
  })
  .ajaxStop(function () {
    $loading.hide();
  });
