<template>
  <va-button icon="edit" size="small" color="primary" round @click="showModal = !showModal">
     edit schedule
  </va-button>
  <va-modal
    v-model="showModal"
    ok-text="Apply"
    :hideDefaultActions="true"
    blur="true"
    noDismiss="true"
  >
       <va-card-title>
        <va-icon class="mr-4" name="schedule"/> Edit recache time
       </va-card-title>
       <va-card-content style="width: 30rem; height: 30rem;">
         <div class="row">
            <div class="flex flex-col xs12 mt-3">
                <va-list>
                    <va-list-item>
                        <va-list-item-section>
                           <VueDatePicker v-model="next_recache" value-type="timestamp" />
                        </va-list-item-section>
                    </va-list-item>
                    <va-list-item class="mt-3">
                        <va-list-item-section>
                            <va-input
                              class="mb-6"
                              label="recache interval"
                              placeholder="168h"
                              v-model="interval"
                            />
                        </va-list-item-section>
                    </va-list-item>
                </va-list>
                <va-divider></va-divider>
                <va-switch
                    v-model="active"
                    true-label="Automatic recaching active"
                    false-label="Automatic recaching is inactive"
                />
                <va-alert
                    color="warning"
                    icon="warning"
                    class="mb-6"
                >
                    <p>
                    The first field sets the next base recache start time.
                    </p>
                    <p>
                      <b class="mr-2">Recache interval</b> the iteration interval between automatic updates is set in hours. For example 168h.
                    </p>
                </va-alert>
            </div>
         </div>
       </va-card-content>
       <va-card-actions>
            <va-spacer></va-spacer>
            <va-button
              color="primary"
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
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

export default {
  name: 'CreateNewCronDialog',
  components: {
    VueDatePicker
  },
  props: {
    msg: String
  },
  data(){
    return {
        showModal: false,
        next_recache: new Date(),
        interval: '300h',
        active: false
    }
  },
  created(){
  },
  mounted(){
  },
  methods: {
    ...mapGetters(
        ['get_current_project', 'get_current_cache_info']
    ),
    ...mapActions(
        ['get_cache_info']
    )
  }
}
</script>

<style scoped>

</style>
