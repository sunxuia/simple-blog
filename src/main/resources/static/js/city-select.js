function selectArea(input, container, option) {
    if (container.html()) {
        container.empty();
        return;
    }
    option = Object.assign({
        returnBtn: true,
        thisBtn: true
    }, option);
    var box = $(
        "<div class='areas'>" +
        "   <h4 class='area-title'>选择城市</h4>" +
        "   <div class='area-selections'></div>" +
        "</div>");
    container.append(box);

    var res = [chinaArea];
    if (!$.isEmpty(input.val())) {
        var curValue = input.val().split("/ ");
        for (var i = 0; i < curValue.length; i++) {
            var parent = res[res.length - 1];
            for (var j = 0; j < parent.children.length; j++) {
                if (parent.children[j].name === curValue[i]) {
                    res.push(parent.children[j]);
                    break;
                }
            }
        }
        res.pop();
    }
    setArea();

    function setArea() {
        var btnContainer = box.find(".area-selections");
        btnContainer.empty();
        var parent = res[res.length - 1];

        if (res.length > 1) {
            if (option.returnBtn) {
                var btn = $("<button class='btn-return'>(返回)</button>");
                btn.on("click", function () {
                    res.pop();
                    setArea();
                });
                btnContainer.append(btn);
            }

            if (option.thisBtn) {
                var btn = $("<button class='btn-area' />");
                btn.text(parent.name);
                btn.on("click", function () {
                    setSelected();
                });
                btnContainer.append(btn);
            }
        }

        parent.children.forEach(function (area) {
            var btn = $("<button class='btn-area' />");
            btn.text(area.name);
            btn.on("click", function () {
                if (area.children) {
                    res.push(area);
                    setArea();
                } else {
                    res.push(area);
                    setSelected();
                }
            });
            btnContainer.append(btn);
        });
    };

    function setSelected() {
        var text = "";
        for (var i = 1; i < res.length; i++) {
            text += "/ " + res[i].name;
        }
        text = text.substring(2, text.length);
        input.val(text);
        container.empty();
    }
}