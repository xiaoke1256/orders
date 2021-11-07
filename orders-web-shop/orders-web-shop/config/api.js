let apiUrl = '';
console.log("process.env.NODE_PACK:"+process.env.NODE_PACK);
switch(process.env.NODE_PACK) {
  case 'production':
    apiUrl = '"http://peer1:8763/store_intra"';
    break;
  default:
    apiUrl = '"api"';
    break;
}
module.exports = apiUrl