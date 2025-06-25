import { Outlet } from 'react-router';
import './index.css';

function App() {
	return (
		<div className='h-screen bg-background'>
			<Outlet />
		</div>
	);
}

export default App;
