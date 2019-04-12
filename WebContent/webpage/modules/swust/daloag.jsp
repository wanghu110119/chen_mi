<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="modal fade bs-example-modal-sm in" tabindex="-1" role="dialog" id="check-view" style="display: block; padding-right: 17px;">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                    <h3 class="modal-title text-center">预约事由</h3>
                </div>
                <div class="modal-body">
                    <p class="text-center">${appointment.contactsThing}</p>
                    <p class="text-center">
                        <span class="notice">备注：</span>
                        ${appointment.remarks}  
                    </p>
                </div>
                <div class="modal-footer text-center">
                    <button type="button" class="btn btn-md confirm" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>