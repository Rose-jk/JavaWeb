<template>
  <div class="register-page">
    <div class="container">
      <div class="register-container">
        <div class="register-left">
          <div class="benefits-content">
            <h1 class="benefits-title">加入我们</h1>
            <p class="benefits-text">注册成为会员，享受更多专属权益</p>
            <div class="benefits-list">
              <div class="benefit">
                <i class="fas fa-gift"></i>
                <div>
                  <h3>新用户礼包</h3>
                  <p>注册即送10元优惠券</p>
                </div>
              </div>
              <div class="benefit">
                <i class="fas fa-shipping-fast"></i>
                <div>
                  <h3>会员免邮</h3>
                  <p>会员享全场免运费</p>
                </div>
              </div>
              <div class="benefit">
                <i class="fas fa-star"></i>
                <div>
                  <h3>积分奖励</h3>
                  <p>消费1元积1分，积分可兑换商品</p>
                </div>
              </div>
              <div class="benefit">
                <i class="fas fa-bolt"></i>
                <div>
                  <h3>专属活动</h3>
                  <p>优先参与限时抢购活动</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="register-right">
          <div class="register-form-container">
            <div class="form-header">
              <h2 class="form-title">创建账户</h2>
              <p class="form-subtitle">请填写以下信息完成注册</p>
            </div>
            
            <form @submit.prevent="handleRegister" class="register-form">
              <div class="form-row">
                <div class="form-group">
                  <label for="username">用户名</label>
                  <div class="input-with-icon">
                    <i class="fas fa-user"></i>
                    <input 
                      type="text" 
                      id="username" 
                      v-model="registerForm.username" 
                      placeholder="请输入用户名"
                      required
                    >
                  </div>
                  <div class="error-message" v-if="errors.username">{{ errors.username }}</div>
                </div>
                
                <div class="form-group">
                  <label for="email">邮箱地址</label>
                  <div class="input-with-icon">
                    <i class="fas fa-envelope"></i>
                    <input 
                      type="email" 
                      id="email" 
                      v-model="registerForm.email" 
                      placeholder="请输入邮箱地址"
                      required
                    >
                  </div>
                  <div class="error-message" v-if="errors.email">{{ errors.email }}</div>
                </div>
              </div>
              
              <div class="form-row">
                <div class="form-group">
                  <label for="phone">手机号码</label>
                  <div class="input-with-icon">
                    <i class="fas fa-phone"></i>
                    <input 
                      type="tel" 
                      id="phone" 
                      v-model="registerForm.phone" 
                      placeholder="请输入手机号码"
                      required
                    >
                  </div>
                  <div class="error-message" v-if="errors.phone">{{ errors.phone }}</div>
                </div>
                
                <div class="form-group">
                  <label for="birthdate">出生日期</label>
                  <div class="input-with-icon">
                    <i class="fas fa-birthday-cake"></i>
                    <input 
                      type="date" 
                      id="birthdate" 
                      v-model="registerForm.birthdate" 
                      required
                    >
                  </div>
                  <div class="error-message" v-if="errors.birthdate">{{ errors.birthdate }}</div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="password">密码</label>
                <div class="input-with-icon">
                  <i class="fas fa-lock"></i>
                  <input 
                    :type="showPassword ? 'text' : 'password'" 
                    id="password" 
                    v-model="registerForm.password" 
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
                <div class="password-strength" :class="passwordStrength">
                  密码强度: {{ passwordStrengthText }}
                </div>
                <div class="error-message" v-if="errors.password">{{ errors.password }}</div>
              </div>
              
              <div class="form-group">
                <label for="confirmPassword">确认密码</label>
                <div class="input-with-icon">
                  <i class="fas fa-lock"></i>
                  <input 
                    :type="showConfirmPassword ? 'text' : 'password'" 
                    id="confirmPassword" 
                    v-model="registerForm.confirmPassword" 
                    placeholder="请再次输入密码"
                    required
                  >
                  <button 
                    type="button" 
                    class="toggle-password" 
                    @click="showConfirmPassword = !showConfirmPassword"
                  >
                    <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                  </button>
                </div>
                <div class="error-message" v-if="errors.confirmPassword">{{ errors.confirmPassword }}</div>
              </div>
              
              <div class="form-group">
                <label for="referralCode">邀请码（选填）</label>
                <div class="input-with-icon">
                  <i class="fas fa-user-friends"></i>
                  <input 
                    type="text" 
                    id="referralCode" 
                    v-model="registerForm.referralCode" 
                    placeholder="请输入邀请码"
                  >
                </div>
              </div>
              
              <div class="form-group terms">
                <label class="checkbox-label">
                  <input type="checkbox" v-model="registerForm.agreeTerms" required>
                  <span>我已阅读并同意 <a href="#" @click.prevent="showTerms = true">《用户协议》</a> 和 <a href="#" @click.prevent="showPrivacy = true">《隐私政策》</a></span>
                </label>
                <div class="error-message" v-if="errors.agreeTerms">{{ errors.agreeTerms }}</div>
              </div>
              
              <button type="submit" class="btn btn-primary register-btn" :disabled="loading">
                <span v-if="loading">注册中...</span>
                <span v-else>立即注册</span>
                <i class="fas fa-user-plus"></i>
              </button>
              
              <div class="login-link">
                已有账户？ <router-link to="/login">立即登录</router-link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 用户协议模态框 -->
    <el-dialog 
      title="用户协议" 
      :visible.sync="showTerms" 
      width="700px"
    >
      <div class="terms-content">
        <h3>美味零食商城用户协议</h3>
        <p>欢迎您注册成为美味零食商城的用户...</p>
        <!-- 这里可以放置完整的用户协议内容 -->
      </div>
      <span slot="footer" class="dialog-footer">
        <button class="btn btn-primary" @click="showTerms = false">我已阅读</button>
      </span>
    </el-dialog>
    
    <!-- 隐私政策模态框 -->
    <el-dialog 
      title="隐私政策" 
      :visible.sync="showPrivacy" 
      width="700px"
    >
      <div class="privacy-content">
        <h3>美味零食商城隐私政策</h3>
        <p>我们非常重视您的隐私保护...</p>
        <!-- 这里可以放置完整的隐私政策内容 -->
      </div>
      <span slot="footer" class="dialog-footer">
        <button class="btn btn-primary" @click="showPrivacy = false">我已阅读</button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        phone: '',
        birthdate: '',
        password: '',
        confirmPassword: '',
        referralCode: '',
        agreeTerms: false
      },
      showPassword: false,
      showConfirmPassword: false,
      loading: false,
      errors: {},
      showTerms: false,
      showPrivacy: false
    }
  },
  computed: {
    passwordStrength() {
      const password = this.registerForm.password
      if (!password) return 'weak'
      
      let strength = 0
      if (password.length >= 8) strength++
      if (/[A-Z]/.test(password)) strength++
      if (/[0-9]/.test(password)) strength++
      if (/[^A-Za-z0-9]/.test(password)) strength++
      
      if (strength <= 1) return 'weak'
      if (strength <= 3) return 'medium'
      return 'strong'
    },
    passwordStrengthText() {
      const map = {
        weak: '弱',
        medium: '中等',
        strong: '强'
      }
      return map[this.passwordStrength]
    }
  },
  methods: {
    validateForm() {
      this.errors = {}
      
      // 用户名验证
      if (!this.registerForm.username) {
        this.errors.username = '用户名不能为空'
      } else if (this.registerForm.username.length < 2) {
        this.errors.username = '用户名至少2个字符'
      }
      
      // 邮箱验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!this.registerForm.email) {
        this.errors.email = '邮箱地址不能为空'
      } else if (!emailRegex.test(this.registerForm.email)) {
        this.errors.email = '请输入有效的邮箱地址'
      }
      
      // 手机号验证
      const phoneRegex = /^1[3-9]\d{9}$/
      if (!this.registerForm.phone) {
        this.errors.phone = '手机号码不能为空'
      } else if (!phoneRegex.test(this.registerForm.phone)) {
        this.errors.phone = '请输入有效的手机号码'
      }
      
      // 出生日期验证
      if (!this.registerForm.birthdate) {
        this.errors.birthdate = '出生日期不能为空'
      } else {
        const birthDate = new Date(this.registerForm.birthdate)
        const today = new Date()
        const age = today.getFullYear() - birthDate.getFullYear()
        
        if (age < 13) {
          this.errors.birthdate = '您必须年满13岁才能注册'
        }
      }
      
      // 密码验证
      if (!this.registerForm.password) {
        this.errors.password = '密码不能为空'
      } else if (this.registerForm.password.length < 6) {
        this.errors.password = '密码长度至少6位'
      }
      
      // 确认密码验证
      if (!this.registerForm.confirmPassword) {
        this.errors.confirmPassword = '请确认密码'
      } else if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.errors.confirmPassword = '两次输入的密码不一致'
      }
      
      // 条款同意验证
      if (!this.registerForm.agreeTerms) {
        this.errors.agreeTerms = '请同意用户协议和隐私政策'
      }
      
      return Object.keys(this.errors).length === 0
    },
    
    async handleRegister() {
      if (!this.validateForm()) {
        return
      }
      
      this.loading = true
      
      try {
        // 模拟API调用
        const response = await this.$store.dispatch('register', this.registerForm)
        
        // 显示成功消息
        this.$message({
          message: '注册成功！正在跳转到登录页面...',
          type: 'success',
          duration: 2000
        })
        
        // 3秒后跳转到登录页面
        setTimeout(() => {
          this.$router.push('/login')
        }, 2000)
        
      } catch (error) {
        this.$message({
          message: error.message || '注册失败，请稍后重试',
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
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.register-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  max-width: 1200px;
  background-color: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 15px 35px rgba(50, 50, 93, 0.1), 0 5px 15px rgba(0, 0, 0, 0.07);
}

.register-left {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.benefits-content {
  max-width: 400px;
}

.benefits-title {
  font-size: 2.5rem;
  margin-bottom: 15px;
  font-weight: 700;
}

.benefits-text {
  font-size: 1.1rem;
  opacity: 0.9;
  margin-bottom: 40px;
  line-height: 1.6;
}

.benefits-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.benefit {
  display: flex;
  align-items: flex-start;
  gap: 15px;
}

.benefit i {
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.benefit h3 {
  font-size: 1.2rem;
  margin-bottom: 5px;
}

.benefit p {
  opacity: 0.8;
  font-size: 0.9rem;
  line-height: 1.5;
}

.register-right {
  padding: 60px 40px;
  overflow-y: auto;
  max-height: 800px;
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

.register-form {
  max-width: 500px;
  margin: 0 auto;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
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
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
  outline: none;
}

.input-with-icon input[type="date"] {
  padding-right: 15px;
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

.password-strength {
  margin-top: 5px;
  font-size: 0.85rem;
  padding: 3px 8px;
  border-radius: 4px;
  display: inline-block;
}

.password-strength.weak {
  background-color: #ffeaea;
  color: #e74c3c;
}

.password-strength.medium {
  background-color: #fff3cd;
  color: #f39c12;
}

.password-strength.strong {
  background-color: #d4edda;
  color: #27ae60;
}

.error-message {
  color: #e74c3c;
  font-size: 0.85rem;
  margin-top: 5px;
}

.terms .checkbox-label {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  color: #555;
  cursor: pointer;
  font-size: 0.95rem;
  line-height: 1.4;
}

.terms .checkbox-label input {
  margin-top: 3px;
}

.terms .checkbox-label a {
  color: #3498db;
  text-decoration: none;
}

.terms .checkbox-label a:hover {
  text-decoration: underline;
}

.register-btn {
  width: 100%;
  padding: 14px;
  font-size: 1.1rem;
  margin-top: 20px;
  margin-bottom: 25px;
}

.register-btn i {
  margin-left: 8px;
}

.login-link {
  text-align: center;
  color: #7f8c8d;
  font-size: 0.95rem;
}

.login-link a {
  color: #ff6b6b;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.login-link a:hover {
  color: #ff5252;
}

.terms-content,
.privacy-content {
  max-height: 400px;
  overflow-y: auto;
  padding-right: 10px;
}

.terms-content h3,
.privacy-content h3 {
  margin-bottom: 15px;
  color: #2c3e50;
}

.terms-content p,
.privacy-content p {
  margin-bottom: 15px;
  line-height: 1.6;
  color: #555;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 992px) {
  .register-container {
    grid-template-columns: 1fr;
  }
  
  .register-left {
    display: none;
  }
  
  .register-right {
    padding: 40px 20px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
    gap: 0;
  }
  
  .benefits-title {
    font-size: 2rem;
  }
}
</style>