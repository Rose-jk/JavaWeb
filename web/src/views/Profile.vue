<template>
  <div class="profile-page">
    <div class="container">
      <div class="profile-header">
        <h1 class="page-title">个人中心</h1>
        <p class="profile-subtitle">管理您的账户信息、订单和偏好设置</p>
      </div>
      
      <div class="profile-content">
        <!-- 左侧菜单 -->
        <div class="profile-sidebar">
          <div class="user-card">
            <div class="user-avatar">
              <i class="fas fa-user-circle"></i>
            </div>
            <div class="user-info">
              <h3 class="user-name">{{ userInfo.username || '用户' }}</h3>
              <p class="user-email">{{ userInfo.email || '未设置邮箱' }}</p>
              <p class="user-member">普通会员</p>
            </div>
          </div>
          
          <nav class="profile-menu">
            <router-link to="/profile" class="menu-item" exact-active-class="active">
              <i class="fas fa-user"></i>
              <span>个人信息</span>
            </router-link>
            <router-link to="/profile/orders" class="menu-item" active-class="active">
              <i class="fas fa-clipboard-list"></i>
              <span>我的订单</span>
            </router-link>
            <router-link to="/profile/addresses" class="menu-item" active-class="active">
              <i class="fas fa-map-marker-alt"></i>
              <span>收货地址</span>
            </router-link>
            <router-link to="/profile/security" class="menu-item" active-class="active">
              <i class="fas fa-shield-alt"></i>
              <span>账户安全</span>
            </router-link>
            <router-link to="/profile/settings" class="menu-item" active-class="active">
              <i class="fas fa-cog"></i>
              <span>偏好设置</span>
            </router-link>
          </nav>
        </div>
        
        <!-- 右侧内容 -->
        <div class="profile-main">
          <router-view>
            <!-- 默认显示个人信息 -->
            <div class="info-section">
              <h2 class="section-title">个人信息</h2>
              
              <div class="info-card">
                <div class="info-header">
                  <h3>基本信息</h3>
                  <button class="btn btn-outline edit-btn" @click="editProfile">
                    <i class="fas fa-edit"></i> 编辑
                  </button>
                </div>
                
                <div class="info-content">
                  <div class="info-row">
                    <div class="info-label">用户名</div>
                    <div class="info-value">{{ userInfo.username || '未设置' }}</div>
                  </div>
                  <div class="info-row">
                    <div class="info-label">邮箱地址</div>
                    <div class="info-value">{{ userInfo.email || '未设置' }}</div>
                  </div>
                  <div class="info-row">
                    <div class="info-label">手机号码</div>
                    <div class="info-value">{{ userInfo.phone || '未设置' }}</div>
                  </div>
                  <div class="info-row">
                    <div class="info-label">注册时间</div>
                    <div class="info-value">{{ userInfo.registerDate || '2023-12-11' }}</div>
                  </div>
                </div>
              </div>
              
              <div class="info-card">
                <h3>会员信息</h3>
                <div class="membership-info">
                  <div class="membership-level">
                    <div class="level-icon">
                      <i class="fas fa-crown"></i>
                    </div>
                    <div class="level-info">
                      <h4>普通会员</h4>
                      <p>再消费 ¥200 升级为白银会员</p>
                    </div>
                  </div>
                  
                  <div class="progress-container">
                    <div class="progress-bar" style="width: 40%"></div>
                  </div>
                  
                  <div class="membership-stats">
                    <div class="stat">
                      <div class="stat-value">¥600</div>
                      <div class="stat-label">累计消费</div>
                    </div>
                    <div class="stat">
                      <div class="stat-value">600</div>
                      <div class="stat-label">当前积分</div>
                    </div>
                    <div class="stat">
                      <div class="stat-value">12</div>
                      <div class="stat-label">订单数量</div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="info-card">
                <h3>近期活动</h3>
                <div class="activity-list">
                  <div class="activity-item">
                    <div class="activity-icon success">
                      <i class="fas fa-shopping-cart"></i>
                    </div>
                    <div class="activity-content">
                      <p>成功下单购买 <strong>香脆薯片组合</strong></p>
                      <span class="activity-time">2小时前</span>
                    </div>
                  </div>
                  <div class="activity-item">
                    <div class="activity-icon info">
                      <i class="fas fa-user-plus"></i>
                    </div>
                    <div class="activity-content">
                      <p>完善了个人信息</p>
                      <span class="activity-time">1天前</span>
                    </div>
                  </div>
                  <div class="activity-item">
                    <div class="activity-icon warning">
                      <i class="fas fa-ticket-alt"></i>
                    </div>
                    <div class="activity-content">
                      <p>使用优惠券减免 ¥10</p>
                      <span class="activity-time">3天前</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Profile',
  data() {
    return {
      userInfo: {
        username: '零食爱好者',
        email: 'user@example.com',
        phone: '13800138000',
        registerDate: '2023-01-15'
      }
    }
  },
  created() {
    // 从localStorage获取用户信息
    const storedUserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (storedUserInfo.username) {
      this.userInfo = { ...this.userInfo, ...storedUserInfo }
    }
  },
  methods: {
    editProfile() {
      this.$message({
        message: '编辑个人信息功能开发中...',
        type: 'info',
        duration: 2000
      })
    }
  }
}
</script>

<style scoped>
.profile-header {
  margin-bottom: 40px;
}

.profile-subtitle {
  color: #7f8c8d;
  text-align: center;
  font-size: 1.1rem;
}

.profile-content {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 30px;
}

.profile-sidebar {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  align-self: start;
  position: sticky;
  top: 20px;
}

.user-card {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
  color: white;
  padding: 30px 20px;
  text-align: center;
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 15px;
  font-size: 2.5rem;
}

.user-name {
  font-size: 1.3rem;
  margin-bottom: 5px;
}

.user-email {
  opacity: 0.9;
  font-size: 0.9rem;
  margin-bottom: 10px;
}

.user-member {
  display: inline-block;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 0.85rem;
}

.profile-menu {
  padding: 20px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 15px 25px;
  text-decoration: none;
  color: #555;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.menu-item:hover {
  background-color: #f8f9fa;
  color: #ff6b6b;
}

.menu-item.active {
  background-color: #fff8f8;
  color: #ff6b6b;
  border-left-color: #ff6b6b;
}

.menu-item i {
  margin-right: 12px;
  width: 20px;
  text-align: center;
}

.profile-main {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 30px;
}

.info-section {
  max-width: 800px;
  margin: 0 auto;
}

.info-card {
  background-color: #f8f9fa;
  border-radius: 10px;
  padding: 25px;
  margin-bottom: 25px;
}

.info-card h3 {
  font-size: 1.3rem;
  margin-bottom: 20px;
  color: #2c3e50;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.edit-btn {
  font-size: 0.9rem;
  padding: 6px 12px;
}

.info-content {
  display: grid;
  gap: 15px;
}

.info-row {
  display: grid;
  grid-template-columns: 120px 1fr;
  align-items: center;
}

.info-label {
  color: #7f8c8d;
  font-weight: 500;
}

.info-value {
  color: #2c3e50;
  font-weight: 500;
}

.membership-info {
  display: grid;
  gap: 20px;
}

.membership-level {
  display: flex;
  align-items: center;
  gap: 15px;
}

.level-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ffd700 0%, #ffa500 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.level-info h4 {
  font-size: 1.2rem;
  margin-bottom: 5px;
  color: #2c3e50;
}

.level-info p {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.progress-container {
  height: 8px;
  background-color: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(to right, #ff6b6b, #ffa500);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.membership-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.stat {
  text-align: center;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: #ff6b6b;
  margin-bottom: 5px;
}

.stat-label {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.activity-list {
  display: grid;
  gap: 15px;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  border-left: 4px solid #3498db;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.activity-icon.success {
  background-color: #27ae60;
}

.activity-icon.info {
  background-color: #3498db;
}

.activity-icon.warning {
  background-color: #f39c12;
}

.activity-content {
  flex: 1;
}

.activity-content p {
  margin-bottom: 5px;
  color: #2c3e50;
}

.activity-time {
  color: #95a5a6;
  font-size: 0.85rem;
}

@media (max-width: 992px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
  
  .profile-sidebar {
    position: static;
  }
  
  .profile-menu {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .menu-item {
    border-left: none;
    border-bottom: 3px solid transparent;
    padding: 10px 15px;
  }
  
  .menu-item.active {
    border-left-color: transparent;
    border-bottom-color: #ff6b6b;
  }
}

@media (max-width: 576px) {
  .profile-main {
    padding: 20px;
  }
  
  .info-card {
    padding: 15px;
  }
  
  .info-row {
    grid-template-columns: 1fr;
    gap: 5px;
  }
  
  .membership-stats {
    grid-template-columns: 1fr;
  }
  
  .activity-item {
    flex-direction: column;
    text-align: center;
  }
}
</style>