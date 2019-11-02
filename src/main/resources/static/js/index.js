function setSearchItem(selected) {
    selected = $(selected);
    var parent = selected.parents(".input-group");
    parent.find(".dropdown-toggle").html(selected.text() + "<span class='caret'></span>")
    parent.find("input[name=search-type]").val(selected.data("value"));
}

var lastTime = new Date();

function showMore() {
    var btn = $("#show-more");
    btn.fadeOut();
    var wait = waitAnimation();
    $("#content").append(wait);
    $.ajax("/index/article-list", {
        type: "get",
        dataType: "json",
        cache: false,
        data: {
            count: 10,
            lastTime: lastTime,
        }, success: function (list) {
            wait.remove();
            if (list.length > 0) {
                $.each(list, function (i, item) {
                    item.createTime = new Date(item.createTime);
                    item.lastModifiedTime = new Date(item.lastModifiedTime);

                    var newTab = $('\
    <div class="content-card">\
        <div class="content-head">\
            <h4>\
                <a class="title-link" href="#"><strong class="title">title</strong></a>\
            </h4>\
            <div class="close" data-toggle="tooltip" data-tooltip="tooltip" data-placement="top" title="不感兴趣" onclick="disappear(this)" hidden>\
                <span class="glyphicon glyphicon-remove"></span>\
            </div>\
            <div class="content-author">\
                <img class="author-avatar" src="../../static/images/default-avatar.png"/>\
                <a class="author-name" href="#">author</a>\
            </div>\
        </div>\
        <div class="content-body">\
            <p class="brief">\
                content brief\
            </p>\
        </div>\
        <div class="content-footer">\
            <a class="view-article btn btn-primary">查看全文</a>\
            <div class="footer-info inner-inline">\
                <p><span class="like-count"></span>赞</p>\
                <p><span class="create-time"></span>发表</p>\
                <p><span class="last-modified-time"></span>修改</p>\
            </div>\
        </div>\
    </div>\
');
                    newTab.find(".title").text(item.title);
                    newTab.find(".title-link").attr("href", item.url);
                    newTab.find(".author-avatar").attr("src", item.author.avatarUrl);
                    newTab.find(".author-name").attr("href", item.author.url).text(item.author.name);
                    newTab.find(".brief").text(item.brief);
                    newTab.find(".view-article").attr("href", item.url);
                    newTab.find(".like-count").text(item.likeCount);
                    newTab.find(".create-time").text(item.createTime.interval());
                    if (item.createTime.getTime() == item.lastModifiedTime.getTime()) {
                        newTab.find(".last-modified-time").parent().html("");
                    } else {
                        newTab.find(".last-modified-time").text(item.lastModifiedTime.interval());
                    }
                    var close = newTab.find(".close");
                    newTab.on({
                        "mouseover": function () {
                            close.fadeIn("fast");
                        }, "mouseleave": function () {
                            close.fadeOut("fast");
                        }
                    });
                    close.tooltip();
                    newTab.data("item", item);
                    $("#content").append(newTab);
                });
            } else {
                btn.text("没有更多数据").prop("disabled", true);
            }
            btn.fadeIn();
        },
        errorHandler: function () {
            wait.remove();
            btn.fadeIn();
        }
    });
}

function disappear(ele) {
    var parent = $(ele).parents(".content-card");
    var item = parent.data("item");
    if (item) {
        //TODO : disappear in server
    }
    parent.fadeOut(function () {
        parent.remove();
    });
}

function search() {
    window.location.href = "/search?type=" + $("[name=search-type]").val() + "&text=" + encodeURI($("[name=search-text]").val());
}

function setAdviceSearch() {
    $.get("/index/advice-search", function (list) {
        list.forEach(function (item) {
            $(".advice-search")
                .append('<a class="search-link" href="' + encodeURI(item.value) + '">' + item.key + '</a>');
        });
    });
}

$(function () {
    setAdviceSearch();
    showMore();
});

