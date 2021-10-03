/**
 * 日期格式化
 * @param date 
 * @param fmt 
 * @returns 
 */
export const dateFmt=(date:Date|string,fmt:string )=>{
  if(typeof(date)==='string'){
    const index = date.indexOf('+');
    if(index>=0){
      date = date.substring(0,index);
    }
    date = new Date(date.replace("-","/").replace('T',' '));
  }
  let ret;
  const opt = {
    "y+": date.getFullYear().toString(),        // 年
    "M+": (date.getMonth() + 1).toString(),     // 月
    "d+": date.getDate().toString(),            // 日
    "H+": date.getHours().toString(),           // 时
    "m+": date.getMinutes().toString(),         // 分
    "s+": date.getSeconds().toString()          // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  } as {[key:string]:string};
  for (let k in opt) {
    ret = new RegExp("(" + k + ")").exec(fmt);
    if (ret) {
        fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
    };
  };
  return fmt;
};