<template>
  <va-icon name="download" class="ml-3" size="small" @click="showModal = !showModal">
  </va-icon>
  <va-modal
    v-model="showModal"
    ok-text="Apply"
    :hideDefaultActions="true"
    blur="true"
    noDismiss="true"
  >
       <va-card-title>
        <va-icon class="mr-4" name="rocket_launch"/> Import cache to csv file
       </va-card-title>
       <va-card-content style="width: 30rem;">
         <div class="row">
            <div class="flex flex-col xs12 mt-3">
                <va-list>
                    <va-list-item>
                        <va-list-item-section>
                            <va-input
                              v-model="this.request.targetFileName"
                              class="mb-6"
                              label="fileName"
                              placeholder="test.csv"
                            />
                        </va-list-item-section>
                    </va-list-item>
                    <va-list-item class="mt-3">
                        <va-list-item-section>
                            <va-input
                              v-model="this.request.startId"
                              class="mb-6"
                              label="startId"
                              placeholder="0"
                            />
                        </va-list-item-section>
                    </va-list-item>
                    <va-list-item class="mt-3">
                        <va-list-item-section>
                            <va-input
                              v-model="this.request.endId"
                              class="mb-6"
                              label="endId"
                              placeholder="1000000"
                            />
                        </va-list-item-section>
                    </va-list-item>
                </va-list>
            </div>
         </div>
       </va-card-content>
       <va-card-actions>
            <va-spacer></va-spacer>
            <va-button
              color="primary"
              @click="cacheToCsvRequest()"
            >
              Create
            </va-button>
            <va-button
              color="primary"
              @click="this.showModal = !this.showModal"
            >
              Cancel
            </va-button>
       </va-card-actions>
  </va-modal>
</template>

<script>
import { mapGetters, mapActions} from 'vuex'
import axios from 'axios';

export default {
  name: 'CacheToCsvDialog',
  components: {

  },
  props: {
    msg: String
  },
  data(){
    return {
        showModal: false,
        request: {
           cache: {
              cacheName: '',
              project: ''
           },
           startId: 0,
           endId: 1000,
           targetFileName: ''
        }
    }
  },
  created(){
     this.init()
  },
  mounted(){
     this.init()
  },
  methods: {
    ...mapGetters(
        ['get_current_project', 'get_current_cache_info']
    ),
    ...mapActions(
        ['get_cache_info']
    ),
    init(){
       let path = this.$route.params.id.split('_')
       let body = {
            projectId: path[0],
            cacheName: path[1]
       }
      this.get_cache_info(body)
      this.request.targetFileName = path[1] + '.csv'
      this.request.endId = this.get_current_cache_info().metadata.cacheMetadata.size
    },
    cacheToCsvRequest(){
        let path = this.$route.params.id.split('_')
        this.request.cache.cacheName = path[1]
        this.request.cache.project = path[0]
        let config = {
            headers: {
                token: localStorage.datapool_token,
            }
        }
        axios.post(this.$store.state.backend + '/api/v1/storage/import', this.request, config).then(function(response){
            let responseBody = response.data

            if (responseBody.success === true){
                console.log(response.data)

            } else {
                status.success = responseBody.success
            }
        }).catch(function (error) {
            console.log(error)
        })
        this.showModal = false
    }
  }
}
</script>

<style scoped>

</style>
