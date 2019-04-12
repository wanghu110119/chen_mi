$(function () {
  $(".book-list").on("click","a",function () {
    $(this).addClass("change-bg").parent().siblings().children("a").removeClass("change-bg");
  });
  $(".buttons").on("click","button",function () {
    $(this).addClass("active").siblings().removeClass("active")
  });
  /*待审核全选代码*/
  $(".checkAll1").click(function(){
    $(this).parent().parent().parent().parent().find("input[type='checkbox']").prop('checked',$(this).prop('checked'));
    console.log( $(this).parent().parent().parent().parent().find("input[type='checkbox']").length);
  });
  $(".t1 input").each(function(){
    $(this).click(function(){
    	console.log($(".t1 input:checked").length+"!!!!!!!"+$(".t1 input").length);
    	console.log($(this).parent().next().html())
      if($(".t1 input:checked").length===$(".t1 input[type='checkbox']").length){
        $(".checkAll1").prop("checked",true)
      }
      else{
        $(".checkAll1").prop("checked",false)
      }
    })
  });
  /*已审核列表代码*/
  $(".checkAll2").click(function(){
    $(this).parent().parent().parent().parent().find("input[type^='check']").prop('checked',$(this).prop('checked'));
  });
  $(".t2 input").each(function(){
    $(this).click(function(){
      if($(".t2 input:checked").length===$(".t2 input").length){
        $(".checkAll2").prop("checked","checked")
      }
      else{
        $(".checkAll2").prop("checked",false)
      }
    })
  });
  $("table").on("click",".check-view",function () {
    $("#check-view").modal("show");
  }).on("click",".edit",function () {
    $("#edit").modal("show");
  }).on("click",".refuse",function () {
    $(this).toggleClass("disabled")
  });
  // 换图片
  $(".back-right").on("click","img",function () {
	  //	  changepicture('${list.id}');
    $(".back-left img").attr("src",$(this).attr("src"));
  });
  $(".modify").click(function () {
    $("textarea").removeAttr("disabled").focus();
    $(".save").css("visibility","visible").click(function () {
      $(this).css("visibility","hidden");
      $("textarea").attr("disabled","disabled");
    })
  })
  $(".modify-password").click(function(){
	  $("#modal-pwd").modal("show");
  })
});