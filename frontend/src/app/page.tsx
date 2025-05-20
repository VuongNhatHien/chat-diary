export default async function Home() {
  const data = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/test`);
  const string = await data.json();
  return <div>{string.data}</div>;
}
