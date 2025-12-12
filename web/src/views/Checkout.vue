<template>
  <div class="checkout-page">
    <div class="container">
      <h1 class="page-title">结算</h1>
      
      <div class="checkout-content">
        <div class="checkout-steps">
          <div class="step active">
            <div class="step-number">1</div>
            <div class="step-text">收货地址</div>
          </div>
          <div class="step" :class="{ active: currentStep >= 2 }">
            <div class="step-number">2</div>
            <div class="step-text">支付方式</div>
          </div>
          <div class="step" :class="{ active: currentStep >= 3 }">
            <div class="step-number">3</div>
            <div class="step-text">确认订单</div>
          </div>
        </div>
        
        <!-- 步骤1: 收货地址 -->
        <div class="checkout-section" v-show="currentStep === 1">
          <h2 class="section-title">收货地址</h2>
          
          <div class="addresses">
            <div 
              class="address-card" 
              v-for="address in addresses" 
              :key="address.id"
              :class="{ selected: selectedAddressId === address.id }"
              @click="selectAddress(address.id)"
            >
              <div class="address-header">
                <h3 class="address-name">{{ address.name }}</h3>
                <span class="address-phone">{{ address.phone }}</span>
                <span class="default-tag" v-if="address.isDefault">默认</span>
              </div>
              <p class="address-detail">{{ address.detail }}</p>
              <div class="address-actions">
                <button class="btn-edit" @click.stop="editAddress(address)">编辑</button>
                <button class="btn-delete" @click.stop="deleteAddress(address.id)">删除</button>
              </div>
            </div>
            
            <div class="address-card add-new" @click="showAddAddress = true">
              <div class="add-address-content">
                <i class="fas fa-plus"></i>
                <p>添加新地址</p>
              </div>
            </div>
          </div>
          
          <div class="form-actions">
            <button class="btn btn-outline" @click="$router.push('/cart')">
              <i class="fas fa-arrow-left"></i> 返回购物车
            </button>
            <button class="btn btn-primary" @click="goToStep(2)">
              下一步：支付方式 <i class="fas fa-arrow-right"></i>
            </button>
          </div>
        </div>
        
        <!-- 步骤2: 支付方式 -->
        <div class="checkout-section" v-show="currentStep === 2">
          <h2 class="section-title">选择支付方式</h2>
          
          <div class="payment-methods">
            <div 
              class="payment-method" 
              v-for="method in paymentMethods" 
              :key="method.id"
              :class="{ selected: selectedPaymentMethod === method.id }"
              @click="selectPaymentMethod(method.id)"
            >
              <div class="method-icon" :style="{ backgroundColor: method.color }">
                <i :class="method.icon"></i>
              </div>
              <div class="method-info">
                <h3 class="method-name">{{ method.name }}</h3>
                <p class="method-description">{{ method.description }}</p>
              </div>
              <div class="method-check">
                <i class="fas fa-check-circle" v-if="selectedPaymentMethod === method.id"></i>
              </div>
            </div>
          </div>
          
          <div class="form-actions">
            <button class="btn btn-outline" @click="goToStep(1)">
              <i class="fas fa-arrow-left"></i> 上一步
            </button>
            <button class="btn btn-primary" @click="goToStep(3)">
              下一步：确认订单 <i class="fas fa-arrow-right"></i>
            </button>
          </div>
        </div>
        
        <!-- 步骤3: 确认订单 -->
        <div class="checkout-section" v-show="currentStep === 3">
          <h2 class="section-title">确认订单</h2>
          
          <div class="order-summary">
            <div class="order-section">
              <h3>收货信息</h3>
              <div class="order-info">
                <p><strong>收货人:</strong> {{ selectedAddress.name }}</p>
                <p><strong>电话:</strong> {{ selectedAddress.phone }}</p>
                <p><strong>地址:</strong> {{ selectedAddress.detail }}</p>
              </div>
            </div>
            
            <div class="order-section">
              <h3>支付方式</h3>
              <div class="order-info">
                <p><strong>支付方式:</strong> {{ selectedPayment.name }}</p>
              </div>
            </div>
            
            <div class="order-section">
              <h3>订单商品</h3>
              <div class="order-items">
                <div class="order-item" v-for="item in orderItems" :key="item.id">
                  <div class="item-info">
                    <div class="item-image">
                      <img :src="item.image" :alt="item.name" @error="setDefaultImage">
                    </div>
                    <div class="item-details">
                      <h4 class="item-name">{{ item.name }}</h4>
                      <p class="item-description">{{ item.description }}</p>
                    </div>
                  </div>
                  <div class="item-quantity">×{{ item.quantity }}</div>
                  <div class="item-price">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
                </div>
              </div>
            </div>
            
            <div class="order-totals">
              <div class="total-row">
                <span>商品总价</span>
                <span>¥{{ orderSummary.subtotal.toFixed(2) }}</span>
              </div>
              <div class="total-row">
                <span>运费</span>
                <span v-if="orderSummary.shippingFee > 0">¥{{ orderSummary.shippingFee.toFixed(2) }}</span>
                <span v-else class="free-shipping">免运费</span>
              </div>
              <div class="total-row" v-if="orderSummary.discount > 0">
                <span>优惠</span>
                <span class="discount">-¥{{ orderSummary.discount.toFixed(2) }}</span>
              </div>
              <div class="total-row grand-total">
                <span>应付总额</span>
                <span class="grand-total-amount">¥{{ orderSummary.total.toFixed(2) }}</span>
              </div>
            </div>
          </div>
          
          <div class="form-actions">
            <button class="btn btn-outline" @click="goToStep(2)">
              <i class="fas fa-arrow-left"></i> 上一步
            </button>
            <button class="btn btn-primary" @click="placeOrder">
              <i class="fas fa-check-circle"></i> 提交订单
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加地址对话框 -->
    <el-dialog 
      title="添加收货地址" 
      :visible.sync="showAddAddress" 
      width="500px"
      :before-close="handleCloseDialog"
    >
      <div class="address-form">
        <div class="form-group">
          <label>收货人姓名</label>
          <input type="text" v-model="newAddress.name" placeholder="请输入收货人姓名">
        </div>
        <div class="form-group">
          <label>手机号码</label>
          <input type="text" v-model="newAddress.phone" placeholder="请输入手机号码">
        </div>
        <div class="form-group">
          <label>详细地址</label>
          <textarea v-model="newAddress.detail" placeholder="请输入详细地址" rows="3"></textarea>
        </div>
        <div class="form-group">
          <label>
            <input type="checkbox" v-model="newAddress.isDefault">
            设为默认地址
          </label>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <button class="btn btn-outline" @click="showAddAddress = false">取消</button>
        <button class="btn btn-primary" @click="saveAddress">保存地址</button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Checkout',
  data() {
    return {
      currentStep: 1,
      selectedAddressId: 1,
      selectedPaymentMethod: 1,
      showAddAddress: false,
      newAddress: {
        name: '',
        phone: '',
        detail: '',
        isDefault: false
      },
      addresses: [
        {
          id: 1,
          name: '张三',
          phone: '13800138000',
          detail: '北京市朝阳区建国路88号SOHO现代城A座1806室',
          isDefault: true
        },
        {
          id: 2,
          name: '李四',
          phone: '13900139000',
          detail: '上海市浦东新区陆家嘴环路1000号汇亚大厦',
          isDefault: false
        }
      ],
      paymentMethods: [
        {
          id: 1,
          name: '支付宝',
          icon: 'fab fa-alipay',
          description: '推荐使用支付宝扫码支付',
          color: '#1677FF'
        },
        {
          id: 2,
          name: '微信支付',
          icon: 'fab fa-weixin',
          description: '使用微信扫描二维码支付',
          color: '#07C160'
        },
        {
          id: 3,
          name: '银行卡支付',
          icon: 'fas fa-credit-card',
          description: '支持储蓄卡、信用卡支付',
          color: '#FF6B6B'
        }
      ],
      orderItems: [
        {
          id: 1,
          name: '香脆薯片组合',
          description: '三种口味混合装，脆爽可口',
          price: 25.99,
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
        }
      ],
      orderSummary: {
        subtotal: 70.48,
        shippingFee: 0,
        discount: 10,
        total: 60.48
      }
    }
  },
  computed: {
    selectedAddress() {
      return this.addresses.find(addr => addr.id === this.selectedAddressId) || this.addresses[0];
    },
    selectedPayment() {
      return this.paymentMethods.find(method => method.id === this.selectedPaymentMethod) || this.paymentMethods[0];
    }
  },
  methods: {
    goToStep(step) {
      if (step >= 1 && step <= 3) {
        this.currentStep = step;
        window.scrollTo(0, 0);
      }
    },
    selectAddress(addressId) {
      this.selectedAddressId = addressId;
    },
    selectPaymentMethod(methodId) {
      this.selectedPaymentMethod = methodId;
    },
    editAddress(address) {
      this.newAddress = { ...address };
      this.showAddAddress = true;
    },
    deleteAddress(addressId) {
      if (this.addresses.length <= 1) {
        this.$message({
          message: '至少需要保留一个收货地址',
          type: 'warning',
          duration: 2000
        });
        return;
      }
      
      this.addresses = this.addresses.filter(addr => addr.id !== addressId);
      
      if (this.selectedAddressId === addressId) {
        this.selectedAddressId = this.addresses[0].id;
      }
      
      this.$message({
        message: '地址已删除',
        type: 'success',
        duration: 1500
      });
    },
    saveAddress() {
      if (!this.newAddress.name || !this.newAddress.phone || !this.newAddress.detail) {
        this.$message({
          message: '请填写完整的地址信息',
          type: 'warning',
          duration: 2000
        });
        return;
      }
      
      if (this.newAddress.isDefault) {
        // 取消其他默认地址
        this.addresses.forEach(addr => {
          addr.isDefault = false;
        });
      }
      
      // 如果是新地址
      if (!this.newAddress.id) {
        const newId = Math.max(...this.addresses.map(a => a.id)) + 1;
        this.newAddress.id = newId;
        this.addresses.push({ ...this.newAddress });
        
        this.$message({
          message: '地址添加成功',
          type: 'success',
          duration: 1500
        });
      } else {
        // 更新现有地址
        const index = this.addresses.findIndex(addr => addr.id === this.newAddress.id);
        if (index !== -1) {
          this.addresses.splice(index, 1, { ...this.newAddress });
          
          this.$message({
            message: '地址更新成功',
            type: 'success',
            duration: 1500
          });
        }
      }
      
      this.showAddAddress = false;
      this.resetNewAddress();
    },
    resetNewAddress() {
      this.newAddress = {
        name: '',
        phone: '',
        detail: '',
        isDefault: false
      };
    },
    handleCloseDialog(done) {
      this.resetNewAddress();
      done();
    },
    placeOrder() {
      // 模拟提交订单
      this.$message({
        message: '订单提交成功！订单号：SNACK20231211001',
        type: 'success',
        duration: 3000
      });
      
      // 3秒后跳转到首页
      setTimeout(() => {
        this.$router.push('/');
      }, 3000);
    },
    setDefaultImage(e) {
      e.target.src = 'https://via.placeholder.com/80x80/FFE4E1/FF6B6B?text=Snack';
    }
  }
}
</script>

<style scoped>
.checkout-steps {
  display: flex;
  justify-content: center;
  margin-bottom: 50px;
  position: relative;
}

.checkout-steps::before {
  content: '';
  position: absolute;
  top: 25px;
  left: 10%;
  right: 10%;
  height: 3px;
  background-color: #eee;
  z-index: 1;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 2;
  flex: 1;
  max-width: 200px;
}

.step-number {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #eee;
  color: #95a5a6;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.step.active .step-number {
  background-color: #ff6b6b;
  color: white;
}

.step-text {
  color: #95a5a6;
  font-weight: 500;
  transition: color 0.3s ease;
}

.step.active .step-text {
  color: #ff6b6b;
  font-weight: 600;
}

.checkout-section {
  background-color: white;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  margin-bottom: 30px;
}

.addresses {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.address-card {
  border: 2px solid #eee;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.address-card:hover {
  border-color: #3498db;
}

.address-card.selected {
  border-color: #ff6b6b;
  background-color: #fff8f8;
}

.address-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.address-name {
  font-size: 1.2rem;
  margin-right: 15px;
  color: #2c3e50;
}

.address-phone {
  color: #7f8c8d;
  margin-right: 15px;
}

.default-tag {
  background-color: #27ae60;
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 0.8rem;
  font-weight: 500;
}

.address-detail {
  color: #555;
  line-height: 1.6;
  margin-bottom: 15px;
}

.address-actions {
  display: flex;
  gap: 10px;
}

.btn-edit,
.btn-delete {
  background: none;
  border: none;
  color: #3498db;
  cursor: pointer;
  font-size: 0.9rem;
  padding: 0;
}

.btn-delete {
  color: #e74c3c;
}

.add-new {
  display: flex;
  align-items: center;
  justify-content: center;
  border-style: dashed;
  min-height: 150px;
}

.add-address-content {
  text-align: center;
  color: #3498db;
}

.add-address-content i {
  font-size: 2.5rem;
  margin-bottom: 10px;
}

.payment-methods {
  margin-bottom: 30px;
}

.payment-method {
  display: flex;
  align-items: center;
  border: 2px solid #eee;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-method:hover {
  border-color: #3498db;
}

.payment-method.selected {
  border-color: #ff6b6b;
  background-color: #fff8f8;
}

.method-icon {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  margin-right: 20px;
  flex-shrink: 0;
}

.method-info {
  flex: 1;
}

.method-name {
  font-size: 1.2rem;
  margin-bottom: 5px;
  color: #2c3e50;
}

.method-description {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.method-check {
  color: #ff6b6b;
  font-size: 1.5rem;
}

.order-summary {
  margin-bottom: 30px;
}

.order-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.order-section h3 {
  font-size: 1.3rem;
  margin-bottom: 15px;
  color: #2c3e50;
}

.order-info p {
  margin-bottom: 8px;
  color: #555;
}

.order-items {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  align-items: center;
  flex: 1;
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

.item-details {
  flex: 1;
}

.item-name {
  font-size: 1.1rem;
  margin-bottom: 5px;
  color: #2c3e50;
}

.item-description {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.item-quantity {
  width: 80px;
  text-align: center;
  font-weight: 500;
}

.item-price {
  width: 100px;
  text-align: right;
  font-weight: 600;
  color: #ff6b6b;
}

.order-totals {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.total-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  color: #555;
}

.grand-total {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 2px solid #ddd;
}

.grand-total-amount {
  color: #ff6b6b;
  font-size: 1.5rem;
}

.free-shipping {
  color: #27ae60;
  font-weight: 500;
}

.discount {
  color: #27ae60;
  font-weight: 500;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
}

.address-form .form-group {
  margin-bottom: 20px;
}

.address-form label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
}

.address-form input[type="text"],
.address-form textarea {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1rem;
  font-family: 'Poppins', sans-serif;
}

.address-form textarea {
  resize: vertical;
}

.address-form input[type="checkbox"] {
  margin-right: 8px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 768px) {
  .checkout-steps::before {
    left: 5%;
    right: 5%;
  }
  
  .step-text {
    font-size: 0.9rem;
  }
  
  .form-actions {
    flex-direction: column;
    gap: 15px;
  }
  
  .form-actions button {
    width: 100%;
  }
  
  .order-item {
    flex-wrap: wrap;
  }
  
  .item-quantity,
  .item-price {
    width: auto;
    margin-left: 95px;
    margin-top: 10px;
  }
  
  .item-price {
    margin-left: auto;
  }
}
</style>