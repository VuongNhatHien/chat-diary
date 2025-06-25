import api from '@/config/api';
import useMe from '@/hooks/useMe';
import { ApiResponse } from '@/types/ApiResponse';
import { UserOutlined } from '@ant-design/icons';
import { Avatar, Dropdown, Image, Typography } from 'antd';
import { MenuProps } from 'antd/lib';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router';
import { useUpdate } from '../../hooks/useUpdate';

export default function Header() {
	const navigate = useNavigate();
	const { mutateAsync } = useUpdate('/auth/logout', { method: 'put' });
	const { data: me } = useMe();
	const [menuItems, setMenuItems] = useState<MenuProps['items']>([]);
	const fileInputRef = useRef<HTMLInputElement>(null);
	const [avatarUrl, setAvatarUrl] = useState<string | null>(null);

	const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
		const file = e.target.files?.[0];
		if (!file) return;

		const formData = new FormData();
		formData.append('file', file);

		try {
			await api.put('/image/upload', formData);

			window.location.reload();
		} catch (err) {
			console.error(err);
		} finally {
			e.target.value = '';
		}
	};

	useEffect(() => {
		(async () => {
			if (me?.avatarUrl) {
				try {
					const avatar = await api.get<ApiResponse<string>>(
						`/image/get?key=${me.avatarUrl}`
					);

					setAvatarUrl(avatar.data.data);
				} catch (error) {
					console.log(error);
				}
			}
		})();
	}, []);

	useEffect(() => {
		let items = [
			{
				key: 'avatar_upload',
				label: (
					<a
						onClick={() => {
							fileInputRef.current?.click();
						}}>
						Chọn ảnh đại diện
					</a>
				),
			},
			{
				key: 'logout',
				label: (
					<a
						onClick={async () => {
							await mutateAsync({});
							window.location.reload();
						}}>
						Đăng xuất
					</a>
				),
			},
		];

		if (avatarUrl)
			items = [
				{
					key: 'avatar_view',
					label: (
						<a
							href={avatarUrl}
							target='_blank'
							rel='noopener noreferrer'>
							Xem ảnh đại diện
						</a>
					),
				},
				...items,
			];

		setMenuItems(items);
	}, [avatarUrl]);

	return (
		<div className='flex h-20 items-center px-6 justify-between border-b-1 border-gray'>
			<input
				type='file'
				ref={fileInputRef}
				style={{ display: 'none' }}
				accept='image/*'
				onChange={handleFileChange}
			/>
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
			<Dropdown
				trigger={['click']}
				menu={{
					items: menuItems,
				}}>
				<Avatar
					size={35}
					src={avatarUrl && <img src={avatarUrl} />}
					icon={!avatarUrl && <UserOutlined />}
					className='cursor-pointer'
				/>
			</Dropdown>
		</div>
	);
}
