(function () {
    function cls(el) {
        return el && el.className ? (" " + String(el.className) + " ") : " ";
    }

    function isTabEl(el) {
        if (!el || !el.className) {
            return false;
        }
        var c = cls(el);
        if (c.indexOf(" z-tabs") >= 0 || c.indexOf(" z-tabbox") >= 0 || c.indexOf(" z-tabpanel") >= 0) {
            return false;
        }
        if (c.indexOf(" z-tab-h") >= 0 || c.indexOf(" z-tab-text") >= 0 || c.indexOf(" z-tab-content") >= 0) {
            return false;
        }
        return c.indexOf(" z-tab ") >= 0 || c.indexOf(" menutabs ") >= 0;
    }

    function hasSeld(el) {
        var c = String(el.className || "");
        if (c.indexOf("z-tab-seld") >= 0 || c.indexOf("z-tab-selected") >= 0) {
            return true;
        }
        var kids = el.getElementsByTagName("*");
        for (var i = 0; i < kids.length; i++) {
            var kc = String(kids[i].className || "");
            if (kc.indexOf("z-tab-seld") >= 0 || kc.indexOf("z-tab-selected") >= 0) {
                return true;
            }
        }
        if (window.zk && zk.Widget) {
            try {
                var w = zk.Widget.$(el);
                if (w && typeof w.isSelected === "function" && w.isSelected()) {
                    return true;
                }
            } catch (ignore) {
            }
        }
        return false;
    }

    function paintOn(el) {
        if (String(el.className).indexOf("cat-tab-on") < 0) {
            el.className += " cat-tab-on";
        }
        el.style.setProperty("border", "2px solid #122536", "important");
        el.style.setProperty("background", "#e8eef4", "important");
        el.style.setProperty("background-color", "#e8eef4", "important");
        el.style.setProperty("background-image", "none", "important");
        el.style.setProperty("border-radius", "6px", "important");
        el.style.setProperty("color", "#122536", "important");
        var texts = el.getElementsByTagName("*");
        for (var i = 0; i < texts.length; i++) {
            var t = texts[i];
            var tc = String(t.className || "");
            if (tc.indexOf("z-tab-text") >= 0 || t.tagName === "SPAN" || t.tagName === "A") {
                t.style.setProperty("color", "#122536", "important");
                t.style.setProperty("font-weight", "800", "important");
                t.style.setProperty("font-size", "14px", "important");
            }
        }
    }

    function paintOff(el) {
        el.className = String(el.className).replace(/\s*cat-tab-on/g, "");
        el.style.removeProperty("border");
        el.style.removeProperty("background");
        el.style.removeProperty("background-color");
        el.style.removeProperty("background-image");
        el.style.removeProperty("border-radius");
        el.style.removeProperty("color");
        var texts = el.getElementsByTagName("*");
        for (var i = 0; i < texts.length; i++) {
            texts[i].style.removeProperty("color");
            texts[i].style.removeProperty("font-weight");
            texts[i].style.removeProperty("font-size");
        }
    }

    window.markCatTabs = function () {
        var roots = document.querySelectorAll(".cat-admin");
        for (var r = 0; r < roots.length; r++) {
            var admin = roots[r];
            var header = admin.querySelector(".z-tabs-header") || admin.querySelector(".z-tabs");
            if (!header) {
                continue;
            }
            var nodes = header.getElementsByTagName("*");
            var tabs = [];
            for (var i = 0; i < nodes.length; i++) {
                if (isTabEl(nodes[i])) {
                    tabs.push(nodes[i]);
                }
            }
            var selectedNode = null;
            if (window.zk && zk.Widget) {
                try {
                    var boxEl = admin.querySelector(".cat-nav");
                    var box = boxEl ? zk.Widget.$(boxEl) : null;
                    if (box && typeof box.getSelectedTab === "function") {
                        var selTab = box.getSelectedTab();
                        if (selTab && typeof selTab.$n === "function") {
                            selectedNode = selTab.$n();
                        }
                    }
                } catch (ignore) {
                }
            }
            for (var j = 0; j < tabs.length; j++) {
                var on = false;
                if (selectedNode && (tabs[j] === selectedNode || tabs[j].contains(selectedNode) || selectedNode.contains(tabs[j]))) {
                    on = true;
                } else if (!selectedNode && hasSeld(tabs[j])) {
                    on = true;
                }
                if (on) {
                    paintOn(tabs[j]);
                } else {
                    paintOff(tabs[j]);
                }
            }
        }
    };

    function bind() {
        window.markCatTabs();
        if (document.body && !document.body.getAttribute("data-cat-tabs")) {
            document.body.setAttribute("data-cat-tabs", "1");
            document.body.addEventListener("click", function (e) {
                var n = e.target;
                while (n && n !== document.body) {
                    var c = String(n.className || "");
                    if (c.indexOf("z-tab") >= 0 || c.indexOf("menutabs") >= 0) {
                        setTimeout(window.markCatTabs, 30);
                        setTimeout(window.markCatTabs, 160);
                        break;
                    }
                    n = n.parentNode;
                }
            }, true);
        }
    }

    if (window.zk && zk.afterMount) {
        zk.afterMount(bind);
    } else if (window.jq) {
        jq(bind);
    } else if (document.addEventListener) {
        document.addEventListener("DOMContentLoaded", bind);
    }
    setTimeout(bind, 200);
    setTimeout(bind, 800);
})();
