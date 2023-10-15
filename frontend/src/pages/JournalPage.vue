<template>
   <div class="row">
       <div class="flex flex-col md12 ma-3">
          <va-breadcrumbs>
             <va-breadcrumbs-item label="Journal" />
          </va-breadcrumbs>
       </div>
       <div class="flex flex-col md12 ma-3">
          <LogTable/>
       </div>
   </div>
</template>

<script>
import LogTable from '@/components/tables/LogTable.vue'
import { mapGetters } from 'vuex'

export default {
  name: 'JournalPage',
  components: {
    LogTable
  },
  props: {
    msg: String
  },
  data(){
  },
  created(){
      if (this.get_current_project().role == 'READER'){
          this.$router.push('/403')
      }
  },
  mounted(){
      if (this.get_current_project().role == 'READER'){
          this.$router.push('/403')
      }
  },
  methods: {
        ...mapGetters(
            ['get_current_datasource', 'check_current_datasource_exist', 'get_current_datasource_table', 'get_current_project']
        ),
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