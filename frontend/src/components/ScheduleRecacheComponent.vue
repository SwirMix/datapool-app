<template>
    <va-card outlined>
        <va-card-content>
        </va-card-content>
    </va-card>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'ScheduleRecacheComponent',
  components: {

  },
  props: {
    msg: String
  },
  data(){
      return {

      }
  },
  methods: {
    ...mapActions(
        ['update_tokens_in_cache']
    ),
    ...mapGetters(
        ['get_current_project', 'get_current_cache_info']
    )
  },
  created(){
  },
  mounted(){
  }
}
</script>