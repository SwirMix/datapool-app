<template>
    <va-card-title>
        Postgresql source
    </va-card-title>
    <va-card-content style="width: 40rem;">
        <div class="row">
           <va-input
              v-model="body.dataSourceName"
              class="mt-3 md12 flex flex-col"
              label="Source name"
           />
           <va-input
              v-model="body.properties.properties.url"
              class="mt-3 md12 flex flex-col"
              label="Source url"
           />
           <va-input
              v-model="body.properties.properties.schema"
              class="mt-3 md12 flex flex-col"
              label="Source schema"
           />
           <va-input
              v-model="body.properties.properties.username"
              class="mt-3 md12 flex flex-col"
              label="Source login"
           />
           <va-input
              v-model="body.properties.properties.password"
              class="mt-3 md12 flex flex-col"
              label="Source password"
           />
        </div>
    </va-card-content>
    <va-card-actions>
       <va-spacer>
       </va-spacer>
       <va-button
          class="ml-3 mt-3 flex flex-col"
          icon="cached"
          size="small"
       >
          check
       </va-button>
       <va-button
          class="ml-3 mt-3 flex flex-col"
          icon="add"
          size="small"
       >
          create source
       </va-button>
    </va-card-actions>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'CreatePgSourceDialog',
  components: {

  },
  props: {
    msg: String
  },
  data(){
    return {
        source_name: '',
        source_url: '',
        body: {
            "projectId": "",
            "dataSourceName": "",
            "properties": {
                "type": "POSTGRESQL",
                "properties": {
                    "schema": "profile",
                    "password": "perfcona",
                    "url": "jdbc:postgresql://localhost:5432/perfcona?currentSchema=datapool",
                    "username": "perfcona"
                  }
            }
        }
    }
  },
  methods: {
    ...mapActions(
        ['create_jdbc_datasource']
    ),
    ...mapGetters(
        ['get_current_project']
    ),
    create_pg_source(){
        this.create_jdbc_datasource(this.body)
    }
  }
}
</script>

<style scoped>

</style>
