<template>
  <div class="cart-page">
    <div class="container">
      <h1 class="page-title">购物车</h1>
      
      <div class="cart-content" v-if="cartItems.length > 0">
        <div class="cart-items">
          <div class="cart-header">
            <div class="header-item product">商品</div>
            <div class="header-item price">单价</div>
            <div class="header-item quantity">数量</div>
            <div class="header-item total">小计</div>
            <div class="header-item actions">操作</div>
          </div>
          
          <div class="cart-item" v-for="item in cartItems" :key="item.id">
            <div class="cart-item-cell product-cell">
              <div class="product-image">
                <img :src="item.image" :alt="item.name" @error="setDefaultImage">
              </div>
              <div class="product-info">
                <h3 class="product-name">{{ item.name }}</h3>
                <p class="product-description">{{ item.description }}</p>
              </div>
            </div>
            
            <div class="cart-item-cell price-cell">
              <div class="current-price">¥{{ item.price.toFixed(2) }}</div>
              <div class="original-price" v-if="item.originalPrice">
                ¥{{ item.originalPrice.toFixed(2) }}
              </div>
            </div>
            
            <div class="cart-item-cell quantity-cell">
              <div class="quantity-controls">
                <button class="quantity-btn" @click="decreaseQuantity(item)">-</button>
                <input 
                  type="number" 
                  v-model.number="item.quantity" 
                  min="1" 
                  max="99"
                  class="quantity-input"
                  @change="updateQuantity(item)"
                >
                <button class="quantity-btn" @click="increaseQuantity(item)">+</button>
              </div>
            </div>
            
            <div class="cart-item-cell total-cell">
              ¥{{ (item.price * item.quantity).toFixed(2) }}
            </div>
            
            <div class="cart-item-cell actions-cell">
              <button class="remove-btn" @click="removeItem(item)">
                <i class="fas fa-trash"></i>
              </button>
            </div>
          </div>
        </div>
        
        <div class="cart-summary">
          <h2 class="summary-title">订单摘要</h2>
          
          <div class="summary-row">
            <span>商品总价</span>
            <span>¥{{ subtotal.toFixed(2) }}</span>
          </div>
          
          <div class="summary-row">
            <span>运费</span>
            <span v-if="shippingFee > 0">¥{{ shippingFee.toFixed(2) }}</span>
            <span v-else class="free-shipping">免运费</span>
          </div>
          
          <div class="summary-row discount-row">
            <span>优惠券</span>
            <div class="discount-input">
              <input type="text" v-model="couponCode" placeholder="输入优惠码">
              <button class="apply-btn" @click="applyCoupon">应用</button>
            </div>
          </div>
          
          <div class="summary-row total-row">
            <span>总计</span>
            <span class="total-amount">¥{{ total.toFixed(2) }}</span>
          </div>
          
          <div class="summary-actions">
            <router-link to="/products" class="btn btn-outline continue-shopping">
              <i class="fas fa-arrow-left"></i> 继续购物
            </router-link>
            <router-link to="/checkout" class="btn btn-primary checkout-btn">
              去结算 <i class="fas fa-arrow-right"></i>
            </router-link>
          </div>
          
          <div class="promo-notice">
            <i class="fas fa-gift"></i>
            <span>满100元免运费，新用户注册即送10元优惠券</span>
          </div>
        </div>
      </div>
      
      <div class="empty-cart" v-else>
        <div class="empty-cart-icon">
          <i class="fas fa-shopping-cart"></i>
        </div>
        <h2 class="empty-cart-title">购物车是空的</h2>
        <p class="empty-cart-text">快去挑选一些美味的零食吧</p>
        <router-link to="/products" class="btn btn-primary">
          去逛逛 <i class="fas fa-arrow-right"></i>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Cart',
  data() {
    return {
      cartItems: [
        {
          id: 1,
          name: '香脆薯片组合',
          description: '三种口味混合装，脆爽可口',
          price: 25.99,
          originalPrice: 32.99,
          quantity: 2,
          image: 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 2,
          name: '巧克力夹心饼干',
          description: '浓郁巧克力夹心，外酥内软',
          price: 18.50,
          quantity: 1,
          image: 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 3,
          name: '混合坚果礼盒',
          description: '五种坚果混合，健康营养',
          price: 45.99,
          originalPrice: 55.99,
          quantity: 1,
          image: 'https://images.unsplash.com/photo-1532939624-3af1308db9a5?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        }
      ],
      couponCode: '',
      discount: 0
    }
  },
  computed: {
    subtotal() {
      return this.cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    },
    shippingFee() {
      return this.subtotal >= 100 ? 0 : 10;
    },
    total() {
      return this.subtotal + this.shippingFee - this.discount;
    }
  },
  methods: {
    decreaseQuantity(item) {
      if (item.quantity > 1) {
        item.quantity--;
        this.updateCart();
      }
    },
    increaseQuantity(item) {
      if (item.quantity < 99) {
        item.quantity++;
        this.updateCart();
      }
    },
    updateQuantity(item) {
      if (item.quantity < 1) item.quantity = 1;
      if (item.quantity > 99) item.quantity = 99;
      this.updateCart();
    },
    removeItem(item) {
      this.cartItems = this.cartItems.filter(cartItem => cartItem.id !== item.id);
      this.$message({
        message: `已从购物车移除${item.name}`,
        type: 'info',
        duration: 1500
      });
    },
    applyCoupon() {
      if (this.couponCode === 'SNACK10') {
        this.discount = 10;
        this.$message({
          message: '优惠券已应用，减免10元',
          type: 'success',
          duration: 2000
        });
      } else if (this.couponCode) {
        this.$message({
          message: '优惠券无效',
          type: 'error',
          duration: 2000
        });
      }
    },
    updateCart() {
      // 这里可以添加更新购物车的逻辑，例如调用API
      console.log('购物车已更新:', this.cartItems);
    },
    setDefaultImage(e) {
      e.target.src = 'https://via.placeholder.com/100x100/FFE4E1/FF6B6B?text=Snack';
    }
  }
}
</script>

<style scoped>
.cart-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
  margin-bottom: 50px;
}

.cart-header {
  display: grid;
  grid-template-columns: 3fr 1fr 1fr 1fr 0.5fr;
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
  font-weight: 600;
  color: #2c3e50;
}

.header-item {
  text-align: center;
}

.header-item.product {
  text-align: left;
}

.cart-item {
  display: grid;
  grid-template-columns: 3fr 1fr 1fr 1fr 0.5fr;
  align-items: center;
  padding: 20px 15px;
  border-bottom: 1px solid #eee;
}

.cart-item-cell {
  padding: 0 10px;
}

.product-cell {
  display: flex;
  align-items: center;
}

.product-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-name {
  font-size: 1.1rem;
  margin-bottom: 5px;
  color: #2c3e50;
}

.product-description {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.price-cell {
  text-align: center;
}

.current-price {
  font-size: 1.2rem;
  font-weight: 600;
  color: #ff6b6b;
  margin-bottom: 5px;
}

.original-price {
  font-size: 0.9rem;
  color: #95a5a6;
  text-decoration: line-through;
}

.quantity-cell {
  text-align: center;
}

.quantity-controls {
  display: flex;
  justify-content: center;
}

.quantity-btn {
  width: 35px;
  height: 35px;
  background-color: #f1f2f6;
  border: 1px solid #ddd;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
}

.quantity-btn:hover {
  background-color: #e0e0e0;
}

.quantity-input {
  width: 50px;
  height: 35px;
  text-align: center;
  border: 1px solid #ddd;
  border-left: none;
  border-right: none;
  font-weight: 500;
}

.total-cell {
  text-align: center;
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
}

.actions-cell {
  text-align: center;
}

.remove-btn {
  background: none;
  border: none;
  color: #e74c3c;
  cursor: pointer;
  font-size: 1.1rem;
  transition: color 0.3s ease;
}

.remove-btn:hover {
  color: #c0392b;
}

.cart-summary {
  background-color: #f8f9fa;
  border-radius: 10px;
  padding: 25px;
  align-self: start;
  position: sticky;
  top: 20px;
}

.summary-title {
  font-size: 1.5rem;
  margin-bottom: 25px;
  color: #2c3e50;
  text-align: center;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.discount-row {
  flex-direction: column;
  align-items: flex-start;
}

.discount-input {
  display: flex;
  width: 100%;
  margin-top: 8px;
}

.discount-input input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px 0 0 5px;
}

.apply-btn {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 0 15px;
  border-radius: 0 5px 5px 0;
  cursor: pointer;
  font-weight: 500;
}

.total-row {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  border-bottom: none;
  padding-top: 15px;
  margin-top: 10px;
  border-top: 2px solid #ddd;
}

.total-amount {
  color: #ff6b6b;
  font-size: 1.5rem;
}

.free-shipping {
  color: #27ae60;
  font-weight: 500;
}

.summary-actions {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 25px;
}

.continue-shopping,
.checkout-btn {
  text-align: center;
  text-decoration: none;
  padding: 12px;
}

.continue-shopping i {
  margin-right: 8px;
}

.checkout-btn i {
  margin-left: 8px;
}

.promo-notice {
  background-color: #fff3cd;
  color: #856404;
  padding: 12px;
  border-radius: 5px;
  margin-top: 20px;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
}

.promo-notice i {
  margin-right: 10px;
  font-size: 1.1rem;
}

.empty-cart {
  text-align: center;
  padding: 80px 0;
}

.empty-cart-icon {
  font-size: 5rem;
  color: #ddd;
  margin-bottom: 20px;
}

.empty-cart-title {
  font-size: 2rem;
  margin-bottom: 15px;
  color: #7f8c8d;
}

.empty-cart-text {
  font-size: 1.1rem;
  color: #95a5a6;
  margin-bottom: 30px;
}

@media (max-width: 992px) {
  .cart-content {
    grid-template-columns: 1fr;
  }
  
  .cart-header,
  .cart-item {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .header-item {
    display: none;
  }
  
  .cart-item-cell {
    display: flex;
    justify-content: space-between;
    padding: 5px 0;
    border-bottom: 1px dashed #eee;
  }
  
  .cart-item-cell:last-child {
    border-bottom: none;
  }
  
  .cart-item-cell::before {
    content: attr(data-label);
    font-weight: 600;
    color: #2c3e50;
  }
  
  .product-cell::before,
  .price-cell::before,
  .quantity-cell::before,
  .total-cell::before,
  .actions-cell::before {
    content: none;
  }
  
  .product-cell {
    flex-direction: column;
    text-align: center;
    padding-bottom: 15px;
    border-bottom: 2px solid #eee;
  }
  
  .product-image {
    margin-right: 0;
    margin-bottom: 15px;
  }
}
</style>