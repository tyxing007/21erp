!function (t) {
    t.fn.combo = function (i) {
        if (0 == this.length)return this;
        var e, s = arguments;
        this.each(function () {
            var a = t(this).data("_combo");
            if ("string" == typeof i) {
                if (!a)return;
                if ("function" == typeof a[i]) {
                    s = Array.prototype.slice.call(s, 1);
                    e = a[i].apply(a, s)
                }
            } else if (!a) {
                a = new t.Combo(t(this), i);
                t(this).data("_combo", a)
            }
        });
        return void 0 === e ? this : e
    };
    t.fn.getCombo = function () {
        return t.Combo.getCombo(this)
    };
    t.Combo = function (i, e) {
        this.obj = i;
        this.opts = t.extend(!0, {}, t.Combo.defaults, e);
        this.dataOpt = this.opts.data;
        this._selectedIndex = -1;
        this._disabled = "undefined" != typeof this.opts.disabled ? !!this.opts.disabled : !!this.obj.attr("disabled");
        t.extend(this, this.opts.callback);
        this._init()
    };
    t.Combo.getCombo = function (i) {
        i = t(i);
        if (0 != i.length) {
            if (1 == i.length)return i.data("_combo");
            if (i.length > 1) {
                var e = [];
                i.each(function () {
                    e.push(t(this).data("_combo"))
                });
                return e
            }
        }
    };
    t.Combo.prototype = {
        constructor: t.Combo, _init: function () {
            var t = this.opts;
            if ("select" == this.obj[0].tagName.toLowerCase()) {
                this.originSelect = this.obj;
                this.dataOpt = this._getDataFromSelect()
            }
            this._createCombo();
            this.loadData(this.dataOpt, t.defaultSelected, t.defaultFlag);
            this._handleDisabled(this._disabled);
            this._bindEvent()
        }, loadData: function (t, i, e) {
            this.xhr && this.xhr.abort();
            this.empty(!1);
            this.dataOpt = t;
            this.mode = this._getRenderMode();
            if (this.mode)if ("local" == this.mode) {
                this._formatData();
                this._populateList(this.formattedData);
                this._setDefaultSelected(i, e)
            } else"remote" == this.mode && this._loadAjaxData(i, e)
        }, activate: function () {
            this.focus || this.input.focus();
            this.wrap.addClass(this.opts.activeCls);
            this.active = !0
        }, _blur: function () {
            if (this.active) {
                this.collapse();
                if (this.opts.editable && this.opts.forceSelection) {
                    this.selectByText(this.input.val());
                    -1 == this._selectedIndex && this.input.val("")
                }
                this.wrap.removeClass(this.opts.activeCls);
                this.active = !1;
                "function" == typeof this.onBlur && this.onBlur()
            }
        }, blur: function () {
            this.focus && this.input.blur();
            this._blur()
        }, _bindEvent: function () {
            var i = this, e = this.opts, s = "." + e.listItemCls;
            i.list.on("click", s, function () {
                t(this).hasClass(e.selectedCls) || i.selectByItem(t(this));
                i.collapse();
                i.input.focus();
                "function" == typeof i.onListClick && i.onListClick()
            }).on("mouseover", s, function () {
                t(this).addClass(e.hoverCls).siblings().removeClass(e.hoverCls)
            }).on("mouseleave", s, function () {
                t(this).removeClass(e.hoverCls)
            });
            i.input.on("focus", function () {
                i.wrap.addClass(e.activeCls);
                i.focus = !0;
                i.active = !0
            }).on("blur", function () {
                i.focus = !1
            });
            e.editable ? i.input.on("click", function () {
            }) : i.input.on("click", function () {
                i._onTriggerClick()
            });
            i.trigger && i.trigger.on("click", function () {
                i._onTriggerClick()
            });
            t(document).on("click", function (e) {
                var s = e.target || e.srcElement;
                0 == t(s).closest(i.wrap).length && 0 == t(s).closest(i.listWrap).length && i.blur()
            });
            this.listWrap.on("click", function (t) {
                t.stopPropagation()
            });
            t(window).on("resize", function () {
                i._setListPosition()
            });
            this._bindKeyEvent()
        }, _bindKeyEvent: function () {
            var i = this, e = this.opts, s = {
                backSpace: 8,
                esc: 27,
                f7: 118,
                up: 38,
                down: 40,
                tab: 9,
                enter: 13,
                home: 36,
                end: 35,
                pageUp: 33,
                pageDown: 34,
                space: 32
            };
            this.input.on("keydown", function (t) {
                switch (t.keyCode) {
                    case s.tab:
                        i._blur();
                        break;
                    case s.down:
                    case s.up:
                        if (i.isExpanded) {
                            var a = t.keyCode == s.down ? "next" : "prev";
                            i._setItemFocus(a)
                        } else i._onTriggerClick();
                        t.preventDefault();
                        break;
                    case s.enter:
                        if (i.isExpanded) {
                            var o = i.list.find("." + e.hoverCls);
                            o.length > 0 && i.selectByItem(o);
                            i.collapse()
                        }
                        break;
                    case s.home:
                    case s.end:
                        if (i.isExpanded) {
                            var o = t.keyCode == s.home ? i.list.find("." + e.listItemCls).eq(0) : i.list.find("." + e.listItemCls).filter(":last");
                            i._scrollToItem(o);
                            t.preventDefault()
                        }
                        break;
                    case s.pageUp:
                    case s.pageDown:
                        if (i.isExpanded) {
                            var a = t.keyCode == s.pageUp ? "up" : "down";
                            i._scrollPage(a);
                            t.preventDefault()
                        }
                }
            }).on("keyup", function (t) {
                if (e.editable) {
                    var a = t.which, o = 8 == a || 9 == a || 13 == a || 27 == a || a >= 16 && 20 >= a || a >= 33 && 40 >= a || a >= 44 && 46 >= a || a >= 112 && 123 >= a || 144 == a || 145 == a, l = i.input.val();
                    o && a != s.backSpace || i.doDelayQuery(l)
                }
            });
            t(document).on("keydown", function (t) {
                t.keyCode == s.esc && i.collapse()
            })
        }, distory: function () {
        }, enable: function () {
            this._handleDisabled(!1)
        }, disable: function (t) {
            t = "undefined" == typeof t ? !0 : !!t;
            this._handleDisabled(t)
        }, _handleDisabled: function (t) {
            var i = this.opts;
            this._disabled = t;
            1 == t ? this.wrap.addClass(i.disabledCls) : this.wrap.removeClass(i.disabledCls);
            this.input.attr("disabled", t)
        }, _createCombo: function () {
            var i, e, s, a = this.opts, o = parseInt(this.opts.width);
            this.originSelect && this.originSelect.hide();
            if ("input" == this.obj[0].tagName.toLowerCase())this.input = this.obj; else {
                e = this.obj.find("." + a.inputCls);
                this.input = e.length > 0 ? e : t('<input type="text" class="' + a.inputCls + '"/>')
            }
            this.input.attr({autocomplete: "off", readOnly: !a.editable}).css({cursor: a.editable ? "" : "default"});
            s = t(this.obj).find("." + a.triggerCls);
            s.length > 0 ? this.trigger = s : a.trigger !== !1 && (this.trigger = t('<span class="' + a.triggerCls + '"></span>'));
            i = this.obj.hasClass(a.wrapCls) ? this.obj : this.obj.find("." + a.wrapCls);
            if (i.length > 0)this.wrap = i.append(this.input, this.trigger); else if (this.trigger) {
                this.wrap = t('<span class="' + a.wrapCls + '"></span>').append(this.input, this.trigger);
                this.originSelect && this.obj[0] == this.originSelect[0] || this.obj[0] == this.input[0] ? this.obj.next().length > 0 ? this.wrap.insertBefore(this.obj.next()) : this.wrap.appendTo(this.obj.parent()) : this.wrap.appendTo(this.obj)
            }
            this.wrap && a.id && this.wrap.attr("id", a.id);
            this.wrap || (this.wrap = this.input);
            this._setComboLayout(o);
            this.list = t("<div />").addClass(a.listCls).css({position: "relative", overflow: "auto"});
            this.listWrap = t("<div />").addClass(a.listWrapCls).attr("id", a.listId).hide().append(this.list).css({
                position: "absolute",
                top: 0,
                zIndex: a.zIndex
            });
            a.extraListHtml && t("<div />").addClass(a.extraListHtmlCls).append(a.extraListHtml).appendTo(this.listWrap);
            if (a.listRenderToBody) {
                t.Combo.allListWrap || (t.Combo.allListWrap = t('<div id="COMBO_WRAP"/>').appendTo("body"));
                this.listWrap.appendTo(t.Combo.allListWrap)
            } else this.wrap.after(this.listWrap)
        }, _setListLayout: function () {
            var t, i, e = this.opts, s = parseInt(e.listHeight), a = 0, o = this.trigger ? this.trigger.outerWidth() : 0, l = parseInt(e.minListWidth), n = parseInt(e.maxListWidth);
            this.listWrap.width("auto");
            this.list.height("auto");
            this.listWrap.show();
            this.isExpanded = !0;
            i = this.list.height();
            if (!isNaN(s) && s >= 0) {
                s = Math.min(s, i);
                this.list.height(s)
            }
            if ("auto" == e.listWidth || "auto" == e.width) {
                t = this.listWrap.outerWidth();
                if (i < this.list.height()) {
                    a = 20;
                    t += a
                }
            } else {
                t = parseInt(e.listWidth);
                isNaN(t) ? t = this.wrap.outerWidth() : null
            }
            if ("auto" == e.width) {
                var r = this.listWrap.outerWidth() + Math.max(o, a);
                this._setComboLayout(r)
            }
            l = isNaN(l) ? this.wrap.outerWidth() : Math.max(l, this.wrap.outerWidth());
            !isNaN(l) && l > t && (t = l);
            !isNaN(n) && t > n && (t = n);
            t -= this.listWrap.outerWidth() - this.listWrap.width();
            this.listWrap.width(t);
            this.listWrap.hide();
            this.isExpanded = !1
        }, _setComboLayout: function (t) {
            if (t) {
                var i = this.opts, e = parseInt(i.maxWidth), s = parseInt(i.minWidth);
                !isNaN(e) && t > e && (t = e);
                !isNaN(s) && s > t && (t = s);
                var a;
                t -= this.wrap.outerWidth() - this.wrap.width();
                this.wrap.width(t);
                if (this.wrap[0] != this.input[0]) {
                    a = t - (this.trigger ? this.trigger.outerWidth() : 0) - (this.input.outerWidth() - this.input.width());
                    this.input.width(a)
                }
            }
        }, _setListPosition: function () {
            if (this.isExpanded) {
                var i, e, s = (this.opts, t(window)), a = this.wrap.offset().top, o = this.wrap.offset().left, l = s.height(), n = s.width(), r = s.scrollTop(), h = s.scrollLeft(), d = this.wrap.outerHeight(), c = this.wrap.outerWidth(), u = this.listWrap.outerHeight(), p = this.listWrap.outerWidth(), f = parseInt(this.listWrap.css("border-top-width"));
                i = a - r + d + u > l && a > u ? a - u + f : a + d - f;
                e = o - h + p > n ? o + c - p : o;
                this.listWrap.css({top: i, left: e})
            }
        }, _getRenderMode: function () {
            var i, e = this.dataOpt;
            t.isFunction(e) && (e = e());
            if (t.isArray(e)) {
                this.rawData = e;
                i = "local"
            } else if ("string" == typeof e) {
                this.url = e;
                i = "remote"
            }
            return i
        }, _loadAjaxData: function (i, e, s) {
            var a = this, o = a.opts, l = o.ajaxOptions, n = t("<div />").addClass(o.loadingCls).text(l.loadingText);
            a.list.append(n);
            a.list.find(o.listTipsCls).remove();
            a._setListLayout();
            a._setListPosition();
            a.xhr = t.ajax({
                url: a.url, type: l.type, dataType: l.dataType, timeout: l.timeout, success: function (o) {
                    n.remove();
                    t.isFunction(l.success) && l.success(o);
                    t.isFunction(l.formatData) && (o = l.formatData(o));
                    if (o) {
                        a.rawData = o;
                        a._formatData();
                        a._populateList(a.formattedData);
                        if ("" === i) {
                            a.lastQuery = s;
                            a.filterData = a.formattedData;
                            a.expand()
                        } else a._setDefaultSelected(i, e);
                        a.xhr = null
                    }
                }, error: function () {
                    n.remove();
                    t("<div />").addClass(o.tipsCls).text(l.errorText).appendTo(a.list);
                    a.xhr = null
                }
            })
        }, getDisabled: function () {
            return this._disabled
        }, getValue: function () {
            return this._selectedIndex > -1 ? this.formattedData[this._selectedIndex].value : this.opts.forceSelection ? "" : this.input.val()
        }, getText: function () {
            return this._selectedIndex > -1 ? this.formattedData[this._selectedIndex].text : this.opts.forceSelection ? "" : this.input.val()
        }, getSelectedIndex: function () {
            return this._selectedIndex
        }, getSelectedRow: function () {
            return this._selectedIndex > -1 ? this.rawData[this._selectedIndex] : void 0
        }, getDataRow: function () {
            return this._selectedIndex > -1 ? this.rawData[this._selectedIndex] : void 0
        }, getAllData: function () {
            return this.formattedData
        }, getAllRawData: function () {
            return this.rawData
        }, _setDefaultSelected: function (i, e) {
            var s = this.opts;
            "function" == typeof i && (defaultSelected = defaultSelected.call(this, this.rawData));
            if (isNaN(parseInt(i)))if (t.isArray(i))this.selectByKey(i[0], i[1], e); else if (this.originSelect) {
                var a = this.originSelect[0].selectedIndex;
                this._setSelected(a, e)
            } else s.autoSelect && this._setSelected(0, e); else {
                var a = parseInt(i);
                this._setSelected(a, e)
            }
        }, selectByIndex: function (t, i) {
            this._setSelected(t, i)
        }, selectByText: function (t, i) {
            if (this.formattedData) {
                for (var e = this.formattedData, s = -1, a = 0, o = e.length; o > a; a++)if (e[a].text === t) {
                    s = a;
                    break
                }
                this._setSelected(s, i)
            }
        }, selectByValue: function (t, i) {
            if (this.formattedData) {
                for (var e = this.formattedData, s = -1, a = 0, o = e.length; o > a; a++)if (e[a].value === t) {
                    s = a;
                    break
                }
                this._setSelected(s, i)
            }
        }, selectByKey: function (t, i, e) {
            if (this.rawData) {
                var s = this, a = s.opts, o = this.rawData, l = -1;
                if (a.addOptions) {
                    o = this.formattedData;
                    for (var n = 0, r = o.length; r > n; n++)if (o[n].value === i) {
                        l = n;
                        break
                    }
                } else for (var n = 0, r = o.length; r > n; n++)if (o[n][t] === i) {
                    l = n;
                    break
                }
                this._setSelected(l, e)
            }
        }, selectByItem: function (t, i) {
            if (t && t.parent()[0] == this.list[0]) {
                var e = t.text();
                this.selectByText(e, i)
            }
        }, _setSelected: function (t, i) {
            var e = this.opts, t = parseInt(t), i = "undefined" != typeof i ? !!i : !0;
            if (!isNaN(t))if (this.formattedData && 0 != this.formattedData.length) {
                var s = this.formattedData.length;
                (-1 > t || t >= s) && (t = -1);
                if (this._selectedIndex != t) {
                    {
                        var a = -1 == t ? null : this.formattedData[t], o = -1 == t ? null : a.rawData, l = -1 == t ? "" : a.text;
                        this.list.find("." + e.listItemCls)
                    }
                    if (!i || "function" != typeof this.beforeChange || this.beforeChange(o)) {
                        e.editable && -1 == t && this.focus || this.input.val(l);
                        this._selectedIndex = t;
                        i && "function" == typeof this.onChange && this.onChange(o);
                        this.originSelect && (this.originSelect[0].selectedIndex = t)
                    }
                }
            } else this._selectedIndex = -1
        }, removeSelected: function (t) {
            this.input.val("");
            this._setSelected(-1, t)
        }, _triggerCallback: function () {
        }, _getDataFromSelect: function () {
            var i = this.opts, e = [];
            t.each(this.originSelect.find("option"), function () {
                var s = t(this), a = {};
                a[i.text] = s.text();
                a[i.value] = s.attr("value");
                e.push(a)
            });
            return e
        }, _formatData: function () {
            if (t.isArray(this.rawData)) {
                var i = this, e = i.opts;
                i.formattedData = [];
                e.emptyOptions && i.formattedData.push({text: "(空)", value: 0});
                e.addOptions && i.formattedData.push(e.addOptions);
                t.each(this.rawData, function (s, a) {
                    var o = {};
                    o.text = t.isFunction(e.formatText) ? e.formatText(a) : a[e.text];
                    o.value = t.isFunction(e.formatValue) ? e.formatValue(a) : a[e.value];
                    o.rawData = a;
                    i.formattedData.push(o)
                })
            }
        }, _filter: function (i) {
            i = "undefined" == typeof i ? "" : i;
            this.input.val() != this.getText() && this.selectByText(this.input.val());
            {
                var e = this.opts, s = this;
                e.maxFilter
            }
            if (!this.opts.cache) {
                "local" == this.mode && t.isFunction(this.dataOpt) && (this.rawData = this.dataOpt());
                this._formatData()
            }
            if (t.isArray(this.formattedData)) {
                if ("" == i)this.filterData = this.formattedData; else {
                    this.filterData = [];
                    t.each(s.formattedData, function (a, o) {
                        var l = o.text;
                        if (t.isFunction(e.customMatch)) {
                            if (!e.customMatch(l, i))return
                        } else {
                            var n = e.caseSensitive ? "" : "i", r = new RegExp(i.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&"), n);
                            if (-1 == l.search(r))return
                        }
                        s.filterData.push(o);
                        return s.filterData.length == e.maxFilter ? !1 : void 0
                    })
                }
                this.lastQuery = i;
                this.list.empty();
                this._populateList(this.filterData);
                this.expand()
            }
        }, doDelayQuery: function (t) {
            var i = this, e = i.opts, s = parseInt(e.queryDelay);
            isNaN(s) && (s = 0);
            i.queryDelay && window.clearTimeout(i.queryDelay);
            i.queryDelay = window.setTimeout(function () {
                i.doQuery(t)
            }, s)
        }, doQuery: function (t) {
            "local" == this.mode || "remote" == this.mode && this.opts.loadOnce ? this._filter(t) : this._loadAjaxData("", !1, t)
        }, _populateList: function (i) {
            if (i) {
                var e = this, s = e.opts;
                if (0 == i.length) {
                    if (s.forceSelection) {
                        t("<div />").addClass(s.tipsCls).html(s.noDataText).appendTo(e.list);
                        this._setListLayout()
                    }
                } else {
                    for (var a = 0, o = i.length; o > a; a++) {
                        var l = i[a], n = l.text, r = l.value;
                        t("<div />").attr({
                            "class": s.listItemCls + (a == this._selectedIndex ? " " + s.selectedCls : ""),
                            "data-value": r
                        }).text(n).appendTo(e.list)
                    }
                    this._setListLayout()
                }
            }
        }, expand: function () {
            var i = this.opts;
            if (this.active && !this.isExpanded && (0 != this.filterData.length || i.noDataText || i.extraListHtmlCls)) {
                this.isExpanded = !0;
                this.listWrap.show();
                this._setListPosition();
                t.isFunction(this.onExpand) && this.onExpand();
                var e = this.list.find("." + i.listItemCls);
                if (0 != e.length) {
                    var s = e.filter("." + i.selectedCls);
                    0 == s.length && (s = e.eq(0).addClass(i.hoverCls));
                    this._scrollToItem(s)
                }
            } else this.listWrap.hide()
        }, collapse: function () {
            if (this.isExpanded) {
                var i = this.opts;
                this.listWrap.hide();
                this.isExpanded = !1;
                this.listItems && this.listItems.removeClass(i.hoverCls);
                t.isFunction(this.onCollapse) && this.onCollapse()
            }
        }, _onTriggerClick: function () {
            if (!this._disabled) {
                this.active = !0;
                this.input.focus();
                this.isExpanded ? this.collapse() : this._filter()
            }
        }, _scrollToItem: function (t) {
            if (t && 0 != t.length) {
                var i = this.list.scrollTop(), e = i + t.position().top, s = i + this.list.height(), a = e + t.outerHeight();
                (i > e || a > s) && this.list.scrollTop(e)
            }
        }, _scrollPage: function (t) {
            var i, e = this.list.scrollTop(), s = this.list.height();
            "up" == t ? i = e - s : "down" == t && (i = e + s);
            this.list.scrollTop(i)
        }, _setItemFocus: function (t) {
            var i, e, s = this.opts, a = this.list.find("." + s.listItemCls);
            if (0 != a.length) {
                var o = a.filter("." + s.hoverCls).eq(0);
                0 == o.length && (o = a.filter("." + s.selectedCls).eq(0));
                if (0 == o.length)i = 0; else {
                    i = a.index(o);
                    i = "next" == t ? i == a.length - 1 ? 0 : i + 1 : 0 == i ? a.length - 1 : i - 1
                }
                e = a.eq(i);
                a.removeClass(s.hoverCls);
                e.addClass(s.hoverCls);
                this._scrollToItem(e)
            }
        }, empty: function () {
            this._setSelected(-1, !1);
            this.input.val("");
            this.list.empty();
            this.rawData = null;
            this.formattedData = null
        }, setEdit: function () {
        }
    };
    t.Combo.defaults = {
        data: null,
        text: "text",
        value: "value",
        formatText: null,
        formatValue: null,
        defaultSelected: void 0,
        defaultFlag: !0,
        autoSelect: !0,
        disabled: void 0,
        editable: !1,
        caseSensitive: !1,
        forceSelection: !0,
        cache: !0,
        queryDelay: 100,
        maxFilter: 10,
        minChars: 0,
        customMatch: null,
        noDataText: "没有匹配的选项",
        width: void 0,
        minWidth: void 0,
        maxWidth: void 0,
        listWidth: void 0,
        listHeight: 150,
        maxListWidth: void 0,
        maxListWidth: void 0,
        zIndex: 1e3,
        listRenderToBody: !0,
        extraListHtml: void 0,
        ajaxOptions: {
            type: "post",
            dataType: "json",
            queryParam: "query",
            timeout: 1e4,
            formatData: null,
            loadingText: "Loading...",
            success: null,
            error: null,
            errorText: "数据加载失败"
        },
        loadOnce: !0,
        id: void 0,
        listId: void 0,
        wrapCls: "ui-combo-wrap",
        focusCls: "ui-combo-focus",
        disabledCls: "ui-combo-disabled",
        activeCls: "ui-combo-active",
        inputCls: "input-txt",
        triggerCls: "trigger",
        listWrapCls: "ui-droplist-wrap",
        listCls: "droplist",
        listItemCls: "list-item",
        selectedCls: "selected",
        hoverCls: "on",
        loadingCls: "loading",
        tipsCls: "tips",
        extraListHtmlCls: "extra-list-ctn",
        callback: {onFocus: null, onBlur: null, beforeChange: null, onChange: null, onExpand: null, onCollapse: null}
    }
}(jQuery);
!function (t) {
    t.extend(t.fn, {
        validate: function (e) {
            if (!this.length)return void(e && e.debug && window.console && console.warn("Nothing selected, can't validate, returning nothing."));
            var i = t.data(this[0], "validator");
            return i ? i : ("undefined" != typeof Worker && this.attr("novalidate", "novalidate"), i = new t.validator(e, this[0]), t.data(this[0], "validator", i), i.settings.onsubmit && (this.validateDelegate(":submit", "click", function (e) {
                i.settings.submitHandler && (i.submitButton = e.target), t(e.target).hasClass("cancel") && (i.cancelSubmit = !0), void 0 !== t(e.target).attr("formnovalidate") && (i.cancelSubmit = !0)
            }), this.submit(function (e) {
                function s() {
                    var s;
                    return i.settings.submitHandler ? (i.submitButton && (s = t("<input type='hidden'/>").attr("name", i.submitButton.name).val(t(i.submitButton).val()).appendTo(i.currentForm)), i.settings.submitHandler.call(i, i.currentForm, e), i.submitButton && s.remove(), !1) : !0
                }

                return i.settings.debug && e.preventDefault(), i.cancelSubmit ? (i.cancelSubmit = !1, s()) : i.form() ? i.pendingRequest ? (i.formSubmitted = !0, !1) : s() : (i.focusInvalid(), !1)
            })), i)
        }, valid: function () {
            if (t(this[0]).is("form"))return this.validate().form();
            var e = !0, i = t(this[0].form).validate();
            return this.each(function () {
                e = e && i.element(this)
            }), e
        }, removeAttrs: function (e) {
            var i = {}, s = this;
            return t.each(e.split(/\s/), function (t, e) {
                i[e] = s.attr(e), s.removeAttr(e)
            }), i
        }, rules: function (e, i) {
            var s = this[0];
            if (e) {
                var a = t.data(s.form, "validator").settings, n = a.rules, r = t.validator.staticRules(s);
                switch (e) {
                    case"add":
                        t.extend(r, t.validator.normalizeRule(i)), delete r.messages, n[s.name] = r, i.messages && (a.messages[s.name] = t.extend(a.messages[s.name], i.messages));
                        break;
                    case"remove":
                        if (!i)return delete n[s.name], r;
                        var o = {};
                        return t.each(i.split(/\s/), function (t, e) {
                            o[e] = r[e], delete r[e]
                        }), o
                }
            }
            var l = t.validator.normalizeRules(t.extend({}, t.validator.classRules(s), t.validator.attributeRules(s), t.validator.dataRules(s), t.validator.staticRules(s)), s);
            if (l.required) {
                var h = l.required;
                delete l.required, l = t.extend({required: h}, l)
            }
            return l
        }
    }), t.extend(t.expr[":"], {
        blank: function (e) {
            return !t.trim("" + t(e).val())
        }, filled: function (e) {
            return !!t.trim("" + t(e).val())
        }, unchecked: function (e) {
            return !t(e).prop("checked")
        }
    }), t.validator = function (e, i) {
        this.settings = t.extend(!0, {}, t.validator.defaults, e), this.currentForm = i, this.init()
    }, t.validator.format = function (e, i) {
        return 1 === arguments.length ? function () {
            var i = t.makeArray(arguments);
            return i.unshift(e), t.validator.format.apply(this, i)
        } : (arguments.length > 2 && i.constructor !== Array && (i = t.makeArray(arguments).slice(1)), i.constructor !== Array && (i = [i]), t.each(i, function (t, i) {
            e = e.replace(RegExp("\\{" + t + "\\}", "g"), function () {
                return i
            })
        }), e)
    }, t.extend(t.validator, {
        defaults: {
            messages: {},
            groups: {},
            rules: {},
            errorClass: "error",
            validClass: "valid",
            errorElement: "label",
            focusInvalid: !0,
            errorContainer: t([]),
            errorLabelContainer: t([]),
            onsubmit: !0,
            ignore: ":hidden",
            ignoreTitle: !1,
            onfocusin: function (t) {
                this.lastActive = t, this.settings.focusCleanup && !this.blockFocusCleanup && (this.settings.unhighlight && this.settings.unhighlight.call(this, t, this.settings.errorClass, this.settings.validClass), this.addWrapper(this.errorsFor(t)).hide())
            },
            onfocusout: function (t) {
                this.checkable(t) || !(t.name in this.submitted) && this.optional(t) || this.element(t)
            },
            onkeyup: function (t, e) {
                (9 !== e.which || "" !== this.elementValue(t)) && (t.name in this.submitted || t === this.lastElement) && this.element(t)
            },
            onclick: function (t) {
                t.name in this.submitted ? this.element(t) : t.parentNode.name in this.submitted && this.element(t.parentNode)
            },
            highlight: function (e, i, s) {
                "radio" === e.type ? this.findByName(e.name).addClass(i).removeClass(s) : t(e).addClass(i).removeClass(s)
            },
            unhighlight: function (e, i, s) {
                "radio" === e.type ? this.findByName(e.name).removeClass(i).addClass(s) : t(e).removeClass(i).addClass(s)
            }
        },
        setDefaults: function (e) {
            t.extend(t.validator.defaults, e)
        },
        messages: {
            required: "This field is required.",
            remote: "Please fix this field.",
            email: "Please enter a valid email address.",
            url: "Please enter a valid URL.",
            date: "Please enter a valid date.",
            dateISO: "Please enter a valid date (ISO).",
            number: "Please enter a valid number.",
            digits: "Please enter only digits.",
            creditcard: "Please enter a valid credit card number.",
            equalTo: "Please enter the same value again.",
            maxlength: t.validator.format("Please enter no more than {0} characters."),
            minlength: t.validator.format("Please enter at least {0} characters."),
            rangelength: t.validator.format("Please enter a value between {0} and {1} characters long."),
            range: t.validator.format("Please enter a value between {0} and {1}."),
            max: t.validator.format("Please enter a value less than or equal to {0}."),
            min: t.validator.format("Please enter a value greater than or equal to {0}.")
        },
        autoCreateRanges: !1,
        prototype: {
            init: function () {
                function e(e) {
                    var i = t.data(this[0].form, "validator"), s = "on" + e.type.replace(/^validate/, "");
                    i.settings[s] && i.settings[s].call(i, this[0], e)
                }

                this.labelContainer = t(this.settings.errorLabelContainer), this.errorContext = this.labelContainer.length && this.labelContainer || t(this.currentForm), this.containers = t(this.settings.errorContainer).add(this.settings.errorLabelContainer), this.submitted = {}, this.valueCache = {}, this.pendingRequest = 0, this.pending = {}, this.invalid = {}, this.reset();
                var i = this.groups = {};
                t.each(this.settings.groups, function (e, s) {
                    "string" == typeof s && (s = s.split(/\s/)), t.each(s, function (t, s) {
                        i[s] = e
                    })
                });
                var s = this.settings.rules;
                t.each(s, function (e, i) {
                    s[e] = t.validator.normalizeRule(i)
                }), t(this.currentForm).validateDelegate(":text, [type='password'], [type='file'], select, textarea, [type='number'], [type='search'] ,[type='tel'], [type='url'], [type='email'], [type='datetime'], [type='date'], [type='month'], [type='week'], [type='time'], [type='datetime-local'], [type='range'], [type='color'] ", "focusin focusout keyup", e).validateDelegate("[type='radio'], [type='checkbox'], select, option", "click", e), this.settings.invalidHandler && t(this.currentForm).bind("invalid-form.validate", this.settings.invalidHandler)
            }, form: function () {
                return this.checkForm(), t.extend(this.submitted, this.errorMap), this.invalid = t.extend({}, this.errorMap), this.valid() || t(this.currentForm).triggerHandler("invalid-form", [this]), this.showErrors(), this.valid()
            }, checkForm: function () {
                this.prepareForm();
                for (var t = 0, e = this.currentElements = this.elements(); e[t]; t++)this.check(e[t]);
                return this.valid()
            }, element: function (e) {
                e = this.validationTargetFor(this.clean(e)), this.lastElement = e, this.prepareElement(e), this.currentElements = t(e);
                var i = this.check(e) !== !1;
                return i ? delete this.invalid[e.name] : this.invalid[e.name] = !0, this.numberOfInvalids() || (this.toHide = this.toHide.add(this.containers)), this.showErrors(), i
            }, showErrors: function (e) {
                if (e) {
                    t.extend(this.errorMap, e), this.errorList = [];
                    for (var i in e)this.errorList.push({message: e[i], element: this.findByName(i)[0]});
                    this.successList = t.grep(this.successList, function (t) {
                        return !(t.name in e)
                    })
                }
                this.settings.showErrors ? this.settings.showErrors.call(this, this.errorMap, this.errorList) : this.defaultShowErrors()
            }, resetForm: function () {
                t.fn.resetForm && t(this.currentForm).resetForm(), this.submitted = {}, this.lastElement = null, this.prepareForm(), this.hideErrors(), this.elements().removeClass(this.settings.errorClass).removeData("previousValue")
            }, numberOfInvalids: function () {
                return this.objectLength(this.invalid)
            }, objectLength: function (t) {
                var e = 0;
                for (var i in t)e++;
                return e
            }, hideErrors: function () {
                this.addWrapper(this.toHide).hide()
            }, valid: function () {
                return 0 === this.size()
            }, size: function () {
                return this.errorList.length
            }, focusInvalid: function () {
                if (this.settings.focusInvalid)try {
                    t(this.findLastActive() || this.errorList.length && this.errorList[0].element || []).filter(":visible").focus().trigger("focusin")
                } catch (e) {
                }
            }, findLastActive: function () {
                var e = this.lastActive;
                return e && 1 === t.grep(this.errorList, function (t) {
                        return t.element.name === e.name
                    }).length && e
            }, elements: function () {
                var e = this, i = {};
                return t(this.currentForm).find("input, select, textarea").not(":submit, :reset, :image, [disabled]").not(this.settings.ignore).filter(function () {
                    return !this.name && e.settings.debug && window.console && console.error("%o has no name assigned", this), this.name in i || !e.objectLength(t(this).rules()) ? !1 : (i[this.name] = !0, !0)
                })
            }, clean: function (e) {
                return t(e)[0]
            }, errors: function () {
                var e = this.settings.errorClass.replace(" ", ".");
                return t(this.settings.errorElement + "." + e, this.errorContext)
            }, reset: function () {
                this.successList = [], this.errorList = [], this.errorMap = {}, this.toShow = t([]), this.toHide = t([]), this.currentElements = t([])
            }, prepareForm: function () {
                this.reset(), this.toHide = this.errors().add(this.containers)
            }, prepareElement: function (t) {
                this.reset(), this.toHide = this.errorsFor(t)
            }, elementValue: function (e) {
                var i = t(e).attr("type"), s = t(e).val();
                return "radio" === i || "checkbox" === i ? t("input[name='" + t(e).attr("name") + "']:checked").val() : "string" == typeof s ? s.replace(/\r/g, "") : s
            }, check: function (e) {
                e = this.validationTargetFor(this.clean(e));
                var i, s = t(e).rules(), a = !1, n = this.elementValue(e);
                for (var r in s) {
                    var o = {method: r, parameters: s[r]};
                    try {
                        if (i = t.validator.methods[r].call(this, n, e, o.parameters), "dependency-mismatch" === i) {
                            a = !0;
                            continue
                        }
                        if (a = !1, "pending" === i)return void(this.toHide = this.toHide.not(this.errorsFor(e)));
                        if (!i)return this.formatAndAdd(e, o), !1
                    } catch (l) {
                        throw this.settings.debug && window.console && console.log("Exception occurred when checking element " + e.id + ", check the '" + o.method + "' method.", l), l
                    }
                }
                return a ? void 0 : (this.objectLength(s) && this.successList.push(e), !0)
            }, customDataMessage: function (e, i) {
                return t(e).data("msg-" + i.toLowerCase()) || e.attributes && t(e).attr("data-msg-" + i.toLowerCase())
            }, customMessage: function (t, e) {
                var i = this.settings.messages[t];
                return i && (i.constructor === String ? i : i[e])
            }, findDefined: function () {
                for (var t = 0; arguments.length > t; t++)if (void 0 !== arguments[t])return arguments[t];
                return void 0
            }, defaultMessage: function (e, i) {
                return this.findDefined(this.customMessage(e.name, i), this.customDataMessage(e, i), !this.settings.ignoreTitle && e.title || void 0, t.validator.messages[i], "<strong>Warning: No message defined for " + e.name + "</strong>")
            }, formatAndAdd: function (e, i) {
                var s = this.defaultMessage(e, i.method), a = /\$?\{(\d+)\}/g;
                "function" == typeof s ? s = s.call(this, i.parameters, e) : a.test(s) && (s = t.validator.format(s.replace(a, "{$1}"), i.parameters)), this.errorList.push({
                    message: s,
                    element: e
                }), this.errorMap[e.name] = s, this.submitted[e.name] = s
            }, addWrapper: function (t) {
                return this.settings.wrapper && (t = t.add(t.parent(this.settings.wrapper))), t
            }, defaultShowErrors: function () {
                var t, e;
                for (t = 0; this.errorList[t]; t++) {
                    var i = this.errorList[t];
                    this.settings.highlight && this.settings.highlight.call(this, i.element, this.settings.errorClass, this.settings.validClass), this.showLabel(i.element, i.message)
                }
                if (this.errorList.length && (this.toShow = this.toShow.add(this.containers)), this.settings.success)for (t = 0; this.successList[t]; t++)this.showLabel(this.successList[t]);
                if (this.settings.unhighlight)for (t = 0, e = this.validElements(); e[t]; t++)this.settings.unhighlight.call(this, e[t], this.settings.errorClass, this.settings.validClass);
                this.toHide = this.toHide.not(this.toShow), this.hideErrors(), this.addWrapper(this.toShow).show()
            }, validElements: function () {
                return this.currentElements.not(this.invalidElements())
            }, invalidElements: function () {
                return t(this.errorList).map(function () {
                    return this.element
                })
            }, showLabel: function (e, i) {
                var s = this.errorsFor(e);
                s.length ? (s.removeClass(this.settings.validClass).addClass(this.settings.errorClass), s.html(i)) : (s = t("<" + this.settings.errorElement + ">").attr("for", this.idOrName(e)).addClass(this.settings.errorClass).html(i || ""), this.settings.wrapper && (s = s.hide().show().wrap("<" + this.settings.wrapper + "/>").parent()), this.labelContainer.append(s).length || (this.settings.errorPlacement ? this.settings.errorPlacement(s, t(e)) : s.insertAfter(e))), !i && this.settings.success && (s.text(""), "string" == typeof this.settings.success ? s.addClass(this.settings.success) : this.settings.success(s, e)), this.toShow = this.toShow.add(s)
            }, errorsFor: function (e) {
                var i = this.idOrName(e);
                return this.errors().filter(function () {
                    return t(this).attr("for") === i
                })
            }, idOrName: function (t) {
                return this.groups[t.name] || (this.checkable(t) ? t.name : t.id || t.name)
            }, validationTargetFor: function (t) {
                return this.checkable(t) && (t = this.findByName(t.name).not(this.settings.ignore)[0]), t
            }, checkable: function (t) {
                return /radio|checkbox/i.test(t.type)
            }, findByName: function (e) {
                return t(this.currentForm).find("[name='" + e + "']")
            }, getLength: function (e, i) {
                switch (i.nodeName.toLowerCase()) {
                    case"select":
                        return t("option:selected", i).length;
                    case"input":
                        if (this.checkable(i))return this.findByName(i.name).filter(":checked").length
                }
                return e.length
            }, depend: function (t, e) {
                return this.dependTypes[typeof t] ? this.dependTypes[typeof t](t, e) : !0
            }, dependTypes: {
                "boolean": function (t) {
                    return t
                }, string: function (e, i) {
                    return !!t(e, i.form).length
                }, "function": function (t, e) {
                    return t(e)
                }
            }, optional: function (e) {
                var i = this.elementValue(e);
                return !t.validator.methods.required.call(this, i, e) && "dependency-mismatch"
            }, startRequest: function (t) {
                this.pending[t.name] || (this.pendingRequest++, this.pending[t.name] = !0)
            }, stopRequest: function (e, i) {
                this.pendingRequest--, 0 > this.pendingRequest && (this.pendingRequest = 0), delete this.pending[e.name], i && 0 === this.pendingRequest && this.formSubmitted && this.form() ? (t(this.currentForm).submit(), this.formSubmitted = !1) : !i && 0 === this.pendingRequest && this.formSubmitted && (t(this.currentForm).triggerHandler("invalid-form", [this]), this.formSubmitted = !1)
            }, previousValue: function (e) {
                return t.data(e, "previousValue") || t.data(e, "previousValue", {
                        old: null,
                        valid: !0,
                        message: this.defaultMessage(e, "remote")
                    })
            }
        },
        classRuleSettings: {
            required: {required: !0},
            email: {email: !0},
            url: {url: !0},
            date: {date: !0},
            dateISO: {dateISO: !0},
            number: {number: !0},
            digits: {digits: !0},
            creditcard: {creditcard: !0}
        },
        addClassRules: function (e, i) {
            e.constructor === String ? this.classRuleSettings[e] = i : t.extend(this.classRuleSettings, e)
        },
        classRules: function (e) {
            var i = {}, s = t(e).attr("class");
            return s && t.each(s.split(" "), function () {
                this in t.validator.classRuleSettings && t.extend(i, t.validator.classRuleSettings[this])
            }), i
        },
        attributeRules: function (e) {
            var i = {}, s = t(e), a = s[0].getAttribute("type");
            for (var n in t.validator.methods) {
                var r;
                "required" === n ? (r = s.get(0).getAttribute(n), "" === r && (r = !0), r = !!r) : r = s.attr(n), /min|max/.test(n) && (null === a || /number|range|text/.test(a)) && (r = Number(r)), r ? i[n] = r : a === n && "range" !== a && (i[n] = !0)
            }
            return i.maxlength && /-1|2147483647|524288/.test(i.maxlength) && delete i.maxlength, i
        },
        dataRules: function (e) {
            var i, s, a = {}, n = t(e);
            for (i in t.validator.methods)s = n.data("rule-" + i.toLowerCase()), void 0 !== s && (a[i] = s);
            return a
        },
        staticRules: function (e) {
            var i = {}, s = t.data(e.form, "validator");
            return s.settings.rules && (i = t.validator.normalizeRule(s.settings.rules[e.name]) || {}), i
        },
        normalizeRules: function (e, i) {
            return t.each(e, function (s, a) {
                if (a === !1)return void delete e[s];
                if (a.param || a.depends) {
                    var n = !0;
                    switch (typeof a.depends) {
                        case"string":
                            n = !!t(a.depends, i.form).length;
                            break;
                        case"function":
                            n = a.depends.call(i, i)
                    }
                    n ? e[s] = void 0 !== a.param ? a.param : !0 : delete e[s]
                }
            }), t.each(e, function (s, a) {
                e[s] = t.isFunction(a) ? a(i) : a
            }), t.each(["minlength", "maxlength"], function () {
                e[this] && (e[this] = Number(e[this]))
            }), t.each(["rangelength", "range"], function () {
                var i;
                e[this] && (t.isArray(e[this]) ? e[this] = [Number(e[this][0]), Number(e[this][1])] : "string" == typeof e[this] && (i = e[this].split(/[\s,]+/), e[this] = [Number(i[0]), Number(i[1])]))
            }), t.validator.autoCreateRanges && (e.min && e.max && (e.range = [e.min, e.max], delete e.min, delete e.max), e.minlength && e.maxlength && (e.rangelength = [e.minlength, e.maxlength], delete e.minlength, delete e.maxlength)), e
        },
        normalizeRule: function (e) {
            if ("string" == typeof e) {
                var i = {};
                t.each(e.split(/\s/), function () {
                    i[this] = !0
                }), e = i
            }
            return e
        },
        addMethod: function (e, i, s) {
            t.validator.methods[e] = i, t.validator.messages[e] = void 0 !== s ? s : t.validator.messages[e], 3 > i.length && t.validator.addClassRules(e, t.validator.normalizeRule(e))
        },
        methods: {
            required: function (e, i, s) {
                if (!this.depend(s, i))return "dependency-mismatch";
                if ("select" === i.nodeName.toLowerCase()) {
                    var a = t(i).val();
                    return a && a.length > 0
                }
                return this.checkable(i) ? this.getLength(e, i) > 0 : t.trim(e).length > 0
            }, email: function (t, e) {
                return this.optional(e) || /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(t)
            }, url: function (t, e) {
                return this.optional(e) || /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(t)
            }, date: function (t, e) {
                return this.optional(e) || !/Invalid|NaN/.test("" + new Date(t))
            }, dateISO: function (t, e) {
                return this.optional(e) || /^\d{4}[\/\-]\d{1,2}[\/\-]\d{1,2}$/.test(t)
            }, number: function (t, e) {
                return this.optional(e) || /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(t)
            }, digits: function (t, e) {
                return this.optional(e) || /^\d+$/.test(t)
            }, creditcard: function (t, e) {
                if (this.optional(e))return "dependency-mismatch";
                if (/[^0-9 \-]+/.test(t))return !1;
                var i = 0, s = 0, a = !1;
                t = t.replace(/\D/g, "");
                for (var n = t.length - 1; n >= 0; n--) {
                    var r = t.charAt(n);
                    s = parseInt(r, 10), a && (s *= 2) > 9 && (s -= 9), i += s, a = !a
                }
                return 0 === i % 10
            }, minlength: function (e, i, s) {
                var a = t.isArray(e) ? e.length : this.getLength(t.trim(e), i);
                return this.optional(i) || a >= s
            }, maxlength: function (e, i, s) {
                var a = t.isArray(e) ? e.length : this.getLength(t.trim(e), i);
                return this.optional(i) || s >= a
            }, rangelength: function (e, i, s) {
                var a = t.isArray(e) ? e.length : this.getLength(t.trim(e), i);
                return this.optional(i) || a >= s[0] && s[1] >= a
            }, min: function (t, e, i) {
                return this.optional(e) || t >= i
            }, max: function (t, e, i) {
                return this.optional(e) || i >= t
            }, range: function (t, e, i) {
                return this.optional(e) || t >= i[0] && i[1] >= t
            }, equalTo: function (e, i, s) {
                var a = t(s);
                return this.settings.onfocusout && a.unbind(".validate-equalTo").bind("blur.validate-equalTo", function () {
                    t(i).valid()
                }), e === a.val()
            }, remote: function (e, i, s) {
                if (this.optional(i))return "dependency-mismatch";
                var a = this.previousValue(i);
                if (this.settings.messages[i.name] || (this.settings.messages[i.name] = {}), a.originalMessage = this.settings.messages[i.name].remote, this.settings.messages[i.name].remote = a.message, s = "string" == typeof s && {url: s} || s, a.old === e)return a.valid;
                a.old = e;
                var n = this;
                this.startRequest(i);
                var r = {};
                return r[i.name] = e, t.ajax(t.extend(!0, {
                    url: s,
                    mode: "abort",
                    port: "validate" + i.name,
                    dataType: "json",
                    data: r,
                    success: function (s) {
                        n.settings.messages[i.name].remote = a.originalMessage;
                        var r = s === !0 || "true" === s;
                        if (r) {
                            var o = n.formSubmitted;
                            n.prepareElement(i), n.formSubmitted = o, n.successList.push(i), delete n.invalid[i.name], n.showErrors()
                        } else {
                            var l = {}, h = s || n.defaultMessage(i, "remote");
                            l[i.name] = a.message = t.isFunction(h) ? h(e) : h, n.invalid[i.name] = !0, n.showErrors(l)
                        }
                        a.valid = r, n.stopRequest(i, r)
                    }
                }, s)), "pending"
            }
        }
    }), t.format = t.validator.format
}(jQuery), function (t) {
    var e = {};
    if (t.ajaxPrefilter)t.ajaxPrefilter(function (t, i, s) {
        var a = t.port;
        "abort" === t.mode && (e[a] && e[a].abort(), e[a] = s)
    }); else {
        var i = t.ajax;
        t.ajax = function (s) {
            var a = ("mode"in s ? s : t.ajaxSettings).mode, n = ("port"in s ? s : t.ajaxSettings).port;
            return "abort" === a ? (e[n] && e[n].abort(), e[n] = i.apply(this, arguments), e[n]) : i.apply(this, arguments)
        }
    }
}(jQuery), function (t) {
    t.extend(t.fn, {
        validateDelegate: function (e, i, s) {
            return this.bind(i, function (i) {
                var a = t(i.target);
                return a.is(e) ? s.apply(a, arguments) : void 0
            })
        }
    })
}(jQuery);
!function (t) {
    t.fn.powerFloat = function (s) {
        return t(this).each(function () {
            var a, r = t.extend({}, i, s || {}), n = function (t, i) {
                e.target && "none" !== e.target.css("display") && e.targetHide();
                e.s = t;
                e.trigger = i
            };
            switch (r.eventType) {
                case"hover":
                    t(this).hover(function () {
                        e.timerHold && (e.flagDisplay = !0);
                        var i = parseInt(r.showDelay, 10);
                        n(r, t(this));
                        if (i) {
                            a && clearTimeout(a);
                            a = setTimeout(function () {
                                e.targetGet.call(e)
                            }, i)
                        } else e.targetGet()
                    }, function () {
                        a && clearTimeout(a);
                        e.timerHold && clearTimeout(e.timerHold);
                        e.flagDisplay = !1;
                        e.targetHold()
                    });
                    r.hoverFollow && t(this).mousemove(function (t) {
                        e.cacheData.left = t.pageX;
                        e.cacheData.top = t.pageY;
                        e.targetGet.call(e);
                        return !1
                    });
                    break;
                case"click":
                    t(this).click(function (i) {
                        if (e.display && e.trigger && i.target === e.trigger.get(0)) {
                            e.flagDisplay = !1;
                            e.displayDetect()
                        } else {
                            n(r, t(this));
                            e.targetGet();
                            t(document).data("mouseupBind") || t(document).bind("mouseup", function (i) {
                                var s = !1;
                                if (e.trigger) {
                                    var a = e.target.attr("id");
                                    if (!a) {
                                        a = "R_" + Math.random();
                                        e.target.attr("id", a)
                                    }
                                    t(i.target).parents().each(function () {
                                        t(this).attr("id") === a && (s = !0)
                                    });
                                    if ("click" === r.eventType && e.display && i.target != e.trigger.get(0) && !s) {
                                        e.flagDisplay = !1;
                                        e.displayDetect()
                                    }
                                }
                                return !1
                            }).data("mouseupBind", !0)
                        }
                    });
                    break;
                case"focus":
                    t(this).focus(function () {
                        var i = t(this);
                        setTimeout(function () {
                            n(r, i);
                            e.targetGet()
                        }, 200)
                    }).blur(function () {
                        e.flagDisplay = !1;
                        setTimeout(function () {
                            e.displayDetect()
                        }, 190)
                    });
                    break;
                default:
                    n(r, t(this));
                    e.targetGet();
                    t(document).unbind("mouseup").data("mouseupBind", !1)
            }
        })
    };
    var e = {
        targetGet: function () {
            if (!this.trigger)return this;
            var i = this.trigger.attr(this.s.targetAttr), s = "function" == typeof this.s.target ? this.s.target.call(this.trigger) : this.s.target;
            switch (this.s.targetMode) {
                case"common":
                    if (s) {
                        var a = typeof s;
                        "object" === a ? s.size() && (e.target = s.eq(0)) : "string" === a && t(s).size() && (e.target = t(s).eq(0))
                    } else i && t("#" + i).size() && (e.target = t("#" + i));
                    if (!e.target)return this;
                    e.targetShow();
                    break;
                case"ajax":
                    var r = s || i;
                    this.targetProtect = !1;
                    if (!r)return;
                    e.cacheData[r] || e.loading();
                    var n = new Image;
                    n.onload = function () {
                        var i = n.width, s = n.height, a = t(window).width(), o = t(window).height(), l = i / s, h = a / o;
                        if (l > h) {
                            if (i > a / 2) {
                                i = a / 2;
                                s = i / l
                            }
                        } else if (s > o / 2) {
                            s = o / 2;
                            i = s * l
                        }
                        var u = '<img class="float_ajax_image" src="' + r + '" width="' + i + '" height = "' + s + '" />';
                        e.cacheData[r] = !0;
                        e.target = t(u);
                        e.targetShow()
                    };
                    n.onerror = function () {
                        if (/(\.jpg|\.png|\.gif|\.bmp|\.jpeg)$/i.test(r)) {
                            e.target = t('<div class="float_ajax_error">图片加载失败。</div>');
                            e.targetShow()
                        } else t.ajax({
                            url: r, success: function (i) {
                                if ("string" == typeof i) {
                                    e.cacheData[r] = !0;
                                    e.target = t('<div class="float_ajax_data">' + i + "</div>");
                                    e.targetShow()
                                }
                            }, error: function () {
                                e.target = t('<div class="float_ajax_error">数据没有加载成功。</div>');
                                e.targetShow()
                            }
                        })
                    };
                    n.src = r;
                    break;
                case"list":
                    var o, l = '<ul class="float_list_ul">';
                    t.isArray(s) && (o = s.length) ? t.each(s, function (t, e) {
                        var i, s, a = "", r = "";
                        0 === t && (r = ' class="float_list_li_first"');
                        t === o - 1 && (r = ' class="float_list_li_last"');
                        "object" == typeof e && (i = e.text.toString()) ? a = (s = e.href || "javascript:") ? '<a href="' + s + '" class="float_list_a">' + i + "</a>" : i : "string" == typeof e && e && (a = e);
                        a && (l += "<li" + r + ">" + a + "</li>")
                    }) : l += '<li class="float_list_null">列表无数据。</li>';
                    l += "</ul>";
                    e.target = t(l);
                    this.targetProtect = !1;
                    e.targetShow();
                    break;
                case"remind":
                    var h = s || i;
                    this.targetProtect = !1;
                    if ("string" == typeof h) {
                        e.target = t("<span>" + h + "</span>");
                        e.targetShow()
                    }
                    break;
                default:
                    var u = s || i, a = typeof u;
                    if (u)if ("string" === a) {
                        if (/^.[^:#\[\.,]*$/.test(u))if (t(u).size()) {
                            e.target = t(u).eq(0);
                            this.targetProtect = !0
                        } else if (t("#" + u).size()) {
                            e.target = t("#" + u).eq(0);
                            this.targetProtect = !0
                        } else {
                            e.target = t("<div>" + u + "</div>");
                            this.targetProtect = !1
                        } else {
                            e.target = t("<div>" + u + "</div>");
                            this.targetProtect = !1
                        }
                        e.targetShow()
                    } else if ("object" === a && !t.isArray(u) && u.size()) {
                        e.target = u.eq(0);
                        this.targetProtect = !0;
                        e.targetShow()
                    }
            }
            return this
        }, container: function () {
            var i = this.s.container, s = this.s.targetMode || "mode";
            this.s.sharpAngle = "ajax" === s || "remind" === s ? !0 : !1;
            this.s.reverseSharp && (this.s.sharpAngle = !this.s.sharpAngle);
            if ("common" !== s) {
                null === i && (i = "plugin");
                if ("plugin" === i) {
                    t("#floatBox_" + s).size() || t('<div id="floatBox_' + s + '" class="float_' + s + '_box"></div>').appendTo(t("body")).hide();
                    i = t("#floatBox_" + s)
                }
                if (i && "string" != typeof i && i.size()) {
                    this.targetProtect && e.target.show().css("position", "static");
                    e.target = i.empty().append(e.target)
                }
            }
            return this
        }, setWidth: function () {
            var t = this.s.width;
            "auto" === t ? this.target.get(0).style.width && this.target.css("width", "auto") : "inherit" === t ? this.target.width(this.trigger.width()) : this.target.css("width", t);
            return this
        }, position: function () {
            if (!this.trigger || !this.target)return this;
            var i, s, a, r, n, o, l, h = 0, u = 0, d = 0, c = 0, f = this.target.data("height"), g = this.target.data("width"), p = t(window).scrollTop(), m = parseInt(this.s.offsets.x, 10) || 0, v = parseInt(this.s.offsets.y, 10) || 0, F = this.cacheData;
            if (!f) {
                f = this.target.outerHeight();
                this.s.hoverFollow && this.target.data("height", f)
            }
            if (!g) {
                g = this.target.outerWidth();
                this.s.hoverFollow && this.target.data("width", g)
            }
            i = this.trigger.offset();
            h = this.trigger.outerHeight();
            u = this.trigger.outerWidth();
            s = i.left;
            a = i.top;
            var b = function () {
                0 > s ? s = 0 : s + h > t(window).width() && (s = t(window).width() - u)
            }, x = function () {
                0 > a ? a = 0 : a + h > t(document).height() && (a = t(document).height() - h)
            };
            if (this.s.hoverFollow && F.left && F.top)if ("x" === this.s.hoverFollow) {
                s = F.left;
                b()
            } else if ("y" === this.s.hoverFollow) {
                a = F.top;
                x()
            } else {
                s = F.left;
                a = F.top;
                b();
                x()
            }
            var y, w = ["4-1", "1-4", "5-7", "2-3", "2-1", "6-8", "3-4", "4-3", "8-6", "1-2", "7-5", "3-2"], C = this.s.position, D = !1;
            t.each(w, function (t, e) {
                e !== C || (D = !0)
            });
            D || (C = "4-1");
            var _ = function (t) {
                var e = "bottom";
                switch (t) {
                    case"1-4":
                    case"5-7":
                    case"2-3":
                        e = "top";
                        break;
                    case"2-1":
                    case"6-8":
                    case"3-4":
                        e = "right";
                        break;
                    case"1-2":
                    case"8-6":
                    case"4-3":
                        e = "left";
                        break;
                    case"4-1":
                    case"7-5":
                    case"3-2":
                        e = "bottom"
                }
                return e
            }, S = function (t) {
                return "5-7" === t || "6-8" === t || "8-6" === t || "7-5" === t ? !0 : !1
            }, k = function (i) {
                var r = 0, n = 0, o = e.s.sharpAngle && e.corner ? !0 : !1;
                if ("right" === i) {
                    n = s + u + g + m;
                    o && (n += e.corner.width());
                    if (n > t(window).width())return !1
                } else if ("bottom" === i) {
                    r = a + h + f + v;
                    o && (r += e.corner.height());
                    if (r > p + t(window).height())return !1
                } else if ("top" === i) {
                    r = f + v;
                    o && (r += e.corner.height());
                    if (r > a - p)return !1
                } else if ("left" === i) {
                    n = g + m;
                    o && (n += e.corner.width());
                    if (n > s)return !1
                }
                return !0
            };
            y = _(C);
            this.s.sharpAngle && this.createSharp(y);
            this.s.edgeAdjust && (k(y) ? !function () {
                if (!S(C)) {
                    var t, e = {
                        top: {right: "2-3", left: "1-4"},
                        right: {top: "2-1", bottom: "3-4"},
                        bottom: {right: "3-2", left: "4-1"},
                        left: {top: "1-2", bottom: "4-3"}
                    }, i = e[y];
                    if (i)for (t in i)k(t) || (C = i[t])
                }
            }() : !function () {
                if (S(C)) {
                    var t = {"5-7": "7-5", "7-5": "5-7", "6-8": "8-6", "8-6": "6-8"};
                    C = t[C]
                } else {
                    var e = {
                        top: {left: "3-2", right: "4-1"},
                        right: {bottom: "1-2", top: "4-3"},
                        bottom: {left: "2-3", right: "1-4"},
                        left: {bottom: "2-1", top: "3-4"}
                    }, i = e[y], s = [];
                    for (name in i)s.push(name);
                    C = k(s[0]) || !k(s[1]) ? i[s[0]] : i[s[1]]
                }
            }());
            var L = _(C), E = C.split("-")[0];
            if (this.s.sharpAngle) {
                this.createSharp(L);
                d = this.corner.width(), c = this.corner.height()
            }
            if (this.s.hoverFollow)if ("x" === this.s.hoverFollow) {
                r = s + m;
                r = "1" === E || "8" === E || "4" === E ? s - (g - u) / 2 + m : s - (g - u) + m;
                if ("1" === E || "5" === E || "2" === E) {
                    n = a - v - f - c;
                    l = a - c - v - 1
                } else {
                    n = a + h + v + c;
                    l = a + h + v + 1
                }
                o = i.left - (d - u) / 2
            } else if ("y" === this.s.hoverFollow) {
                n = "1" === E || "5" === E || "2" === E ? a - (f - h) / 2 + v : a - (f - h) + v;
                if ("1" === E || "8" === E || "4" === E) {
                    r = s - g - m - d;
                    o = s - d - m - 1
                } else {
                    r = s + u - m + d;
                    o = s + u + m + 1
                }
                l = i.top - (c - h) / 2
            } else {
                r = s + m;
                n = a + v
            } else switch (L) {
                case"top":
                    n = a - v - f - c;
                    r = "1" == E ? s - m : "5" === E ? s - (g - u) / 2 - m : s - (g - u) - m;
                    l = a - c - v - 1;
                    o = s - (d - u) / 2;
                    break;
                case"right":
                    r = s + u + m + d;
                    n = "2" == E ? a + v : "6" === E ? a - (f - h) / 2 + v : a - (f - h) + v;
                    o = s + u + m + 1;
                    l = a - (c - h) / 2;
                    break;
                case"bottom":
                    n = a + h + v + c;
                    r = "4" == E ? s + m : "7" === E ? s - (g - u) / 2 + m : s - (g - u) + m;
                    l = a + h + v + 1;
                    o = s - (d - u) / 2;
                    break;
                case"left":
                    r = s - g - m - d;
                    n = "2" == E ? a - v : "6" === E ? a - (g - u) / 2 - v : a - (f - h) - v;
                    o = r + d;
                    l = a - (g - d) / 2
            }
            c && d && this.corner && this.corner.css({left: o, top: l, zIndex: this.s.zIndex + 1});
            this.target.css({position: "absolute", left: r, top: n, zIndex: this.s.zIndex});
            return this
        }, createSharp: function (e) {
            var i, s, a = "", r = "", n = {
                left: "right",
                right: "left",
                bottom: "top",
                top: "bottom"
            }, o = n[e] || "top";
            if (this.target) {
                i = this.target.css("background-color");
                parseInt(this.target.css("border-" + o + "-width")) > 0 && (s = this.target.css("border-" + o + "-color"));
                a = s && "transparent" !== s ? 'style="color:' + s + ';"' : 'style="display:none;"';
                r = i && "transparent" !== i ? 'style="color:' + i + ';"' : 'style="display:none;"'
            }
            var l = '<div id="floatCorner_' + e + '" class="float_corner float_corner_' + e + '"><span class="corner corner_1" ' + a + '>◆</span><span class="corner corner_2" ' + r + ">◆</span></div>";
            t("#floatCorner_" + e).size() || t("body").append(t(l));
            this.corner = t("#floatCorner_" + e);
            return this
        }, targetHold: function () {
            if (this.s.hoverHold) {
                var t = parseInt(this.s.hideDelay, 10) || 200;
                this.target && this.target.hover(function () {
                    e.flagDisplay = !0
                }, function () {
                    e.timerHold && clearTimeout(e.timerHold);
                    e.flagDisplay = !1;
                    e.targetHold()
                });
                e.timerHold = setTimeout(function () {
                    e.displayDetect.call(e)
                }, t)
            } else this.displayDetect();
            return this
        }, loading: function () {
            this.target = t('<div class="float_loading"></div>');
            this.targetShow();
            this.target.removeData("width").removeData("height");
            return this
        }, displayDetect: function () {
            if (!this.flagDisplay && this.display) {
                this.targetHide();
                this.timerHold = null
            }
            return this
        }, targetShow: function () {
            e.cornerClear();
            this.display = !0;
            this.container().setWidth().position();
            this.target.show();
            t.isFunction(this.s.showCall) && this.s.showCall.call(this.trigger, this.target);
            return this
        }, targetHide: function () {
            this.display = !1;
            this.targetClear();
            this.cornerClear();
            t.isFunction(this.s.hideCall) && this.s.hideCall.call(this.trigger);
            this.target = null;
            this.trigger = null;
            this.s = {};
            this.targetProtect = !1;
            return this
        }, targetClear: function () {
            if (this.target) {
                this.target.data("width") && this.target.removeData("width").removeData("height");
                this.targetProtect && this.target.children().hide().appendTo(t("body"));
                this.target.unbind().hide()
            }
        }, cornerClear: function () {
            this.corner && this.corner.remove()
        }, target: null, trigger: null, s: {}, cacheData: {}, targetProtect: !1
    };
    t.powerFloat = {};
    t.powerFloat.hide = function () {
        e.targetHide()
    };
    var i = {
        width: "auto",
        offsets: {x: 0, y: 0},
        zIndex: 999,
        eventType: "hover",
        showDelay: 0,
        hideDelay: 0,
        hoverHold: !0,
        hoverFollow: !1,
        targetMode: "common",
        target: null,
        targetAttr: "rel",
        container: null,
        reverseSharp: !1,
        position: "4-1",
        edgeAdjust: !0,
        showCall: t.noop,
        hideCall: t.noop
    }
}(jQuery);
!function (t, e) {
    "use strict";
    var i = "function" == typeof moment, s = !!t.addEventListener, a = t.document, n = t.setTimeout, r = function (t, e, i, a) {
        s ? t.addEventListener(e, i, !!a) : t.attachEvent("on" + e, i)
    }, o = function (t, e, i, a) {
        s ? t.removeEventListener(e, i, !!a) : t.detachEvent("on" + e, i)
    }, l = function (t, e, i) {
        var s;
        if (a.createEvent) {
            s = a.createEvent("HTMLEvents");
            s.initEvent(e, !0, !1);
            s = y(s, i);
            t.dispatchEvent(s)
        } else if (a.createEventObject) {
            s = a.createEventObject();
            s = y(s, i);
            t.fireEvent("on" + e, s)
        }
    }, h = function (t) {
        return t.trim ? t.trim() : t.replace(/^\s+|\s+$/g, "")
    }, u = function (t, e) {
        return -1 !== (" " + t.className + " ").indexOf(" " + e + " ")
    }, d = function (t, e) {
        u(t, e) || (t.className = "" === t.className ? e : t.className + " " + e)
    }, c = function (t, e) {
        t.className = h((" " + t.className + " ").replace(" " + e + " ", " "))
    }, f = function (t) {
        return /Array/.test(Object.prototype.toString.call(t))
    }, g = function (t) {
        return /Date/.test(Object.prototype.toString.call(t)) && !isNaN(t.getTime())
    }, p = function (t) {
        return new Date(Date.parse(t.replace(/\.|\-/g, "/")))
    }, m = function (t) {
        return t % 4 === 0 && t % 100 !== 0 || t % 400 === 0
    }, v = function (t, e) {
        return [31, m(t) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][e]
    }, F = function (t) {
        g(t) && t.setHours(0, 0, 0, 0)
    }, b = function (t, e) {
        return t.getTime() === e.getTime()
    }, y = function (t, i, s) {
        var a, n;
        for (a in i) {
            n = t[a] !== e;
            n && "object" == typeof i[a] && i[a].nodeName === e ? g(i[a]) ? s && (t[a] = new Date(i[a].getTime())) : f(i[a]) ? s && (t[a] = i[a].slice(0)) : t[a] = y({}, i[a], s) : (s || !n) && (t[a] = i[a])
        }
        return t
    }, D = {
        field: null,
        bound: e,
        format: "YYYY-MM-DD",
        defaultDate: null,
        setDefaultDate: !1,
        firstDay: 0,
        minDate: null,
        maxDate: null,
        yearRange: 10,
        minYear: 1990,
        maxYear: 2099,
        minMonth: e,
        maxMonth: e,
        isRTL: !1,
        yearSuffix: "年",
        showMonthAfterYear: !0,
        numberOfMonths: 1,
        i18n: {
            months: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            monthsShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
            weekdays: ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
            weekdaysShort: ["日", "一", "二", "三", "四", "五", "六"]
        },
        onSelect: null,
        onOpen: null,
        onClose: null,
        onDraw: null
    }, x = function (t, e, i) {
        e += t.firstDay;
        for (; e >= 7;)e -= 7;
        return i ? t.i18n.weekdaysShort[e] : t.i18n.weekdays[e]
    }, w = function (t, e, i, s, a) {
        if (a)return '<td class="is-empty"></td>';
        var n = [];
        s && n.push("is-disabled");
        i && n.push("is-today");
        e && n.push("is-selected");
        return '<td data-day="' + t + '" class="' + n.join(" ") + '"><a class="pika-button" href="javascript:void(0);">' + t + "</a></td>"
    }, _ = function (t, e) {
        return "<tr>" + (e ? t.reverse() : t).join("") + "</tr>"
    }, C = function (t) {
        return "<tbody>" + t.join("") + "</tbody>"
    }, k = function (t) {
        var e, i = [];
        for (e = 0; 7 > e; e++)i.push('<th scope="col"><abbr title="' + x(t, e) + '">' + x(t, e, !0) + "</abbr></th>");
        return "<thead>" + (t.isRTL ? i.reverse() : i).join("") + "</thead>"
    }, S = function (t) {
        var e, i, s, a, n, r = t._o, o = t._m, l = t._y, h = l === r.minYear, u = l === r.maxYear, d = '<div class="pika-title">', c = !0, g = !0;
        d += '<a class="pika-prev' + (c ? "" : " is-disabled") + '" href="javascript:void(0);">&lt;</a>';
        for (s = [], e = 0; 12 > e; e++)s.push('<option value="' + e + '"' + (e === o ? " selected" : "") + (h && e < r.minMonth || u && e > r.maxMonth ? "disabled" : "") + ">" + r.i18n.months[e] + "</option>");
        a = '<div class="pika-label pika-label-month">' + r.i18n.months[o] + '<select class="pika-select pika-select-month">' + s.join("") + "</select></div>";
        if (f(r.yearRange)) {
            e = r.yearRange[0];
            i = r.yearRange[1] + 1
        } else {
            e = l - r.yearRange;
            i = 1 + l + r.yearRange
        }
        for (s = []; i > e && e <= r.maxYear; e++)e >= r.minYear && s.push('<option value="' + e + '"' + (e === l ? " selected" : "") + ">" + e + "</option>");
        n = '<div class="pika-label pika-label-year">' + l + r.yearSuffix + '<select class="pika-select pika-select-year">' + s.join("") + "</select></div>";
        d += r.showMonthAfterYear ? n + a : a + n;
        h && (0 === o || r.minMonth >= o) && (c = !1);
        u && (11 === o || r.maxMonth <= o) && (g = !1);
        d += '<a class="pika-next' + (g ? "" : " is-disabled") + '" href="javascript:void(0);">&gt;</a>';
        return d += "</div>"
    }, E = function (t, e) {
        return '<table cellpadding="0" cellspacing="0" class="pika-table">' + k(t) + C(e) + "</table>"
    }, I = function (e) {
        var o = this, l = o.config(e);
        o._onMouseDown = function (e) {
            if (o._v) {
                e = e || t.event;
                var i = e.target || e.srcElement;
                if (i) {
                    if (!u(i, "is-disabled")) {
                        if (u(i, "pika-button") && !u(i, "is-empty")) {
                            o.setDate(new Date(o._y, o._m, parseInt(i.innerHTML, 10)));
                            l.bound && n(function () {
                                o.hide()
                            }, 100);
                            return
                        }
                        u(i, "pika-prev") ? o.prevMonth() : u(i, "pika-next") && o.nextMonth()
                    }
                    o._c = !0;
                    if (!u(i, "pika-select")) {
                        if (!e.preventDefault) {
                            e.returnValue = !1;
                            e.cancelBubble = !0;
                            return !1
                        }
                        e.preventDefault();
                        e.stopPropagation()
                    }
                }
            }
        };
        o._onChange = function (e) {
            e = e || t.event;
            var i = e.target || e.srcElement;
            i && (u(i, "pika-select-month") ? o.gotoMonth(i.value) : u(i, "pika-select-year") && o.gotoYear(i.value))
        };
        o._onInputChange = function (t) {
            var e;
            if (t.firedBy !== o) {
                if (i) {
                    e = moment(l.field.value, l.format);
                    e = e && e.isValid() ? e.toDate() : null
                } else e = p(l.field.value);
                o.setDate(g(e) ? e : null);
                o._v || o.show()
            }
        };
        o._onInputFocus = function () {
            o.show()
        };
        o._onInputClick = function () {
            o.show()
        };
        o._onInputBlur = function () {
            o._c || (o._b = n(function () {
                o.hide()
            }, 50));
            o._c = !1
        };
        o._onClick = function (e) {
            e = e || t.event;
            var i = e.target || e.srcElement, a = i;
            if (i) {
                if (!s && u(i, "pika-select") && !i.onchange) {
                    i.setAttribute("onchange", "return;");
                    r(i, "change", o._onChange)
                }
                do if (u(a, "pika-single"))return; while (a = a.parentNode);
                o._v && i !== l.trigger && o.hide()
            }
        };
        o.el = a.createElement("div");
        o.el.className = "pika-single" + (l.isRTL ? " is-rtl" : "");
        r(o.el, "mousedown", o._onMouseDown, !0);
        r(o.el, "change", o._onChange);
        if (l.field) {
            l.bound ? a.body.appendChild(o.el) : l.field.parentNode.insertBefore(o.el, l.field.nextSibling);
            r(l.field, "change", o._onInputChange);
            if (!l.defaultDate) {
                l.defaultDate = i && l.field.value ? moment(l.field.value, l.format).toDate() : p(l.field.value);
                l.setDefaultDate = !0
            }
        }
        var h = l.defaultDate;
        g(h) ? l.setDefaultDate ? o.setDate(h, !0) : o.gotoDate(h) : o.gotoDate(new Date);
        if (l.bound) {
            this.hide();
            o.el.className += " is-bound";
            r(l.trigger, "click", o._onInputClick);
            r(l.trigger, "focus", o._onInputFocus);
            r(l.trigger, "blur", o._onInputBlur)
        } else this.show()
    };
    I.prototype = {
        config: function (t) {
            this._o || (this._o = y({}, D, !0));
            var i = y(this._o, t, !0);
            i.isRTL = !!i.isRTL;
            i.field = i.field && i.field.nodeName ? i.field : null;
            i.bound = !!(i.bound !== e ? i.field && i.bound : i.field);
            i.trigger = i.trigger && i.trigger.nodeName ? i.trigger : i.field;
            var s = parseInt(i.numberOfMonths, 10) || 1;
            i.numberOfMonths = s > 4 ? 4 : s;
            g(i.minDate) || (i.minDate = !1);
            g(i.maxDate) || (i.maxDate = !1);
            i.minDate && i.maxDate && i.maxDate < i.minDate && (i.maxDate = i.minDate = !1);
            if (i.minDate) {
                F(i.minDate);
                i.minYear = i.minDate.getFullYear();
                i.minMonth = i.minDate.getMonth()
            }
            if (i.maxDate) {
                F(i.maxDate);
                i.maxYear = i.maxDate.getFullYear();
                i.maxMonth = i.maxDate.getMonth()
            }
            if (f(i.yearRange)) {
                var a = (new Date).getFullYear() - 10;
                i.yearRange[0] = parseInt(i.yearRange[0], 10) || a;
                i.yearRange[1] = parseInt(i.yearRange[1], 10) || a
            } else {
                i.yearRange = Math.abs(parseInt(i.yearRange, 10)) || D.yearRange;
                i.yearRange > 100 && (i.yearRange = 100)
            }
            return i
        }, toString: function (t) {
            if (!g(this._d))return "";
            var e = this._d.getFullYear(), s = this._d.getMonth() + 1, a = this._d.getDate();
            s = 10 > s ? "0" + s : s;
            a = 10 > a ? "0" + a : a;
            return g(this._d) ? i ? moment(this._d).format(t || this._o.format) : e + "-" + s + "-" + a : ""
        }, getMoment: function () {
            return i ? moment(this._d) : null
        }, setMoment: function (t) {
            i && moment.isMoment(t) && this.setDate(t.toDate())
        }, getDate: function () {
            return g(this._d) ? new Date(this._d.getTime()) : null
        }, setDate: function (t, e) {
            if (!t) {
                this._d = null;
                return this.draw()
            }
            "string" == typeof t && (t = p(t));
            if (g(t)) {
                var i = this._o.minDate, s = this._o.maxDate;
                g(i) && i > t ? t = i : g(s) && t > s && (t = s);
                this._d = new Date(t.getTime());
                F(this._d);
                this.gotoDate(this._d);
                if (this._o.field) {
                    this._o.field.value = this.toString();
                    l(this._o.field, "change", {firedBy: this})
                }
                e || "function" != typeof this._o.onSelect || this._o.onSelect.call(this, this.getDate())
            }
        }, gotoDate: function (t) {
            if (g(t)) {
                this._y = t.getFullYear();
                this._m = t.getMonth();
                this.draw()
            }
        }, gotoToday: function () {
            this.gotoDate(new Date)
        }, gotoMonth: function (t) {
            if (!isNaN(t = parseInt(t, 10))) {
                this._m = 0 > t ? 0 : t > 11 ? 11 : t;
                this.draw()
            }
        }, nextMonth: function () {
            if (++this._m > 11) {
                this._m = 0;
                this._y++
            }
            this.draw()
        }, prevMonth: function () {
            if (--this._m < 0) {
                this._m = 11;
                this._y--
            }
            this.draw()
        }, gotoYear: function (t) {
            if (!isNaN(t)) {
                this._y = parseInt(t, 10);
                this.draw()
            }
        }, setMinDate: function (t) {
            this._o.minDate = t
        }, setMaxDate: function (t) {
            this._o.maxDate = t
        }, draw: function (t) {
            if (this._v || t) {
                var e = this._o, i = e.minYear, s = e.maxYear, a = e.minMonth, r = e.maxMonth;
                if (this._y <= i) {
                    this._y = i;
                    !isNaN(a) && this._m < a && (this._m = a)
                }
                if (this._y >= s) {
                    this._y = s;
                    !isNaN(r) && this._m > r && (this._m = r)
                }
                this.el.innerHTML = S(this) + this.render(this._y, this._m);
                if (e.bound) {
                    this.adjustPosition();
                    "hidden" !== e.field.type && n(function () {
                        e.trigger.focus()
                    }, 1)
                }
                if ("function" == typeof this._o.onDraw) {
                    var o = this;
                    n(function () {
                        o._o.onDraw.call(o)
                    }, 0)
                }
            }
        }, adjustPosition: function () {
            var e, i, s, n = this._o.trigger, r = n, o = this.el.offsetWidth, l = this.el.offsetHeight, h = t.innerWidth || a.documentElement.clientWidth, u = t.innerHeight || a.documentElement.clientHeight, f = t.pageYOffset || a.body.scrollTop || a.documentElement.scrollTop;
            d(this.el, "is-hidden");
            if ("function" == typeof n.getBoundingClientRect) {
                s = n.getBoundingClientRect();
                e = s.left + t.pageXOffset;
                i = s.bottom + t.pageYOffset
            } else {
                e = r.offsetLeft;
                i = r.offsetTop + r.offsetHeight;
                for (; r = r.offsetParent;) {
                    e += r.offsetLeft;
                    i += r.offsetTop
                }
            }
            c(this.el, "is-hidden");
            e + o > h && (e = e - o + n.offsetWidth);
            i + l > u + f && (i = i - l - n.offsetHeight);
            this.el.style.cssText = "position:absolute;left:" + e + "px;top:" + i + "px;"
        }, render: function (t, e) {
            var i = this._o, s = new Date, a = v(t, e), n = new Date(t, e, 1).getDay(), r = [], o = [];
            F(s);
            if (i.firstDay > 0) {
                n -= i.firstDay;
                0 > n && (n += 7)
            }
            for (var l = a + n, h = l; h > 7;)h -= 7;
            l += 7 - h;
            for (var u = 0, d = 0; l > u; u++) {
                var c = new Date(t, e, 1 + (u - n)), f = i.minDate && c < i.minDate || i.maxDate && c > i.maxDate, p = g(this._d) ? b(c, this._d) : !1, m = b(c, s), y = n > u || u >= a + n;
                o.push(w(1 + (u - n), p, m, f, y));
                if (7 === ++d) {
                    r.push(_(o, i.isRTL));
                    o = [];
                    d = 0
                }
            }
            return E(i, r)
        }, isVisible: function () {
            return this._v
        }, show: function () {
            if (!this._v) {
                this._o.bound && r(a, "click", this._onClick);
                c(this.el, "is-hidden");
                this._v = !0;
                this.draw();
                "function" == typeof this._o.onOpen && this._o.onOpen.call(this)
            }
        }, hide: function () {
            var t = this._v;
            if (t !== !1) {
                this._o.bound && o(a, "click", this._onClick);
                this.el.style.cssText = "";
                d(this.el, "is-hidden");
                this._v = !1;
                t !== e && "function" == typeof this._o.onClose && this._o.onClose.call(this)
            }
        }, destroy: function () {
            this.hide();
            o(this.el, "mousedown", this._onMouseDown, !0);
            o(this.el, "change", this._onChange);
            if (this._o.field) {
                o(this._o.field, "change", this._onInputChange);
                if (this._o.bound) {
                    o(this._o.trigger, "click", this._onInputClick);
                    o(this._o.trigger, "focus", this._onInputFocus);
                    o(this._o.trigger, "blur", this._onInputBlur)
                }
            }
            this.el.parentNode && this.el.parentNode.removeChild(this.el)
        }
    };
    t.Pikaday = I
}(window);
!function (t, e) {
    t.define && "function" == typeof define ? define("/js/common/plugins/datepicker/pikaday", ["jquery"], function (i) {
        e(i("jquery"), t.Pikaday)
    }) : e(t.jQuery, t.Pikaday)
}(this, function (t, e) {
    t && (t.fn.datepicker = t.fn.pikaday = function () {
        var i = arguments;
        i && i.length || (i = [{}]);
        return this.each(function () {
            var s = t(this), a = s.data("pikaday");
            if (a instanceof e)"string" == typeof i[0] && "function" == typeof a[i[0]] && a[i[0]].apply(a, Array.prototype.slice.call(i, 1)); else if ("object" == typeof i[0]) {
                var n = t.extend({}, i[0]);
                n.field = s[0];
                s.data("pikaday", new e(n))
            }
        })
    })
});
!function ($) {
    var settings = {}, roots = {}, caches = {}, _consts = {
        className: {
            BUTTON: "button",
            LEVEL: "level",
            ICO_LOADING: "ico_loading",
            SWITCH: "switch"
        },
        event: {
            NODECREATED: "ztree_nodeCreated",
            CLICK: "ztree_click",
            EXPAND: "ztree_expand",
            COLLAPSE: "ztree_collapse",
            ASYNC_SUCCESS: "ztree_async_success",
            ASYNC_ERROR: "ztree_async_error",
            REMOVE: "ztree_remove"
        },
        id: {A: "_a", ICON: "_ico", SPAN: "_span", SWITCH: "_switch", UL: "_ul"},
        line: {ROOT: "root", ROOTS: "roots", CENTER: "center", BOTTOM: "bottom", NOLINE: "noline", LINE: "line"},
        folder: {OPEN: "open", CLOSE: "close", DOCU: "docu"},
        node: {CURSELECTED: "curSelectedNode"}
    }, _setting = {
        treeId: "",
        treeObj: null,
        view: {
            addDiyDom: null,
            autoCancelSelected: !0,
            dblClickExpand: !0,
            expandSpeed: "fast",
            fontCss: {},
            nameIsHTML: !1,
            selectedMulti: !0,
            showIcon: !0,
            showLine: !0,
            showTitle: !0,
            txtSelectedEnable: !1
        },
        data: {
            key: {children: "children", name: "name", title: "", url: "url"},
            simpleData: {enable: !1, idKey: "id", pIdKey: "pId", rootPId: null},
            keep: {parent: !1, leaf: !1}
        },
        async: {
            enable: !1,
            contentType: "application/x-www-form-urlencoded",
            type: "post",
            dataType: "text",
            url: "",
            autoParam: [],
            otherParam: [],
            dataFilter: null
        },
        callback: {
            beforeAsync: null,
            beforeClick: null,
            beforeDblClick: null,
            beforeRightClick: null,
            beforeMouseDown: null,
            beforeMouseUp: null,
            beforeExpand: null,
            beforeCollapse: null,
            beforeRemove: null,
            onAsyncError: null,
            onAsyncSuccess: null,
            onNodeCreated: null,
            onClick: null,
            onDblClick: null,
            onRightClick: null,
            onMouseDown: null,
            onMouseUp: null,
            onExpand: null,
            onCollapse: null,
            onRemove: null
        }
    }, _initRoot = function (e) {
        var t = data.getRoot(e);
        if (!t) {
            t = {};
            data.setRoot(e, t)
        }
        t[e.data.key.children] = [];
        t.expandTriggerFlag = !1;
        t.curSelectedList = [];
        t.noSelection = !0;
        t.createdNodes = [];
        t.zId = 0;
        t._ver = (new Date).getTime()
    }, _initCache = function (e) {
        var t = data.getCache(e);
        if (!t) {
            t = {};
            data.setCache(e, t)
        }
        t.nodes = [];
        t.doms = []
    }, _bindEvent = function (e) {
        var t = e.treeObj, i = consts.event;
        t.bind(i.NODECREATED, function (t, i, n) {
            tools.apply(e.callback.onNodeCreated, [t, i, n])
        });
        t.bind(i.CLICK, function (t, i, n, a, s) {
            tools.apply(e.callback.onClick, [i, n, a, s])
        });
        t.bind(i.EXPAND, function (t, i, n) {
            tools.apply(e.callback.onExpand, [t, i, n])
        });
        t.bind(i.COLLAPSE, function (t, i, n) {
            tools.apply(e.callback.onCollapse, [t, i, n])
        });
        t.bind(i.ASYNC_SUCCESS, function (t, i, n, a) {
            tools.apply(e.callback.onAsyncSuccess, [t, i, n, a])
        });
        t.bind(i.ASYNC_ERROR, function (t, i, n, a, s, o) {
            tools.apply(e.callback.onAsyncError, [t, i, n, a, s, o])
        });
        t.bind(i.REMOVE, function (t, i, n) {
            tools.apply(e.callback.onRemove, [t, i, n])
        })
    }, _unbindEvent = function (e) {
        var t = e.treeObj, i = consts.event;
        t.unbind(i.NODECREATED).unbind(i.CLICK).unbind(i.EXPAND).unbind(i.COLLAPSE).unbind(i.ASYNC_SUCCESS).unbind(i.ASYNC_ERROR).unbind(i.REMOVE)
    }, _eventProxy = function (e) {
        var t = e.target, i = data.getSetting(e.data.treeId), n = "", a = null, s = "", o = "", r = null, l = null, d = null;
        if (tools.eqs(e.type, "mousedown"))o = "mousedown"; else if (tools.eqs(e.type, "mouseup"))o = "mouseup"; else if (tools.eqs(e.type, "contextmenu"))o = "contextmenu"; else if (tools.eqs(e.type, "click"))if (tools.eqs(t.tagName, "span") && null !== t.getAttribute("treeNode" + consts.id.SWITCH)) {
            n = tools.getNodeMainDom(t).id;
            s = "switchNode"
        } else {
            d = tools.getMDom(i, t, [{tagName: "a", attrName: "treeNode" + consts.id.A}]);
            if (d) {
                n = tools.getNodeMainDom(d).id;
                s = "clickNode"
            }
        } else if (tools.eqs(e.type, "dblclick")) {
            o = "dblclick";
            d = tools.getMDom(i, t, [{tagName: "a", attrName: "treeNode" + consts.id.A}]);
            if (d) {
                n = tools.getNodeMainDom(d).id;
                s = "switchNode"
            }
        }
        if (o.length > 0 && 0 == n.length) {
            d = tools.getMDom(i, t, [{tagName: "a", attrName: "treeNode" + consts.id.A}]);
            d && (n = tools.getNodeMainDom(d).id)
        }
        if (n.length > 0) {
            a = data.getNodeCache(i, n);
            switch (s) {
                case"switchNode":
                    a.isParent ? tools.eqs(e.type, "click") || tools.eqs(e.type, "dblclick") && tools.apply(i.view.dblClickExpand, [i.treeId, a], i.view.dblClickExpand) ? r = handler.onSwitchNode : s = "" : s = "";
                    break;
                case"clickNode":
                    r = handler.onClickNode
            }
        }
        switch (o) {
            case"mousedown":
                l = handler.onZTreeMousedown;
                break;
            case"mouseup":
                l = handler.onZTreeMouseup;
                break;
            case"dblclick":
                l = handler.onZTreeDblclick;
                break;
            case"contextmenu":
                l = handler.onZTreeContextmenu
        }
        var c = {stop: !1, node: a, nodeEventType: s, nodeEventCallback: r, treeEventType: o, treeEventCallback: l};
        return c
    }, _initNode = function (e, t, i, n, a, s) {
        if (i) {
            var o = data.getRoot(e), r = e.data.key.children;
            i.level = t;
            i.tId = e.treeId + "_" + ++o.zId;
            i.parentTId = n ? n.tId : null;
            i.open = "string" == typeof i.open ? tools.eqs(i.open, "true") : !!i.open;
            if (i[r] && i[r].length > 0) {
                i.isParent = !0;
                i.zAsync = !0
            } else {
                i.isParent = "string" == typeof i.isParent ? tools.eqs(i.isParent, "true") : !!i.isParent;
                i.open = i.isParent && !e.async.enable ? i.open : !1;
                i.zAsync = !i.isParent
            }
            i.isFirstNode = a;
            i.isLastNode = s;
            i.getParentNode = function () {
                return data.getNodeCache(e, i.parentTId)
            };
            i.getPreNode = function () {
                return data.getPreNode(e, i)
            };
            i.getNextNode = function () {
                return data.getNextNode(e, i)
            };
            i.isAjaxing = !1;
            data.fixPIdKeyValue(e, i)
        }
    }, _init = {
        bind: [_bindEvent],
        unbind: [_unbindEvent],
        caches: [_initCache],
        nodes: [_initNode],
        proxys: [_eventProxy],
        roots: [_initRoot],
        beforeA: [],
        afterA: [],
        innerBeforeA: [],
        innerAfterA: [],
        zTreeTools: []
    }, data = {
        addNodeCache: function (e, t) {
            data.getCache(e).nodes[data.getNodeCacheId(t.tId)] = t
        }, getNodeCacheId: function (e) {
            return e.substring(e.lastIndexOf("_") + 1)
        }, addAfterA: function (e) {
            _init.afterA.push(e)
        }, addBeforeA: function (e) {
            _init.beforeA.push(e)
        }, addInnerAfterA: function (e) {
            _init.innerAfterA.push(e)
        }, addInnerBeforeA: function (e) {
            _init.innerBeforeA.push(e)
        }, addInitBind: function (e) {
            _init.bind.push(e)
        }, addInitUnBind: function (e) {
            _init.unbind.push(e)
        }, addInitCache: function (e) {
            _init.caches.push(e)
        }, addInitNode: function (e) {
            _init.nodes.push(e)
        }, addInitProxy: function (e, t) {
            t ? _init.proxys.splice(0, 0, e) : _init.proxys.push(e)
        }, addInitRoot: function (e) {
            _init.roots.push(e)
        }, addNodesData: function (e, t, i) {
            var n = e.data.key.children;
            t[n] || (t[n] = []);
            if (t[n].length > 0) {
                t[n][t[n].length - 1].isLastNode = !1;
                view.setNodeLineIcos(e, t[n][t[n].length - 1])
            }
            t.isParent = !0;
            t[n] = t[n].concat(i)
        }, addSelectedNode: function (e, t) {
            var i = data.getRoot(e);
            data.isSelectedNode(e, t) || i.curSelectedList.push(t)
        }, addCreatedNode: function (e, t) {
            if (e.callback.onNodeCreated || e.view.addDiyDom) {
                var i = data.getRoot(e);
                i.createdNodes.push(t)
            }
        }, addZTreeTools: function (e) {
            _init.zTreeTools.push(e)
        }, exSetting: function (e) {
            $.extend(!0, _setting, e)
        }, fixPIdKeyValue: function (e, t) {
            e.data.simpleData.enable && (t[e.data.simpleData.pIdKey] = t.parentTId ? t.getParentNode()[e.data.simpleData.idKey] : e.data.simpleData.rootPId)
        }, getAfterA: function () {
            for (var e = 0, t = _init.afterA.length; t > e; e++)_init.afterA[e].apply(this, arguments)
        }, getBeforeA: function () {
            for (var e = 0, t = _init.beforeA.length; t > e; e++)_init.beforeA[e].apply(this, arguments)
        }, getInnerAfterA: function () {
            for (var e = 0, t = _init.innerAfterA.length; t > e; e++)_init.innerAfterA[e].apply(this, arguments)
        }, getInnerBeforeA: function () {
            for (var e = 0, t = _init.innerBeforeA.length; t > e; e++)_init.innerBeforeA[e].apply(this, arguments)
        }, getCache: function (e) {
            return caches[e.treeId]
        }, getNextNode: function (e, t) {
            if (!t)return null;
            for (var i = e.data.key.children, n = t.parentTId ? t.getParentNode() : data.getRoot(e), a = 0, s = n[i].length - 1; s >= a; a++)if (n[i][a] === t)return a == s ? null : n[i][a + 1];
            return null
        }, getNodeByParam: function (e, t, i, n) {
            if (!t || !i)return null;
            for (var a = e.data.key.children, s = 0, o = t.length; o > s; s++) {
                if (t[s][i] == n)return t[s];
                var r = data.getNodeByParam(e, t[s][a], i, n);
                if (r)return r
            }
            return null
        }, getNodeCache: function (e, t) {
            if (!t)return null;
            var i = caches[e.treeId].nodes[data.getNodeCacheId(t)];
            return i ? i : null
        }, getNodeName: function (e, t) {
            var i = e.data.key.name;
            return "" + t[i]
        }, getNodeTitle: function (e, t) {
            var i = "" === e.data.key.title ? e.data.key.name : e.data.key.title;
            return "" + t[i]
        }, getNodes: function (e) {
            return data.getRoot(e)[e.data.key.children]
        }, getNodesByParam: function (e, t, i, n) {
            if (!t || !i)return [];
            for (var a = e.data.key.children, s = [], o = 0, r = t.length; r > o; o++) {
                t[o][i] == n && s.push(t[o]);
                s = s.concat(data.getNodesByParam(e, t[o][a], i, n))
            }
            return s
        }, getNodesByParamFuzzy: function (e, t, i, n) {
            if (!t || !i)return [];
            var a = e.data.key.children, s = [];
            n = n.toLowerCase();
            for (var o = 0, r = t.length; r > o; o++) {
                "string" == typeof t[o][i] && t[o][i].toLowerCase().indexOf(n) > -1 && s.push(t[o]);
                s = s.concat(data.getNodesByParamFuzzy(e, t[o][a], i, n))
            }
            return s
        }, getNodesByFilter: function (e, t, i, n, a) {
            if (!t)return n ? null : [];
            for (var s = e.data.key.children, o = n ? null : [], r = 0, l = t.length; l > r; r++) {
                if (tools.apply(i, [t[r], a], !1)) {
                    if (n)return t[r];
                    o.push(t[r])
                }
                var d = data.getNodesByFilter(e, t[r][s], i, n, a);
                if (n && d)return d;
                o = n ? d : o.concat(d)
            }
            return o
        }, getPreNode: function (e, t) {
            if (!t)return null;
            for (var i = e.data.key.children, n = t.parentTId ? t.getParentNode() : data.getRoot(e), a = 0, s = n[i].length; s > a; a++)if (n[i][a] === t)return 0 == a ? null : n[i][a - 1];
            return null
        }, getRoot: function (e) {
            return e ? roots[e.treeId] : null
        }, getRoots: function () {
            return roots
        }, getSetting: function (e) {
            return settings[e]
        }, getSettings: function () {
            return settings
        }, getZTreeTools: function (e) {
            var t = this.getRoot(this.getSetting(e));
            return t ? t.treeTools : null
        }, initCache: function () {
            for (var e = 0, t = _init.caches.length; t > e; e++)_init.caches[e].apply(this, arguments)
        }, initNode: function () {
            for (var e = 0, t = _init.nodes.length; t > e; e++)_init.nodes[e].apply(this, arguments)
        }, initRoot: function () {
            for (var e = 0, t = _init.roots.length; t > e; e++)_init.roots[e].apply(this, arguments)
        }, isSelectedNode: function (e, t) {
            for (var i = data.getRoot(e), n = 0, a = i.curSelectedList.length; a > n; n++)if (t === i.curSelectedList[n])return !0;
            return !1
        }, removeNodeCache: function (e, t) {
            var i = e.data.key.children;
            if (t[i])for (var n = 0, a = t[i].length; a > n; n++)arguments.callee(e, t[i][n]);
            data.getCache(e).nodes[data.getNodeCacheId(t.tId)] = null
        }, removeSelectedNode: function (e, t) {
            for (var i = data.getRoot(e), n = 0, a = i.curSelectedList.length; a > n; n++)if (t === i.curSelectedList[n] || !data.getNodeCache(e, i.curSelectedList[n].tId)) {
                i.curSelectedList.splice(n, 1);
                n--;
                a--
            }
        }, setCache: function (e, t) {
            caches[e.treeId] = t
        }, setRoot: function (e, t) {
            roots[e.treeId] = t
        }, setZTreeTools: function () {
            for (var e = 0, t = _init.zTreeTools.length; t > e; e++)_init.zTreeTools[e].apply(this, arguments)
        }, transformToArrayFormat: function (e, t) {
            if (!t)return [];
            var i = e.data.key.children, n = [];
            if (tools.isArray(t))for (var a = 0, s = t.length; s > a; a++) {
                n.push(t[a]);
                t[a][i] && (n = n.concat(data.transformToArrayFormat(e, t[a][i])))
            } else {
                n.push(t);
                t[i] && (n = n.concat(data.transformToArrayFormat(e, t[i])))
            }
            return n
        }, transformTozTreeFormat: function (e, t) {
            var i, n, a = e.data.simpleData.idKey, s = e.data.simpleData.pIdKey, o = e.data.key.children;
            if (!a || "" == a || !t)return [];
            if (tools.isArray(t)) {
                var r = [], l = [];
                for (i = 0, n = t.length; n > i; i++)l[t[i][a]] = t[i];
                for (i = 0, n = t.length; n > i; i++)if (l[t[i][s]] && t[i][a] != t[i][s]) {
                    l[t[i][s]][o] || (l[t[i][s]][o] = []);
                    l[t[i][s]][o].push(t[i])
                } else r.push(t[i]);
                return r
            }
            return [t]
        }
    }, event = {
        bindEvent: function () {
            for (var e = 0, t = _init.bind.length; t > e; e++)_init.bind[e].apply(this, arguments)
        }, unbindEvent: function () {
            for (var e = 0, t = _init.unbind.length; t > e; e++)_init.unbind[e].apply(this, arguments)
        }, bindTree: function (e) {
            var t = {treeId: e.treeId}, i = e.treeObj;
            e.view.txtSelectedEnable || i.bind("selectstart", function (e) {
                var t = e.originalEvent.srcElement.nodeName.toLowerCase();
                return "input" === t || "textarea" === t
            }).css({"-moz-user-select": "-moz-none"});
            i.bind("click", t, event.proxy);
            i.bind("dblclick", t, event.proxy);
            i.bind("mouseover", t, event.proxy);
            i.bind("mouseout", t, event.proxy);
            i.bind("mousedown", t, event.proxy);
            i.bind("mouseup", t, event.proxy);
            i.bind("contextmenu", t, event.proxy)
        }, unbindTree: function (e) {
            var t = e.treeObj;
            t.unbind("click", event.proxy).unbind("dblclick", event.proxy).unbind("mouseover", event.proxy).unbind("mouseout", event.proxy).unbind("mousedown", event.proxy).unbind("mouseup", event.proxy).unbind("contextmenu", event.proxy)
        }, doProxy: function () {
            for (var e = [], t = 0, i = _init.proxys.length; i > t; t++) {
                var n = _init.proxys[t].apply(this, arguments);
                e.push(n);
                if (n.stop)break
            }
            return e
        }, proxy: function (e) {
            var t = data.getSetting(e.data.treeId);
            if (!tools.uCanDo(t, e))return !0;
            for (var i = event.doProxy(e), n = !0, a = !1, s = 0, o = i.length; o > s; s++) {
                var r = i[s];
                if (r.nodeEventCallback) {
                    a = !0;
                    n = r.nodeEventCallback.apply(r, [e, r.node]) && n
                }
                if (r.treeEventCallback) {
                    a = !0;
                    n = r.treeEventCallback.apply(r, [e, r.node]) && n
                }
            }
            return n
        }
    }, handler = {
        onSwitchNode: function (e, t) {
            var i = data.getSetting(e.data.treeId);
            if (t.open) {
                if (0 == tools.apply(i.callback.beforeCollapse, [i.treeId, t], !0))return !0;
                data.getRoot(i).expandTriggerFlag = !0;
                view.switchNode(i, t)
            } else {
                if (0 == tools.apply(i.callback.beforeExpand, [i.treeId, t], !0))return !0;
                data.getRoot(i).expandTriggerFlag = !0;
                view.switchNode(i, t)
            }
            return !0
        }, onClickNode: function (e, t) {
            var i = data.getSetting(e.data.treeId), n = i.view.autoCancelSelected && (e.ctrlKey || e.metaKey) && data.isSelectedNode(i, t) ? 0 : i.view.autoCancelSelected && (e.ctrlKey || e.metaKey) && i.view.selectedMulti ? 2 : 1;
            if (0 == tools.apply(i.callback.beforeClick, [i.treeId, t, n], !0))return !0;
            0 === n ? view.cancelPreSelectedNode(i, t) : view.selectNode(i, t, 2 === n);
            i.treeObj.trigger(consts.event.CLICK, [e, i.treeId, t, n]);
            return !0
        }, onZTreeMousedown: function (e, t) {
            var i = data.getSetting(e.data.treeId);
            tools.apply(i.callback.beforeMouseDown, [i.treeId, t], !0) && tools.apply(i.callback.onMouseDown, [e, i.treeId, t]);
            return !0
        }, onZTreeMouseup: function (e, t) {
            var i = data.getSetting(e.data.treeId);
            tools.apply(i.callback.beforeMouseUp, [i.treeId, t], !0) && tools.apply(i.callback.onMouseUp, [e, i.treeId, t]);
            return !0
        }, onZTreeDblclick: function (e, t) {
            var i = data.getSetting(e.data.treeId);
            tools.apply(i.callback.beforeDblClick, [i.treeId, t], !0) && tools.apply(i.callback.onDblClick, [e, i.treeId, t]);
            return !0
        }, onZTreeContextmenu: function (e, t) {
            var i = data.getSetting(e.data.treeId);
            tools.apply(i.callback.beforeRightClick, [i.treeId, t], !0) && tools.apply(i.callback.onRightClick, [e, i.treeId, t]);
            return "function" != typeof i.callback.onRightClick
        }
    }, tools = {
        apply: function (e, t, i) {
            return "function" == typeof e ? e.apply(zt, t ? t : []) : i
        }, canAsync: function (e, t) {
            var i = e.data.key.children;
            return e.async.enable && t && t.isParent && !(t.zAsync || t[i] && t[i].length > 0)
        }, clone: function (e) {
            if (null === e)return null;
            var t = tools.isArray(e) ? [] : {};
            for (var i in e)t[i] = e[i]instanceof Date ? new Date(e[i].getTime()) : "object" == typeof e[i] ? arguments.callee(e[i]) : e[i];
            return t
        }, eqs: function (e, t) {
            return e.toLowerCase() === t.toLowerCase()
        }, isArray: function (e) {
            return "[object Array]" === Object.prototype.toString.apply(e)
        }, $: function (e, t, i) {
            if (t && "string" != typeof t) {
                i = t;
                t = ""
            }
            return "string" == typeof e ? $(e, i ? i.treeObj.get(0).ownerDocument : null) : $("#" + e.tId + t, i ? i.treeObj : null)
        }, getMDom: function (e, t, i) {
            if (!t)return null;
            for (; t && t.id !== e.treeId;) {
                for (var n = 0, a = i.length; t.tagName && a > n; n++)if (tools.eqs(t.tagName, i[n].tagName) && null !== t.getAttribute(i[n].attrName))return t;
                t = t.parentNode
            }
            return null
        }, getNodeMainDom: function (e) {
            return $(e).parent("li").get(0) || $(e).parentsUntil("li").parent().get(0)
        }, isChildOrSelf: function (e, t) {
            return $(e).closest("#" + t).length > 0
        }, uCanDo: function () {
            return !0
        }
    }, view = {
        addNodes: function (e, t, i, n) {
            if (!e.data.keep.leaf || !t || t.isParent) {
                tools.isArray(i) || (i = [i]);
                e.data.simpleData.enable && (i = data.transformTozTreeFormat(e, i));
                if (t) {
                    var a = $$(t, consts.id.SWITCH, e), s = $$(t, consts.id.ICON, e), o = $$(t, consts.id.UL, e);
                    if (!t.open) {
                        view.replaceSwitchClass(t, a, consts.folder.CLOSE);
                        view.replaceIcoClass(t, s, consts.folder.CLOSE);
                        t.open = !1;
                        o.css({display: "none"})
                    }
                    data.addNodesData(e, t, i);
                    view.createNodes(e, t.level + 1, i, t);
                    n || view.expandCollapseParentNode(e, t, !0)
                } else {
                    data.addNodesData(e, data.getRoot(e), i);
                    view.createNodes(e, 0, i, null)
                }
            }
        }, appendNodes: function (e, t, i, n, a, s) {
            if (!i)return [];
            for (var o = [], r = e.data.key.children, l = 0, d = i.length; d > l; l++) {
                var c = i[l];
                if (a) {
                    var u = n ? n : data.getRoot(e), h = u[r], f = h.length == i.length && 0 == l, g = l == i.length - 1;
                    data.initNode(e, t, c, n, f, g, s);
                    data.addNodeCache(e, c)
                }
                var p = [];
                c[r] && c[r].length > 0 && (p = view.appendNodes(e, t + 1, c[r], c, a, s && c.open));
                if (s) {
                    view.makeDOMNodeMainBefore(o, e, c);
                    view.makeDOMNodeLine(o, e, c);
                    data.getBeforeA(e, c, o);
                    view.makeDOMNodeNameBefore(o, e, c);
                    data.getInnerBeforeA(e, c, o);
                    view.makeDOMNodeIcon(o, e, c);
                    data.getInnerAfterA(e, c, o);
                    view.makeDOMNodeNameAfter(o, e, c);
                    data.getAfterA(e, c, o);
                    c.isParent && c.open && view.makeUlHtml(e, c, o, p.join(""));
                    view.makeDOMNodeMainAfter(o, e, c);
                    data.addCreatedNode(e, c)
                }
            }
            return o
        }, appendParentULDom: function (e, t) {
            var i = [], n = $$(t, e);
            if (!n.get(0) && t.parentTId) {
                view.appendParentULDom(e, t.getParentNode());
                n = $$(t, e)
            }
            var a = $$(t, consts.id.UL, e);
            a.get(0) && a.remove();
            var s = e.data.key.children, o = view.appendNodes(e, t.level + 1, t[s], t, !1, !0);
            view.makeUlHtml(e, t, i, o.join(""));
            n.append(i.join(""))
        }, asyncNode: function (setting, node, isSilent, callback) {
            var i, l;
            if (node && !node.isParent) {
                tools.apply(callback);
                return !1
            }
            if (node && node.isAjaxing)return !1;
            if (0 == tools.apply(setting.callback.beforeAsync, [setting.treeId, node], !0)) {
                tools.apply(callback);
                return !1
            }
            if (node) {
                node.isAjaxing = !0;
                var icoObj = $$(node, consts.id.ICON, setting);
                icoObj.attr({style: "", "class": consts.className.BUTTON + " " + consts.className.ICO_LOADING})
            }
            var tmpParam = {};
            for (i = 0, l = setting.async.autoParam.length; node && l > i; i++) {
                var pKey = setting.async.autoParam[i].split("="), spKey = pKey;
                if (pKey.length > 1) {
                    spKey = pKey[1];
                    pKey = pKey[0]
                }
                tmpParam[spKey] = node[pKey]
            }
            if (tools.isArray(setting.async.otherParam))for (i = 0, l = setting.async.otherParam.length; l > i; i += 2)tmpParam[setting.async.otherParam[i]] = setting.async.otherParam[i + 1]; else for (var p in setting.async.otherParam)tmpParam[p] = setting.async.otherParam[p];
            var _tmpV = data.getRoot(setting)._ver;
            $.ajax({
                contentType: setting.async.contentType,
                type: setting.async.type,
                url: tools.apply(setting.async.url, [setting.treeId, node], setting.async.url),
                data: tmpParam,
                dataType: setting.async.dataType,
                success: function (msg) {
                    if (_tmpV == data.getRoot(setting)._ver) {
                        var newNodes = [];
                        try {
                            newNodes = msg && 0 != msg.length ? "string" == typeof msg ? eval("(" + msg + ")") : msg : []
                        } catch (err) {
                            newNodes = msg
                        }
                        if (node) {
                            node.isAjaxing = null;
                            node.zAsync = !0
                        }
                        view.setNodeLineIcos(setting, node);
                        if (newNodes && "" !== newNodes) {
                            newNodes = tools.apply(setting.async.dataFilter, [setting.treeId, node, newNodes], newNodes);
                            view.addNodes(setting, node, newNodes ? tools.clone(newNodes) : [], !!isSilent)
                        } else view.addNodes(setting, node, [], !!isSilent);
                        setting.treeObj.trigger(consts.event.ASYNC_SUCCESS, [setting.treeId, node, msg]);
                        tools.apply(callback)
                    }
                },
                error: function (e, t, i) {
                    if (_tmpV == data.getRoot(setting)._ver) {
                        node && (node.isAjaxing = null);
                        view.setNodeLineIcos(setting, node);
                        setting.treeObj.trigger(consts.event.ASYNC_ERROR, [setting.treeId, node, e, t, i])
                    }
                }
            });
            return !0
        }, cancelPreSelectedNode: function (e, t) {
            for (var i = data.getRoot(e).curSelectedList, n = 0, a = i.length - 1; a >= n; a--)if (!t || t === i[a]) {
                $$(i[a], consts.id.A, e).removeClass(consts.node.CURSELECTED);
                if (t) {
                    data.removeSelectedNode(e, t);
                    break
                }
            }
            t || (data.getRoot(e).curSelectedList = [])
        }, createNodeCallback: function (e) {
            if (e.callback.onNodeCreated || e.view.addDiyDom)for (var t = data.getRoot(e); t.createdNodes.length > 0;) {
                var i = t.createdNodes.shift();
                tools.apply(e.view.addDiyDom, [e.treeId, i]);
                e.callback.onNodeCreated && e.treeObj.trigger(consts.event.NODECREATED, [e.treeId, i])
            }
        }, createNodes: function (e, t, i, n) {
            if (i && 0 != i.length) {
                var a = data.getRoot(e), s = e.data.key.children, o = !n || n.open || !!$$(n[s][0], e).get(0);
                a.createdNodes = [];
                var r = view.appendNodes(e, t, i, n, !0, o);
                if (n) {
                    var l = $$(n, consts.id.UL, e);
                    l.get(0) && l.append(r.join(""))
                } else e.treeObj.append(r.join(""));
                view.createNodeCallback(e)
            }
        }, destroy: function (e) {
            if (e) {
                data.initCache(e);
                data.initRoot(e);
                event.unbindTree(e);
                event.unbindEvent(e);
                e.treeObj.empty();
                delete settings[e.treeId]
            }
        }, expandCollapseNode: function (e, t, i, n, a) {
            var s = data.getRoot(e), o = e.data.key.children;
            if (t) {
                if (s.expandTriggerFlag) {
                    var r = a;
                    a = function () {
                        r && r();
                        t.open ? e.treeObj.trigger(consts.event.EXPAND, [e.treeId, t]) : e.treeObj.trigger(consts.event.COLLAPSE, [e.treeId, t])
                    };
                    s.expandTriggerFlag = !1
                }
                if (!t.open && t.isParent && (!$$(t, consts.id.UL, e).get(0) || t[o] && t[o].length > 0 && !$$(t[o][0], e).get(0))) {
                    view.appendParentULDom(e, t);
                    view.createNodeCallback(e)
                }
                if (t.open != i) {
                    var l = $$(t, consts.id.UL, e), d = $$(t, consts.id.SWITCH, e), c = $$(t, consts.id.ICON, e);
                    if (t.isParent) {
                        t.open = !t.open;
                        t.iconOpen && t.iconClose && c.attr("style", view.makeNodeIcoStyle(e, t));
                        if (t.open) {
                            view.replaceSwitchClass(t, d, consts.folder.OPEN);
                            view.replaceIcoClass(t, c, consts.folder.OPEN);
                            if (0 == n || "" == e.view.expandSpeed) {
                                l.show();
                                tools.apply(a, [])
                            } else if (t[o] && t[o].length > 0)l.slideDown(e.view.expandSpeed, a); else {
                                l.show();
                                tools.apply(a, [])
                            }
                        } else {
                            view.replaceSwitchClass(t, d, consts.folder.CLOSE);
                            view.replaceIcoClass(t, c, consts.folder.CLOSE);
                            if (0 != n && "" != e.view.expandSpeed && (t[o] && t[o].length > 0))l.slideUp(e.view.expandSpeed, a); else {
                                l.hide();
                                tools.apply(a, [])
                            }
                        }
                    } else tools.apply(a, [])
                } else tools.apply(a, [])
            } else tools.apply(a, [])
        }, expandCollapseParentNode: function (e, t, i, n, a) {
            if (t)if (t.parentTId) {
                view.expandCollapseNode(e, t, i, n);
                t.parentTId && view.expandCollapseParentNode(e, t.getParentNode(), i, n, a)
            } else view.expandCollapseNode(e, t, i, n, a)
        }, expandCollapseSonNode: function (e, t, i, n, a) {
            var s = data.getRoot(e), o = e.data.key.children, r = t ? t[o] : s[o], l = t ? !1 : n, d = data.getRoot(e).expandTriggerFlag;
            data.getRoot(e).expandTriggerFlag = !1;
            if (r)for (var c = 0, u = r.length; u > c; c++)r[c] && view.expandCollapseSonNode(e, r[c], i, l);
            data.getRoot(e).expandTriggerFlag = d;
            view.expandCollapseNode(e, t, i, n, a)
        }, makeDOMNodeIcon: function (e, t, i) {
            var n = data.getNodeName(t, i), a = t.view.nameIsHTML ? n : n.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
            e.push("<span id='", i.tId, consts.id.ICON, "' title='' treeNode", consts.id.ICON, " class='", view.makeNodeIcoClass(t, i), "' style='", view.makeNodeIcoStyle(t, i), "'></span><span id='", i.tId, consts.id.SPAN, "'>", a, "</span>")
        }, makeDOMNodeLine: function (e, t, i) {
            e.push("<span id='", i.tId, consts.id.SWITCH, "' title='' class='", view.makeNodeLineClass(t, i), "' treeNode", consts.id.SWITCH, "></span>")
        }, makeDOMNodeMainAfter: function (e) {
            e.push("</li>")
        }, makeDOMNodeMainBefore: function (e, t, i) {
            e.push("<li id='", i.tId, "' class='", consts.className.LEVEL, i.level, "' tabindex='0' hidefocus='true' treenode>")
        }, makeDOMNodeNameAfter: function (e) {
            e.push("</a>")
        }, makeDOMNodeNameBefore: function (e, t, i) {
            var n = data.getNodeTitle(t, i), a = view.makeNodeUrl(t, i), s = view.makeNodeFontCss(t, i), o = [];
            for (var r in s)o.push(r, ":", s[r], ";");
            e.push("<a id='", i.tId, consts.id.A, "' class='", consts.className.LEVEL, i.level, "' treeNode", consts.id.A, ' onclick="', i.click || "", '" ', null != a && a.length > 0 ? "href='" + a + "'" : "", " target='", view.makeNodeTarget(i), "' style='", o.join(""), "'");
            tools.apply(t.view.showTitle, [t.treeId, i], t.view.showTitle) && n && e.push("title='", n.replace(/'/g, "&#39;").replace(/</g, "&lt;").replace(/>/g, "&gt;"), "'");
            e.push(">")
        }, makeNodeFontCss: function (e, t) {
            var i = tools.apply(e.view.fontCss, [e.treeId, t], e.view.fontCss);
            return i && "function" != typeof i ? i : {}
        }, makeNodeIcoClass: function (e, t) {
            var i = ["ico"];
            if (!t.isAjaxing) {
                i[0] = (t.iconSkin ? t.iconSkin + "_" : "") + i[0];
                i.push(t.isParent ? t.open ? consts.folder.OPEN : consts.folder.CLOSE : consts.folder.DOCU)
            }
            return consts.className.BUTTON + " " + i.join("_")
        }, makeNodeIcoStyle: function (e, t) {
            var i = [];
            if (!t.isAjaxing) {
                var n = t.isParent && t.iconOpen && t.iconClose ? t.open ? t.iconOpen : t.iconClose : t.icon;
                n && i.push("background:url(", n, ") 0 0 no-repeat;");
                0 != e.view.showIcon && tools.apply(e.view.showIcon, [e.treeId, t], !0) || i.push("width:0px;height:0px;")
            }
            return i.join("")
        }, makeNodeLineClass: function (e, t) {
            var i = [];
            i.push(e.view.showLine ? 0 == t.level && t.isFirstNode && t.isLastNode ? consts.line.ROOT : 0 == t.level && t.isFirstNode ? consts.line.ROOTS : t.isLastNode ? consts.line.BOTTOM : consts.line.CENTER : consts.line.NOLINE);
            i.push(t.isParent ? t.open ? consts.folder.OPEN : consts.folder.CLOSE : consts.folder.DOCU);
            return view.makeNodeLineClassEx(t) + i.join("_")
        }, makeNodeLineClassEx: function (e) {
            return consts.className.BUTTON + " " + consts.className.LEVEL + e.level + " " + consts.className.SWITCH + " "
        }, makeNodeTarget: function (e) {
            return e.target || "_blank"
        }, makeNodeUrl: function (e, t) {
            var i = e.data.key.url;
            return t[i] ? t[i] : null
        }, makeUlHtml: function (e, t, i, n) {
            i.push("<ul id='", t.tId, consts.id.UL, "' class='", consts.className.LEVEL, t.level, " ", view.makeUlLineClass(e, t), "' style='display:", t.open ? "block" : "none", "'>");
            i.push(n);
            i.push("</ul>")
        }, makeUlLineClass: function (e, t) {
            return e.view.showLine && !t.isLastNode ? consts.line.LINE : ""
        }, removeChildNodes: function (e, t) {
            if (t) {
                var i = e.data.key.children, n = t[i];
                if (n) {
                    for (var a = 0, s = n.length; s > a; a++)data.removeNodeCache(e, n[a]);
                    data.removeSelectedNode(e);
                    delete t[i];
                    if (e.data.keep.parent)$$(t, consts.id.UL, e).empty(); else {
                        t.isParent = !1;
                        t.open = !1;
                        var o = $$(t, consts.id.SWITCH, e), r = $$(t, consts.id.ICON, e);
                        view.replaceSwitchClass(t, o, consts.folder.DOCU);
                        view.replaceIcoClass(t, r, consts.folder.DOCU);
                        $$(t, consts.id.UL, e).remove()
                    }
                }
            }
        }, setFirstNode: function (e, t) {
            var i = e.data.key.children, n = t[i].length;
            n > 0 && (t[i][0].isFirstNode = !0)
        }, setLastNode: function (e, t) {
            var i = e.data.key.children, n = t[i].length;
            n > 0 && (t[i][n - 1].isLastNode = !0)
        }, removeNode: function (e, t) {
            var i = data.getRoot(e), n = e.data.key.children, a = t.parentTId ? t.getParentNode() : i;
            t.isFirstNode = !1;
            t.isLastNode = !1;
            t.getPreNode = function () {
                return null
            };
            t.getNextNode = function () {
                return null
            };
            if (data.getNodeCache(e, t.tId)) {
                $$(t, e).remove();
                data.removeNodeCache(e, t);
                data.removeSelectedNode(e, t);
                for (var s = 0, o = a[n].length; o > s; s++)if (a[n][s].tId == t.tId) {
                    a[n].splice(s, 1);
                    break
                }
                view.setFirstNode(e, a);
                view.setLastNode(e, a);
                var r, l, d, c = a[n].length;
                if (e.data.keep.parent || 0 != c) {
                    if (e.view.showLine && c > 0) {
                        var u = a[n][c - 1];
                        r = $$(u, consts.id.UL, e);
                        l = $$(u, consts.id.SWITCH, e);
                        d = $$(u, consts.id.ICON, e);
                        if (a == i)if (1 == a[n].length)view.replaceSwitchClass(u, l, consts.line.ROOT); else {
                            var h = $$(a[n][0], consts.id.SWITCH, e);
                            view.replaceSwitchClass(a[n][0], h, consts.line.ROOTS);
                            view.replaceSwitchClass(u, l, consts.line.BOTTOM)
                        } else view.replaceSwitchClass(u, l, consts.line.BOTTOM);
                        r.removeClass(consts.line.LINE)
                    }
                } else {
                    a.isParent = !1;
                    a.open = !1;
                    r = $$(a, consts.id.UL, e);
                    l = $$(a, consts.id.SWITCH, e);
                    d = $$(a, consts.id.ICON, e);
                    view.replaceSwitchClass(a, l, consts.folder.DOCU);
                    view.replaceIcoClass(a, d, consts.folder.DOCU);
                    r.css("display", "none")
                }
            }
        }, replaceIcoClass: function (e, t, i) {
            if (t && !e.isAjaxing) {
                var n = t.attr("class");
                if (void 0 != n) {
                    var a = n.split("_");
                    switch (i) {
                        case consts.folder.OPEN:
                        case consts.folder.CLOSE:
                        case consts.folder.DOCU:
                            a[a.length - 1] = i
                    }
                    t.attr("class", a.join("_"))
                }
            }
        }, replaceSwitchClass: function (e, t, i) {
            if (t) {
                var n = t.attr("class");
                if (void 0 != n) {
                    var a = n.split("_");
                    switch (i) {
                        case consts.line.ROOT:
                        case consts.line.ROOTS:
                        case consts.line.CENTER:
                        case consts.line.BOTTOM:
                        case consts.line.NOLINE:
                            a[0] = view.makeNodeLineClassEx(e) + i;
                            break;
                        case consts.folder.OPEN:
                        case consts.folder.CLOSE:
                        case consts.folder.DOCU:
                            a[1] = i
                    }
                    t.attr("class", a.join("_"));
                    i !== consts.folder.DOCU ? t.removeAttr("disabled") : t.attr("disabled", "disabled")
                }
            }
        }, selectNode: function (e, t, i) {
            i || view.cancelPreSelectedNode(e);
            $$(t, consts.id.A, e).addClass(consts.node.CURSELECTED);
            data.addSelectedNode(e, t)
        }, setNodeFontCss: function (e, t) {
            var i = $$(t, consts.id.A, e), n = view.makeNodeFontCss(e, t);
            n && i.css(n)
        }, setNodeLineIcos: function (e, t) {
            if (t) {
                var i = $$(t, consts.id.SWITCH, e), n = $$(t, consts.id.UL, e), a = $$(t, consts.id.ICON, e), s = view.makeUlLineClass(e, t);
                0 == s.length ? n.removeClass(consts.line.LINE) : n.addClass(s);
                i.attr("class", view.makeNodeLineClass(e, t));
                t.isParent ? i.removeAttr("disabled") : i.attr("disabled", "disabled");
                a.removeAttr("style");
                a.attr("style", view.makeNodeIcoStyle(e, t));
                a.attr("class", view.makeNodeIcoClass(e, t))
            }
        }, setNodeName: function (e, t) {
            var i = data.getNodeTitle(e, t), n = $$(t, consts.id.SPAN, e);
            n.empty();
            e.view.nameIsHTML ? n.html(data.getNodeName(e, t)) : n.text(data.getNodeName(e, t));
            if (tools.apply(e.view.showTitle, [e.treeId, t], e.view.showTitle)) {
                var a = $$(t, consts.id.A, e);
                a.attr("title", i ? i : "")
            }
        }, setNodeTarget: function (e, t) {
            var i = $$(t, consts.id.A, e);
            i.attr("target", view.makeNodeTarget(t))
        }, setNodeUrl: function (e, t) {
            var i = $$(t, consts.id.A, e), n = view.makeNodeUrl(e, t);
            null == n || 0 == n.length ? i.removeAttr("href") : i.attr("href", n)
        }, switchNode: function (e, t) {
            if (t.open || !tools.canAsync(e, t))view.expandCollapseNode(e, t, !t.open); else if (e.async.enable) {
                if (!view.asyncNode(e, t)) {
                    view.expandCollapseNode(e, t, !t.open);
                    return
                }
            } else t && view.expandCollapseNode(e, t, !t.open)
        }
    };
    $.fn.zTree = {
        consts: _consts, _z: {tools: tools, view: view, event: event, data: data}, getZTreeObj: function (e) {
            var t = data.getZTreeTools(e);
            return t ? t : null
        }, destroy: function (e) {
            if (e && e.length > 0)view.destroy(data.getSetting(e)); else for (var t in settings)view.destroy(settings[t])
        }, init: function (e, t, i) {
            var n = tools.clone(_setting);
            $.extend(!0, n, t);
            n.treeId = e.attr("id");
            n.treeObj = e;
            n.treeObj.empty();
            settings[n.treeId] = n;
            "undefined" == typeof document.body.style.maxHeight && (n.view.expandSpeed = "");
            data.initRoot(n);
            var a = data.getRoot(n), s = n.data.key.children;
            i = i ? tools.clone(tools.isArray(i) ? i : [i]) : [];
            a[s] = n.data.simpleData.enable ? data.transformTozTreeFormat(n, i) : i;
            data.initCache(n);
            event.unbindTree(n);
            event.bindTree(n);
            event.unbindEvent(n);
            event.bindEvent(n);
            var o = {
                setting: n, addNodes: function (e, t, i) {
                    function a() {
                        view.addNodes(n, e, s, 1 == i)
                    }

                    if (!t)return null;
                    e || (e = null);
                    if (e && !e.isParent && n.data.keep.leaf)return null;
                    var s = tools.clone(tools.isArray(t) ? t : [t]);
                    tools.canAsync(n, e) ? view.asyncNode(n, e, i, a) : a();
                    return s
                }, cancelSelectedNode: function (e) {
                    view.cancelPreSelectedNode(n, e)
                }, destroy: function () {
                    view.destroy(n)
                }, expandAll: function (e) {
                    e = !!e;
                    view.expandCollapseSonNode(n, null, e, !0);
                    return e
                }, expandNode: function (e, t, i, a, s) {
                    if (!e || !e.isParent)return null;
                    t !== !0 && t !== !1 && (t = !e.open);
                    s = !!s;
                    if (s && t && 0 == tools.apply(n.callback.beforeExpand, [n.treeId, e], !0))return null;
                    if (s && !t && 0 == tools.apply(n.callback.beforeCollapse, [n.treeId, e], !0))return null;
                    t && e.parentTId && view.expandCollapseParentNode(n, e.getParentNode(), t, !1);
                    if (t === e.open && !i)return null;
                    data.getRoot(n).expandTriggerFlag = s;
                    if (!tools.canAsync(n, e) && i)view.expandCollapseSonNode(n, e, t, !0, function () {
                        if (a !== !1)try {
                            $$(e, n).focus().blur()
                        } catch (t) {
                        }
                    }); else {
                        e.open = !t;
                        view.switchNode(this.setting, e);
                        if (a !== !1)try {
                            $$(e, n).focus().blur()
                        } catch (o) {
                        }
                    }
                    return t
                }, getNodes: function () {
                    return data.getNodes(n)
                }, getNodeByParam: function (e, t, i) {
                    return e ? data.getNodeByParam(n, i ? i[n.data.key.children] : data.getNodes(n), e, t) : null
                }, getNodeByTId: function (e) {
                    return data.getNodeCache(n, e)
                }, getNodesByParam: function (e, t, i) {
                    return e ? data.getNodesByParam(n, i ? i[n.data.key.children] : data.getNodes(n), e, t) : null
                }, getNodesByParamFuzzy: function (e, t, i) {
                    return e ? data.getNodesByParamFuzzy(n, i ? i[n.data.key.children] : data.getNodes(n), e, t) : null
                }, getNodesByFilter: function (e, t, i, a) {
                    t = !!t;
                    return e && "function" == typeof e ? data.getNodesByFilter(n, i ? i[n.data.key.children] : data.getNodes(n), e, t, a) : t ? null : []
                }, getNodeIndex: function (e) {
                    if (!e)return null;
                    for (var t = n.data.key.children, i = e.parentTId ? e.getParentNode() : data.getRoot(n), a = 0, s = i[t].length; s > a; a++)if (i[t][a] == e)return a;
                    return -1
                }, getSelectedNodes: function () {
                    for (var e = [], t = data.getRoot(n).curSelectedList, i = 0, a = t.length; a > i; i++)e.push(t[i]);
                    return e
                }, isSelectedNode: function (e) {
                    return data.isSelectedNode(n, e)
                }, reAsyncChildNodes: function (e, t, i) {
                    if (this.setting.async.enable) {
                        var a = !e;
                        a && (e = data.getRoot(n));
                        if ("refresh" == t) {
                            for (var s = this.setting.data.key.children, o = 0, r = e[s] ? e[s].length : 0; r > o; o++)data.removeNodeCache(n, e[s][o]);
                            data.removeSelectedNode(n);
                            e[s] = [];
                            if (a)this.setting.treeObj.empty(); else {
                                var l = $$(e, consts.id.UL, n);
                                l.empty()
                            }
                        }
                        view.asyncNode(this.setting, a ? null : e, !!i)
                    }
                }, refresh: function () {
                    this.setting.treeObj.empty();
                    var e = data.getRoot(n), t = e[n.data.key.children];
                    data.initRoot(n);
                    e[n.data.key.children] = t;
                    data.initCache(n);
                    view.createNodes(n, 0, e[n.data.key.children])
                }, removeChildNodes: function (e) {
                    if (!e)return null;
                    var t = n.data.key.children, i = e[t];
                    view.removeChildNodes(n, e);
                    return i ? i : null
                }, removeNode: function (e, t) {
                    if (e) {
                        t = !!t;
                        if (!t || 0 != tools.apply(n.callback.beforeRemove, [n.treeId, e], !0)) {
                            view.removeNode(n, e);
                            t && this.setting.treeObj.trigger(consts.event.REMOVE, [n.treeId, e])
                        }
                    }
                }, selectNode: function (e, t) {
                    if (e && tools.uCanDo(n)) {
                        t = n.view.selectedMulti && t;
                        if (e.parentTId)view.expandCollapseParentNode(n, e.getParentNode(), !0, !1, function () {
                            try {
                                $$(e, n).focus().blur()
                            } catch (t) {
                            }
                        }); else try {
                            $$(e, n).focus().blur()
                        } catch (i) {
                        }
                        view.selectNode(n, e, t)
                    }
                }, transformTozTreeNodes: function (e) {
                    return data.transformTozTreeFormat(n, e)
                }, transformToArray: function (e) {
                    return data.transformToArrayFormat(n, e)
                }, updateNode: function (e) {
                    if (e) {
                        var t = $$(e, n);
                        if (t.get(0) && tools.uCanDo(n)) {
                            view.setNodeName(n, e);
                            view.setNodeTarget(n, e);
                            view.setNodeUrl(n, e);
                            view.setNodeLineIcos(n, e);
                            view.setNodeFontCss(n, e)
                        }
                    }
                }
            };
            a.treeTools = o;
            data.setZTreeTools(n, o);
            a[s] && a[s].length > 0 ? view.createNodes(n, 0, a[s]) : n.async.enable && n.async.url && "" !== n.async.url && view.asyncNode(n);
            return o
        }
    };
    var zt = $.fn.zTree, $$ = tools.$, consts = zt.consts
}(jQuery);
!function (e) {
    var t = {
        event: {CHECK: "ztree_check"},
        id: {CHECK: "_check"},
        checkbox: {
            STYLE: "checkbox",
            DEFAULT: "chk",
            DISABLED: "disable",
            FALSE: "false",
            TRUE: "true",
            FULL: "full",
            PART: "part",
            FOCUS: "focus"
        },
        radio: {STYLE: "radio", TYPE_ALL: "all", TYPE_LEVEL: "level"}
    }, i = {
        check: {
            enable: !1,
            autoCheckTrigger: !1,
            chkStyle: t.checkbox.STYLE,
            nocheckInherit: !1,
            chkDisabledInherit: !1,
            radioType: t.radio.TYPE_LEVEL,
            chkboxType: {Y: "ps", N: "ps"}
        }, data: {key: {checked: "checked"}}, callback: {beforeCheck: null, onCheck: null}
    }, n = function (e) {
        var t = b.getRoot(e);
        t.radioCheckedList = []
    }, a = function () {
    }, s = function (e) {
        var t = e.treeObj, i = C.event;
        t.bind(i.CHECK, function (t, i, n, a) {
            t.srcEvent = i;
            N.apply(e.callback.onCheck, [t, n, a])
        })
    }, o = function (e) {
        var t = e.treeObj, i = C.event;
        t.unbind(i.CHECK)
    }, r = function (e) {
        var t = e.target, i = b.getSetting(e.data.treeId), n = "", a = null, s = "", o = "", r = null, l = null;
        if (N.eqs(e.type, "mouseover")) {
            if (i.check.enable && N.eqs(t.tagName, "span") && null !== t.getAttribute("treeNode" + C.id.CHECK)) {
                n = N.getNodeMainDom(t).id;
                s = "mouseoverCheck"
            }
        } else if (N.eqs(e.type, "mouseout")) {
            if (i.check.enable && N.eqs(t.tagName, "span") && null !== t.getAttribute("treeNode" + C.id.CHECK)) {
                n = N.getNodeMainDom(t).id;
                s = "mouseoutCheck"
            }
        } else if (N.eqs(e.type, "click") && i.check.enable && N.eqs(t.tagName, "span") && null !== t.getAttribute("treeNode" + C.id.CHECK)) {
            n = N.getNodeMainDom(t).id;
            s = "checkNode"
        }
        if (n.length > 0) {
            a = b.getNodeCache(i, n);
            switch (s) {
                case"checkNode":
                    r = f.onCheckNode;
                    break;
                case"mouseoverCheck":
                    r = f.onMouseoverCheck;
                    break;
                case"mouseoutCheck":
                    r = f.onMouseoutCheck
            }
        }
        var d = {
            stop: "checkNode" === s,
            node: a,
            nodeEventType: s,
            nodeEventCallback: r,
            treeEventType: o,
            treeEventCallback: l
        };
        return d
    }, l = function (e, t, i, n) {
        if (i) {
            var a = e.data.key.checked;
            "string" == typeof i[a] && (i[a] = N.eqs(i[a], "true"));
            i[a] = !!i[a];
            i.checkedOld = i[a];
            "string" == typeof i.nocheck && (i.nocheck = N.eqs(i.nocheck, "true"));
            i.nocheck = !!i.nocheck || e.check.nocheckInherit && n && !!n.nocheck;
            "string" == typeof i.chkDisabled && (i.chkDisabled = N.eqs(i.chkDisabled, "true"));
            i.chkDisabled = !!i.chkDisabled || e.check.chkDisabledInherit && n && !!n.chkDisabled;
            "string" == typeof i.halfCheck && (i.halfCheck = N.eqs(i.halfCheck, "true"));
            i.halfCheck = !!i.halfCheck;
            i.check_Child_State = -1;
            i.check_Focus = !1;
            i.getCheckStatus = function () {
                return b.getCheckStatus(e, i)
            };
            if (e.check.chkStyle == C.radio.STYLE && e.check.radioType == C.radio.TYPE_ALL && i[a]) {
                var s = b.getRoot(e);
                s.radioCheckedList.push(i)
            }
        }
    }, d = function (e, t, i) {
        e.data.key.checked;
        if (e.check.enable) {
            b.makeChkFlag(e, t);
            i.push("<span ID='", t.tId, C.id.CHECK, "' class='", y.makeChkClass(e, t), "' treeNode", C.id.CHECK, t.nocheck === !0 ? " style='display:none;'" : "", "></span>")
        }
    }, c = function (e, t) {
        t.checkNode = function (e, t, i, n) {
            var a = this.setting.data.key.checked;
            if (e.chkDisabled !== !0) {
                t !== !0 && t !== !1 && (t = !e[a]);
                n = !!n;
                if ((e[a] !== t || i) && (!n || 0 != N.apply(this.setting.callback.beforeCheck, [this.setting.treeId, e], !0)) && N.uCanDo(this.setting) && this.setting.check.enable && e.nocheck !== !0) {
                    e[a] = t;
                    var s = k(e, C.id.CHECK, this.setting);
                    (i || this.setting.check.chkStyle === C.radio.STYLE) && y.checkNodeRelation(this.setting, e);
                    y.setChkClass(this.setting, s, e);
                    y.repairParentChkClassWithSelf(this.setting, e);
                    n && this.setting.treeObj.trigger(C.event.CHECK, [null, this.setting.treeId, e])
                }
            }
        };
        t.checkAllNodes = function (e) {
            y.repairAllChk(this.setting, !!e)
        };
        t.getCheckedNodes = function (e) {
            var t = this.setting.data.key.children;
            e = e !== !1;
            return b.getTreeCheckedNodes(this.setting, b.getRoot(this.setting)[t], e)
        };
        t.getChangeCheckedNodes = function () {
            var e = this.setting.data.key.children;
            return b.getTreeChangeCheckedNodes(this.setting, b.getRoot(this.setting)[e])
        };
        t.setChkDisabled = function (e, t, i, n) {
            t = !!t;
            i = !!i;
            n = !!n;
            y.repairSonChkDisabled(this.setting, e, t, n);
            y.repairParentChkDisabled(this.setting, e.getParentNode(), t, i)
        };
        var i = t.updateNode;
        t.updateNode = function (e, n) {
            i && i.apply(t, arguments);
            if (e && this.setting.check.enable) {
                var a = k(e, this.setting);
                if (a.get(0) && N.uCanDo(this.setting)) {
                    var s = k(e, C.id.CHECK, this.setting);
                    (1 == n || this.setting.check.chkStyle === C.radio.STYLE) && y.checkNodeRelation(this.setting, e);
                    y.setChkClass(this.setting, s, e);
                    y.repairParentChkClassWithSelf(this.setting, e)
                }
            }
        }
    }, u = {
        getRadioCheckedList: function (e) {
            for (var t = b.getRoot(e).radioCheckedList, i = 0, n = t.length; n > i; i++)if (!b.getNodeCache(e, t[i].tId)) {
                t.splice(i, 1);
                i--;
                n--
            }
            return t
        }, getCheckStatus: function (e, t) {
            if (!e.check.enable || t.nocheck || t.chkDisabled)return null;
            var i = e.data.key.checked, n = {
                checked: t[i],
                half: t.halfCheck ? t.halfCheck : e.check.chkStyle == C.radio.STYLE ? 2 === t.check_Child_State : t[i] ? t.check_Child_State > -1 && t.check_Child_State < 2 : t.check_Child_State > 0
            };
            return n
        }, getTreeCheckedNodes: function (e, t, i, n) {
            if (!t)return [];
            var a = e.data.key.children, s = e.data.key.checked, o = i && e.check.chkStyle == C.radio.STYLE && e.check.radioType == C.radio.TYPE_ALL;
            n = n ? n : [];
            for (var r = 0, l = t.length; l > r; r++) {
                if (t[r].nocheck !== !0 && t[r].chkDisabled !== !0 && t[r][s] == i) {
                    n.push(t[r]);
                    if (o)break
                }
                b.getTreeCheckedNodes(e, t[r][a], i, n);
                if (o && n.length > 0)break
            }
            return n
        }, getTreeChangeCheckedNodes: function (e, t, i) {
            if (!t)return [];
            var n = e.data.key.children, a = e.data.key.checked;
            i = i ? i : [];
            for (var s = 0, o = t.length; o > s; s++) {
                t[s].nocheck !== !0 && t[s].chkDisabled !== !0 && t[s][a] != t[s].checkedOld && i.push(t[s]);
                b.getTreeChangeCheckedNodes(e, t[s][n], i)
            }
            return i
        }, makeChkFlag: function (e, t) {
            if (t) {
                var i = e.data.key.children, n = e.data.key.checked, a = -1;
                if (t[i])for (var s = 0, o = t[i].length; o > s; s++) {
                    var r = t[i][s], l = -1;
                    if (e.check.chkStyle == C.radio.STYLE) {
                        l = r.nocheck === !0 || r.chkDisabled === !0 ? r.check_Child_State : r.halfCheck === !0 ? 2 : r[n] ? 2 : r.check_Child_State > 0 ? 2 : 0;
                        if (2 == l) {
                            a = 2;
                            break
                        }
                        0 == l && (a = 0)
                    } else if (e.check.chkStyle == C.checkbox.STYLE) {
                        l = r.nocheck === !0 || r.chkDisabled === !0 ? r.check_Child_State : r.halfCheck === !0 ? 1 : r[n] ? -1 === r.check_Child_State || 2 === r.check_Child_State ? 2 : 1 : r.check_Child_State > 0 ? 1 : 0;
                        if (1 === l) {
                            a = 1;
                            break
                        }
                        if (2 === l && a > -1 && s > 0 && l !== a) {
                            a = 1;
                            break
                        }
                        if (2 === a && l > -1 && 2 > l) {
                            a = 1;
                            break
                        }
                        l > -1 && (a = l)
                    }
                }
                t.check_Child_State = a
            }
        }
    }, h = {}, f = {
        onCheckNode: function (e, t) {
            if (t.chkDisabled === !0)return !1;
            var i = b.getSetting(e.data.treeId), n = i.data.key.checked;
            if (0 == N.apply(i.callback.beforeCheck, [i.treeId, t], !0))return !0;
            t[n] = !t[n];
            y.checkNodeRelation(i, t);
            var a = k(t, C.id.CHECK, i);
            y.setChkClass(i, a, t);
            y.repairParentChkClassWithSelf(i, t);
            i.treeObj.trigger(C.event.CHECK, [e, i.treeId, t]);
            return !0
        }, onMouseoverCheck: function (e, t) {
            if (t.chkDisabled === !0)return !1;
            var i = b.getSetting(e.data.treeId), n = k(t, C.id.CHECK, i);
            t.check_Focus = !0;
            y.setChkClass(i, n, t);
            return !0
        }, onMouseoutCheck: function (e, t) {
            if (t.chkDisabled === !0)return !1;
            var i = b.getSetting(e.data.treeId), n = k(t, C.id.CHECK, i);
            t.check_Focus = !1;
            y.setChkClass(i, n, t);
            return !0
        }
    }, g = {}, p = {
        checkNodeRelation: function (e, t) {
            var i, n, a, s = e.data.key.children, o = e.data.key.checked, r = C.radio;
            if (e.check.chkStyle == r.STYLE) {
                var l = b.getRadioCheckedList(e);
                if (t[o])if (e.check.radioType == r.TYPE_ALL) {
                    for (n = l.length - 1; n >= 0; n--) {
                        i = l[n];
                        if (i[o] && i != t) {
                            i[o] = !1;
                            l.splice(n, 1);
                            y.setChkClass(e, k(i, C.id.CHECK, e), i);
                            i.parentTId != t.parentTId && y.repairParentChkClassWithSelf(e, i)
                        }
                    }
                    l.push(t)
                } else {
                    var d = t.parentTId ? t.getParentNode() : b.getRoot(e);
                    for (n = 0, a = d[s].length; a > n; n++) {
                        i = d[s][n];
                        if (i[o] && i != t) {
                            i[o] = !1;
                            y.setChkClass(e, k(i, C.id.CHECK, e), i)
                        }
                    }
                } else if (e.check.radioType == r.TYPE_ALL)for (n = 0, a = l.length; a > n; n++)if (t == l[n]) {
                    l.splice(n, 1);
                    break
                }
            } else {
                t[o] && (!t[s] || 0 == t[s].length || e.check.chkboxType.Y.indexOf("s") > -1) && y.setSonNodeCheckBox(e, t, !0);
                t[o] || t[s] && 0 != t[s].length && !(e.check.chkboxType.N.indexOf("s") > -1) || y.setSonNodeCheckBox(e, t, !1);
                t[o] && e.check.chkboxType.Y.indexOf("p") > -1 && y.setParentNodeCheckBox(e, t, !0);
                !t[o] && e.check.chkboxType.N.indexOf("p") > -1 && y.setParentNodeCheckBox(e, t, !1)
            }
        }, makeChkClass: function (e, t) {
            var i = e.data.key.checked, n = C.checkbox, a = C.radio, s = "";
            s = t.chkDisabled === !0 ? n.DISABLED : t.halfCheck ? n.PART : e.check.chkStyle == a.STYLE ? t.check_Child_State < 1 ? n.FULL : n.PART : t[i] ? 2 === t.check_Child_State || -1 === t.check_Child_State ? n.FULL : n.PART : t.check_Child_State < 1 ? n.FULL : n.PART;
            var o = e.check.chkStyle + "_" + (t[i] ? n.TRUE : n.FALSE) + "_" + s;
            o = t.check_Focus && t.chkDisabled !== !0 ? o + "_" + n.FOCUS : o;
            return C.className.BUTTON + " " + n.DEFAULT + " " + o
        }, repairAllChk: function (e, t) {
            if (e.check.enable && e.check.chkStyle === C.checkbox.STYLE)for (var i = e.data.key.checked, n = e.data.key.children, a = b.getRoot(e), s = 0, o = a[n].length; o > s; s++) {
                var r = a[n][s];
                r.nocheck !== !0 && r.chkDisabled !== !0 && (r[i] = t);
                y.setSonNodeCheckBox(e, r, t)
            }
        }, repairChkClass: function (e, t) {
            if (t) {
                b.makeChkFlag(e, t);
                if (t.nocheck !== !0) {
                    var i = k(t, C.id.CHECK, e);
                    y.setChkClass(e, i, t)
                }
            }
        }, repairParentChkClass: function (e, t) {
            if (t && t.parentTId) {
                var i = t.getParentNode();
                y.repairChkClass(e, i);
                y.repairParentChkClass(e, i)
            }
        }, repairParentChkClassWithSelf: function (e, t) {
            if (t) {
                var i = e.data.key.children;
                t[i] && t[i].length > 0 ? y.repairParentChkClass(e, t[i][0]) : y.repairParentChkClass(e, t)
            }
        }, repairSonChkDisabled: function (e, t, i, n) {
            if (t) {
                var a = e.data.key.children;
                t.chkDisabled != i && (t.chkDisabled = i);
                y.repairChkClass(e, t);
                if (t[a] && n)for (var s = 0, o = t[a].length; o > s; s++) {
                    var r = t[a][s];
                    y.repairSonChkDisabled(e, r, i, n)
                }
            }
        }, repairParentChkDisabled: function (e, t, i, n) {
            if (t) {
                t.chkDisabled != i && n && (t.chkDisabled = i);
                y.repairChkClass(e, t);
                y.repairParentChkDisabled(e, t.getParentNode(), i, n)
            }
        }, setChkClass: function (e, t, i) {
            if (t) {
                i.nocheck === !0 ? t.hide() : t.show();
                t.attr("class", y.makeChkClass(e, i))
            }
        }, setParentNodeCheckBox: function (e, t, i, n) {
            var a = e.data.key.children, s = e.data.key.checked, o = k(t, C.id.CHECK, e);
            n || (n = t);
            b.makeChkFlag(e, t);
            if (t.nocheck !== !0 && t.chkDisabled !== !0) {
                t[s] = i;
                y.setChkClass(e, o, t);
                e.check.autoCheckTrigger && t != n && e.treeObj.trigger(C.event.CHECK, [null, e.treeId, t])
            }
            if (t.parentTId) {
                var r = !0;
                if (!i)for (var l = t.getParentNode()[a], d = 0, c = l.length; c > d; d++)if (l[d].nocheck !== !0 && l[d].chkDisabled !== !0 && l[d][s] || (l[d].nocheck === !0 || l[d].chkDisabled === !0) && l[d].check_Child_State > 0) {
                    r = !1;
                    break
                }
                r && y.setParentNodeCheckBox(e, t.getParentNode(), i, n)
            }
        }, setSonNodeCheckBox: function (e, t, i, n) {
            if (t) {
                var a = e.data.key.children, s = e.data.key.checked, o = k(t, C.id.CHECK, e);
                n || (n = t);
                var r = !1;
                if (t[a])for (var l = 0, d = t[a].length; d > l && t.chkDisabled !== !0; l++) {
                    var c = t[a][l];
                    y.setSonNodeCheckBox(e, c, i, n);
                    c.chkDisabled === !0 && (r = !0)
                }
                if (t != b.getRoot(e) && t.chkDisabled !== !0) {
                    r && t.nocheck !== !0 && b.makeChkFlag(e, t);
                    if (t.nocheck !== !0 && t.chkDisabled !== !0) {
                        t[s] = i;
                        r || (t.check_Child_State = t[a] && t[a].length > 0 ? i ? 2 : 0 : -1)
                    } else t.check_Child_State = -1;
                    y.setChkClass(e, o, t);
                    e.check.autoCheckTrigger && t != n && t.nocheck !== !0 && t.chkDisabled !== !0 && e.treeObj.trigger(C.event.CHECK, [null, e.treeId, t])
                }
            }
        }
    }, v = {tools: g, view: p, event: h, data: u};
    e.extend(!0, e.fn.zTree.consts, t);
    e.extend(!0, e.fn.zTree._z, v);
    var m = e.fn.zTree, N = m._z.tools, C = m.consts, y = m._z.view, b = m._z.data, k = (m._z.event, N.$);
    b.exSetting(i);
    b.addInitBind(s);
    b.addInitUnBind(o);
    b.addInitCache(a);
    b.addInitNode(l);
    b.addInitProxy(r, !0);
    b.addInitRoot(n);
    b.addBeforeA(d);
    b.addZTreeTools(c);
    var w = y.createNodes;
    y.createNodes = function (e, t, i, n) {
        w && w.apply(y, arguments);
        i && y.repairParentChkClassWithSelf(e, n)
    };
    var T = y.removeNode;
    y.removeNode = function (e, t) {
        var i = t.getParentNode();
        T && T.apply(y, arguments);
        if (t && i) {
            y.repairChkClass(e, i);
            y.repairParentChkClass(e, i)
        }
    };
    var D = y.appendNodes;
    y.appendNodes = function (e, t, i, n) {
        var a = "";
        D && (a = D.apply(y, arguments));
        n && b.makeChkFlag(e, n);
        return a
    }
}(jQuery);
!function (e) {
    var t = {
        event: {DRAG: "ztree_drag", DROP: "ztree_drop", RENAME: "ztree_rename", DRAGMOVE: "ztree_dragmove"},
        id: {EDIT: "_edit", INPUT: "_input", REMOVE: "_remove"},
        move: {TYPE_INNER: "inner", TYPE_PREV: "prev", TYPE_NEXT: "next"},
        node: {
            CURSELECTED_EDIT: "curSelectedNode_Edit",
            TMPTARGET_TREE: "tmpTargetzTree",
            TMPTARGET_NODE: "tmpTargetNode"
        }
    }, i = {
        edit: {
            enable: !1,
            editNameSelectAll: !1,
            showRemoveBtn: !0,
            showRenameBtn: !0,
            removeTitle: "remove",
            renameTitle: "rename",
            drag: {
                autoExpandTrigger: !1,
                isCopy: !0,
                isMove: !0,
                prev: !0,
                next: !0,
                inner: !0,
                minMoveSize: 5,
                borderMax: 10,
                borderMin: -5,
                maxShowNodeNum: 5,
                autoOpenTime: 500
            }
        },
        view: {addHoverDom: null, removeHoverDom: null},
        callback: {
            beforeDrag: null,
            beforeDragOpen: null,
            beforeDrop: null,
            beforeEditName: null,
            beforeRename: null,
            onDrag: null,
            onDragMove: null,
            onDrop: null,
            onRename: null
        }
    }, n = function (e) {
        var t = y.getRoot(e), i = y.getRoots();
        t.curEditNode = null;
        t.curEditInput = null;
        t.curHoverNode = null;
        t.dragFlag = 0;
        t.dragNodeShowBefore = [];
        t.dragMaskList = new Array;
        i.showHoverDom = !0
    }, a = function () {
    }, s = function (e) {
        var t = e.treeObj, i = N.event;
        t.bind(i.RENAME, function (t, i, n, a) {
            m.apply(e.callback.onRename, [t, i, n, a])
        });
        t.bind(i.DRAG, function (t, i, n, a) {
            m.apply(e.callback.onDrag, [i, n, a])
        });
        t.bind(i.DRAGMOVE, function (t, i, n, a) {
            m.apply(e.callback.onDragMove, [i, n, a])
        });
        t.bind(i.DROP, function (t, i, n, a, s, o, r) {
            m.apply(e.callback.onDrop, [i, n, a, s, o, r])
        })
    }, o = function (e) {
        var t = e.treeObj, i = N.event;
        t.unbind(i.RENAME);
        t.unbind(i.DRAG);
        t.unbind(i.DRAGMOVE);
        t.unbind(i.DROP)
    }, r = function (e) {
        var t = e.target, i = y.getSetting(e.data.treeId), n = e.relatedTarget, a = "", s = null, o = "", r = "", l = null, d = null, c = null;
        if (m.eqs(e.type, "mouseover")) {
            c = m.getMDom(i, t, [{tagName: "a", attrName: "treeNode" + N.id.A}]);
            if (c) {
                a = m.getNodeMainDom(c).id;
                o = "hoverOverNode"
            }
        } else if (m.eqs(e.type, "mouseout")) {
            c = m.getMDom(i, n, [{tagName: "a", attrName: "treeNode" + N.id.A}]);
            if (!c) {
                a = "remove";
                o = "hoverOutNode"
            }
        } else if (m.eqs(e.type, "mousedown")) {
            c = m.getMDom(i, t, [{tagName: "a", attrName: "treeNode" + N.id.A}]);
            if (c) {
                a = m.getNodeMainDom(c).id;
                o = "mousedownNode"
            }
        }
        if (a.length > 0) {
            s = y.getNodeCache(i, a);
            switch (o) {
                case"mousedownNode":
                    l = h.onMousedownNode;
                    break;
                case"hoverOverNode":
                    l = h.onHoverOverNode;
                    break;
                case"hoverOutNode":
                    l = h.onHoverOutNode
            }
        }
        var u = {stop: !1, node: s, nodeEventType: o, nodeEventCallback: l, treeEventType: r, treeEventCallback: d};
        return u
    }, l = function (e, t, i) {
        if (i) {
            i.isHover = !1;
            i.editNameFlag = !1
        }
    }, d = function (e, t) {
        t.cancelEditName = function (e) {
            var t = y.getRoot(this.setting);
            t.curEditNode && C.cancelCurEditNode(this.setting, e ? e : null, !0)
        };
        t.copyNode = function (e, t, i, n) {
            function a() {
                C.addNodes(s.setting, e, [o], n)
            }

            if (!t)return null;
            if (e && !e.isParent && this.setting.data.keep.leaf && i === N.move.TYPE_INNER)return null;
            var s = this, o = m.clone(t);
            if (!e) {
                e = null;
                i = N.move.TYPE_INNER
            }
            if (i == N.move.TYPE_INNER)m.canAsync(this.setting, e) ? C.asyncNode(this.setting, e, n, a) : a(); else {
                C.addNodes(this.setting, e.parentNode, [o], n);
                C.moveNode(this.setting, e, o, i, !1, n)
            }
            return o
        };
        t.editName = function (e) {
            if (e && e.tId && e === y.getNodeCache(this.setting, e.tId)) {
                e.parentTId && C.expandCollapseParentNode(this.setting, e.getParentNode(), !0);
                C.editNode(this.setting, e)
            }
        };
        t.moveNode = function (e, t, i, n) {
            function a() {
                C.moveNode(s.setting, e, t, i, !1, n)
            }

            if (!t)return t;
            if (e && !e.isParent && this.setting.data.keep.leaf && i === N.move.TYPE_INNER)return null;
            if (e && (t.parentTId == e.tId && i == N.move.TYPE_INNER || b(t, this.setting).find("#" + e.tId).length > 0))return null;
            e || (e = null);
            var s = this;
            m.canAsync(this.setting, e) && i === N.move.TYPE_INNER ? C.asyncNode(this.setting, e, n, a) : a();
            return t
        };
        t.setEditable = function (e) {
            this.setting.edit.enable = e;
            return this.refresh()
        }
    }, c = {
        setSonNodeLevel: function (e, t, i) {
            if (i) {
                var n = e.data.key.children;
                i.level = t ? t.level + 1 : 0;
                if (i[n])for (var a = 0, s = i[n].length; s > a; a++)i[n][a] && y.setSonNodeLevel(e, i, i[n][a])
            }
        }
    }, u = {}, h = {
        onHoverOverNode: function (e, t) {
            var i = y.getSetting(e.data.treeId), n = y.getRoot(i);
            n.curHoverNode != t && h.onHoverOutNode(e);
            n.curHoverNode = t;
            C.addHoverDom(i, t)
        }, onHoverOutNode: function (e) {
            var t = y.getSetting(e.data.treeId), i = y.getRoot(t);
            if (i.curHoverNode && !y.isSelectedNode(t, i.curHoverNode)) {
                C.removeTreeDom(t, i.curHoverNode);
                i.curHoverNode = null
            }
        }, onMousedownNode: function (i, n) {
            function a(i) {
                if (0 == c.dragFlag && Math.abs(P - i.clientX) < d.edit.drag.minMoveSize && Math.abs(A - i.clientY) < d.edit.drag.minMoveSize)return !0;
                var n, a, o, r, l, h = d.data.key.children;
                E.css("cursor", "pointer");
                if (0 == c.dragFlag) {
                    if (0 == m.apply(d.callback.beforeDrag, [d.treeId, g], !0)) {
                        s(i);
                        return !0
                    }
                    for (n = 0, a = g.length; a > n; n++) {
                        0 == n && (c.dragNodeShowBefore = []);
                        o = g[n];
                        if (o.isParent && o.open) {
                            C.expandCollapseNode(d, o, !o.open);
                            c.dragNodeShowBefore[o.tId] = !0
                        } else c.dragNodeShowBefore[o.tId] = !1
                    }
                    c.dragFlag = 1;
                    u.showHoverDom = !1;
                    m.showIfameMask(d, !0);
                    var f = !0, x = -1;
                    if (g.length > 1) {
                        var M = g[0].parentTId ? g[0].getParentNode()[h] : y.getNodes(d);
                        l = [];
                        for (n = 0, a = M.length; a > n; n++) {
                            if (void 0 !== c.dragNodeShowBefore[M[n].tId]) {
                                f && x > -1 && x + 1 !== n && (f = !1);
                                l.push(M[n]);
                                x = n
                            }
                            if (g.length === l.length) {
                                g = l;
                                break
                            }
                        }
                    }
                    if (f) {
                        w = g[0].getPreNode();
                        T = g[g.length - 1].getNextNode()
                    }
                    p = b("<ul class='zTreeDragUL'></ul>", d);
                    for (n = 0, a = g.length; a > n; n++) {
                        o = g[n];
                        o.editNameFlag = !1;
                        C.selectNode(d, o, n > 0);
                        C.removeTreeDom(d, o);
                        if (!(n > d.edit.drag.maxShowNodeNum - 1)) {
                            r = b("<li id='" + o.tId + "_tmp'></li>", d);
                            r.append(b(o, N.id.A, d).clone());
                            r.css("padding", "0");
                            r.children("#" + o.tId + N.id.A).removeClass(N.node.CURSELECTED);
                            p.append(r);
                            if (n == d.edit.drag.maxShowNodeNum - 1) {
                                r = b("<li id='" + o.tId + "_moretmp'><a>  ...  </a></li>", d);
                                p.append(r)
                            }
                        }
                    }
                    p.attr("id", g[0].tId + N.id.UL + "_tmp");
                    p.addClass(d.treeObj.attr("class"));
                    p.appendTo(E);
                    v = b("<span class='tmpzTreeMove_arrow'></span>", d);
                    v.attr("id", "zTreeMove_arrow_tmp");
                    v.appendTo(E);
                    d.treeObj.trigger(N.event.DRAG, [i, d.treeId, g])
                }
                if (1 == c.dragFlag) {
                    if (k && v.attr("id") == i.target.id && L && i.clientX + D.scrollLeft() + 2 > e("#" + L + N.id.A, k).offset().left) {
                        var j = e("#" + L + N.id.A, k);
                        i.target = j.length > 0 ? j.get(0) : i.target
                    } else if (k) {
                        k.removeClass(N.node.TMPTARGET_TREE);
                        L && e("#" + L + N.id.A, k).removeClass(N.node.TMPTARGET_NODE + "_" + N.move.TYPE_PREV).removeClass(N.node.TMPTARGET_NODE + "_" + t.move.TYPE_NEXT).removeClass(N.node.TMPTARGET_NODE + "_" + t.move.TYPE_INNER)
                    }
                    k = null;
                    L = null;
                    _ = !1;
                    I = d;
                    var z = y.getSettings();
                    for (var B in z)if (z[B].treeId && z[B].edit.enable && z[B].treeId != d.treeId && (i.target.id == z[B].treeId || e(i.target).parents("#" + z[B].treeId).length > 0)) {
                        _ = !0;
                        I = z[B]
                    }
                    var H = D.scrollTop(), $ = D.scrollLeft(), Y = I.treeObj.offset(), U = I.treeObj.get(0).scrollHeight, W = I.treeObj.get(0).scrollWidth, q = i.clientY + H - Y.top, K = I.treeObj.height() + Y.top - i.clientY - H, V = i.clientX + $ - Y.left, G = I.treeObj.width() + Y.left - i.clientX - $, X = q < d.edit.drag.borderMax && q > d.edit.drag.borderMin, Z = K < d.edit.drag.borderMax && K > d.edit.drag.borderMin, Q = V < d.edit.drag.borderMax && V > d.edit.drag.borderMin, J = G < d.edit.drag.borderMax && G > d.edit.drag.borderMin, et = q > d.edit.drag.borderMin && K > d.edit.drag.borderMin && V > d.edit.drag.borderMin && G > d.edit.drag.borderMin, tt = X && I.treeObj.scrollTop() <= 0, it = Z && I.treeObj.scrollTop() + I.treeObj.height() + 10 >= U, nt = Q && I.treeObj.scrollLeft() <= 0, at = J && I.treeObj.scrollLeft() + I.treeObj.width() + 10 >= W;
                    if (i.target && m.isChildOrSelf(i.target, I.treeId)) {
                        for (var st = i.target; st && st.tagName && !m.eqs(st.tagName, "li") && st.id != I.treeId;)st = st.parentNode;
                        var ot = !0;
                        for (n = 0, a = g.length; a > n; n++) {
                            o = g[n];
                            if (st.id === o.tId) {
                                ot = !1;
                                break
                            }
                            if (b(o, d).find("#" + st.id).length > 0) {
                                ot = !1;
                                break
                            }
                        }
                        if (ot && i.target && m.isChildOrSelf(i.target, st.id + N.id.A)) {
                            k = e(st);
                            L = st.id
                        }
                    }
                    o = g[0];
                    if (et && m.isChildOrSelf(i.target, I.treeId)) {
                        !k && (i.target.id == I.treeId || tt || it || nt || at) && (_ || !_ && o.parentTId) && (k = I.treeObj);
                        X ? I.treeObj.scrollTop(I.treeObj.scrollTop() - 10) : Z && I.treeObj.scrollTop(I.treeObj.scrollTop() + 10);
                        Q ? I.treeObj.scrollLeft(I.treeObj.scrollLeft() - 10) : J && I.treeObj.scrollLeft(I.treeObj.scrollLeft() + 10);
                        k && k != I.treeObj && k.offset().left < I.treeObj.offset().left && I.treeObj.scrollLeft(I.treeObj.scrollLeft() + k.offset().left - I.treeObj.offset().left)
                    }
                    p.css({top: i.clientY + H + 3 + "px", left: i.clientX + $ + 3 + "px"});
                    var rt = 0, lt = 0;
                    if (k && k.attr("id") != I.treeId) {
                        var dt = null == L ? null : y.getNodeCache(I, L), ct = (i.ctrlKey || i.metaKey) && d.edit.drag.isMove && d.edit.drag.isCopy || !d.edit.drag.isMove && d.edit.drag.isCopy, ut = !(!w || L !== w.tId), ht = !(!T || L !== T.tId), ft = o.parentTId && o.parentTId == L, gt = (ct || !ht) && m.apply(I.edit.drag.prev, [I.treeId, g, dt], !!I.edit.drag.prev), pt = (ct || !ut) && m.apply(I.edit.drag.next, [I.treeId, g, dt], !!I.edit.drag.next), vt = !(!ct && ft || I.data.keep.leaf && !dt.isParent || !m.apply(I.edit.drag.inner, [I.treeId, g, dt], !!I.edit.drag.inner));
                        if (gt || pt || vt) {
                            var mt = e("#" + L + N.id.A, k), Nt = dt.isLastNode ? null : e("#" + dt.getNextNode().tId + N.id.A, k.next()), Ct = mt.offset().top, yt = mt.offset().left, bt = gt ? vt ? .25 : pt ? .5 : 1 : -1, kt = pt ? vt ? .75 : gt ? .5 : 0 : -1, wt = (i.clientY + H - Ct) / mt.height();
                            if ((1 == bt || bt >= wt && wt >= -.2) && gt) {
                                rt = 1 - v.width();
                                lt = Ct - v.height() / 2;
                                R = N.move.TYPE_PREV
                            } else if ((0 == kt || wt >= kt && 1.2 >= wt) && pt) {
                                rt = 1 - v.width();
                                lt = null == Nt || dt.isParent && dt.open ? Ct + mt.height() - v.height() / 2 : Nt.offset().top - v.height() / 2;
                                R = N.move.TYPE_NEXT
                            } else {
                                rt = 5 - v.width();
                                lt = Ct;
                                R = N.move.TYPE_INNER
                            }
                            v.css({display: "block", top: lt + "px", left: yt + rt + "px"});
                            mt.addClass(N.node.TMPTARGET_NODE + "_" + R);
                            (F != L || S != R) && (O = (new Date).getTime());
                            if (dt && dt.isParent && R == N.move.TYPE_INNER) {
                                var Tt = !0;
                                if (window.zTreeMoveTimer && window.zTreeMoveTargetNodeTId !== dt.tId) {
                                    clearTimeout(window.zTreeMoveTimer);
                                    window.zTreeMoveTargetNodeTId = null
                                } else window.zTreeMoveTimer && window.zTreeMoveTargetNodeTId === dt.tId && (Tt = !1);
                                if (Tt) {
                                    window.zTreeMoveTimer = setTimeout(function () {
                                        if (R == N.move.TYPE_INNER && dt && dt.isParent && !dt.open && (new Date).getTime() - O > I.edit.drag.autoOpenTime && m.apply(I.callback.beforeDragOpen, [I.treeId, dt], !0)) {
                                            C.switchNode(I, dt);
                                            I.edit.drag.autoExpandTrigger && I.treeObj.trigger(N.event.EXPAND, [I.treeId, dt])
                                        }
                                    }, I.edit.drag.autoOpenTime + 50);
                                    window.zTreeMoveTargetNodeTId = dt.tId
                                }
                            }
                        } else {
                            k = null;
                            L = "";
                            R = N.move.TYPE_INNER;
                            v.css({display: "none"});
                            if (window.zTreeMoveTimer) {
                                clearTimeout(window.zTreeMoveTimer);
                                window.zTreeMoveTargetNodeTId = null
                            }
                        }
                    } else {
                        R = N.move.TYPE_INNER;
                        k && m.apply(I.edit.drag.inner, [I.treeId, g, null], !!I.edit.drag.inner) ? k.addClass(N.node.TMPTARGET_TREE) : k = null;
                        v.css({display: "none"});
                        if (window.zTreeMoveTimer) {
                            clearTimeout(window.zTreeMoveTimer);
                            window.zTreeMoveTargetNodeTId = null
                        }
                    }
                    F = L;
                    S = R;
                    d.treeObj.trigger(N.event.DRAGMOVE, [i, d.treeId, g])
                }
                return !1
            }

            function s(i) {
                function n() {
                    if (_) {
                        if (!f)for (var e = 0, t = g.length; t > e; e++)C.removeNode(d, g[e]);
                        if (R == N.move.TYPE_INNER)C.addNodes(I, w, T); else {
                            C.addNodes(I, w.getParentNode(), T);
                            if (R == N.move.TYPE_PREV)for (e = 0, t = T.length; t > e; e++)C.moveNode(I, w, T[e], R, !1); else for (e = -1, t = T.length - 1; t > e; t--)C.moveNode(I, w, T[t], R, !1)
                        }
                    } else if (f && R == N.move.TYPE_INNER)C.addNodes(I, w, T); else {
                        f && C.addNodes(I, w.getParentNode(), T);
                        if (R != N.move.TYPE_NEXT)for (e = 0, t = T.length; t > e; e++)C.moveNode(I, w, T[e], R, !1); else for (e = -1, t = T.length - 1; t > e; t--)C.moveNode(I, w, T[t], R, !1)
                    }
                    C.selectNodes(I, T);
                    b(T[0], d).focus().blur();
                    d.treeObj.trigger(N.event.DROP, [i, I.treeId, T, w, R, f])
                }

                if (window.zTreeMoveTimer) {
                    clearTimeout(window.zTreeMoveTimer);
                    window.zTreeMoveTargetNodeTId = null
                }
                F = null;
                S = null;
                D.unbind("mousemove", a);
                D.unbind("mouseup", s);
                D.unbind("selectstart", o);
                E.css("cursor", "auto");
                if (k) {
                    k.removeClass(N.node.TMPTARGET_TREE);
                    L && e("#" + L + N.id.A, k).removeClass(N.node.TMPTARGET_NODE + "_" + N.move.TYPE_PREV).removeClass(N.node.TMPTARGET_NODE + "_" + t.move.TYPE_NEXT).removeClass(N.node.TMPTARGET_NODE + "_" + t.move.TYPE_INNER)
                }
                m.showIfameMask(d, !1);
                u.showHoverDom = !0;
                if (0 != c.dragFlag) {
                    c.dragFlag = 0;
                    var r, l, h;
                    for (r = 0, l = g.length; l > r; r++) {
                        h = g[r];
                        if (h.isParent && c.dragNodeShowBefore[h.tId] && !h.open) {
                            C.expandCollapseNode(d, h, !h.open);
                            delete c.dragNodeShowBefore[h.tId]
                        }
                    }
                    p && p.remove();
                    v && v.remove();
                    var f = (i.ctrlKey || i.metaKey) && d.edit.drag.isMove && d.edit.drag.isCopy || !d.edit.drag.isMove && d.edit.drag.isCopy;
                    !f && k && L && g[0].parentTId && L == g[0].parentTId && R == N.move.TYPE_INNER && (k = null);
                    if (k) {
                        var w = null == L ? null : y.getNodeCache(I, L);
                        if (0 == m.apply(d.callback.beforeDrop, [I.treeId, g, w, R, f], !0)) {
                            C.selectNodes(x, g);
                            return
                        }
                        var T = f ? m.clone(g) : g;
                        R == N.move.TYPE_INNER && m.canAsync(I, w) ? C.asyncNode(I, w, !1, n) : n()
                    } else {
                        C.selectNodes(x, g);
                        d.treeObj.trigger(N.event.DROP, [i, d.treeId, g, null, null, null])
                    }
                }
            }

            function o() {
                return !1
            }

            var r, l, d = y.getSetting(i.data.treeId), c = y.getRoot(d), u = y.getRoots();
            if (2 == i.button || !d.edit.enable || !d.edit.drag.isCopy && !d.edit.drag.isMove)return !0;
            var h = i.target, f = y.getRoot(d).curSelectedList, g = [];
            if (y.isSelectedNode(d, n))for (r = 0, l = f.length; l > r; r++) {
                if (f[r].editNameFlag && m.eqs(h.tagName, "input") && null !== h.getAttribute("treeNode" + N.id.INPUT))return !0;
                g.push(f[r]);
                if (g[0].parentTId !== f[r].parentTId) {
                    g = [n];
                    break
                }
            } else g = [n];
            C.editNodeBlur = !0;
            C.cancelCurEditNode(d);
            var p, v, k, w, T, D = e(d.treeObj.get(0).ownerDocument), E = e(d.treeObj.get(0).ownerDocument.body), _ = !1, I = d, x = d, F = null, S = null, L = null, R = N.move.TYPE_INNER, P = i.clientX, A = i.clientY, O = (new Date).getTime();
            m.uCanDo(d) && D.bind("mousemove", a);
            D.bind("mouseup", s);
            D.bind("selectstart", o);
            i.preventDefault && i.preventDefault();
            return !0
        }
    }, f = {
        getAbs: function (e) {
            var t = e.getBoundingClientRect(), i = document.body.scrollTop + document.documentElement.scrollTop, n = document.body.scrollLeft + document.documentElement.scrollLeft;
            return [t.left + n, t.top + i]
        }, inputFocus: function (e) {
            if (e.get(0)) {
                e.focus();
                m.setCursorPosition(e.get(0), e.val().length)
            }
        }, inputSelect: function (e) {
            if (e.get(0)) {
                e.focus();
                e.select()
            }
        }, setCursorPosition: function (e, t) {
            if (e.setSelectionRange) {
                e.focus();
                e.setSelectionRange(t, t)
            } else if (e.createTextRange) {
                var i = e.createTextRange();
                i.collapse(!0);
                i.moveEnd("character", t);
                i.moveStart("character", t);
                i.select()
            }
        }, showIfameMask: function (e, t) {
            for (var i = y.getRoot(e); i.dragMaskList.length > 0;) {
                i.dragMaskList[0].remove();
                i.dragMaskList.shift()
            }
            if (t)for (var n = b("iframe", e), a = 0, s = n.length; s > a; a++) {
                var o = n.get(a), r = m.getAbs(o), l = b("<div id='zTreeMask_" + a + "' class='zTreeMask' style='top:" + r[1] + "px; left:" + r[0] + "px; width:" + o.offsetWidth + "px; height:" + o.offsetHeight + "px;'></div>", e);
                l.appendTo(b("body", e));
                i.dragMaskList.push(l)
            }
        }
    }, g = {
        addEditBtn: function (e, t) {
            if (!(t.editNameFlag || b(t, N.id.EDIT, e).length > 0) && m.apply(e.edit.showRenameBtn, [e.treeId, t], e.edit.showRenameBtn)) {
                var i = b(t, N.id.A, e), n = "<span class='" + N.className.BUTTON + " edit' id='" + t.tId + N.id.EDIT + "' title='" + m.apply(e.edit.renameTitle, [e.treeId, t], e.edit.renameTitle) + "' treeNode" + N.id.EDIT + " style='display:none;'></span>";
                i.append(n);
                b(t, N.id.EDIT, e).bind("click", function () {
                    if (!m.uCanDo(e) || 0 == m.apply(e.callback.beforeEditName, [e.treeId, t], !0))return !1;
                    C.editNode(e, t);
                    return !1
                }).show()
            }
        }, addRemoveBtn: function (e, t) {
            if (!(t.editNameFlag || b(t, N.id.REMOVE, e).length > 0) && m.apply(e.edit.showRemoveBtn, [e.treeId, t], e.edit.showRemoveBtn)) {
                var i = b(t, N.id.A, e), n = "<span class='" + N.className.BUTTON + " remove' id='" + t.tId + N.id.REMOVE + "' title='" + m.apply(e.edit.removeTitle, [e.treeId, t], e.edit.removeTitle) + "' treeNode" + N.id.REMOVE + " style='display:none;'></span>";
                i.append(n);
                b(t, N.id.REMOVE, e).bind("click", function () {
                    if (!m.uCanDo(e) || 0 == m.apply(e.callback.beforeRemove, [e.treeId, t], !0))return !1;
                    C.removeNode(e, t);
                    e.treeObj.trigger(N.event.REMOVE, [e.treeId, t]);
                    return !1
                }).bind("mousedown", function () {
                    return !0
                }).show()
            }
        }, addHoverDom: function (e, t) {
            if (y.getRoots().showHoverDom) {
                t.isHover = !0;
                if (e.edit.enable) {
                    C.addEditBtn(e, t);
                    C.addRemoveBtn(e, t)
                }
                m.apply(e.view.addHoverDom, [e.treeId, t])
            }
        }, cancelCurEditNode: function (e, t, i) {
            var n = y.getRoot(e), a = e.data.key.name, s = n.curEditNode;
            if (s) {
                var o = n.curEditInput, r = t ? t : i ? s[a] : o.val();
                if (m.apply(e.callback.beforeRename, [e.treeId, s, r, i], !0) === !1)return !1;
                s[a] = r;
                e.treeObj.trigger(N.event.RENAME, [e.treeId, s, i]);
                var l = b(s, N.id.A, e);
                l.removeClass(N.node.CURSELECTED_EDIT);
                o.unbind();
                C.setNodeName(e, s);
                s.editNameFlag = !1;
                n.curEditNode = null;
                n.curEditInput = null;
                C.selectNode(e, s, !1)
            }
            n.noSelection = !0;
            return !0
        }, editNode: function (e, t) {
            var i = y.getRoot(e);
            C.editNodeBlur = !1;
            if (y.isSelectedNode(e, t) && i.curEditNode == t && t.editNameFlag)setTimeout(function () {
                m.inputFocus(i.curEditInput)
            }, 0); else {
                var n = e.data.key.name;
                t.editNameFlag = !0;
                C.removeTreeDom(e, t);
                C.cancelCurEditNode(e);
                C.selectNode(e, t, !1);
                b(t, N.id.SPAN, e).html("<input type=text class='rename' id='" + t.tId + N.id.INPUT + "' treeNode" + N.id.INPUT + " >");
                var a = b(t, N.id.INPUT, e);
                a.attr("value", t[n]);
                e.edit.editNameSelectAll ? m.inputSelect(a) : m.inputFocus(a);
                a.bind("blur", function () {
                    C.editNodeBlur || C.cancelCurEditNode(e)
                }).bind("keydown", function (t) {
                    if ("13" == t.keyCode) {
                        C.editNodeBlur = !0;
                        C.cancelCurEditNode(e)
                    } else"27" == t.keyCode && C.cancelCurEditNode(e, null, !0)
                }).bind("click", function () {
                    return !1
                }).bind("dblclick", function () {
                    return !1
                });
                b(t, N.id.A, e).addClass(N.node.CURSELECTED_EDIT);
                i.curEditInput = a;
                i.noSelection = !1;
                i.curEditNode = t
            }
        }, moveNode: function (e, t, i, n, a, s) {
            var o = y.getRoot(e), r = e.data.key.children;
            if (t != i && (!e.data.keep.leaf || !t || t.isParent || n != N.move.TYPE_INNER)) {
                var l = i.parentTId ? i.getParentNode() : o, d = null === t || t == o;
                d && null === t && (t = o);
                d && (n = N.move.TYPE_INNER);
                var c = t.parentTId ? t.getParentNode() : o;
                n != N.move.TYPE_PREV && n != N.move.TYPE_NEXT && (n = N.move.TYPE_INNER);
                if (n == N.move.TYPE_INNER)if (d)i.parentTId = null; else {
                    if (!t.isParent) {
                        t.isParent = !0;
                        t.open = !!t.open;
                        C.setNodeLineIcos(e, t)
                    }
                    i.parentTId = t.tId
                }
                var u, h;
                if (d) {
                    u = e.treeObj;
                    h = u
                } else {
                    s || n != N.move.TYPE_INNER ? s || C.expandCollapseNode(e, t.getParentNode(), !0, !1) : C.expandCollapseNode(e, t, !0, !1);
                    u = b(t, e);
                    h = b(t, N.id.UL, e);
                    if (u.get(0) && !h.get(0)) {
                        var f = [];
                        C.makeUlHtml(e, t, f, "");
                        u.append(f.join(""))
                    }
                    h = b(t, N.id.UL, e)
                }
                var g = b(i, e);
                g.get(0) ? u.get(0) || g.remove() : g = C.appendNodes(e, i.level, [i], null, !1, !0).join("");
                h.get(0) && n == N.move.TYPE_INNER ? h.append(g) : u.get(0) && n == N.move.TYPE_PREV ? u.before(g) : u.get(0) && n == N.move.TYPE_NEXT && u.after(g);
                var p, v, m = -1, k = 0, w = null, T = null, D = i.level;
                if (i.isFirstNode) {
                    m = 0;
                    if (l[r].length > 1) {
                        w = l[r][1];
                        w.isFirstNode = !0
                    }
                } else if (i.isLastNode) {
                    m = l[r].length - 1;
                    w = l[r][m - 1];
                    w.isLastNode = !0
                } else for (p = 0, v = l[r].length; v > p; p++)if (l[r][p].tId == i.tId) {
                    m = p;
                    break
                }
                m >= 0 && l[r].splice(m, 1);
                if (n != N.move.TYPE_INNER)for (p = 0, v = c[r].length; v > p; p++)c[r][p].tId == t.tId && (k = p);
                if (n == N.move.TYPE_INNER) {
                    t[r] || (t[r] = new Array);
                    if (t[r].length > 0) {
                        T = t[r][t[r].length - 1];
                        T.isLastNode = !1
                    }
                    t[r].splice(t[r].length, 0, i);
                    i.isLastNode = !0;
                    i.isFirstNode = 1 == t[r].length
                } else if (t.isFirstNode && n == N.move.TYPE_PREV) {
                    c[r].splice(k, 0, i);
                    T = t;
                    T.isFirstNode = !1;
                    i.parentTId = t.parentTId;
                    i.isFirstNode = !0;
                    i.isLastNode = !1
                } else if (t.isLastNode && n == N.move.TYPE_NEXT) {
                    c[r].splice(k + 1, 0, i);
                    T = t;
                    T.isLastNode = !1;
                    i.parentTId = t.parentTId;
                    i.isFirstNode = !1;
                    i.isLastNode = !0
                } else {
                    n == N.move.TYPE_PREV ? c[r].splice(k, 0, i) : c[r].splice(k + 1, 0, i);
                    i.parentTId = t.parentTId;
                    i.isFirstNode = !1;
                    i.isLastNode = !1
                }
                y.fixPIdKeyValue(e, i);
                y.setSonNodeLevel(e, i.getParentNode(), i);
                C.setNodeLineIcos(e, i);
                C.repairNodeLevelClass(e, i, D);
                if (!e.data.keep.parent && l[r].length < 1) {
                    l.isParent = !1;
                    l.open = !1;
                    var E = b(l, N.id.UL, e), _ = b(l, N.id.SWITCH, e), I = b(l, N.id.ICON, e);
                    C.replaceSwitchClass(l, _, N.folder.DOCU);
                    C.replaceIcoClass(l, I, N.folder.DOCU);
                    E.css("display", "none")
                } else w && C.setNodeLineIcos(e, w);
                T && C.setNodeLineIcos(e, T);
                if (e.check && e.check.enable && C.repairChkClass) {
                    C.repairChkClass(e, l);
                    C.repairParentChkClassWithSelf(e, l);
                    l != i.parent && C.repairParentChkClassWithSelf(e, i)
                }
                s || C.expandCollapseParentNode(e, i.getParentNode(), !0, a)
            }
        }, removeEditBtn: function (e, t) {
            b(t, N.id.EDIT, e).unbind().remove()
        }, removeRemoveBtn: function (e, t) {
            b(t, N.id.REMOVE, e).unbind().remove()
        }, removeTreeDom: function (e, t) {
            t.isHover = !1;
            C.removeEditBtn(e, t);
            C.removeRemoveBtn(e, t);
            m.apply(e.view.removeHoverDom, [e.treeId, t])
        }, repairNodeLevelClass: function (e, t, i) {
            if (i !== t.level) {
                var n = b(t, e), a = b(t, N.id.A, e), s = b(t, N.id.UL, e), o = N.className.LEVEL + i, r = N.className.LEVEL + t.level;
                n.removeClass(o);
                n.addClass(r);
                a.removeClass(o);
                a.addClass(r);
                s.removeClass(o);
                s.addClass(r)
            }
        }, selectNodes: function (e, t) {
            for (var i = 0, n = t.length; n > i; i++)C.selectNode(e, t[i], i > 0)
        }
    }, p = {tools: f, view: g, event: u, data: c};
    e.extend(!0, e.fn.zTree.consts, t);
    e.extend(!0, e.fn.zTree._z, p);
    var v = e.fn.zTree, m = v._z.tools, N = v.consts, C = v._z.view, y = v._z.data, b = (v._z.event, m.$);
    y.exSetting(i);
    y.addInitBind(s);
    y.addInitUnBind(o);
    y.addInitCache(a);
    y.addInitNode(l);
    y.addInitProxy(r);
    y.addInitRoot(n);
    y.addZTreeTools(d);
    var k = C.cancelPreSelectedNode;
    C.cancelPreSelectedNode = function (e, t) {
        for (var i = y.getRoot(e).curSelectedList, n = 0, a = i.length; a > n; n++)if (!t || t === i[n]) {
            C.removeTreeDom(e, i[n]);
            if (t)break
        }
        k && k.apply(C, arguments)
    };
    var w = C.createNodes;
    C.createNodes = function (e, t, i, n) {
        w && w.apply(C, arguments);
        i && C.repairParentChkClassWithSelf && C.repairParentChkClassWithSelf(e, n)
    };
    var T = C.makeNodeUrl;
    C.makeNodeUrl = function (e) {
        return e.edit.enable ? null : T.apply(C, arguments)
    };
    var D = C.removeNode;
    C.removeNode = function (e, t) {
        var i = y.getRoot(e);
        i.curEditNode === t && (i.curEditNode = null);
        D && D.apply(C, arguments)
    };
    var E = C.selectNode;
    C.selectNode = function (e, t) {
        var i = y.getRoot(e);
        if (y.isSelectedNode(e, t) && i.curEditNode == t && t.editNameFlag)return !1;
        E && E.apply(C, arguments);
        C.addHoverDom(e, t);
        return !0
    };
    var _ = m.uCanDo;
    m.uCanDo = function (e, t) {
        var i = y.getRoot(e);
        if (t && (m.eqs(t.type, "mouseover") || m.eqs(t.type, "mouseout") || m.eqs(t.type, "mousedown") || m.eqs(t.type, "mouseup")))return !0;
        if (i.curEditNode) {
            C.editNodeBlur = !1;
            i.curEditInput.focus()
        }
        return !i.curEditNode && (_ ? _.apply(C, arguments) : !0)
    }
}(jQuery);
(typeof define === "function") && define(function (require) {
    require("/js/common/plugins/datepicker/pikaday");
});