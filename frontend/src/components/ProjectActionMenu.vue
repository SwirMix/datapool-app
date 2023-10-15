<template>
  <va-button-dropdown
    preset="secondary"
    class="mb-3 mt-2"
    label="actions"
    border-color="primary"
    icon="settings"
    size="small"
  >
      <va-sidebar-item v-if="check_project_admin">
        <router-link :to="{ name: 'AdminPage', params: {}}">
            <va-sidebar-item-content>
              <va-icon name="settings"/>
              <va-sidebar-item-title >
                Settings
              </va-sidebar-item-title>
            </va-sidebar-item-content>
        </router-link>
      </va-sidebar-item>
      <va-sidebar-item @click="set_create_cache_dialog(true)" v-if="this.get_current_project().role !=='READER'">
        <va-sidebar-item-content>
          <va-icon name="rocket_launch"/>
          <va-sidebar-item-title >
              Create runtime cache
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
      <va-sidebar-item @click="profile = !profile">
        <va-sidebar-item-content>
          <va-icon name="manage_accounts"/>
          <va-sidebar-item-title >
              Update account password
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
      <va-sidebar-item @click="uploadCSV = !uploadCSV" v-if="this.get_current_project().role !== 'READER'">
        <va-sidebar-item-content>
          <va-icon name="upload_file"/>
          <va-sidebar-item-title>
              Upload CSV
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
      <va-sidebar-item @click="aboutModal = !aboutModal">
        <va-sidebar-item-content>
          <va-icon name="info"/>
          <va-sidebar-item-title >
              About
          </va-sidebar-item-title>
        </va-sidebar-item-content>
      </va-sidebar-item>
  </va-button-dropdown>
  <va-modal
    v-model="aboutModal"
    style="background-color: #e3e3e3;"
  >
    <template #content="{ ok }" >
      <va-card-title>Information version 1.0.1</va-card-title>
      <va-card-content>
        <p>HttpDatapool - это инструмент предоставляющий данные по REST запросу в соответствии с заданными стратегиями</p>
        <va-divider/>
        <Footer/>
      </va-card-content>
      <va-card-actions>
        <va-button
          color="warning"
          @click="ok"
        >
          Ok!
        </va-button>
      </va-card-actions>
    </template>
  </va-modal>
  <va-modal
    v-model="this.$store.state.createCacheDialog"
    blur
  >
    <template #content >
        <CreateCacheDialog/>
    </template>
  </va-modal>
  <va-modal
    v-model="this.profile"
    blur
  >
    <template #content >
    <va-card-title>
        <va-icon name="manage_accounts" class="mr-3"></va-icon> Update password
    </va-card-title>
      <va-card-content>
        <div class="row">
          <div class="flex flex-col xs5 ma-2">
            <va-input
              v-model="change_password.newPass"
              class="mb-12"
              label="new password"
              placeholder=""
            />
          </div>
          <div class="flex flex-col xs5 ma-2">
            <va-input
              v-model="change_password.old"
              class="mb-12"
              label="old password"
              placeholder=""
            />
          </div>
        </div>
      </va-card-content>
      <va-card-actions>
        <va-spacer></va-spacer>
        <va-button
          color="primary"
          @click="update_password()"
        >
          Update
        </va-button>
      </va-card-actions>
    </template>
  </va-modal>
  <va-modal
    v-model="this.uploadCSV"
    blur
  >
    <template #content >
    <va-card-title>
        <va-icon name="upload_file" class="mr-3"></va-icon> Upload CSV to storage
    </va-card-title>
      <va-card-content>
        <div class="row">
               <div class="flex flex-col md12">
                      <va-file-upload
                        v-model="csvFile"
                        dropzone
                        file-types="csv"
                        dropZoneText="Upload CSV to Storage"
                        uploadButtonText="select file"
                      />
               </div>
        </div>
      </va-card-content>
      <va-card-actions>
        <va-spacer></va-spacer>
        <va-button
          color="primary"
          @click="upload_file()"
        >
          Upload files
        </va-button>
      </va-card-actions>
    </template>
  </va-modal>
</template>

<script>
import Footer from '@/components/Footer.vue'
import CreateCacheDialog from '@/components/cards/CreateCacheDialog.vue'
import {mapGetters, mapMutations} from 'vuex'
import axios from 'axios';

export default {
  name: 'ProjectActionMenu',
  components: {
    Footer,
    CreateCacheDialog
  },
  props: {
    msg: String
  },
  computed: {
    check_project_admin: function() {
        if (this.get_user_info().role === 'ADMIN'){
            return true
        } else {
            return false
        }
    }
  },
  data(){
    return {
        aboutModal: false,
        createCache: false,
        profile: false,
        uploadCSV: false,
        change_password: {
            old: '',
            newPass: ''
        },
        csvFile: [],
    }
  },
  methods: {
    ...mapMutations(
        ['set_create_cache_dialog']
    ),
    ...mapGetters(
        ['get_user_info', 'get_create_cache_dialog', 'get_current_project']
    ),
    upload_file(){
        this.csvFile.forEach((file) => {
            var formData = new FormData();
            formData.append("file", file);
            axios.post(
                this.$store.state.backend + '/api/v1/storage/' + this.get_current_project().id + '/upload',
                formData, {
                headers: {
                  'Content-Type': 'multipart/form-data',
                  'token': localStorage.datapool_token
                }
            })
        })
        this.uploadCSV = false;
    },
    update_password(){
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }

        let toast = this.$vaToast
        axios.post(this.$store.state.backend + '/api/v1/account/update/pass', this.change_password, config).then(function(response){
            let responseBody = response.data
            if (responseBody.success === true){
                console.log(response.data)
                toast.init({ message: 'success color', color: 'success' })
            } else {
                status.success = responseBody.success
                toast.init({ message: 'failed update pass', color: 'warning' })
            }
        }).catch(function (error) {
            console.log(error)
            toast.init({ message: 'Failed update pass. Check params.', color: 'warning' })
        })
    }
  }
}
</script>

<style scoped>

</style>
