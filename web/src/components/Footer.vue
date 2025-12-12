<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3 class="footer-title">美味零食商城</h3>
          <p class="footer-text">为您提供最新鲜、最美味的零食选择，让每一天都充满甜蜜滋味。</p>
        </div>
        
        <div class="footer-section">
          <h3 class="footer-title">快速链接</h3>
          <ul class="footer-links">
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/products">所有零食</router-link></li>
            <li><router-link to="/cart">购物车</router-link></li>
          </ul>
        </div>
        
        <div class="footer-section">
          <h3 class="footer-title">联系我们</h3>
          <p class="footer-text">
            <i class="fas fa-phone"></i> 400-123-4567<br>
            <i class="fas fa-envelope"></i> support@snackshop.com<br>
            <i class="fas fa-map-marker-alt"></i> 北京市朝阳区零食大道1号
          </p>
        </div>
      </div>
      
      <div class="footer-bottom">
        <p>&copy; 2023 美味零食商城. 保留所有权利.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
.footer {
  background-color: #2c3e50;
  color: #ecf0f1;
  padding: 40px 0 20px;
  margin-top: 50px;
}

.footer-content {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 30px;
}

.footer-section {
  flex: 1;
  min-width: 250px;
  margin-bottom: 20px;
  padding-right: 20px;
}

.footer-title {
  font-size: 1.3rem;
  margin-bottom: 15px;
  color: #ff6b6b;
}

.footer-text {
  line-height: 1.8;
  color: #bdc3c7;
}

.footer-links {
  list-style: none;
}

.footer-links li {
  margin-bottom: 10px;
}

.footer-links a {
  color: #bdc3c7;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #ff6b6b;
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #34495e;
  color: #95a5a6;
  font-size: 0.9rem;
}

.footer-text i {
  margin-right: 8px;
  width: 20px;
}
</style>