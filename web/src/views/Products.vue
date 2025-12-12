<template>
  <div class="products-page">
    <div class="container">
      <h1 class="page-title">所有零食</h1>
      
      <div class="products-controls">
        <div class="search-filter">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索零食名称..."
            class="search-input"
          >
          <button class="search-btn">
            <i class="fas fa-search"></i>
          </button>
        </div>
        
        <div class="filter-options">
          <select v-model="selectedCategory" class="filter-select">
            <option value="">所有分类</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
          
          <select v-model="sortBy" class="filter-select">
            <option value="default">默认排序</option>
            <option value="price-low">价格从低到高</option>
            <option value="price-high">价格从高到低</option>
            <option value="name">按名称排序</option>
          </select>
        </div>
      </div>
      
      <div class="products-grid">
        <ProductCard 
          v-for="product in filteredProducts" 
          :key="product.id" 
          :product="product"
          @view-details="goToProductDetail"
          @add-to-cart="addToCart"
        />
      </div>
      
      <!-- 分页 -->
      <div class="pagination" v-if="totalPages > 1">
        <button 
          class="pagination-btn" 
          :disabled="currentPage === 1"
          @click="changePage(currentPage - 1)"
        >
          <i class="fas fa-chevron-left"></i> 上一页
        </button>
        
        <span class="pagination-info">
          第 {{ currentPage }} 页 / 共 {{ totalPages }} 页
        </span>
        
        <button 
          class="pagination-btn" 
          :disabled="currentPage === totalPages"
          @click="changePage(currentPage + 1)"
        >
          下一页 <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ProductCard from '@/components/ProductCard.vue'

export default {
  name: 'Products',
  components: {
    ProductCard
  },
  data() {
    return {
      searchQuery: '',
      selectedCategory: '',
      sortBy: 'default',
      currentPage: 1,
      itemsPerPage: 8,
      categories: [
        { id: 1, name: '薯片膨化' },
        { id: 2, name: '饼干糕点' },
        { id: 3, name: '糖果巧克力' },
        { id: 4, name: '坚果炒货' },
        { id: 5, name: '肉干肉脯' },
        { id: 6, name: '进口零食' }
      ],
      products: [
        {
          id: 1,
          name: '香脆薯片组合',
          description: '三种口味混合装，脆爽可口，休闲必备',
          price: 25.99,
          originalPrice: 32.99,
          category: 1,
          image: 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 2,
          name: '巧克力夹心饼干',
          description: '浓郁巧克力夹心，外酥内软，甜蜜享受',
          price: 18.50,
          category: 2,
          image: 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 3,
          name: '混合坚果礼盒',
          description: '五种坚果混合，健康营养，每日一包',
          price: 45.99,
          originalPrice: 55.99,
          category: 4,
          image: 'https://images.unsplash.com/photo-1532939624-3af1308db9a5?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 4,
          name: '果味软糖大礼包',
          description: '多种水果口味，Q弹有嚼劲，童年回忆',
          price: 22.80,
          category: 3,
          image: 'https://images.unsplash.com/photo-1563805042-7684c019e1cb?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 5,
          name: '香辣牛肉干',
          description: '传统工艺制作，香辣可口，回味无穷',
          price: 38.50,
          category: 5,
          image: 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 6,
          name: '日本进口抹茶饼干',
          description: '正宗宇治抹茶制作，茶香浓郁',
          price: 32.99,
          category: 6,
          image: 'https://images.unsplash.com/photo-1558322456-bb57647bf5f0?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 7,
          name: '海苔脆片',
          description: '低卡健康零食，海苔香脆，营养丰富',
          price: 15.99,
          category: 1,
          image: 'https://images.unsplash.com/photo-1516100882582-96c3a05fe590?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 8,
          name: '比利时黑巧克力',
          description: '72%可可含量，口感醇厚，微苦回甘',
          price: 65.00,
          originalPrice: 78.00,
          category: 3,
          image: 'https://images.unsplash.com/photo-1511381939415-e44015466834?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 9,
          name: '芝士味玉米片',
          description: '浓郁芝士风味，玉米香脆，停不下来',
          price: 21.50,
          category: 1,
          image: 'https://images.unsplash.com/photo-1551887375-1a7c05e8762a?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 10,
          name: '芒果干',
          description: '菲律宾进口芒果，自然风干，酸甜可口',
          price: 28.80,
          category: 6,
          image: 'https://images.unsplash.com/photo-1528502668757-8c45371d89c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 11,
          name: '原味瓜子',
          description: '内蒙古特级瓜子，颗粒饱满，原香原味',
          price: 12.99,
          category: 4,
          image: 'https://images.unsplash.com/photo-1574323347402-f3e8f5c6c9b0?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        },
        {
          id: 12,
          name: '蛋黄酥',
          description: '传统中式糕点，外皮酥脆，蛋黄咸香',
          price: 32.00,
          category: 2,
          image: 'https://images.unsplash.com/photo-1563729784474-d77dbb933a9e?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
        }
      ]
    }
  },
  computed: {
    filteredProducts() {
      let filtered = [...this.products];
      
      // 搜索过滤
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        filtered = filtered.filter(product => 
          product.name.toLowerCase().includes(query) || 
          product.description.toLowerCase().includes(query)
        );
      }
      
      // 分类过滤
      if (this.selectedCategory) {
        filtered = filtered.filter(product => 
          product.category === parseInt(this.selectedCategory)
        );
      }
      
      // 排序
      if (this.sortBy === 'price-low') {
        filtered.sort((a, b) => a.price - b.price);
      } else if (this.sortBy === 'price-high') {
        filtered.sort((a, b) => b.price - a.price);
      } else if (this.sortBy === 'name') {
        filtered.sort((a, b) => a.name.localeCompare(b.name));
      }
      
      // 分页
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      return filtered.slice(startIndex, startIndex + this.itemsPerPage);
    },
    totalPages() {
      const filteredCount = this.products.filter(product => {
        if (this.searchQuery) {
          const query = this.searchQuery.toLowerCase();
          return product.name.toLowerCase().includes(query) || 
                 product.description.toLowerCase().includes(query);
        }
        if (this.selectedCategory) {
          return product.category === parseInt(this.selectedCategory);
        }
        return true;
      }).length;
      
      return Math.ceil(filteredCount / this.itemsPerPage);
    }
  },
  methods: {
    goToProductDetail(productId) {
      this.$router.push({ name: 'ProductDetail', params: { id: productId } });
    },
    addToCart(product) {
      console.log('添加商品到购物车:', product);
      this.$message({
        message: `已添加${product.name}到购物车`,
        type: 'success',
        duration: 1500
      });
    },
    changePage(page) {
      this.currentPage = page;
      window.scrollTo(0, 0);
    }
  },
  watch: {
    searchQuery() {
      this.currentPage = 1;
    },
    selectedCategory() {
      this.currentPage = 1;
    }
  }
}
</script>

<style scoped>
.products-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 20px;
}

.search-filter {
  display: flex;
  flex: 1;
  max-width: 400px;
}

.search-input {
  flex: 1;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 5px 0 0 5px;
  font-size: 1rem;
}

.search-btn {
  background-color: #ff6b6b;
  color: white;
  border: none;
  padding: 0 20px;
  border-radius: 0 5px 5px 0;
  cursor: pointer;
  font-size: 1.1rem;
}

.filter-options {
  display: flex;
  gap: 15px;
}

.filter-select {
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: white;
  font-size: 1rem;
  cursor: pointer;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
  margin-bottom: 40px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin: 40px 0;
}

.pagination-btn {
  padding: 10px 20px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.pagination-btn:hover:not(:disabled) {
  background-color: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-info {
  font-size: 1rem;
  color: #666;
}

@media (max-width: 768px) {
  .products-controls {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-filter {
    max-width: 100%;
  }
  
  .filter-options {
    width: 100%;
    justify-content: space-between;
  }
  
  .filter-select {
    flex: 1;
  }
}
</style>