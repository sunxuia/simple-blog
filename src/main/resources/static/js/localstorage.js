function getViewHistory() {
    var list;
    try {
        list = localStorage["view-history"];
        list = JSON.parse(list);
        var current = new Date();
        for (var i = list.length - 1; i >= 0; i--) {
            var item = list[i];
            if (i > 99 ||
                item == null ||
                item.time == null ||
                (item.time = new Date(item.time).getTime()) == NaN ||
                current - new Date(item.time) > 1000 * 60 * 60 * 24 * 7) {
                list.splice(i, 1);
            }
        }
    } catch (err) {
        list = new Array();
    }
    return list;
}

function saveViewHistory(url, title) {
    if (window.localStorage) {
        list = getViewHistory();
        for (var i = 0; i < list.length; i++) {
            if (list[i].url == url && list[i].title == title) {
                list.splice(i, 1);
                break;
            }
        }
        list.unshift({
            "time": new Date(),
            "url": url,
            "title": title
        });
        localStorage["view-history"] = JSON.stringify(list);
    }
}