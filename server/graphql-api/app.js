const express = require('express')
const graphqlHTTP = require('express-graphql');
const cors = require('cors');
const schema = require('./schema.js');

const app = express();

// allow cross-origin requests
app.use(cors());

// bind express with graphql
app.use('/graphql', graphqlHTTP({
    schema,
    graphiql: true
}));

app.listen(4000, () => {
    console.log('now listening for requests on port 4000');
});