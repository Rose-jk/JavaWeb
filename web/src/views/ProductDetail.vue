<template>
  <div class="product-detail">
    <div class="container" v-if="product">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <router-link to="/">首页</router-link>
        <span class="separator">/</span>
        <router-link to="/products">所有零食</router-link>
        <span class="separator">/</span>
        <span class="current">{{ product.name }}</span>
      </div>
      
      <div class="product-detail-content">
        <!-- 产品图片 -->
        <div class="product-images">
          <div class="main-image">
            <img :src="product.image" :alt="product.name" @error="setDefaultImage">
          </div>
        </div>
        
        <!-- 产品信息 -->
        <div class="product-info">
          <h1 class="product-title">{{ product.name }}</h1>
          <div class="product-rating">
            <span class="stars">
              <i v-for="n in 5" :key="n" class="fas fa-star" :class="{ 'filled': n <= product.rating }"></i>
            </span>
            <span class="rating-text">{{ product.rating }} ({{ product.reviewCount }}条评价)</span>
          </div>
          
          <div class="product-price-section">
            <div class="current-price">¥{{ product.price.toFixed(2) }}</div>
            <div class="original-price" v-if="product.originalPrice">
              ¥{{ product.originalPrice.toFixed(2) }}
            </div>
            <div class="discount" v-if="product.originalPrice">
              节省 ¥{{ (product.originalPrice - product.price).toFixed(2) }}
            </div>
          </div>
          
          <div class="product-description">
            <h3>产品描述</h3>
            <p>{{ product.fullDescription }}</p>
          </div>
          
          <div class="product-specs">
            <h3>产品规格</h3>
            <ul>
              <li><strong>净含量:</strong> {{ product.weight }}</li>
              <li><strong>保质期:</strong> {{ product.expiryDate }}</li>
              <li><strong>产地:</strong> {{ product.origin }}</li>
              <li><strong>存储方式:</strong> {{ product.storage }}</li>
            </ul>
          </div>
          
          <div class="quantity-selector">
            <label for="quantity">数量:</label>
            <div class="quantity-controls">
              <button class="quantity-btn" @click="decreaseQuantity">-</button>
              <input 
                type="number" 
                id="quantity" 
                v-model.number="quantity" 
                min="1" 
                max="99"
                class="quantity-input"
              >
              <button class="quantity-btn" @click="increaseQuantity">+</button>
            </div>
          </div>
          
          <div class="product-actions">
            <button class="btn btn-primary add-to-cart-btn" @click="addToCart">
              <i class="fas fa-cart-plus"></i> 加入购物车
            </button>
            <button class="btn btn-outline buy-now-btn" @click="buyNow">
              <i class="fas fa-bolt"></i> 立即购买
            </button>
          </div>
          
          <div class="product-tags">
            <span class="tag" v-for="tag in product.tags" :key="tag">{{ tag }}</span>
          </div>
        </div>
      </div>
      
      <!-- 相关产品 -->
      <div class="related-products" v-if="relatedProducts.length > 0">
        <h2 class="section-title">相关推荐</h2>
        <div class="related-products-grid">
          <ProductCard 
            v-for="product in relatedProducts" 
            :key="product.id" 
            :product="product"
            @view-details="goToProductDetail"
            @add-to-cart="addToCart"
          />
        </div>
      </div>
    </div>
    
    <div class="container" v-else>
      <div class="loading">
        <i class="fas fa-spinner fa-spin"></i>
        <p>加载中...</p>
      </div>
    </div>
  </div>
</template>

<script>
import ProductCard from '@/components/ProductCard.vue'

export default {
  name: 'ProductDetail',
  components: {
    ProductCard
  },
  data() {
    return {
      product: null,
      quantity: 1,
      relatedProducts: []
    }
  },
  created() {
    this.fetchProduct();
    this.fetchRelatedProducts();
  },
  methods: {
    fetchProduct() {
      const productId = parseInt(this.$route.params.id);
      
      // 模拟API调用
      const products = [
        {
          id: 1,
          name: '香脆薯片组合',
          description: '三种口味混合装',
          fullDescription: '精选优质马铃薯，采用独特的切片工艺和低温油炸技术，保留了马铃薯的原香和营养成分。三种口味（原味、烧烤味、番茄味）混合装，满足不同口味需求。独立小包装设计，方便携带，随时随地享受美味。',
          price: 25.99,
          originalPrice: 32.99,
          rating: 4.5,
          reviewCount: 128,
          weight: '150g x 3包',
          expiryDate: '12个月',
          origin: '中国北京',
          storage: '阴凉干燥处，避免阳光直射',
          category: 1,
          tags: ['畅销', '新品', '组合装'],
          image: 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 2,
          name: '巧克力夹心饼干',
          description: '浓郁巧克力夹心，外酥内软',
          fullDescription: '采用优质小麦粉和进口可可粉制作，饼干外皮酥脆，内夹浓郁巧克力馅料。不含人工香精和防腐剂，适合全家享用。独立包装，每包两片，方便控制食用量。',
          price: 18.50,
          originalPrice: null,
          rating: 4.7,
          reviewCount: 256,
          weight: '200g (10片装)',
          expiryDate: '9个月',
          origin: '比利时',
          storage: '阴凉干燥处',
          category: 2,
          tags: ['进口', '巧克力', '饼干'],
          image: 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 3,
          name: '混合坚果礼盒',
          description: '五种坚果混合，健康营养',
          fullDescription: '精选五种优质坚果（核桃、杏仁、腰果、夏威夷果、碧根果），低温烘焙，保留原香和营养。不添加人工色素和防腐剂，是健康零食的绝佳选择。精美礼盒包装，适合送礼。',
          price: 45.99,
          originalPrice: 55.99,
          rating: 4.8,
          reviewCount: 89,
          weight: '500g',
          expiryDate: '6个月',
          origin: '美国',
          storage: '密封冷藏',
          category: 4,
          tags: ['健康', '礼盒', '进口'],
          image: 'https://images.unsplash.com/photo-1532939624-3af1308db9a5?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        }
      ];
      
      this.product = products.find(p => p.id === productId) || products[0];
    },
    fetchRelatedProducts() {
      // 模拟相关产品数据
      this.relatedProducts = [
        {
          id: 4,
          name: '果味软糖大礼包',
          description: '多种水果口味，Q弹有嚼劲',
          price: 22.80,
          image: 'https://images.unsplash.com/photo-1563805042-7684c019e1cb?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 5,
          name: '香辣牛肉干',
          description: '传统工艺制作，香辣可口',
          price: 38.50,
          image: 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 6,
          name: '日本进口抹茶饼干',
          description: '正宗宇治抹茶制作，茶香浓郁',
          price: 32.99,
          image: 'https://images.unsplash.com/photo-1558322456-bb57647bf5f0?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        }
      ];
    },
    decreaseQuantity() {
      if (this.quantity > 1) {
        this.quantity--;
      }
    },
    increaseQuantity() {
      if (this.quantity < 99) {
        this.quantity++;
      }
    },
    addToCart() {
      const cartItem = {
        ...this.product,
        quantity: this.quantity
      };
      
      console.log('添加到购物车:', cartItem);
      this.$message({
        message: `已添加${this.quantity}件${this.product.name}到购物车`,
        type: 'success',
        duration: 2000
      });
    },
    buyNow() {
      this.addToCart();
      this.$router.push('/checkout');
    },
    goToProductDetail(productId) {
      this.$router.push({ name: 'ProductDetail', params: { id: productId } });
    },
    setDefaultImage(e) {
      e.target.src = 'https://via.placeholder.com/500x400/FFE4E1/FF6B6B?text=Snack+Image';
    }
  }
}
</script>

<style scoped>
.breadcrumb {
  margin-bottom: 30px;
  font-size: 0.95rem;
}

.breadcrumb a {
  color: #666;
  text-decoration: none;
  transition: color 0.3s ease;
}

.breadcrumb a:hover {
  color: #ff6b6b;
}

.breadcrumb .separator {
  margin: 0 10px;
  color: #ccc;
}

.breadcrumb .current {
  color: #ff6b6b;
  font-weight: 500;
}

.product-detail-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 50px;
  margin-bottom: 60px;
}

.product-images {
  display: flex;
  flex-direction: column;
}

.main-image {
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.main-image img {
  width: 100%;
  height: auto;
  display: block;
}

.product-title {
  font-size: 2.2rem;
  margin-bottom: 10px;
  color: #2c3e50;
}

.product-rating {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.stars {
  color: #ddd;
  margin-right: 10px;
}

.stars .filled {
  color: #ffc107;
}

.rating-text {
  color: #7f8c8d;
  font-size: 0.95rem;
}

.product-price-section {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.current-price {
  font-size: 2.5rem;
  font-weight: 700;
  color: #ff6b6b;
  margin-bottom: 5px;
}

.original-price {
  font-size: 1.2rem;
  color: #95a5a6;
  text-decoration: line-through;
  margin-bottom: 5px;
}

.discount {
  font-size: 1rem;
  color: #27ae60;
  font-weight: 500;
}

.product-description,
.product-specs {
  margin-bottom: 25px;
}

.product-description h3,
.product-specs h3 {
  font-size: 1.3rem;
  margin-bottom: 10px;
  color: #2c3e50;
}

.product-description p {
  line-height: 1.7;
  color: #555;
}

.product-specs ul {
  list-style: none;
  padding-left: 0;
}

.product-specs li {
  margin-bottom: 8px;
  color: #555;
}

.product-specs strong {
  color: #2c3e50;
  margin-right: 10px;
  min-width: 80px;
  display: inline-block;
}

.quantity-selector {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.quantity-selector label {
  margin-right: 15px;
  font-weight: 500;
  color: #2c3e50;
}

.quantity-controls {
  display: flex;
  align-items: center;
}

.quantity-btn {
  width: 40px;
  height: 40px;
  background-color: #f1f2f6;
  border: 1px solid #ddd;
  font-size: 1.2rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantity-btn:hover {
  background-color: #e0e0e0;
}

.quantity-input {
  width: 60px;
  height: 40px;
  text-align: center;
  border: 1px solid #ddd;
  border-left: none;
  border-right: none;
  font-size: 1.1rem;
  font-weight: 500;
}

.product-actions {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
}

.add-to-cart-btn,
.buy-now-btn {
  flex: 1;
  padding: 15px;
  font-size: 1.1rem;
}

.add-to-cart-btn i,
.buy-now-btn i {
  margin-right: 8px;
}

.product-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag {
  background-color: #e8f4fc;
  color: #3498db;
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.related-products {
  margin-top: 50px;
}

.related-products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
}

.loading {
  text-align: center;
  padding: 100px 0;
  color: #7f8c8d;
  font-size: 1.2rem;
}

.loading i {
  font-size: 2.5rem;
  margin-bottom: 15px;
  color: #ff6b6b;
}

@media (max-width: 992px) {
  .product-detail-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
}

@media (max-width: 576px) {
  .product-actions {
    flex-direction: column;
  }
}
</style>