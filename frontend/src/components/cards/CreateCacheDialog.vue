<template>
   <va-card-title>
    <va-icon name="rocket_launch"/> Create cache request
   </va-card-title>
      <va-card-content v-if = 'get_runtime_cache_status().success === true'>
        <div class="row">
          <div class="flex flex-col xs8">
            <va-input
              v-model="new_cache_name"
              :rules="[(new_cache_name) => new_cache_name.length > 0 || `pls input name`]"
              class="mb-6"
              label="cache name"
              style="width: 25rem;"
              placeholder="..."
            />
          </div>
          <div class="flex flex-col xs3 ml-3">
            <va-select
              style="width: 25rem;"
              v-model="new_cache_type"
              :options="['PERSISTENCE']"
              outline
            />
          </div>
          <div class="flex flex-col xs12 mt-3">
            <va-input
               v-model="columns"
               label="columns. Enter separated by commas"
               style="width: 40rem;"
               class="mb-6"
             />
          </div>
        </div>
      </va-card-content>
      <va-content v-else>
          <va-alert
            color="warning"
            icon="warning"
            class="mb-6"
          >
           {{get_runtime_cache_status().msg}}
          </va-alert>
      </va-content>
      <va-card-content>
          <va-alert
            color="warning"
            icon="warning"
            class="mb-6"
          >
           For this type of cache, only columns of type text are available.
          </va-alert>
      </va-card-content>
      <va-card-actions>
        <va-spacer></va-spacer>
        <va-button
          color="primary"
          @click="create_runtime_cache_request()"
        >
          Create
        </va-button>
      </va-card-actions>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'

export default {
  name: 'CreateCacheDialog',
  components: {

  },
  props: {
    msg: String
  },
  data(){
    return {
      success: true,
      message: '',
      new_cache_type: 'PERSISTENCE',
      new_cache_name: 'default',
      columns: 'default',

      body: {
        "cache": {
            "cacheName": "string",
            "project": "string"
        },
        "columns": {},
        "cacheType": "PERSISTENCE"
      }
    }
  },
  methods: {
    ...mapGetters(
        ['get_current_project', 'get_runtime_cache_status']
    ),
    ...mapMutations(
        ['set_create_cache_dialog']
    ),
    ...mapActions(
        ['create_runtime_cache']
    ),
    create_runtime_cache_request(){
        this.body.cache.project = this.get_current_project().id
        this.body.cache.cacheName = this.new_cache_name
        let final_cols = {}
        if (this.columns.includes(',')){
            let cols = this.columns.split(',')
            cols.forEach(function(item) {
               if (item !== ''){
                final_cols[item] = 'text'
               }
            });
        } else {
            final_cols[this.columns]='text'
        }
        this.body.columns = final_cols
        this.body.cacheType = this.new_cache_type

        this.create_runtime_cache(this.body)
    }
  }
}
</script>

<style scoped>

</style>
