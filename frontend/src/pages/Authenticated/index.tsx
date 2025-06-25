import { Spin } from 'antd';
import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router';
import useMe from '../../hooks/useMe';
import Header from './Header';

export default function Authenticated() {
	const { isPending, error } = useMe();
	const navigate = useNavigate();

	useEffect(() => {
		if (error) {
			navigate('/auth/login', { replace: true });
		}
	}, [error]);

	if (isPending) return <Spin fullscreen />;

	return (
		<div>
			<Header />
			<div	 style={{ height: 'calc(100vh - 70px)' }}>
				<Outlet />
			</div>
		</div>
	);
}
