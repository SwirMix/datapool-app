import * as VueRouter from 'vue-router';
import HomePage from '@/pages/HomePage.vue'
import TokenPage from '@/pages/TokenPage.vue'
import JournalPage from '@/pages/JournalPage.vue'
import DataSourcePage from '@/pages/DataSourcePage.vue'
import DataSourceDetails from '@/pages/DataSourceDetails.vue'
import CacheDetails from '@/pages/CacheDetails.vue'
import AdminPage from '@/pages/AdminPage.vue'
import PermissionDeniedPage from '@/pages/PermissionDeniedPage.vue'
import NotFoundPage from '@/pages/NotFoundPage.vue'
import GlobalAdminPage from '@/pages/GlobalAdminPage.vue'
import CacheConsumersPage from '@/pages/CacheConsumersPage.vue'

const router = VueRouter.createRouter({
  history: VueRouter.createWebHistory(),
  routes: [
    {
      path: '/admin',
      component: GlobalAdminPage,
      name: 'GlobalAdminPage'
    },
    {
      path: '/',
      component: HomePage,
      name: 'HomePage'
    },
    {
      path: '/tokens',
      component: TokenPage,
      name: 'TokenPage'
    },
    {
      path: '/log',
      component: JournalPage,
      name: 'JournalPage'
    },
    {
      path: '/datasources',
      component: DataSourcePage,
      name: 'DataSourcePage'
    },
     {
       path: '/administration',
       component: AdminPage,
       name: 'AdminPage'
     },
    {
      path: '/datasources/:id',
      component: DataSourceDetails,
      name: 'DataSourceDetails'
    },
    {
      path: '/cache/:id',
      component: CacheDetails,
      name: 'CacheDetails'
    },
    {
      path: '/cache/:id/:consumer',
      component: CacheConsumersPage,
      name: 'CacheConsumersPage'
    },
    {
      path: '/403',
      component: PermissionDeniedPage,
      name: 'PermissionDeniedPage'
    },
    {
      path: '/404',
      component: NotFoundPage,
      name: 'NotFoundPage'
    }
  ]
})

export default router
