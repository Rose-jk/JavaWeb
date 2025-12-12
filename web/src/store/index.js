import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null,
    token: localStorage.getItem('userToken') || null,
    cart: [],
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
      localStorage.removeItem('userToken')
      localStorage.removeItem('userInfo')
    },
    
    SET_LOADING(state, isLoading) {
      state.isLoading = isLoading
    },
    
    ADD_TO_CART(state, product) {
      const existingItem = state.cart.find(item => item.id === product.id)
      if (existingItem) {
        existingItem.quantity += product.quantity || 1
      } else {
        state.cart.push({ ...product, quantity: product.quantity || 1 })
      }
    },
    
    REMOVE_FROM_CART(state, productId) {
      state.cart = state.cart.filter(item => item.id !== productId)
    },
    
    UPDATE_CART_ITEM(state, { productId, quantity }) {
      const item = state.cart.find(item => item.id === productId)
      if (item) {
        item.quantity = quantity
      }
    },
    
    CLEAR_CART(state) {
      state.cart = []
    }
  },
  
  actions: {
    async login({ commit }, credentials) {
      commit('SET_LOADING', true)
      
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // 模拟API响应
        const mockResponse = {
          success: true,
          token: 'mock-jwt-token-123456',
          user: {
            id: 1,
            username: credentials.email.split('@')[0],
            email: credentials.email,
            phone: '13800138000',
            avatar: null,
            registerDate: new Date().toISOString().split('T')[0]
          }
        }
        
        if (mockResponse.success) {
          commit('SET_TOKEN', mockResponse.token)
          commit('SET_USER', mockResponse.user)
          return mockResponse
        } else {
          throw new Error('登录失败')
        }
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async register({ commit }, userData) {
      commit('SET_LOADING', true)
      
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1500))
        
        // 模拟API响应
        const mockResponse = {
          success: true,
          message: '注册成功',
          user: {
            id: Math.floor(Math.random() * 1000) + 1,
            username: userData.username,
            email: userData.email,
            phone: userData.phone,
            birthdate: userData.birthdate,
            registerDate: new Date().toISOString().split('T')[0]
          }
        }
        
        return mockResponse
      } catch (error) {
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    logout({ commit }) {
      commit('LOGOUT')
    },
    
    addToCart({ commit }, product) {
      commit('ADD_TO_CART', product)
    },
    
    removeFromCart({ commit }, productId) {
      commit('REMOVE_FROM_CART', productId)
    },
    
    updateCartItem({ commit }, payload) {
      commit('UPDATE_CART_ITEM', payload)
    },
    
    clearCart({ commit }) {
      commit('CLEAR_CART')
    }
  },
  
  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    cartItems: state => state.cart,
    cartCount: state => state.cart.reduce((count, item) => count + item.quantity, 0),
    cartTotal: state => state.cart.reduce((total, item) => total + (item.price * item.quantity), 0)
  }
})