<template>
  <div class="va-table-responsive mt-1 mr-2">
      <va-card v-if="this.get_current_cache_info().metadata.cacheMetadata.status === 'READY'">
        <va-card-title>
            <va-select
              v-model="strategy"
              :options="['RANDOM', 'SEQUENTIAL', 'KEY', 'STACK', 'HASH']"
            />
            <va-input
              v-model="key"
              class="ml-2"
              placeholder="key"
              mb-3
            />
            <va-spacer></va-spacer>
            <va-button
               color="primary"
               class="mr-2"
               size="small"
               @click="request()"
            >
               request
            </va-button>
        </va-card-title>
        <va-card-content>
            <JsonViewer :value="get_datapool_check_data()" copyable boxed sort theme="dark"  @onKeyClick="keyClick"/>
        </va-card-content>
      </va-card>
      <div class="row justify-space-around" style="padding-top: 2%;" v-else>
        <v-container fill-height>
          <v-layout row wrap align-center>
              <div class="flex flex-col xs" style="height: 100%;">
                <div class="item row">
                  <div class="flex flex-col xs12">
                    <va-avatar
                        size="10rem"
                        class="mr-4"
                        :src='require("@/assets/mock.png")'
                    >
                    </va-avatar>
                  </div>
                  <div class="flex flex-col xs12">
                    <h1>Wait for READY status</h1>
                  </div>
                </div>
              </div>
          </v-layout>
        </v-container>
      </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import {JsonViewer} from "vue3-json-viewer"
import "vue3-json-viewer/dist/index.css";

export default {
  name: 'CacheDataTable',
  components: {
    JsonViewer
  },
  props: {
    msg: String
  },
  computed: {
  },
  data(){
    return {
        strategy: 'RANDOM',
        key: 0,
        columns: [],
        page: 1,
        data: [],
        processed: false,
        result: {
            "key": 65649,
            "hash": -619175589,
            "data": {
              "actual_duration": {
                "type": "interval",
                "value": "0 years 0 mons 0 days 0 hours 45 mins 0.0 secs",
                "years": 0,
                "months": 0,
                "days": 0,
                "hours": 0,
                "minutes": 45,
                "wholeSeconds": 0,
                "microSeconds": 0,
                "seconds": 0,
                "null": true
              },
              "departure_airport_name": "Саранск",
              "arrival_airport": "SVO",
              "arrival_airport_name": "Шереметьево",
              "departure_city": "Саранск",
               }
        }
    }
  },
  methods: {
    ...mapGetters([
        'get_current_cache_info',
        'get_datapool_check_data'
    ]),
    ...mapActions([
        'check_datapool_data'
    ]),
    request(){
        let body = {
            project: this.get_current_cache_info().metadata.cacheMetadata.cache.project,
            cacheName: this.get_current_cache_info().metadata.cacheMetadata.cache.cacheName,
            key: this.key,
            strategy: this.strategy
        }
        this.check_datapool_data(body)
    }
  },
  mounted() {

  },
  created() {

  }
}
</script>

<style scoped>

</style>
