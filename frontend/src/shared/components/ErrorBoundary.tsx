import { Button, Result } from 'antd';
import { isRouteErrorResponse, useNavigate, useRouteError } from 'react-router';

export default function RouteErrorBoundary() {
	const navigate = useNavigate();
	const error = useRouteError();

	if (isRouteErrorResponse(error)) {
		return (
			<Result
				status='404'
				title='404'
				subTitle='Trang này không tồn tại, vui lòng kiểm tra lại.'
				extra={
					<Button
						type='primary'
						onClick={() => navigate('/', { replace: true })}>
						Về trang chủ
					</Button>
				}
			/>
		);
	}

	return (
		<Result
			status='500'
			title='500'
			subTitle='Đã có lỗi xảy ra'
			extra={
				<Button
					type='primary'
					onClick={() => navigate('/')}>
					Về trang chủ
				</Button>
			}
		/>
	);
}
