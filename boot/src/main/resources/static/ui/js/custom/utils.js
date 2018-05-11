//检查email邮箱
function isEmail(str) {
	emailReg = /^[\w\.\-]+@([\w\-]+\.)+[a-z]{2,4}$/;
	return emailReg.test(str);
}

//验证电话号码
function isPhoneNumber(str) {
	patrn = /^[\d\+\-\s]+$/;
    if(!patrn.exec(str)) {  
        return false;  
    }  
    return true;  
}

//验证手机号码
function isCellPhoneNumber(str) {
	patrn = /^[0-9]{11}$/;  
    if(!patrn.exec(str)) {  
        return false;  
    }  
    return true;
}

function isNumber(str) {
	numberReg = /^\d+$/;
	return numberReg.test(str);
}

function isFloat(str) {
	var reg = /^\d+\.{0,1}\d+|\d+$/;
	return reg.test(str);
}