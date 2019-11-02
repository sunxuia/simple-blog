function sidebarDock(sidebar, container, option) {
    var justifySidebar = function () {
        sidebar.width(container.width());
    };
    justifySidebar();
    sidebar.affix(option).on("affixed-top.bs.affix", function () {
        sidebar.css("position", "relative");
    }).on("affix.bs.affix", function () {
        justifySidebar();
        sidebar.css("position", "fixed");
    })/*.on("affixed-bottom.bs.affix", function () {
        sidebar.css("position", "fixed");
    })*/;
    $(window).on("resize", function () {
        justifySidebar();
        if (sidebar.hasClass("affix")) {
            sidebar.removeClass("affix");
            if (document.documentElement.scrollTop == 0) {
                // 当在显示状态下移动到fixed 状态时隐藏, 再拖动页面到顶, 再显示, 就会出现没有触发affix-top 事件的情况
                sidebar.addClass("affix-top");
            } else {
                sidebar.addClass("affix");
            }
        }
    });
}