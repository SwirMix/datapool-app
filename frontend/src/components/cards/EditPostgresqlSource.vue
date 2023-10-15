<template>
    <va-card
        square
        outlined
        style="height: 100%;"
    >
          <va-card-content>
            <div class="row">
                <va-input
                    v-model="modify_source.id"
                    class="mt-3 flex flex-col md12"
                    label="this.modify_source"
                    readonly
                />
                <va-input
                    v-model="modify_source.name"
                    class="mt-3 flex flex-col md12"
                    label="Name"
                />
                <va-input
                    v-model="modify_source.projectId"
                    class="mt-3 flex flex-col md12"
                    label="projectId"
                    readonly
                />
                <va-input
                    v-model="modify_source.properties.type"
                    class="mt-3 flex flex-col md12"
                    label="type"
                    readonly
                />
                <va-input
                  v-model="stringify_source"
                  class="mb-12 mt-3"
                  type="textarea"
                  label="properties"
                  :min-rows="20"
                  :max-rows="40"
                />
            </div>
          </va-card-content>
          <va-card-actions>
           <va-button
              class="mt-3 flex flex-col md2"
              icon="save"
              size="small"
           >
              save
           </va-button>
           <va-button
              class="mt-3 ml-3 flex flex-col md2"
              icon="delete"
              size="small"
           >
              delete
           </va-button>
          </va-card-actions>
    </va-card>
</template>

<script>

export default {
  name: 'EditPostgresqlSource',
  components: {

  },
  computed: {
    id: function() {
        let id = this.source_id
        return id
    }
  },
  props: {
    source_id: String
  },
  data(){
    return {
        modify_source: {
            id: '',
            name: 'main_postgresql_db',
            projectId: '711bff9d-a873-4497-873f-fbfceb220071',
            properties: {
                type: 'POSTGRESQL',
                properties: {
                    schema: "profile",
                    password: "perfcona",
                    url: "jdbc:postgresql://localhost:5432/perfcona?currentSchema=datapool",
                    username: "perfcona"
                }
            }
        },
        stringify_source: ''
    }
  },
  methods: {

  },
  mounted() {
    this.modify_source.id = this.source_id
  },
  created(){
    this.modify_source.id = this.source_id
    this.stringify_source = JSON.stringify(this.modify_source.properties.properties)
  }
}
</script>

<style scoped>

</style>
