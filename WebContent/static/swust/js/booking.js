$(function () {
  $("#nav").on("click",".search",function () {
    $(this).siblings("input").addClass("change-long");
    $(this).children("img").attr("src","/mht_oeg/static/swust/images/graySearch.png");
  }).on("click",".modifyPwd",function () {
        $("#modal-pwd").modal("show");
  });
  $(".state-menu").on("click","a",function () {
    $(this).addClass("current").parent().siblings().children("a").removeClass("current")
  });
  $(".buttons").on("click","button",function () {
    $(this).addClass("active").siblings().removeClass("active")
  });
  $("table").on("click",".check-view",function () {
    $("#check-view").modal("show");
  });
//  $("button:contains('新增')").click(function () {
//    location.href="newBooking.html";
//  });
});