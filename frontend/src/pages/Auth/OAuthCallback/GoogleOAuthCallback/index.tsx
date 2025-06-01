import { useUpdate } from '@/hooks/useUpdate';
import { Spin } from 'antd';
import { useEffect } from 'react';
import { useSearchParams } from 'react-router';

export default function GoogleOAuthCallback() {
	const [searchParams] = useSearchParams();
	const { mutateAsync } = useUpdate(
		`/auth/google-login?code=${searchParams.get('code')}`
	);

	useEffect(() => {
		(async () => {
			await mutateAsync({});
			window.location.replace('/');
		})();
	}, []);
	return <Spin fullscreen />;
}
