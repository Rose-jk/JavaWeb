<template>
  <header class="header">
    <div class="container">
      <div class="header-content">
        <div class="logo">
          <router-link to="/" class="logo-link">
            <i class="fas fa-cookie-bite"></i>
            <span class="logo-text">美味零食商城</span>
          </router-link>
        </div>
        
        <div class="search-container">
          <input 
            type="text" 
            placeholder="搜索零食..." 
            class="search-input"
            v-model="searchQuery"
            @keyup.enter="performSearch"
          >
          <button class="search-btn" @click="performSearch">
            <i class="fas fa-search"></i>
          </button>
        </div>
        
        <nav class="nav">
          <ul class="nav-list">
            <li class="nav-item">
              <router-link to="/" class="nav-link">首页</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/products" class="nav-link">所有零食</router-link>
            </li>
            <li class="nav-item" v-if="isAuthenticated">
              <router-link to="/orders" class="nav-link">我的订单</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/cart" class="nav-link cart-link">
                <i class="fas fa-shopping-cart"></i>
                购物车
                <span class="cart-count" v-if="cartCount > 0">{{ cartCount }}</span>
              </router-link>
            </li>
            
            <!-- 用户菜单 -->
            <li class="nav-item user-menu" v-if="isAuthenticated">
              <div class="user-dropdown" @click="toggleUserMenu">
                <div class="user-avatar">
                  <i class="fas fa-user-circle"></i>
                </div>
                <span class="user-name">{{ userInfo.username || '用户' }}</span>
                <i class="fas fa-chevron-down dropdown-arrow"></i>
                
                <div class="dropdown-menu" v-show="showUserMenu">
                  <router-link to="/profile" class="dropdown-item">
                    <i class="fas fa-user"></i> 个人中心
                  </router-link>
                  <router-link to="/orders" class="dropdown-item">
                    <i class="fas fa-clipboard-list"></i> 我的订单
                  </router-link>
                  <div class="dropdown-divider"></div>
                  <button class="dropdown-item logout-btn" @click="handleLogout">
                    <i class="fas fa-sign-out-alt"></i> 退出登录
                  </button>
                </div>
              </div>
            </li>
            
            <li class="nav-item" v-else>
              <router-link to="/login" class="nav-link login-link">
                <i class="fas fa-sign-in-alt"></i> 登录/注册
              </router-link>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: 'Header',
  data() {
    return {
      searchQuery: '',
      showUserMenu: false,
      cartCount: 3
    }
  },
  computed: {
    isAuthenticated() {
      return localStorage.getItem('userToken') !== null
    },
    userInfo() {
      return JSON.parse(localStorage.getItem('userInfo') || '{}')
    }
  },
  methods: {
    performSearch() {
      if (this.searchQuery.trim()) {
        this.$router.push({ 
          path: '/products', 
          query: { search: this.searchQuery } 
        })
        this.searchQuery = ''
      }
    },
    toggleUserMenu() {
      this.showUserMenu = !this.showUserMenu
    },
    handleLogout() {
      // 清除本地存储
      localStorage.removeItem('userToken')
      localStorage.removeItem('userInfo')
      
      this.$message({
        message: '已成功退出登录',
        type: 'success',
        duration: 1500
      })
      
      // 关闭下拉菜单
      this.showUserMenu = false
      
      // 跳转到首页
      if (this.$route.path !== '/') {
        this.$router.push('/')
      } else {
        // 如果是首页，刷新页面以更新状态
        window.location.reload()
      }
    }
  },
  mounted() {
    // 点击其他地方关闭用户菜单
    document.addEventListener('click', (e) => {
      if (!this.$el.contains(e.target)) {
        this.showUserMenu = false
      }
    })
  }
}
</script>

<style scoped>
.header {
  background-color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 15px 0;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #ff6b6b;
  font-size: 1.5rem;
  font-weight: 700;
}

.logo-link i {
  margin-right: 10px;
  font-size: 1.8rem;
}

.logo-text {
  font-family: 'Dancing Script', cursive;
  font-size: 1.8rem;
}

.search-container {
  flex: 1;
  max-width: 400px;
  margin: 0 30px;
  position: relative;
}

.search-input {
  width: 100%;
  padding: 10px 20px 10px 45px;
  border: 1px solid #ddd;
  border-radius: 25px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: #ff6b6b;
  box-shadow: 0 0 0 3px rgba(255, 107, 107, 0.1);
  outline: none;
}

.search-btn {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #7f8c8d;
  cursor: pointer;
  font-size: 1.1rem;
}

.nav-list {
  display: flex;
  list-style: none;
  align-items: center;
}

.nav-item {
  margin-left: 25px;
}

.nav-link {
  text-decoration: none;
  color: #555;
  font-weight: 500;
  font-size: 1.1rem;
  transition: color 0.3s ease;
  position: relative;
  display: flex;
  align-items: center;
}

.nav-link:hover, .nav-link.router-link-exact-active {
  color: #ff6b6b;
}

.nav-link i {
  margin-right: 5px;
}

.cart-count {
  background-color: #ff6b6b;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  margin-left: 5px;
}

.login-link {
  background-color: #ff6b6b;
  color: white !important;
  padding: 8px 15px;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.login-link:hover {
  background-color: #ff5252;
}

.user-menu {
  position: relative;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.user-dropdown:hover {
  background-color: #f5f5f5;
}

.user-avatar {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  background-color: #3498db;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 8px;
}

.user-avatar i {
  font-size: 1.3rem;
}

.user-name {
  font-weight: 500;
  color: #2c3e50;
  margin-right: 5px;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-arrow {
  font-size: 0.8rem;
  color: #7f8c8d;
  transition: transform 0.3s ease;
}

.user-dropdown:hover .dropdown-arrow {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  min-width: 180px;
  padding: 10px 0;
  margin-top: 10px;
  z-index: 1000;
}

.dropdown-menu::before {
  content: '';
  position: absolute;
  top: -10px;
  right: 20px;
  border-left: 10px solid transparent;
  border-right: 10px solid transparent;
  border-bottom: 10px solid white;
}

.dropdown-item {
  display: block;
  padding: 10px 20px;
  text-decoration: none;
  color: #555;
  font-size: 0.95rem;
  transition: background-color 0.3s ease;
  text-align: left;
  width: 100%;
  border: none;
  background: none;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
  color: #ff6b6b;
}

.dropdown-item i {
  margin-right: 10px;
  width: 20px;
}

.dropdown-divider {
  height: 1px;
  background-color: #eee;
  margin: 10px 0;
}

.logout-btn {
  color: #e74c3c;
}

.logout-btn:hover {
  background-color: #ffeaea;
}

@media (max-width: 992px) {
  .search-container {
    display: none;
  }
  
  .nav-list {
    flex-wrap: wrap;
  }
  
  .nav-item {
    margin-left: 15px;
  }
  
  .user-name {
    display: none;
  }
}

@media (max-width: 576px) {
  .header-content {
    flex-wrap: wrap;
  }
  
  .logo {
    flex: 1;
  }
  
  .nav {
    order: 3;
    width: 100%;
    margin-top: 15px;
  }
  
  .nav-list {
    justify-content: space-between;
  }
  
  .nav-item {
    margin-left: 0;
  }
}
</style>