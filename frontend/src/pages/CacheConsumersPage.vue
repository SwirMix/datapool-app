<template>
   <div class="row">
       <div class="flex flex-col md12 ma-2">
          <va-breadcrumbs>
             <va-breadcrumbs-item label="Cache" />
             <va-breadcrumbs-item :label="$route.params.id" />
             <va-breadcrumbs-item :label="$route.params.consumer" />
          </va-breadcrumbs>
       </div>
       <div class="flex flex-col md2 ma-2">
         <va-card outlined>
            <va-card-content>
                <h5>Consumer: TC3_SBERPRANK</h5>
                <va-spacer></va-spacer>
                <h6>Success rate: 95%</h6>
            </va-card-content>
            <va-card-content>
                <Pie :data="data" :options="options" />
            </va-card-content>
         </va-card>
       </div>
       <div class="flex flex-col xs12 ma-2">
        <va-card outlined>
            <va-card-content>
              <va-tabs v-model="tab" class="mt-3">
                <template #tabs>
                  <va-tab>
                    <va-icon
                      name="check"
                      class="mr-3"
                    />
                    Success
                  </va-tab>
                  <va-tab>
                    <va-icon
                      name="cancel"
                      class="mr-3"
                    />
                    Failed
                  </va-tab>
                </template>
              </va-tabs>
            </va-card-content>
        </va-card>
       </div>
       <div class="flex flex-col md12 ma-2">
        <CacheDataTable/>
       </div>
   </div>
</template>

<script>
import { Pie } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
ChartJS.register(ArcElement, Tooltip, Legend)
import CacheDataTable from '@/components/tables/CacheDataTable.vue'



export default {
  name: 'CacheConsumersPage',
  components: {
    Pie,
    CacheDataTable
  },
  props: {
    msg: String
  },
  data(){
    return {
        tab: 0,
        data: {
            labels: ['success', 'failed'],
            datasets: [
                {
                    backgroundColor: ['#41B883', '#E46651'],
                    data: [12332, 124]
                }
            ]
        },
        consumers: [
            {
                name: 'TC3_SBERPRANK',
                success: 1231,
                failed: 123,
                rate: 95
            }
        ],
        columns: [
          { key: "name", sortable: true},
          { key: "success", sortable: true},
          { key: "failed", sortable: true},
          { key: "rate", sortable: true},
          { key: "actions", sortable: false},
        ],
    }
  }
}
</script>