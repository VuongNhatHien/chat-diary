import { Splitter } from 'antd';
import { Outlet } from 'react-router';
import LeftNavBar from './LeftNavBar';

export default function Home() {
	return (
		<Splitter>
			<Splitter.Panel
				className='bg-background-2'
				resizable={false}
				defaultSize='25%'>
				<LeftNavBar />
			</Splitter.Panel>
			<Splitter.Panel>
				<div className='pt-8 h-full'>
					<Outlet />
				</div>
			</Splitter.Panel>
		</Splitter>
	);
}
