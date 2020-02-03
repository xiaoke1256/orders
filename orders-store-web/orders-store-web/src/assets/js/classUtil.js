export default {  
   // 获取class
    getClass(el) {
        return el.getAttribute('class')
    },
    // 设置class
    setClass(el, cls) {
        return el.setAttribute('class', cls)
    },
    
    // 当然彩蛋压轴戏肯定是在最后的啦
    // 判断class是否存在
    hasClass(elements, cName) {
        return !!elements.className.match(new RegExp("(\\s|^)" + cName + "(\\s|$)"));
    },
    // 添加clss
    addClass(elements, cName) {
        if (!this.hasClass(elements, cName)) {
            elements.className += " " + cName;
        }
    },
    // 删除class
    removeClass(elements, cName) {
        if (this.hasClass(elements, cName)) {
            elements.className = elements.className.replace(new RegExp("(\\s|^)" + cName + "(\\s|$)"), " ");
        }
    },
    // 切换class
    toggleClass(elements, cName) {
        if (this.hasClass(elements, cName)) {
            elements.className = elements.className.replace(new RegExp("(\\s|^)" + cName + "(\\s|$)"), "");
        } else {
            elements.className += " " + cName;
        }
    }
}      