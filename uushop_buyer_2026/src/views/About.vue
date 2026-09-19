<template>
  <div class="about-wrapper">
    <div class="about">
    <h1>This is an about page</h1>
    </div>
  </div>
</template>

<style scoped>
/* ===== 桌面端样式（无媒体查询） ===== */
.about-wrapper {
  min-height: 100vh;
  background-color: #f2f4f8;
  display: flex;
  align-items: center;
  justify-content: center;
  padding-bottom: 120px;
}
.about-wrapper .about {
  background: #ffffff;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}
.about-wrapper .about:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}
</style>
