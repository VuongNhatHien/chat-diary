import { Button, Image, Typography } from 'antd';
import { useUpdate } from '../../hooks/useUpdate';

export default function Header() {
	const { mutateAsync } = useUpdate('/auth/logout', { method: 'put' });

	return (
		<div className='flex h-20 shadow-md items-center px-8 justify-between'>
			<div className='flex items-center gap-4 cursor-pointer'>
				<Image
					src='https://minecraft.wiki/images/Written_Book_JE2_BE2.gif?c6510'
					height={35}
					preview={false}
				/>
				<Typography.Text strong>
					<div className='text-xl'>Chat Diary</div>
				</Typography.Text>
			</div>
			<Button
				type='text'
				onClick={async () => {
					await mutateAsync({});
					window.location.reload();
				}}>
				<Typography.Text>Đăng xuất</Typography.Text>
			</Button>
		</div>
	);
}
