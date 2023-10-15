<template>
   <div class="row">
       <div class="flex flex-col md12 ma-3">
          <va-breadcrumbs>
             <va-breadcrumbs-item label="Datasource" />
          </va-breadcrumbs>
       </div>
       <div class="flex flex-col md12 ma-3">
          <DataSourceTable/>
       </div>
   </div>
</template>

<script>
import DataSourceTable from '@/components/tables/DataSourceTable.vue'

export default {
  name: 'DataSourcePage',
  components: {
    DataSourceTable
  },
  props: {
    msg: String
  },
  data(){
  }
}
</script>

<style lang="scss" scoped>
@import "vuestic-ui/styles/grid";

.row {
  min-height: 6rem;

  & + .row {
    border-top: 1px solid var(--va-background-border);
  }
}

.item {
  border: 1px solid var(--va-background-border);
  background-color: var(--va-background-primary);
  text-align: center;
}
</style>