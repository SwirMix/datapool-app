<template>
  <va-navbar
    style="height: 70px;"
    class="flex flex-col md12"
  >
    <template #left>
      <va-navbar-item class="logo">
        <va-avatar
            size="small"
            class="mr-4"
            :src='require("@/assets/amber.png")'
        >
        </va-avatar>
        AMBER PROJECT (KV)
      </va-navbar-item>
    </template>
    <template #right>
        <va-navbar-item>
          <va-badge
            :text="this.selected_project.id"
            color="warning"
            class="mr-6"
          />
        </va-navbar-item>
        <va-select
              v-model="selected_project"
              label="project selector"
              :options="get_projects_for_options_field()"
              text-by="text"
              style="width: 700px;"
        />
        <va-navbar-item class="ml-4">
          <ProjectActionMenu/>
        </va-navbar-item>
        <va-navbar-item class="mt-2 mr-2">
            <va-button
                preset="secondary"
                border-color="primary"
                size="small"
                class="ml-5 mb-2"
                icon="cached"
                text-color="primary"
                @click="this.set_current_project(this.get_current_project().id)"
            >
                refresh
            </va-button>
        </va-navbar-item>
        <va-navbar-item class="ml-4 mt-2 mr-4" v-if="check_admin_status" @click="go_to_admin_page()">
          <va-popover placement="left" message="Administration">
            <va-icon name="admin_panel_settings">
            </va-icon>
          </va-popover>
        </va-navbar-item>
    </template>
  </va-navbar>
</template>

<script>
import ProjectActionMenu from '@/components/ProjectActionMenu.vue'
import {mapGetters, mapMutations} from 'vuex'

export default {
  name: 'NavBar',
  components: {
    ProjectActionMenu
  },
  watch: {
     selected_project: {
        handler(){
            this.set_current_project(this.selected_project.id)
        }
     }
  },
  props: {
    msg: String
  },
  computed: {
    check_admin_status: function(){
        return this.get_global_admin()
    }
  },
  data(){
    return {
      value: {},
      selected_project: {},
    }
  },
  methods: {
    ...mapGetters(
        [
            'get_user_info',
            'get_projects_for_options_field',
            'get_current_project',
            'get_current_project_for_option_field',
            'get_global_admin'
        ]
    ),
    ...mapMutations(
        ['set_current_project']
    ),
    copyId(id){
        navigator.clipboard.writeText(id);
        this.$vaToast.init({ message: 'Copy projectId (Ctrl + V)', color: 'primary' })
    },
    go_to_admin_page(){
        this.$router.push('/admin')
    }
  },
  mounted(){
    this.selected_project = this.get_current_project_for_option_field()
  },
  created(){
    this.selected_project = this.get_current_project_for_option_field()
  }
}
</script>

<style scoped>
.logo {
  font-weight: 300;
  font-size: 1.3rem;
}
</style>
