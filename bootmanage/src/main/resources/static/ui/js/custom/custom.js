//存储tab的id
var tabObj={};

//添加tab
function addTab(menu) {
    var menuId = $(menu).attr('menuId');
    $('.nav-tabs > li.active').removeClass('active');
    $('.tab-content > div.active').removeClass('active');
    $('#side-menu li.active').removeClass('active');
    if (!tabObj[menuId]){
        var menuName = $(menu).attr('menuName');
        var className = $(menu).children('li:first-child').attr('class');
        var li = '<li id="'+menuId+'_li"><a data-toggle="tab" href="#'+menuId+'_Tab"><i class="'+className+'"></i><span class="nav-label">'+menuName+'</span><i class="tclose icon-close"></li>';
        $('.nav-tabs').append($(li));
        tabObj[menuId] = 1;
        //添加frame
        var menuHref = $(menu).attr('href');
        var div = '<div id="'+menuId+'_Tab" class="tab-pane"><iframe src="'+menuHref+'" width="100%" frameborder="0" scrolling="no"></iframe></div>';
        $('.tab-content').append($(div));
    }else{
    	// 刷新Iframe
    	var menuHref = $(menu).attr('href');
    	var tabid = menuId + "_Tab";
    	$('#'+tabid + ' iframe').each(function(){
    		$(this).attr("src", menuHref);
		});
    }
    $('#'+menuId+'_li').addClass('active');
    $('#'+menuId+'_Tab').addClass('active');
    $('#side-menu a[menuId='+menuId+']').parents('li:not(:first)').addClass('active');
}

// Custom scripts
$(function(){
    function resizeIframe(){
        var $iframe = $('#page-wrapper .tab-pane.active > iframe');
        var iframeHeight = $iframe.contents().find('body').outerHeight();
        $iframe.height(iframeHeight);
        setTimeout( function() { resizeIframe(); }, 250 );
    };
    resizeIframe();
    $(window).resize(function() {
        resizeIframe();
    });
    $('iframe').load(function() {
        resizeIframe();
    });
    //侧边栏菜单控制tab
    // $('#side-menu > li').on('click', function (e) {
    //     var i = $(this).index();
    //     var tab = $('#page-wrapper .nav-tabs > li:eq('+i+')');
    //     tab.removeClass('hidden');
    //     tab.children('a').trigger('click');
    // });
    //dashboard页面去掉page-heading
/*  function togglePageheading(){
        if($('#side-menu > li:first').hasClass('active')){
            $('.page-heading').addClass('hidden');
        }
        else{
            $('.page-heading').removeClass('hidden');
        }
    }
    togglePageheading();
*/


    $('#side-menu a[opt=1]').on('click',function (e) {
        e.preventDefault();
        addTab($(this));
    });

    //顶部导航栏
    //用户管理
    $('.user-management > .dropdown-menu a:not(.logout)').on('click', function (e) {
        e.preventDefault();
        addTab($(this));
        // var i = $(this).parent().index();
        // var sideMenu = $('#side-menu > li:nth-last-of-type(2) > ul > li:eq('+i+') > a');
        // sideMenu.trigger('click');

    });
    
    //系统设置
    $('.setting > a').on('click', function (e) {
        e.preventDefault();
        var sideMenu = $('#side-menu > li:nth-last-of-type(2) > a');
        sideMenu.trigger('click');
    });
    
    //tab控制侧边栏
    $('#page-wrapper .nav-tabs ').on('click','[data-toggle]', function (e) {
        var activeMenu = $('#side-menu li.active:last').find('ul:last');
        var menuId = $(this).parent().attr('id').replace('_li','');
        var sideMenu = $('#side-menu a[menuId='+menuId+']');
        $('#side-menu li.active').removeClass('active');
        if (activeMenu.find(sideMenu).length==0){
            if (!sideMenu.parents('.nav-first-level').children('ul:first').hasClass('in')){
                sideMenu.parents('.nav-first-level').children('a:first').trigger('click');
            }
            if (!sideMenu.parents('ul:first').hasClass('in')){
                sideMenu.parents('ul:first').prev().trigger('click');
            }
        }
        sideMenu.parents('li:not(:first)').addClass('active');
    });
    
    //关闭tab
    $('.nav-tabs').on('click','.tclose', function (e) {
        e.preventDefault();
        e.stopPropagation();
        var tab = $(this).parent().parent();

        if (tab.hasClass('active')){
            var nextShowTabs = tab.nextAll(":not(.active)");
            var j = nextShowTabs.length;
            if(j>0){
                tab.nextAll(":not(.active):first").children('a').trigger('click');//如果是关闭当前tab且不是最后一个,显示下一个非"hidden"的tab
            }else{
                tab.prevAll(":not(.active):first").children('a').trigger('click');//如果是关闭当前tab且是最后一个,显示上一个非"hidden"的tab
            }
        }
        tab.remove();
        delete(tabObj[tab.attr('id').replace('_li','')]);
    });
    
    //table 全选
    $('#checkAll').on('ifChanged', function(event){
        if (this.checked) {  
            $(this).parents('table').find('input[type=checkbox]').iCheck('check'); 
        } 
        else {   //反之 取消全选   
            $(this).parents('table').find('input[type=checkbox]').iCheck('uncheck');  
        }  
    })
    $('tr input[type=checkbox]').on('ifChanged', function(event){
        if (this.checked) {  
            $(this).parents('tr').addClass('checked'); 
        } 
        else {   //反之 取消全选   
            $(this).parents('tr').removeClass('checked'); 
        }  
    })

    // check treeview js
    $('.css3-treeview').delegate("label input:checkbox", "change", function(e) {
        e.stopPropagation();
        var
            checkbox = $(this),
            nestedList = checkbox.parent().next().next(),
            nestedListCheckbox = nestedList.find("input:checkbox");
     
        if(checkbox.is(":checked")) {
            return nestedListCheckbox.prop("checked", true);
        }
        nestedListCheckbox.prop("checked", false);
    });
    // panel heading select all
    $('.select-tree').delegate("input:checkbox", "change", function(e) {
        e.stopPropagation();
        var
            checkbox = $(this),
            nestedList = checkbox.parents('.panel-heading').next('.panel-body').find('.css3-treeview'),
            nestedListCheckbox = nestedList.find("input:checkbox");     
        if(checkbox.is(":checked")) {
            return nestedListCheckbox.prop("checked", true);
        }
        nestedListCheckbox.prop("checked", false);
    });
    // collapse treeview panel
    var treePanelHeading = $('.select-tree').parent('.panel-heading');
    treePanelHeading.css({
        cursor: 'pointer',
        'user-select': 'none'
    });
    treePanelHeading.next('.panel-body').addClass('hidden');
    treePanelHeading.on('click', function (e) {
        $(this).next('.panel-body').toggleClass('hidden');
    });
    // collapse treeview
    $('.css3-treeview ul > li').each(function() {
        var li = $(this),
            arrow = li.children('span'),
            childLi = li.children('ul').children('li');

        if(childLi.length){
            arrow.on('click',function(e){
                li.toggleClass('expand');
            });
        }
        else{
            li.addClass('nochild');
        }
    });
})
/***
  Iframe modal
***/
$(function() {
    $('[data-toggle=modal]').on('click', function (e) {
        var id = $(this).attr('data-target').substr(1);
        window.parent.$("#"+id).modal();
    })
});
/***
  fancybox
***/
$(function(){
    $('.fancybox').click(function(e){
        e.preventDefault();
        window.parent.$.fancybox({
            href: this.href,
            title: this.title
        });
    });
    $('.fancybox_iframe').click(function(e){
        e.preventDefault();
        window.parent.$.fancybox({
            type: 'iframe',
            padding: 20,
            href: this.href
            
        });
    });
})
$(function(){

    // MetsiMenu
    $('#side-menu').metisMenu();

    // Collapse ibox function
    $('.collapse-link').on('click', function (e) {
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
    });

    // Close ibox function
    $('.close-link').on('click', function (e) {
        var content = $(this).closest('div.ibox');
        content.remove();
    });

    // Small todo handler
    $('.check-link').on('click', function (e) {
        var button = $(this).find('i');
        var label = $(this).next('span');
        button.toggleClass('fa-check-square').toggleClass('fa-square-o');
        label.toggleClass('todo-completed');
        return false;
    });


    // minimalize menu
    $('.navbar-minimalize').on('click', function (e) {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    });

    // tooltips
    $('.tooltip-demo').tooltip({
        selector: "[data-toggle=tooltip]",
        container: "body"
    });

    // Full height of sidebar
    function fix_height() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".sidebard-panel").css("min-height", heightWithoutNavbar + "px");
    }
    fix_height();

    $(window).bind("load resize click scroll", function() {
        if(!$("body").hasClass('body-small')) {
            fix_height();
        }
    })

    $("[data-toggle=popover]")
        .popover();
});


// For demo purpose - animation css script
function animationHover(element, animation){
    element = $(element);
    element.hover(
        function() {
            element.addClass('animated ' + animation);
        },
        function(){
            //wait for animation to finish before removing classes
            window.setTimeout( function(){
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

// Minimalize menu when screen is less than 768px
$(function() {
    $(window).bind("load resize", function() {
        if ($(this).width() < 769 && !$('body').hasClass('mini-navbar')) {
            $('body').addClass('mini-navbar');
        } else if ($(this).width() >= 769 && $('body').hasClass('mini-navbar')){
            $('body').removeClass('mini-navbar');
            SmoothlyMenu();
        }
    })
})

function SmoothlyMenu() {
    if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
        // Hide menu in order to smoothly turn on when maximize menu
        $('#side-menu').hide();
        // For smoothly turn on menu
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 100);
    } else {
        // Remove all inline style from jquery fadeIn function to reset menu state
        $('#side-menu').removeAttr('style');
    }
}

// Dragable panels
// function WinMove() {
//     $("div.ibox").not('.no-drop')
//         .draggable({
//             revert: true,
//             zIndex: 2000,
//             cursor: "move",
//             handle: '.ibox-title',
//             opacity: 0.8,
//             drag: function(){
//                 var finalOffset = $(this).offset();
//                 var finalxPos = finalOffset.left;
//                 var finalyPos = finalOffset.top;
//                 // Add div with above id to see position of panel
//                 $('#posX').text('Final X: ' + finalxPos);
//                 $('#posY').text('Final Y: ' + finalyPos);
//             },
//         })
//         .droppable({
//             tolerance: 'pointer',
//             drop: function (event, ui) {
//                 var draggable = ui.draggable;
//                 var droppable = $(this);
//                 var dragPos = draggable.position();
//                 var dropPos = droppable.position();
//                 draggable.swap(droppable);
//                 setTimeout(function () {
//                     var dropmap = droppable.find('[id^=map-]');
//                     var dragmap = draggable.find('[id^=map-]');
//                     if (dragmap.length > 0 || dropmap.length > 0) {
//                         dragmap.resize();
//                         dropmap.resize();
//                     }
//                     else {
//                         draggable.resize();
//                         droppable.resize();
//                     }
//                 }, 50);
//                 setTimeout(function () {
//                     draggable.find('[id^=map-]').resize();
//                     droppable.find('[id^=map-]').resize();
//                 }, 250);
//             }
//         });
// }
jQuery.fn.swap = function (b) {
    b = jQuery(b)[0];
    var a = this[0];
    var t = a.parentNode.insertBefore(document.createTextNode(''), a);
    b.parentNode.insertBefore(a, b);
    t.parentNode.insertBefore(b, t);
    t.parentNode.removeChild(t);
    return this;
};

/*iCheck*/
$(function(){
    $('input.i-checks').iCheck({
        checkboxClass: 'icheckbox',
        radioClass: 'iradio'
    });
});

/*FileUpload*/
$(function(){
    $('.fileinput-preview + div > .upload-btn').on('click',function(event) {
        event.preventDefault();
        $(this).parent().prev('.fileinput-preview').append('<span><i class="fa fa-spinner fa-spin"></i></span>');
        $(this).attr('disabled', true);//避免用户多次提交
    });
});

/*custom-fileupload*/
$(function(){
    $('.custom-fileupload input[type=file]').change(function(event) {
        var path = $(this).val();
        var filename = path.replace(/^.*\\/, "");
        var filenameinput = $(this).parent().parent().prev().find('input');
        var filenameicon = filenameinput.prev();
        filenameinput.val(filename);
        filenameicon.removeClass('hidden');
    });
});

/*表单form*/
$(function(){
    $('#form-submit').submit(function(event) { // 避免用户多次提交
        var subButton = $(this).find(':submit');
        subButton.html('<i class="fa fa-spin fa-spinner"></i>    正在提交').attr('disabled', true);
        return false;
    });
});

/*外部链接新建标签显示*/
$(function(){
    $('a[href^="http://"],a[href^="https://"]').attr('target','_blank');
});

// var closeTab = function (id) {
//     //如果关闭的是当前激活的TAB，激活他的前一个TAB
//     if ($("li.active").attr('id') == "tab_" + id) {
//         $("#tab_" + id).prev().addClass('active');
//         $("#" + id).prev().addClass('active');
//     }
//     //关闭TAB
//     $("#tab_" + id).remove();
//     $("#" + id).remove();
// };
