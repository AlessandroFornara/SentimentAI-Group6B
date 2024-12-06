<!--HTML-->
<template>
  <head>
    <title> Emotino </title>
  </head>
  <body>
    <h1 class = "titolo" > Welcome to Emotino </h1>
    <h1>{{ msg }}</h1>
  </body>

</template>


<!--javascript-->
<script>
export default {
  name: 'HelloWorld',
  data() {
    return {
      msg: ''
    }
  },
  mounted() {
    fetch("/api/messages/hello")
        .then((response) => response.text())
        .then((data) => {
          this.msg = data;
        });
  }
}
</script>

<!--CSS-->
<style>
.titolo {
  color: blue;
  font-size: 50px;
  font-family: Arial,fantasy;
  text-align: center;
}
</style>