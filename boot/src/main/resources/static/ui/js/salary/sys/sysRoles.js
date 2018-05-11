/**
 * Created by Administrator on 2017/8/25.
 */
/*<![CDATA[*/
// 验证角色编码唯一性
function ajaxCheckRoleCode(roleCode) {
    var flag = false;//提交时候 验证
    if ($("#roleCode").val()) {
        $.ajax({
            type: 'POST',
            url: "/sys/roles/checkRoleCode?roleCode="
            + $("#" + roleCode).val(),
            success: function (data) {
                if (data.status == 1) {
                    flag = true;
                } else {
                    $("#" + roleCode).val("");
                    layer.msg(data.info);
                }
            }
        }, JSON);
    }
    return flag;
}
// 验证角色名称唯一性
function ajaxCheckRoleName(roleName) {
    var flag = false;//提交时候 验证
    if ($("#roleName").val()) {
        $.ajax({
            type: 'POST',
            url: "/sys/roles/checkRoleName?roleName="
            + $("#" + roleName).val(),
            success: function (data) {
                if (data.status == 1) {
                    flag = true;
                } else {
                    $("#" + roleName).val("");
                    layer.msg(data.info);
                }
            }
        }, JSON);
    }
    return flag;
}

/**返回*/
function back() {
    window.location.href = "/sys/roles/init";
}

var treeResolver = function (data, topNodeId) {
    var result = [];
    parseChildren(data);
    if (data.id == topNodeId) {
        result.push(data);
    } else if (data instanceof Array) {
        result = data;
    } else {
        result = data.children;
    }
    return result;
};

/**
 * 处理服务器返回的树形节点
 */
var parseChildren = function (data, topNodeId) {
    if (data.childSize > 0 && data.children.length == 0) {
        data.state = "closed";
    }
    if (data.children && data.children.length > 0)
        for (var i = 0; i < data.children.length; i++) {
            parseChildren(data.children[i]);
        }
};

/**增加页面*/


/**增加页面进行调用 根据系统展示不同的资源列表*/
function treeForAddFunc(resourceViewId, sysType) {
    $("#" + resourceViewId).tree({
        url: "/sys/resources/findTreeBySysType?resourcesSysType=" + sysType,
        checkbox: true,
        cascadeCheck: false,
        onBeforeSelect: function (node) {
        },
        onCheck: function (node, checked) {
            if (checked) {
                var parentNode = $("#" + resourceViewId).tree('getParent', node.target);
                if (parentNode != null) {
                    $("#" + resourceViewId).tree('check', parentNode.target);
                }
            } else {
                var childNode = $("#" + resourceViewId).tree('getChildren', node.target);
                if (childNode.length > 0) {
                    for (var i = 0; i < childNode.length; i++) {
                        $("#" + resourceViewId).tree('uncheck', childNode[i].target);
                    }
                }
            }
        },
        loadFilter: function (data) {
            //过滤
            return treeResolver(data, "1");
        }
    });
}

/**显示隐藏树列表*/
function treeForAddFuncDis() {
    var sysType = $("#sysType").val();
    for (var i = 0; i < sysTypeIdArray.length; i++) {
        if (sysType == sysTypeIdArray[i]) {
            $(".ztree").css("display", "none");
            $("#" + sysTypeIdArray[i]).css("display", "block");
            break;
        }
    }
}