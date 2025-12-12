<template>
  <div id="app">
    <Header />
    <main class="main-content">
      <router-view />
    </main>
    <Footer />
  </div>
</template>

<script>
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

export default {
  name: 'App',
  components: {
    Header,
    Footer
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Poppins', sans-serif;
  background-color: #f8f9fa;
  color: #333;
  line-height: 1.6;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding-top: 20px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
}

.page-title {
  font-size: 2.2rem;
  margin-bottom: 30px;
  color: #2c3e50;
  text-align: center;
  font-weight: 600;
}

.section-title {
  font-size: 1.8rem;
  margin: 30px 0 20px;
  color: #2c3e50;
  border-left: 5px solid #ff6b6b;
  padding-left: 15px;
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  text-decoration: none;
}

.btn-primary {
  background-color: #ff6b6b;
  color: white;
}

.btn-primary:hover {
  background-color: #ff5252;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.btn-secondary {
  background-color: #3498db;
  color: white;
}

.btn-secondary:hover {
  background-color: #2980b9;
}

.btn-outline {
  background-color: transparent;
  border: 2px solid #ff6b6b;
  color: #ff6b6b;
}

.btn-outline:hover {
  background-color: #ff6b6b;
  color: white;
}
</style>