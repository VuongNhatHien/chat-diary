import express from 'express';
const app = express();

app.get('/', (req, res) => {
  res.send('Chat Diary server is running!');
});

const port = Bun.env.PORT;
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});