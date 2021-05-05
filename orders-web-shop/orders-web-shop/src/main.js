"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
var vue_1 = __importDefault(require("vue"));
var App_vue_1 = __importDefault(require("./App.vue"));
var router_1 = __importDefault(require("./router"));
vue_1.default.config.productionTip = false;
/* eslint-disable no-new */
new vue_1.default({
    el: '#app',
    router: router_1.default,
    components: { App: App_vue_1.default },
    template: '<App/>'
});
