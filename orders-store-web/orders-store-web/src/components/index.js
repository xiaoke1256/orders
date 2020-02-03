import Modal from './Modal.vue'
Modal.install=function(Vue){
  Vue.component(Modal.name,Modal)
}
console.log('注册组件....');
export default Modal;