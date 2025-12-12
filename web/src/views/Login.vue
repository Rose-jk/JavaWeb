<template>
  <div class="login-page">
    <div class="container">
      <div class="login-container">
        <div class="login-left">
          <div class="welcome-content">
            <h1 class="welcome-title">欢迎回来</h1>
            <p class="welcome-text">登录您的账户，继续探索美味的零食世界</p>
            <div class="features">
              <div class="feature">
                <i class="fas fa-shipping-fast"></i>
                <span>快速配送</span>
              </div>
              <div class="feature">
                <i class="fas fa-shield-alt"></i>
                <span>安全支付</span>
              </div>
              <div class="feature">
                <i class="fas fa-tags"></i>
                <span>专属优惠</span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="login-right">
          <div class="login-form-container">
            <div class="form-header">
              <h2 class="form-title">登录</h2>
              <p class="form-subtitle">请登录您的账户</p>
            </div>
            
            <form @submit.prevent="handleLogin" class="login-form">
              <div class="form-group">
                <label for="email">邮箱地址</label>
                <div class="input-with-icon">
                  <i class="fas fa-envelope"></i>
                  <input 
                    type="email" 
                    id="email" 
                    v-model="loginForm.email" 
                    placeholder="请输入邮箱地址"
                    required
                  >
                </div>
                <div class="error-message" v-if="errors.email">{{ errors.email }}</div>
              </div>
              
              <div class="form-group">
                <label for="password">密码</label>
                <div class="input-with-icon">
                  <i class="fas fa-lock"></i>
                  <input 
                    :type="showPassword ? 'text' : 'password'" 
                    id="password" 
                    v-model="loginForm.password" 
                    placeholder="请输入密码"
                    required
                  >
                  <button 
                    type="button" 
                    class="toggle-password" 
                    @click="showPassword = !showPassword"
                  >
                    <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                  </button>
                </div>
                <div class="error-message" v-if="errors.password">{{ errors.password }}</div>
              </div>
              
              <div class="form-options">
                <label class="remember-me">
                  <input type="checkbox" v-model="loginForm.remember">
                  记住我
                </label>
                <router-link to="/forgot-password" class="forgot-password">忘记密码？</router-link>
              </div>
              
              <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
                <span v-if="loading">登录中...</span>
                <span v-else>登录</span>
                <i class="fas fa-sign-in-alt"></i>
              </button>
              
              <div class="social-login">
                <p class="divider">或使用社交账号登录</p>
                <div class="social-buttons">
                  <button type="button" class="social-btn wechat">
                    <i class="fab fa-weixin"></i>
                  </button>
                  <button type="button" class="social-btn qq">
                    <i class="fab fa-qq"></i>
                  </button>
                  <button type="button" class="social-btn weibo">
                    <i class="fab fa-weibo"></i>
                  </button>
                </div>
              </div>
              
              <div class="register-link">
                还没有账户？ <router-link to="/register">立即注册</router-link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        email: '',
        password: '',
        remember: false
      },
      showPassword: false,
      loading: false,
      errors: {}
    }
  },
  methods: {
    validateForm() {
      this.errors = {}
      
      // 邮箱验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!this.loginForm.email) {
        this.errors.email = '邮箱地址不能为空'
      } else if (!emailRegex.test(this.loginForm.email)) {
        this.errors.email = '请输入有效的邮箱地址'
      }
      
      // 密码验证
      if (!this.loginForm.password) {
        this.errors.password = '密码不能为空'
      } else if (this.loginForm.password.length < 6) {
        this.errors.password = '密码长度至少6位'
      }
      
      return Object.keys(this.errors).length === 0
    },
    
    async handleLogin() {
      if (!this.validateForm()) {
        return
      }
      
      this.loading = true
      
      try {
        // 模拟API调用
        await this.$store.dispatch('login', this.loginForm)
        
        // 显示成功消息
        this.$message({
          message: '登录成功！',
          type: 'success',
          duration: 2000
        })
        
        // 跳转到首页或来源页面
        const redirect = this.$route.query.redirect || '/'
        this.$router.push(redirect)
        
      } catch (error) {
        this.$message({
          message: error.message || '登录失败，请检查邮箱和密码',
          type: 'error',
          duration: 3000
        })
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.login-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  max-width: 1000px;
  background-color: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 15px 35px rgba(50, 50, 93, 0.1), 0 5px 15px rgba(0, 0, 0, 0.07);
}

.login-left {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
  color: white;
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.welcome-content {
  text-align: center;
}

.welcome-title {
  font-size: 2.5rem;
  margin-bottom: 15px;
  font-weight: 700;
}

.welcome-text {
  font-size: 1.1rem;
  opacity: 0.9;
  margin-bottom: 40px;
  line-height: 1.6;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  font-size: 1.1rem;
}

.feature i {
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-right {
  padding: 60px 40px;
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-title {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 10px;
}

.form-subtitle {
  color: #7f8c8d;
  font-size: 1rem;
}

.login-form {
  max-width: 400px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 25px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
}

.input-with-icon {
  position: relative;
}

.input-with-icon i {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #7f8c8d;
}

.input-with-icon input {
  width: 100%;
  padding: 12px 15px 12px 45px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.input-with-icon input:focus {
  border-color: #ff6b6b;
  box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.1);
  outline: none;
}

.toggle-password {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #7f8c8d;
  cursor: pointer;
  font-size: 1.1rem;
}

.error-message {
  color: #e74c3c;
  font-size: 0.85rem;
  margin-top: 5px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #555;
  cursor: pointer;
  font-size: 0.95rem;
}

.remember-me input {
  width: 16px;
  height: 16px;
}

.forgot-password {
  color: #3498db;
  text-decoration: none;
  font-size: 0.95rem;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #2980b9;
}

.login-btn {
  width: 100%;
  padding: 14px;
  font-size: 1.1rem;
  margin-bottom: 25px;
}

.login-btn i {
  margin-left: 8px;
}

.social-login {
  margin-bottom: 25px;
}

.divider {
  text-align: center;
  position: relative;
  color: #95a5a6;
  margin-bottom: 20px;
}

.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 40%;
  height: 1px;
  background-color: #eee;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.social-btn {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: none;
  font-size: 1.3rem;
  color: white;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.social-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.social-btn.wechat {
  background-color: #07C160;
}

.social-btn.qq {
  background-color: #12B7F5;
}

.social-btn.weibo {
  background-color: #E6162D;
}

.register-link {
  text-align: center;
  color: #7f8c8d;
  font-size: 0.95rem;
}

.register-link a {
  color: #ff6b6b;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.register-link a:hover {
  color: #ff5252;
}

@media (max-width: 768px) {
  .login-container {
    grid-template-columns: 1fr;
  }
  
  .login-left {
    padding: 40px 20px;
    display: none;
  }
  
  .login-right {
    padding: 40px 20px;
  }
  
  .welcome-title {
    font-size: 2rem;
  }
}
</style>