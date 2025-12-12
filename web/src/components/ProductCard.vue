<template>
  <div class="product-card">
    <div class="product-image">
      <img :src="product.image" :alt="product.name" @error="setDefaultImage">
      <div class="product-overlay">
        <button class="btn-overlay" @click="viewDetails">
          <i class="fas fa-eye"></i> 查看详情
        </button>
      </div>
    </div>
    
    <div class="product-info">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-description">{{ product.description }}</p>
      
      <div class="product-footer">
        <div class="product-price">
          <span class="current-price">¥{{ product.price.toFixed(2) }}</span>
          <span class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice.toFixed(2) }}</span>
        </div>
        
        <div class="product-actions">
          <button class="btn-add-to-cart" @click="addToCart">
            <i class="fas fa-cart-plus"></i> 加入购物车
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true
    }
  },
  methods: {
    viewDetails() {
      this.$emit('view-details', this.product.id);
    },
    addToCart() {
      this.$emit('add-to-cart', this.product);
      this.$message({
        message: `已添加${this.product.name}到购物车`,
        type: 'success',
        duration: 1500
      });
    },
    setDefaultImage(e) {
      e.target.src = 'https://via.placeholder.com/300x200/FFE4E1/FF6B6B?text=Snack+Image';
    }
  }
}
</script>

<style scoped>
.product-card {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.12);
}

.product-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.btn-overlay {
  background-color: #ff6b6b;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.btn-overlay:hover {
  background-color: #ff5252;
}

.product-info {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 1.3rem;
  margin-bottom: 10px;
  color: #2c3e50;
}

.product-description {
  color: #7f8c8d;
  margin-bottom: 15px;
  flex: 1;
  font-size: 0.95rem;
  line-height: 1.5;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.product-price {
  display: flex;
  flex-direction: column;
}

.current-price {
  font-size: 1.5rem;
  font-weight: 700;
  color: #ff6b6b;
}

.original-price {
  font-size: 1rem;
  color: #95a5a6;
  text-decoration: line-through;
}

.btn-add-to-cart {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
}

.btn-add-to-cart:hover {
  background-color: #2980b9;
}

.btn-add-to-cart i {
  margin-right: 5px;
}
</style>