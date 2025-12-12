import Vue from 'vue'
import Vuex from 'vuex'
import api from '../api'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: JSON.parse(localStorage.getItem('userInfo')) || null,
    token: localStorage.getItem('userToken') || null,
    cart: [],
    products: [],
    categories: [],
    isLoading: false
  },
  
  mutations: {
    SET_USER(state, user) {
      state.user = user
      localStorage.setItem('userInfo', JSON.stringify(user))
    },
    
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        localStorage.setItem('userToken', token)
      } else {
        localStorage.removeItem('userToken')
      }
    },
    
    LOGOUT(state) {
      state.user = null
      state.token = null
      state.cart = []
      localStorage.removeItem('userToken')
      localStorage.removeItem('userInfo')
    },
    
    SET_PRODUCTS(state, products) {
      state.products = products
    },
    
    SET_CATEGORIES(state, categories) {
      state.categories = categories
    },
    
    SET_CART(state, cart) {
      state.cart = cart
    },
    
    ADD_TO_CART(state, item) {
      const existing = state.cart.find(i => i.productId === item.productId)
      if (existing) {
        existing.quantity += item.quantity
      } else {
        state.cart.push(item)
      }
    },
    
    UPDATE_CART_ITEM(state, { productId, quantity }) {
      const item = state.cart.find(i => i.productId === productId)
      if (item) {
        item.quantity = quantity
      }
    },
    
    REMOVE_FROM_CART(state, productId) {
      state.cart = state.cart.filter(i => i.productId !== productId)
    },
    
    CLEAR_CART(state) {
      state.cart = []
    },
    
    SET_LOADING(state, isLoading) {
      state.isLoading = isLoading
    }
  },
  
  actions: {
    // 登录
    async login({ commit }, credentials) {
      commit('SET_LOADING', true)
      try {
        const response = await api.auth.login(credentials)
        const { token, user } = response.data
        
        commit('SET_TOKEN', token)
        commit('SET_USER', user)
        
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    // 注册
    async register({ commit }, userData) {
      commit('SET_LOADING', true)
      try {
        const response = await api.auth.register(userData)
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    // 登出
    logout({ commit }) {
      commit('LOGOUT')
    },
    
    // 获取商品列表
    async fetchProducts({ commit }) {
      commit('SET_LOADING', true)
      try {
        const response = await api.product.getAllProducts()
        commit('SET_PRODUCTS', response.data)
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    // 获取商品分类
    async fetchCategories({ commit }) {
      try {
        const response = await api.product.getCategories()
        commit('SET_CATEGORIES', response.data)
        return response
      } catch (error) {
        throw error
      }
    },
    
    // 搜索商品
    async searchProducts({ commit }, keyword) {
      commit('SET_LOADING', true)
      try {
        const response = await api.product.searchProducts(keyword)
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    // 获取购物车
    async fetchCart({ commit }) {
      try {
        const response = await api.cart.getCart()
        commit('SET_CART', response.data)
        return response
      } catch (error) {
        throw error
      }
    },
    
    // 添加到购物车
    async addToCart({ commit }, { productId, quantity }) {
      try {
        const response = await api.cart.addToCart(productId, quantity)
        commit('ADD_TO_CART', { productId, quantity })
        return response
      } catch (error) {
        throw error
      }
    },
    
    // 更新购物车
    async updateCartItem({ commit }, { productId, quantity }) {
      try {
        const response = await api.cart.updateCartItem(productId, quantity)
        commit('UPDATE_CART_ITEM', { productId, quantity })
        return response
      } catch (error) {
        throw error
      }
    },
    
    // 从购物车移除
    async removeFromCart({ commit }, productId) {
      try {
        const response = await api.cart.removeFromCart(productId)
        commit('REMOVE_FROM_CART', productId)
        return response
      } catch (error) {
        throw error
      }
    },
    
    // 清空购物车
    async clearCart({ commit }) {
      try {
        const response = await api.cart.clearCart()
        commit('CLEAR_CART')
        return response
      } catch (error) {
        throw error
      }
    },
    
    // 创建订单
    async createOrder({ commit, dispatch }, orderData) {
      commit('SET_LOADING', true)
      try {
        const response = await api.order.createOrder(orderData)
        // 订单创建成功后清空购物车
        await dispatch('clearCart')
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    // 获取订单列表
    async fetchOrders({ commit }, status) {
      commit('SET_LOADING', true)
      try {
        const response = await api.order.getOrders(status)
        return response
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    }
  },
  
  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    cartItems: state => state.cart,
    cartCount: state => state.cart.reduce((count, item) => count + item.quantity, 0),
    cartTotal: state => state.cart.reduce((total, item) => {
      // 这里需要根据productId获取商品价格
      const product = state.products.find(p => p.id === item.productId)
      return total + (product ? product.price * item.quantity : 0)
    }, 0),
    products: state => state.products,
    categories: state => state.categories,
    isLoading: state => state.isLoading
  }
})