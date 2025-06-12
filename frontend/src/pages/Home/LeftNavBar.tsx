import useGet from '@/hooks/useGet';
import { useUpdate } from '@/hooks/useUpdate';
import ExButton from '@/shared/components/ExButton';
import { ChatRoom } from '@/types/Chat';
import { MessageOutlined, PlusCircleFilled } from '@ant-design/icons';
import { Spin } from 'antd';
import { useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router';

export default function LeftNavBar() {
	const navigate = useNavigate();
	const location = useLocation();
	const scrollContainerRef = useRef<HTMLDivElement>(null);
	const buttonRefs = useRef<{ [key: string]: HTMLElement | null }>({});

	const { data: chatRoomsOpt, isPending } =
		useGet<ChatRoom[]>('/me/chat-rooms');
	const { mutateAsync: createChatRoom } = useUpdate<object, ChatRoom>(
		'/chat-rooms',
		{
			invalidateKeys: ['/me/chat-rooms'],
		}
	);

	useEffect(() => {
		if (
			!isPending &&
			chatRoomsOpt &&
			location.pathname.includes('/conversation/')
		) {
			const currentChatId = location.pathname.split('/conversation/')[1];
			const activeButton = buttonRefs.current[currentChatId];

			if (activeButton && scrollContainerRef.current) {
				setTimeout(() => {
					activeButton.scrollIntoView({
						block: 'center',
						inline: 'nearest',
					});
				}, 100);
			}
		}
	}, [isPending, chatRoomsOpt, location.pathname]);

	return (
		<div className='flex flex-col pt-8 w-full px-3 h-full overflow-x-hidden'>
			<div className='flex flex-col gap-1'>
				<ExButton
					onClick={async () => {
						const chatRoom = await createChatRoom({});
						navigate(`/conversation/${chatRoom.id}`);
					}}>
					<div className='flex gap-3 items-center'>
						<PlusCircleFilled className='!text-primary text-2xl' />
						<div className='text-primary font-bold'>Tạo cuộc trò chuyện</div>
					</div>
				</ExButton>
				<ExButton to='/'>
					<div className='flex gap-3'>
						<MessageOutlined className='text-2xl' />
						<div>Nhật ký</div>
					</div>
				</ExButton>
			</div>
			<div className='text-sm mx-3 mt-8'>Gần đây</div>
			<div
				ref={scrollContainerRef}
				className='flex flex-col mt-3 overflow-y-auto -mr-4 mb-2'>
				{(() => {
					if (isPending) return <Spin />;
					const chatRooms = chatRoomsOpt ?? [];
					return chatRooms.map((chatRoom) => {
						return (
							<div
								key={chatRoom.id}
								ref={(el: HTMLDivElement | null) => {
									buttonRefs.current[chatRoom.id] = el;
								}}>
								<ExButton to={`/conversation/${chatRoom.id}`}>
									<div className='truncate'>{chatRoom.name}</div>
								</ExButton>
							</div>
						);
					});
				})()}
			</div>
		</div>
	);
}
