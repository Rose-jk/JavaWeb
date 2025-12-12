<template>
  <div class="orders-page">
    <div class="container">
      <h1 class="page-title">我的订单</h1>
      
      <div class="orders-filter">
        <div class="filter-tabs">
          <button 
            v-for="tab in tabs" 
            :key="tab.value"
            class="filter-tab"
            :class="{ active: activeTab === tab.value }"
            @click="activeTab = tab.value"
          >
            {{ tab.label }}
            <span class="tab-count">{{ tab.count }}</span>
          </button>
        </div>
        
        <div class="search-orders">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索订单号或商品名称"
            class="search-input"
          >
          <button class="search-btn">
            <i class="fas fa-search"></i>
          </button>
        </div>
      </div>
      
      <div class="orders-list" v-if="filteredOrders.length > 0">
        <div class="order-card" v-for="order in filteredOrders" :key="order.id">
          <div class="order-header">
            <div class="order-info">
              <h3 class="order-number">订单号: {{ order.orderNumber }}</h3>
              <span class="order-date">{{ order.date }}</span>
              <span class="order-status" :class="order.status">
                {{ getStatusText(order.status) }}
              </span>
            </div>
            <div class="order-amount">
              实付: <span class="amount">¥{{ order.totalAmount.toFixed(2) }}</span>
            </div>
          </div>
          
          <div class="order-items">
            <div class="order-item" v-for="item in order.items" :key="item.id">
              <div class="item-image">
                <img :src="item.image" :alt="item.name" @error="setDefaultImage">
              </div>
              <div class="item-info">
                <h4 class="item-name">{{ item.name }}</h4>
                <p class="item-spec">{{ item.spec }}</p>
              </div>
              <div class="item-quantity">×{{ item.quantity }}</div>
              <div class="item-price">¥{{ item.price.toFixed(2) }}</div>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-actions">
              <button 
                class="btn btn-outline" 
                v-if="order.status === 'pending'"
                @click="cancelOrder(order.id)"
              >
                取消订单
              </button>
              <button 
                class="btn btn-outline" 
                v-if="order.status === 'pending'"
                @click="payOrder(order.id)"
              >
                立即支付
              </button>
              <button 
                class="btn btn-outline" 
                v-if="order.status === 'shipped'"
                @click="confirmReceipt(order.id)"
              >
                确认收货
              </button>
              <router-link 
                :to="`/order/${order.id}`" 
                class="btn btn-outline"
              >
                查看详情
              </router-link>
              <button 
                class="btn btn-outline" 
                v-if="order.status === 'completed' && !order.isReviewed"
                @click="writeReview(order.id)"
              >
                评价
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <div class="empty-orders" v-else>
        <div class="empty-icon">
          <i class="fas fa-clipboard-list"></i>
        </div>
        <h2 class="empty-title">暂无订单</h2>
        <p class="empty-text">快去挑选一些美味的零食吧</p>
        <router-link to="/products" class="btn btn-primary">
          去逛逛 <i class="fas fa-arrow-right"></i>
        </router-link>
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
export default {
  name: 'Orders',
  data() {
    return {
      activeTab: 'all',
      searchQuery: '',
      currentPage: 1,
      itemsPerPage: 5,
      tabs: [
        { label: '全部', value: 'all', count: 8 },
        { label: '待付款', value: 'pending', count: 2 },
        { label: '待发货', value: 'processing', count: 1 },
        { label: '待收货', value: 'shipped', count: 1 },
        { label: '已完成', value: 'completed', count: 4 },
        { label: '已取消', value: 'cancelled', count: 0 }
      ],
      orders: [
        {
          id: 1,
          orderNumber: 'SNACK20231211001',
          date: '2023-12-11 14:30:22',
          status: 'completed',
          totalAmount: 70.48,
          isReviewed: true,
          items: [
            {
              id: 1,
              name: '香脆薯片组合',
              spec: '三种口味混合装',
              price: 25.99,
              quantity: 2,
              image: 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
            },
            {
              id: 2,
              name: '巧克力夹心饼干',
              spec: '200g装',
              price: 18.50,
              quantity: 1,
              image: 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
            }
          ]
        },
        {
          id: 2,
          orderNumber: 'SNACK20231210002',
          date: '2023-12-10 09:15:45',
          status: 'shipped',
          totalAmount: 45.99,
          isReviewed: false,
          items: [
            {
              id: 3,
              name: '混合坚果礼盒',
              spec: '500g礼盒装',
              price: 45.99,
              quantity: 1,
              image: 'https://images.unsplash.com/photo-1532939624-3af1308db9a5?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
            }
          ]
        },
        {
          id: 3,
          orderNumber: 'SNACK20231209003',
          date: '2023-12-09 16:20:33',
          status: 'pending',
          totalAmount: 102.48,
          isReviewed: false,
          items: [
            {
              id: 4,
              name: '果味软糖大礼包',
              spec: '混合口味',
              price: 22.80,
              quantity: 1,
              image: 'https://images.unsplash.com/photo-1563805042-7684c019e1cb?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
            },
            {
              id: 5,
              name: '香辣牛肉干',
              spec: '150g袋装',
              price: 38.50,
              quantity: 2,
              image: 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
            },
            {
              id: 6,
              name: '日本进口抹茶饼干',
              spec: '180g盒装',
              price: 32.99,
              quantity: 1,
              image: 'https://images.unsplash.com/photo-1558322456-bb57647bf5f0?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
            }
          ]
        },
        {
          id: 4,
          orderNumber: 'SNACK20231208004',
          date: '2023-12-08 11:05:12',
          status: 'processing',
          totalAmount: 28.80,
          isReviewed: false,
          items: [
            {
              id: 10,
              name: '芒果干',
              spec: '菲律宾进口',
              price: 28.80,
              quantity: 1,
              image: 'https://images.unsplash.com/photo-1528502668757-8c45371d89c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
            }
          ]
        }
      ]
    }
  },
  computed: {
    filteredOrders() {
      let filtered = this.orders
      
      // 根据标签过滤
      if (this.activeTab !== 'all') {
        filtered = filtered.filter(order => order.status === this.activeTab)
      }
      
      // 根据搜索词过滤
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase()
        filtered = filtered.filter(order => 
          order.orderNumber.toLowerCase().includes(query) ||
          order.items.some(item => item.name.toLowerCase().includes(query))
        )
      }
      
      // 分页
      const startIndex = (this.currentPage - 1) * this.itemsPerPage
      return filtered.slice(startIndex, startIndex + this.itemsPerPage)
    },
    totalPages() {
      let filtered = this.orders
      
      if (this.activeTab !== 'all') {
        filtered = filtered.filter(order => order.status === this.activeTab)
      }
      
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase()
        filtered = filtered.filter(order => 
          order.orderNumber.toLowerCase().includes(query) ||
          order.items.some(item => item.name.toLowerCase().includes(query))
        )
      }
      
      return Math.ceil(filtered.length / this.itemsPerPage)
    }
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        pending: '待付款',
        processing: '待发货',
        shipped: '待收货',
        completed: '已完成',
        cancelled: '已取消'
      }
      return statusMap[status] || '未知状态'
    },
    cancelOrder(orderId) {
      this.$confirm('确定要取消这个订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const order = this.orders.find(o => o.id === orderId)
        if (order) {
          order.status = 'cancelled'
          
          // 更新标签计数
          const allTab = this.tabs.find(tab => tab.value === 'all')
          const cancelledTab = this.tabs.find(tab => tab.value === 'cancelled')
          const currentTab = this.tabs.find(tab => tab.value === order.status)
          
          if (cancelledTab) cancelledTab.count++
          if (currentTab) currentTab.count--
          
          this.$message({
            message: '订单已取消',
            type: 'success',
            duration: 2000
          })
        }
      }).catch(() => {
        // 用户点击了取消
      })
    },
    payOrder(orderId) {
      this.$message({
        message: '跳转到支付页面...',
        type: 'info',
        duration: 1500
      })
      
      setTimeout(() => {
        const order = this.orders.find(o => o.id === orderId)
        if (order) {
          order.status = 'processing'
          
          // 更新标签计数
          const pendingTab = this.tabs.find(tab => tab.value === 'pending')
          const processingTab = this.tabs.find(tab => tab.value === 'processing')
          
          if (pendingTab) pendingTab.count--
          if (processingTab) processingTab.count++
          
          this.$message({
            message: '支付成功！订单状态已更新',
            type: 'success',
            duration: 2000
          })
        }
      }, 1500)
    },
    confirmReceipt(orderId) {
      this.$confirm('确定已经收到商品了吗？', '确认收货', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const order = this.orders.find(o => o.id === orderId)
        if (order) {
          order.status = 'completed'
          
          // 更新标签计数
          const shippedTab = this.tabs.find(tab => tab.value === 'shipped')
          const completedTab = this.tabs.find(tab => tab.value === 'completed')
          
          if (shippedTab) shippedTab.count--
          if (completedTab) completedTab.count++
          
          this.$message({
            message: '收货确认成功！',
            type: 'success',
            duration: 2000
          })
        }
      }).catch(() => {
        // 用户点击了取消
      })
    },
    writeReview(orderId) {
      this.$message({
        message: '跳转到评价页面...',
        type: 'info',
        duration: 1500
      })
    },
    changePage(page) {
      this.currentPage = page
      window.scrollTo(0, 0)
    },
    setDefaultImage(e) {
      e.target.src = 'https://via.placeholder.com/80x80/FFE4E1/FF6B6B?text=Snack'
    }
  },
  watch: {
    activeTab() {
      this.currentPage = 1
    },
    searchQuery() {
      this.currentPage = 1
    }
  }
}
</script>

<style scoped>
.orders-filter {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 20px;
}

.filter-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-tab {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 20px;
  cursor: pointer;
  font-size: 0.95rem;
  color: #555;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 5px;
}

.filter-tab:hover {
  border-color: #ff6b6b;
  color: #ff6b6b;
}

.filter-tab.active {
  background-color: #ff6b6b;
  border-color: #ff6b6b;
  color: white;
}

.tab-count {
  background-color: rgba(255, 255, 255, 0.2);
  padding: 1px 6px;
  border-radius: 10px;
  font-size: 0.8rem;
}

.search-orders {
  display: flex;
  max-width: 300px;
}

.search-input {
  flex: 1;
  padding: 8px 15px;
  border: 1px solid #ddd;
  border-radius: 5px 0 0 5px;
  font-size: 0.95rem;
}

.search-btn {
  background-color: #ff6b6b;
  color: white;
  border: none;
  padding: 0 15px;
  border-radius: 0 5px 5px 0;
  cursor: pointer;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 40px;
}

.order-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
}

.order-info {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.order-number {
  font-size: 1.1rem;
  color: #2c3e50;
  margin: 0;
}

.order-date {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.order-status {
  padding: 3px 10px;
  border-radius: 12px;
  font-size: 0.85rem;
  font-weight: 500;
}

.order-status.pending {
  background-color: #fff3cd;
  color: #856404;
}

.order-status.processing {
  background-color: #d1ecf1;
  color: #0c5460;
}

.order-status.shipped {
  background-color: #d4edda;
  color: #155724;
}

.order-status.completed {
  background-color: #d4edda;
  color: #155724;
}

.order-status.cancelled {
  background-color: #f8d7da;
  color: #721c24;
}

.order-amount {
  font-size: 1.1rem;
  color: #2c3e50;
}

.order-amount .amount {
  color: #ff6b6b;
  font-weight: 600;
  font-size: 1.2rem;
}

.order-items {
  padding: 20px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px dashed #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 1rem;
  margin-bottom: 5px;
  color: #2c3e50;
}

.item-spec {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.item-quantity {
  width: 80px;
  text-align: center;
  color: #555;
}

.item-price {
  width: 100px;
  text-align: right;
  font-weight: 500;
  color: #ff6b6b;
}

.order-footer {
  padding: 20px;
  background-color: #f8f9fa;
  border-top: 1px solid #eee;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.empty-orders {
  text-align: center;
  padding: 80px 0;
}

.empty-icon {
  font-size: 5rem;
  color: #ddd;
  margin-bottom: 20px;
}

.empty-title {
  font-size: 2rem;
  margin-bottom: 15px;
  color: #7f8c8d;
}

.empty-text {
  font-size: 1.1rem;
  color: #95a5a6;
  margin-bottom: 30px;
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
  .orders-filter {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-orders {
    max-width: 100%;
  }
  
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .order-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .order-actions {
    flex-wrap: wrap;
    justify-content: flex-start;
  }
  
  .order-item {
    flex-wrap: wrap;
  }
  
  .item-quantity,
  .item-price {
    margin-left: 95px;
    margin-top: 10px;
  }
  
  .item-price {
    margin-left: auto;
  }
}
</style>