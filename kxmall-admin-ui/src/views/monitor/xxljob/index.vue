<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "XxlJob",
  components: { iFrame },
  data() {
    return {
      url: import.meta.env.VITE_APP_XXL_JOB_ADMIN
    };
  },
};
</script>
