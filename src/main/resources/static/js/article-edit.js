$(function () {
    $("[type=file]").fileinput({
        language: "zh",
        browseLabel: "选择文件上传",
        maxFileSize: 5 * 1024,
        maxFileCount: 1,
        autoReplace: true,
        showPreview: false,
        uploadAsync: true,
        uploadUrl: "/article-edit/attachment/upload",
        uploadExtraData: {
            "articleId": $("[name=id]").val(),
            "_csrf": $.csrf()
        },
        elErrorContainer: "#attachment-input-error",
        showUpload: false,
        showCaption: false,
        showRemove: false,
        browseClass: "btn btn-primary",
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
    }).on("filebatchselected", function (event, files) {
        $(this).fileinput("upload");
    }).on('fileuploaded', function (event, data, id, index) {
        var row = $(
            '<tr>\n' +
            '    <td><a href="#" class="attachment-link">title</a></td>\n' +
            '    <td class="inner-inline">\n' +
            '        <button type="button" class="btn btn-info btn-sm btn-copy-link">\n' +
            '            复制链接\n' +
            '        </button>\n' +
            '         在页面显示<input type="checkbox" class="chk-attachment-show"/>\n' +
            '        <div style="vertical-align: middle">\n' +
            '            <div class="input-group">\n' +
            '                <input type="text" autocomplete="off" class="form-control input-sm attachment-name" value="文件名"/>\n' +
            '                <span class="input-group-btn"><button type="button" class="btn btn-default btn-sm">修改文件名</button></span>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '        <button type="button" class="btn btn-danger btn-sm btn-attachment-delete">删除</button>\n' +
            '    </td>\n' +
            '</tr>\n');
        var res = data.response[0];
        row.data("id", res.id);
        row.find(".attachment-link").attr("href", res.url).attr("download", res.name).text(res.name);
        row.find("td:last-child [name=filename]").val(data.files[index].name);
        initialAttachmentRow(row);
        $("table").append(row);
    });
    $.each($("#content-attachment tr"), function (i, item) {
        initialAttachmentRow($(item));
    });

    var form = $("#article-form");
    form.find("[name=visible]").bootstrapSwitch({
        size: "small",
        onColor: "success",
        offColor: "info",
        onText: "公开",
        offText: "不公开"
    });
    $(".release").on("click", function () {
        form.attr("action", "/article-edit/release");
        form.validator().on("submit", function (e) {
            if (!e.isDefaultPrevented()) {
                form.find("[name=contentText]").val(editor.txt.html());
            }
        }).submit();
    });
    $(".save-as-draft").on("click", function () {
        form.attr("action", "/article-edit/save-as-draft");
        form.validator().on("submit", function (e) {
            if (!e.isDefaultPrevented()) {
                form.find("[name=contentText]").val(editor.txt.html());
            }
        }).submit();
    })
});

function initialAttachmentRow(row) {
    // copy link
    new ClipboardJS(row.find(".btn-copy-link")[0], {
        text: function (btn) {
            return row.find(".attachment-link").attr("href");
        }
    });

    // is public switch
    row.find(".chk-attachment-show").bootstrapSwitch({
        onColor: "success",
        offColor: "success",
        onText: "显示",
        offText: "隐藏",
        onSwitchChange: function () {
            $.put("/article-edit/attachment/" + row.data("id"), {
                "visibility": $(this).prop("checked")
            }, function () {
            }, function () {
                $(this).bootstrapSwitch("toggleState");
            });
        }
    });

    // modify name
    row.find(".attachment-name")
        .val(row.find(".attachment-link").text())
        .parents(".input-group")
        .find("button")
        .on("click", function () {
            $.put("/article-edit/attachment/" + row.data("id"), {
                "newName": row.find(".attachment-name").val()
            }, function () {
                row.find(".attachment-link").text(row.find(".attachment-name").val());
            });
        });

    // delete
    row.find(".btn-attachment-delete").on("click", function () {
        $.delete("/article-edit/attachment/" + row.data("id"), function () {
            row.remove();
        });
    });
}



