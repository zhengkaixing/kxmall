<template>
  <div class="navbar">
    <div class="navbar-left">
      <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
      <breadcrumb id="breadcrumb-container" v-if="!topNav" class="breadcrumb-container" />
      <top-nav id="topmenu-container" v-if="topNav" class="topmenu-container" />
    </div>

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <search id="header-search" class="right-menu-item" />

        <el-tooltip content="全屏" effect="dark" placement="bottom">
          <screenfull id="screenfull" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>
      </template>

      <theme-picker class="right-menu-item theme-picker-item" />

      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar" alt="avatar">
          <span v-if="name" class="user-name">{{ name }}</span>
          <el-icon class="caret-icon"><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/user/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <el-dropdown-item @click="setting = true">
              <span>布局设置</span>
            </el-dropdown-item>
            <el-dropdown-item divided @click="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import ThemePicker from '@/components/ThemePicker'

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    ThemePicker
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device',
      'name'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = import.meta.env.VITE_APP_CONTEXT_PATH + 'index'
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding-right: 16px;

  .navbar-left {
    display: flex;
    align-items: center;
    min-width: 0;
    flex: 1;
  }

  .hamburger-container {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    padding: 0 15px;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container,
  .topmenu-container {
    min-width: 0;
  }

  .right-menu {
    display: flex;
    align-items: center;
    height: 100%;
    flex-shrink: 0;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 10px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .theme-picker-item {
      padding: 0 12px;

      :deep(.el-color-picker__trigger) {
        width: 22px;
        height: 22px;
        padding: 2px;
        border-radius: 4px;
      }
    }

    .avatar-container {
      margin-left: 4px;

      .avatar-wrapper {
        display: flex;
        align-items: center;
        height: 50px;
        padding: 0 8px;
        cursor: pointer;
        outline: none;

        .user-avatar {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          object-fit: cover;
        }

        .user-name {
          max-width: 96px;
          margin-left: 8px;
          color: #303133;
          font-size: 14px;
          line-height: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .caret-icon {
          margin-left: 4px;
          font-size: 12px;
          color: #909399;
        }

        &:hover {
          .user-name,
          .caret-icon {
            color: var(--el-color-primary);
          }
        }
      }
    }
  }
}
</style>
