import { Spin } from 'antd';
import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router';
import useMe from '../../hooks/useMe';

export default function Auth() {
	const { data, isPending } = useMe();
	const navigate = useNavigate();

	useEffect(() => {
		if (!isPending && data) {
			navigate('/', { replace: true });
		}
	}, [isPending, data, navigate]);

	if (isPending) return <Spin fullscreen />;
	if (data) return null;

	return <Outlet />;
}
