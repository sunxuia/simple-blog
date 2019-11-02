$.ajaxSetup({
    beforeSend: function (xhr, settings) {
        if ((settings.type == 'POST' || settings.type == 'PUT' || settings.type == 'DELETE') &&
            !(/^http:.*/.test(settings.url) || /^https:.*/.test(settings.url))) {
            xhr.setRequestHeader("X-CSRF-TOKEN", $("meta[name='_csrf']").attr("content"));
            xhr.withCredentials = true;
        }
        if (settings.error == undefined) {
            settings.error = function (xhr, status, err) {
                // var msg = "<p>调用地址 : " + this.url + "<br /> 代码 : " + status;
                // if (err != undefined) {
                //     msg = msg + "<br />错误信息 : " + JSON.stringify(err) + "</p>";
                // }
                flashModal("alert alert-danger", err);
                if (settings.errorHandler) {
                    settings.errorHandler(xhr, status, err);
                }
            }
        }

        // if (settings.dataType == "json" && settings.success) {
        //     var callback = settings.success;
        //     settings.success = function (data) {
        //         console.log(data);
        //         callback(data);
        //     };
        // }
    }
});

function showModal(titleOrContent, content) {
    var title = "";
    if (content == undefined) {
        content = titleOrContent;
    } else {
        title = titleOrContent;
    }
    if (content instanceof String && !title.startsWith("<")) {
        content = "<p>" + content + "</p>";
    }
    var modal = $("#modal-template");
    if (modal.length == 0) {
        var modal = $(
            "<div class='modal fade'>\n" +
            "  <div class='modal-dialog'>\n" +
            "    <div class='modal-content'>\n" +
            "      <div class='modal-header inner-inline'>\n" +
            "        <button type='button' class='close' data-dismiss='modal'>&times;</button>\n" +
            "        <h4></h4>\n" +
            "      </div>\n" +
            "      <div class='modal-body'>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n");
    }
    modal.find(".modal-header > h4").text(title);
    modal.find(".modal-body").html(content);
    modal.modal();
}

function flashModal(klass, content) {
    if (content instanceof String && !title.startsWith("<")) {
        content = "<p>" + content + "</p>";
    }
    var modal = $(
        "<div class='modal'>" +
        "  <div class='modal-dialog'>" +
        "    <div class='" + klass + "'>" +
        content +
        "    </div>" +
        "  </div>" +
        "</div>");
    $("body").append(modal);
    modal.fadeIn(1000, function () {
        setInterval(function () {
            modal.fadeOut(1000, function () {
                modal.remove();
            });
        }, 1000);
    });
}

function bottomWarning(content) {
    if (content instanceof String && !title.startsWith("<")) {
        content = "<p>" + content + "</p>";
    }
    var bar = $(
        "<div class='bottom-bar'>\n" +
        "  <div class='container'>\n" +
        "    <div class='msg'>" + content + "</div>\n" +
        "    <div class='close' style='font-size: 35px;' onclick='$(this).parents(\".bottom-bar\").remove()'>&times;</div>\n" +
        "  </div>\n" +
        "</div>");
    $("body").append(bar);
    bar.height(bar.find(".msg").height());
}

function waitAnimation() {
    return $(
        "<div class='spinner'>\n" +
        "  <div class='rect1'></div>\n" +
        "  <div class='rect2'></div>\n" +
        "  <div class='rect3'></div>\n" +
        "  <div class='rect4'></div>\n" +
        "  <div class='rect5'></div>\n" +
        "</div>\n");
}

Date.prototype.format = function (fmt) {
    var keys = {
        "M+": this.getMonth() + 1,
        "d+": this.getDay(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) { //year
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in keys)
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ?
                (keys[k]) :
                (("00" + keys[k]).substr(("" + keys[k]).length)));
        }
    return fmt;
}

Date.prototype.interval = function () {
    var interval = new Date() - this;
    if (interval < 1000 * 60) {
        return "刚才";
    } else if (interval < 1000 * 60 * 60) {
        return Math.round(interval / 1000 / 60) + "分钟前";
    } else if (interval < 1000 * 60 * 60 * 24) {
        return Math.round(interval / 1000 / 60 / 60) + "小时前";
    } else if (interval < 1000 * 60 * 60 * 24 * 30) {
        return Math.round(interval / 1000 / 60 / 60 / 24) + "天前";
    } else {
        return this.format("yyyy年MM月dd日");
    }
};

$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        }
        else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$.fn.addCsrf = function () {
    this.append('<input type="hidden" name="_csrf" value="' + $.csrf() + '" />');
};

$.csrf = function () {
    return $("meta[name=_csrf]").attr("content");
}

$.isEmpty = function (val) {
    if (val == null) {
        return true;
    }
    return val.length === 0;
}

$.toNumber = function (val) {
    if (val == null) {
        return val;
    }
    if (typeof(val) == "number") {
        return val;
    }
    if (typeof(val) == "string") {
        var res = val.replace(/^(\d+(?:\.\d+)?).*$/, "$1");
        if ($.isEmpty(res)) {
            return null;
        }
        return Number.parseFloat(res);
    }
    return null;
};

$.post = function () {
    var url = arguments[0], data, success, type, error;
    for (var i = 1; i < arguments.length; i++) {
        var arg = arguments[i];
        switch (typeof(arg)) {
            case "function" :
                if (!success) {
                    success = arg;
                } else {
                    error = arg;
                }
                break;
            case "object":
                data = arg;
                break;
            case "string":
                type = arg;
                break;
        }
    }
    $.ajax(url, {
        "method": "post",
        "data": data,
        "success": success,
        "type": type,
        "errorHandler": error
    });
};

$.delete = function () {
    var url = arguments[0], data, success, type, error;
    for (var i = 1; i < arguments.length; i++) {
        var arg = arguments[i];
        switch (typeof(arg)) {
            case "function" :
                if (!success) {
                    success = arg;
                } else {
                    error = arg;
                }
                break;
            case "object":
                data = arg;
                break;
            case "string":
                type = arg;
                break;
        }
    }
    data = Object.assign(data ? data : {}, {"_method": "delete"});
    $.ajax(url, {
        "method": "post",
        "data": data,
        "success": success,
        "type": type,
        "errorHandler": error
    });
};
$.put = function () {
    var url = arguments[0], data, success, type, error;
    for (var i = 1; i < arguments.length; i++) {
        var arg = arguments[i];
        switch (typeof(arg)) {
            case "function" :
                if (!success) {
                    success = arg;
                } else {
                    error = arg;
                }
                break;
            case "object":
                data = arg;
                break;
            case "string":
                type = arg;
                break;
        }
    }
    data = Object.assign(data ? data : {}, {"_method": "put"});
    $.ajax(url, {
        "method": "post",
        "data": data,
        "success": success,
        "type": type,
        "errorHandler": error
    });
};

$(function () {
    +function initialSidebarSmallCard() {
        $(".sidebar .small-card").on({
            'mouseover': function () {
                $(this).find(".card-name").animate({
                    top: "2px"
                }, 300);
            },
            "mouseleave": function () {
                $(this).find(".card-name").animate({
                    top: "60px"
                }, 200)
            }
        });
    }();

    $(".login-button").on("click", function () {
        window.open($(this).data("href"), "newWindow", "location=no,status=no");
    });
});