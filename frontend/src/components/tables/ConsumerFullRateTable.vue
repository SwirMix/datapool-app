<template>
<div class="row">
 <div class="flex flex-col xs12">
    <va-card outlined>
        <va-card-content>
         <div class="row">
          <div class="flex flex-col xs4">
           <VueDatePicker v-model="query.from.time" value-type="timestamp"/>
          </div>
          <div class="flex flex-col xs4 ml-1" style="text-align: start;">
           <VueDatePicker v-model="query.to.time" value-type="timestamp"/>
          </div>
          <div class="flex flex-col xs3 ml-3" style="text-align: start;">
            <va-button
              icon="query_stats"
              border-color="primary"
              @click="get_consumers()"
            >
             exec UTC+00
            </va-button>
          </div>
         </div>
        </va-card-content>
    </va-card>
 </div>
 <div class="flex flex-col xs2">
  <div class="row" style="height: 100%;">
   <div class="flex flex-col xs12">
    <va-card outlined style="height: 100%;">
        <va-card-title>
            <h6>percentage contribution</h6>
        </va-card-title>
        <va-card-content>
            <Pie :data="get_consumers_chart()" :options="options"/>
        </va-card-content>
    </va-card>
   </div>
  </div>
 </div>
 <div class="flex flex-col xs10">
  <div class="va-table-responsive mt-1">
      <va-card>
        <va-card-title>
            <h6>Consumers</h6>
        </va-card-title>
        <va-card-content>
            <va-data-table
                    :items="this.get_consumers_list()"
                    :columns="columns"
                    :wrapper-size="600"
                    :item-size="46"
                    virtual-scroller
                    sticky-header
            >
                <template #cell(percentage) = "{ rowData }">
                   {{rowData.percentage}} %
                </template>
                <template #cell(successrate) = "{ rowData }">
                   {{((rowData.count - rowData.errors)/rowData.count)*100}} %
                </template>
                <template #cell(actions) = "{ rowData }">
                    <div style="text-align: end;">
                      <router-link :to="{ name: 'CacheDetails', params: { id: get_current_project().id + '_' + rowData.cache }}">
                          <va-button
                            icon="info"
                            color="primary"
                            class="mr-2"
                            size="small"
                          >
                            cache
                          </va-button>
                      </router-link>
                    </div>
                </template>
           </va-data-table>
        </va-card-content>
      </va-card>
  </div>
 </div>
</div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import Moment from "moment";
import { Pie } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
ChartJS.register(ArcElement, Tooltip, Legend)
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

export default {
  name: 'ConsumerRateTable',
  components: {
    Pie,
    VueDatePicker
  },
  props: {
    msg: String
  },
  data(){
    return {
          myFormat: {
            stringify: (date) => {
              return date ? Moment(date).format('LL') : null
            },
            parse: (value) => {
              return value ? Moment(value, 'LL').toDate() : null
            }
          },
        query: {
            from: {
                time: new Date().getTime() - 3600*1000*12
            },
            to: {
                time: new Date().getTime()
            }
        },
        consumer_filter: '',
        columns: [
          { key: "cache", sortable: true },
          { key: "consumer", sortable: true },
          { key: "percentage", sortable: true },
          { key: "count", sortable: true },
          { key: "strategy", sortable: true},
          { key: "actions", sortable: false }
        ],
        options: {
           plugins: {
              legend: {
                 display: false
              }
           }
        }
    }
  },
  methods: {
    ...mapGetters(
        ['get_consumers_list', 'get_current_project', 'get_consumers_chart', 'get_consumers_success_rate']
    ),
    ...mapActions(
        ['request_consumers_list_all']
    ),
    get_consumers(){
        let body = {
            project: this.get_current_project().id,
            start: Moment(this.query.from.time).unix(),
            end: Moment(this.query.to.time).unix()
        }
        this.request_consumers_list_all(body)
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
