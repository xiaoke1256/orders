'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')
const apiUrl = require('./api')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: apiUrl
})
