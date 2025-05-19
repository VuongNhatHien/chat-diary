export default async function Home() {
  //test
  const data = await fetch("http://localhost:8080/test");
  const string = await data.json();
  return <div>{string.data}</div>;
}
