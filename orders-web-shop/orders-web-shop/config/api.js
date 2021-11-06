let apiUrl = '';
switch(process.env.NODE_PACK) {
  case 'production':
    apiUrl = '"http://localhost:8763"';
    break;
  default:
    apiUrl = '"http://peer1:8763"';
    break;
}
module.exports = apiUrl