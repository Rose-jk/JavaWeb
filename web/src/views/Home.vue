<template>
  <div class="home">
    <!-- 英雄区域 -->
    <section class="hero">
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">发现零食新世界</h1>
          <p class="hero-subtitle">来自全球的美味零食，满足您的味蕾体验</p>
          <router-link to="/products" class="btn btn-primary hero-btn">
            立即选购 <i class="fas fa-arrow-right"></i>
          </router-link>
        </div>
      </div>
    </section>
    
    <!-- 特色产品 -->
    <div class="container">
      <h2 class="section-title">热销零食</h2>
      <div class="products-grid">
        <ProductCard 
          v-for="product in featuredProducts" 
          :key="product.id" 
          :product="product"
          @view-details="goToProductDetail"
          @add-to-cart="addToCart"
        />
      </div>
      
      <!-- 分类区域 -->
      <h2 class="section-title">零食分类</h2>
      <div class="categories">
        <div class="category-card" v-for="category in categories" :key="category.id">
          <div class="category-icon" :style="{ backgroundColor: category.color }">
            <i :class="category.icon"></i>
          </div>
          <h3 class="category-name">{{ category.name }}</h3>
          <p class="category-count">{{ category.count }} 种零食</p>
        </div>
      </div>
      
      <!-- 促销区域 -->
      <div class="promo-banner">
        <div class="promo-content">
          <h2 class="promo-title">限时优惠</h2>
          <p class="promo-text">全场零食满100减20，新用户注册即送10元优惠券</p>
          <router-link to="/products" class="btn btn-outline">立即抢购</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProductCard from '@/components/ProductCard.vue'

export default {
  name: 'Home',
  components: {
    ProductCard
  },
  data() {
    return {
      featuredProducts: [
        {
          id: 1,
          name: '香脆薯片组合',
          description: '三种口味混合装，脆爽可口，休闲必备',
          price: 25.99,
          originalPrice: 32.99,
          image: 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 2,
          name: '巧克力夹心饼干',
          description: '浓郁巧克力夹心，外酥内软，甜蜜享受',
          price: 18.50,
          image: 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-1.2.1&auto=format&fit=crop&w-800&q=80'
        },
        {
          id: 3,
          name: '混合坚果礼盒',
          description: '五种坚果混合，健康营养，每日一包',
          price: 45.99,
          originalPrice: 55.99,
          image: 'https://images.unsplash.com/photo-1532939624-3af1308db9a5?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 4,
          name: '果味软糖大礼包',
          description: '多种水果口味，Q弹有嚼劲，童年回忆',
          price: 22.80,
          image: 'https://images.unsplash.com/photo-1563805042-7684c019e1cb?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        }
      ],
      categories: [
        { id: 1, name: '薯片膨化', icon: 'fas fa-cookie', color: '#FF9F43', count: 25 },
        { id: 2, name: '饼干糕点', icon: 'fas fa-birthday-cake', color: '#FF6B6B', count: 32 },
        { id: 3, name: '糖果巧克力', icon: 'fas fa-candy-cane', color: '#EE5A24', count: 48 },
        { id: 4, name: '坚果炒货', icon: 'fas fa-seedling', color: '#A3CB38', count: 18 },
        { id: 5, name: '肉干肉脯', icon: 'fas fa-drumstick-bite', color: '#ED4C67', count: 15 },
        { id: 6, name: '进口零食', icon: 'fas fa-globe-americas', color: '#12CBC4', count: 36 }
      ]
    }
  },
  methods: {
    goToProductDetail(productId) {
      this.$router.push({ name: 'ProductDetail', params: { id: productId } });
    },
    addToCart(product) {
      // 这里将实现添加购物车逻辑
      console.log('添加商品到购物车:', product);
    }
  }
}
</script>

<style scoped>
.hero {
  background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), 
              url('https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80');
  background-size: cover;
  background-position: center;
  color: white;
  padding: 100px 0;
  text-align: center;
  margin-bottom: 50px;
}

.hero-title {
  font-size: 3.5rem;
  margin-bottom: 15px;
  font-weight: 700;
}

.hero-subtitle {
  font-size: 1.3rem;
  margin-bottom: 30px;
  opacity: 0.9;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.hero-btn {
  font-size: 1.2rem;
  padding: 12px 30px;
}

.hero-btn i {
  margin-left: 8px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
  margin-bottom: 50px;
}

.categories {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 20px;
  margin-bottom: 50px;
}

.category-card {
  background-color: white;
  border-radius: 10px;
  padding: 25px 20px;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.category-card:hover {
  transform: translateY(-5px);
}

.category-icon {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 15px;
  color: white;
  font-size: 1.8rem;
}

.category-name {
  font-size: 1.2rem;
  margin-bottom: 8px;
  color: #2c3e50;
}

.category-count {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.promo-banner {
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
  border-radius: 15px;
  padding: 40px;
  margin: 50px 0;
  text-align: center;
  color: white;
}

.promo-title {
  font-size: 2.2rem;
  margin-bottom: 15px;
}

.promo-text {
  font-size: 1.2rem;
  margin-bottom: 25px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}
</style>