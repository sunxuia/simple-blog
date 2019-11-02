$(function () {
    $.each($(".comment .comment-user"), function (i, user) {
        user = $(user);
        user.css("height", user.parent().height() - 5); //minus margin
    });

    $(".comment .comment-reply").on("click", function () {
        var comment = $(this).parents(".comment"),
            commentOption = comment.find(".comment-option");
        if (commentOption.next()[0] &&
            commentOption.next()[0].nodeName.toLowerCase() == "form") {
            commentOption.next().remove();
            return;
        }
        var createComment = $(
            '<form class="form" method="post" action="/article/reply-comment">\n' +
            '    <input type="hidden" name="reply-to"/>\n' +
            '    <textarea name="text" class="form-control" rows="2" maxlength="100" \n' +
            '              placeholder="回复评论, 不超过100字."></textarea>\n' +
            '    <button class="btn btn-info btn-sm" style="margin-top: 5px; margin-left: 5px">提交</button>\n' +
            '</form>\n');
        createComment.addCsrf();
        createComment.find("[name=reply-to]").val(comment.data("id"));
        commentOption.after(createComment);
        createComment.find("textarea").focus();
    });

    $(".comment .comment-delete").on("click", function () {
        var comment = $(this).parents(".comment");
        $.delete("/comment/" + comment.data("id"), function () {
            comment.remove();
        });
    });

    $(".btn-like").on("click", function () {
        $(this).find("span").toggleClass("fa-thumbs-o-up").toggleClass("fa-thumbs-up");
        $.post("/article/like/" + articleId);
    });

    $(".btn-collect").on("click", function () {
        var modal = $("#collect-modal");
        modal.modal();
    });

    collectCheckboxBind(".collect-select");

    $("#collect-create-new").on("click", function () {
        var li = $(
            '<li class="inner-inline-block">\n' +
            '    <div class="collect-create-form">\n' +
            '        <div class="input-group" style="width:300px">\n' +
            '            <input type="text" autocomplete="off" class="form-control input-sm" value="新建收藏夹"/>\n' +
            '            <span class="input-group-btn">' +
            '                <button type="button" class="btn btn-primary btn-sm btn-create">创建</button>\n' +
            '                <button type="button" class="btn btn-default btn-sm btn-cancel">取消</button>\n' +
            '            </span>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '    <input type="checkbox" class="round-input collect-select"/>\n' +
            '    <input type="hidden" class="collect-id"/>\n' +
            '</li>\n'
        );
        li.find(".btn-create").on("click", function () {
            createCollection(li);
        });
        li.find(".btn-cancel").on("click", function () {
            li.remove();
        });
        collectCheckboxBind(li.find(".collect-select"));
        $("#collect-modal").find("ul").append(li);
    });
});

function collectCheckboxBind(cb) {
    $(cb).on("click", function () {
        var checkbox = $(this),
            collectionId = checkbox.parents("li").data("id");

        function uploadData() {
            collectionId = checkbox.parents("li").data("id");
            $.ajax("/collection/" + collectionId + "/" + articleId, {
                method: "post",
                data: {
                    collect: checkbox.prop("checked")
                },
                errorHandler: errorHandler
            });
        };

        function errorHandler() {
            checkbox.prop("checked", !checkbox.prop("checked"));
        }

        if ($.isEmpty(collectionId)) {
            createCollection(checkbox.parents("li"), uploadData, errorHandler);
        } else {
            uploadData();
        }
    });
}

function createCollection(li, success, failure) {
    $.ajax("/collection/", {
        method: "post",
        data: {
            "name" : li.find("[type=text]").val()
        },
        success: function (data) {
            li.find(".collect-create-form").replaceWith('<label class="collect-name">' + data.name + '</label>');
            li.find(".collect-id").val(data.id);
            if (success) {
                li.data("id", data.id);
                success();
            }
        },
        errorHandler: failure
    });
}