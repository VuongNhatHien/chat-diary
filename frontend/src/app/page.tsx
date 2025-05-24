export default async function Home() {
  //test2
  const data = await fetch(`${process.env.BACKEND_URL}/test`);
  const string = await data.json();
  return <div>{string.data}</div>;
}
