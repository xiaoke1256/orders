let apiUrl = '';
console.log("process.env.NODE_PACK:"+process.env.NODE_PACK);
switch(process.env.NODE_PACK) {
  case 'production':
    apiUrl = '"http://peer1:8763"';
    break;
  default:
    apiUrl = '"http://localhost:8763"';
    break;
}
module.exports = apiUrl