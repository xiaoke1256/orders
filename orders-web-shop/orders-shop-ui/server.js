/*
const express = require('express');
const proxy = require('http-proxy-middleware');

const app = express();
app.use('/static', express.static(`${__dirname}/static`));
app.use('/api', proxy({
    target: 'http://localhost:8763/store_intra',
    changeOrigin: true,
}));

app.get('/*', (req, res) => {
    res.sendFile(`${__dirname}/index.html`);
});
app.listen(8080);
*/
