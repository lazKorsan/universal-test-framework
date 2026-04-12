

fetch('https://jsonplaceholder.typicode.com/posts/1111', {
  method: 'PATCH',
  body: JSON.stringify({
    title: 'family',
  }),
  headers: {
    'Content-type': 'application/json; charset=UTF-8',
  },
})
  .then((response) => response.json())
  .then((json) => console.log(json));



  fetch('https://jsonplaceholder.typicode.com/posts/1111')
    .then((response) => response.json())
    .then((json) => console.log(json));