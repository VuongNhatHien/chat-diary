import { Button, Image, Typography } from 'antd';
import { useNavigate } from 'react-router';
import { useUpdate } from '../../hooks/useUpdate';

export default function Header() {
	const navigate = useNavigate();
	const { mutateAsync } = useUpdate('/auth/logout', { method: 'put' });

	return (
		<div className='flex h-20 items-center px-6 justify-between border-b-1 border-gray'>
			<div
				className='flex items-center gap-4 cursor-pointer'
				onClick={() => navigate('/')}>
				<Image
					src='https://minecraft.wiki/images/Written_Book_JE2_BE2.gif?c6510'
					height={35}
					preview={false}
				/>
				<div className='font-medium'>
					<Typography.Text className='!text-2xl'>Chat Diary</Typography.Text>
				</div>
			</div>
			<Button
				type='text'
				onClick={async () => {
					await mutateAsync({});
					window.location.reload();
				}}>
				<div>Đăng xuất</div>
			</Button>
		</div>
	);
}
