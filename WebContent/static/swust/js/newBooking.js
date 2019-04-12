$(function () {
  $("#header").load("include/header.html").on("click",".search",function () {
    $(this).siblings("input").addClass("change-long");
    $(this).children("img").attr("src","images/graySearch.png");
  }).on("click",".modifyPwd",function () {
    $("#modal-pwd").modal("show");
  });
});