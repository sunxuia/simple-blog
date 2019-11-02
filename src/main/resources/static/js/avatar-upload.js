$(function () {
    // debugger;
    var avatar = $('#fileUpload');
    var _this = this;
    setAvatar(avatar,
        '/setting/avatar',
        function () {
            return {
                width: $.toNumber($('.jcrop-active').css('width')),
                height: $.toNumber($('.jcrop-active').css('height')),
                avatarX: $('#avatar-x').val(),
                avatarY: $('#avatar-y').val(),
                avatarWidth: $('#avatar-width').val(),
                avatarHeight: $('#avatar-height').val(),
                _csrf: $.csrf()
            }
        },
        $('#alert'));

    var api;
    $('#fileUpload').on('change', function () {
        var img = $('#cut-img');
        var input = $('#fileUpload');
        if (input[0].files && input[0].files[0]) {
            var reader = new FileReader();
            reader.readAsDataURL(input[0].files[0]);
            reader.onload = function (e) {
                img.attr('src', e.target.result);
                if (api) {
                    api.destroy();
                }
                $('#avatar-x').val(20);
                $('#avatar-y').val(20);
                $('#avatar-width').val(200);
                $('#avatar-height').val(200);

                img.Jcrop({
                    setSelect: [20, 20, 200, 200],
                    handleSize: 10,
                    aspectRatio: 1,
                    onSelect: updateCords,
                    boxWidth: img.parent().css('width').replace(/px$/, ''),
                    boxHeight: img.parent().css('height').replace(/px$/, ''),
                }, function () {
                    api = this;
                });
            };
        }

        function updateCords(obj) {
            $("#avatar-x").val(obj.x);
            $("#avatar-y").val(obj.y);
            $("#avatar-width").val(obj.w);
            $("#avatar-height").val(obj.h);
        }
    });
    $(".btn-ok").on("click", function () {
        if (avatar.val()) {
            avatar.fileinput('upload');
        } else {
            alert("请先选择文件");
        }
    });

    function setAvatar(target, uploadUrl, extraData, alert) {
        target.fileinput({
            language: 'zh', //设置语言
            maxFileSize: 2048, //文件最大容量
            maxFileCount: 1,
            autoReplace: true,
            uploadExtraData: extraData, //上传时除了文件以外的其他额外数据
            showPreview: false, //隐藏预览
            uploadAsync: true, //ajax同步
            dropZoneEnabled: false, //是否显示拖拽区域
            uploadUrl: uploadUrl, //上传的地址
            elErrorContainer: alert, //错误信息内容容器
            allowedFileExtensions: ['jpg', 'png'], //接收的文件后缀
            showUpload: false, //是否显示上传按钮
            showCaption: false, //是否显示标题
            showRemove: false,
            browseClass: "btn btn-primary", //按钮样式
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
        }).on('fileuploaded', function (event, data, id, index) {
            $("#avatar-upload").modal("hide");
            $(".avatar-preview").attr("src", data.response[0]);
            $("#cut-img").attr("src", data.response[0]).css("display", "block");
            $(".jcrop-active").remove();
            avatar.fileinput();

        });
    };
    this.alert = function (target, alert) {
        target.on('fileuploaderror', function (event, data, msg) {
            alert.removeClass('hidden').html(msg);
            _this.fileinput('disable');
        });
        target.on('fileclear', function (event) {
            alert.addClass('hidden').html();
        });
        target.on('fileloaded', function (event, file, previewId, index, reader) {
            alert.addClass('hidden').html();
        });
        target.on('fileuploaded', function (event, data) {
            if (!data.response.status) {
                alert.html(data.response.message).removeClass('hidden');
            }
        });
    };
});
