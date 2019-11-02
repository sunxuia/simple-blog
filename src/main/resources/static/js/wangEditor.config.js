$(function () {
    editor.customConfig = Object.assign(editor.customConfig, {
        menus: [
            'head',  // 标题
            'bold',  // 粗体
            'fontSize',  // 字号
            'fontName',  // 字体
            'italic',  // 斜体
            'underline',  // 下划线
            'strikeThrough',  // 删除线
            'foreColor',  // 文字颜色
            'backColor',  // 背景颜色
            'link',  // 插入链接
            'list',  // 列表
            'justify',  // 对齐方式
            'quote',  // 引用
            'image',  // 插入图片
            'table',  // 表格
            'video',  // 插入视频
            'code',  // 插入代码
            'undo',  // 撤销
            'redo'  // 重复
        ],
        customUploadImg: function (files, insert) {
            // files 是 input 中选中的文件列表
            // insert 是获取图片 url 后，插入到编辑器的方法, 上传代码返回结果之后，将图片插入到编辑器中
            $.ajax("/article/attach/upload", {
                method: "post",
                data: files,
                success: function (data) {
                    if (data.success) {
                        insert(data.imgUrl);
                    }
                }
            });

        }, linkImgCallback: function (url) {
            //TODO : perhaps we should save it into storage?
        }
    });
    editor.create();
});
