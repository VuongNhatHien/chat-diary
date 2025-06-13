import api from '@/config/api';
import useGet from '@/hooks/useGet';
import { useUpdate } from '@/hooks/useUpdate';
import { ApiResponse } from '@/types/ApiResponse';
import { ChatRoom } from '@/types/Chat';
import { BookOutlined, PlusOutlined, RedoOutlined } from '@ant-design/icons';
import { Button, Empty, Modal } from 'antd';
import { formatDate } from 'date-fns';
import { vi } from 'date-fns/locale';
import _ from 'lodash';
import { useState } from 'react';
import Markdown from 'react-markdown';
import { useNavigate } from 'react-router';

export default function History() {
	const navigate = useNavigate();
	const { data: chatRoomsRes, isPending } =
		useGet<ChatRoom[]>(`/me/chat-rooms`);
	const { mutateAsync: createChatRoom } = useUpdate<object, ChatRoom>(
		'/chat-rooms',
		{
			invalidateKeys: ['/me/chat-rooms'],
		}
	);

	const [modalOpen, setModalOpen] = useState(false);

	const [currentSummarized, setCurrentSummarized] = useState<string | null>(
		null
	);

	const [selectedDate, setSelectedDate] = useState<string | null>(null);

	const { mutateAsync: summarize, isPending: summarizing } = useUpdate<
		object,
		string
	>(`/me/summaries/${selectedDate}`);

	const onOpenModal = async (date: string) => {
		setSelectedDate(date);
		const summarizedRes = await api.get<ApiResponse<string | null>>(
			`/me/summaries/${date}`
		);

		setCurrentSummarized(summarizedRes.data.data);
		setModalOpen(true);
	};

	const onCloseModal = () => {
		setCurrentSummarized(null);
		setModalOpen(false);
	};

	const onSummarize = async () => {
		const summazied = await summarize({});
		setCurrentSummarized(summazied);
	};

	return (
		<div className='px-20 pt-5'>
			<div className='flex justify-between'>
				<div className='text-2xl mb-10 font-medium'>Lịch sử trò chuyện</div>
				<Button
					icon={<PlusOutlined />}
					type='primary'
					onClick={async () => {
						const chatRoom = await createChatRoom({});
						navigate(`/conversation/${chatRoom.id}`);
					}}>
					Bắt đầu trò chuyện
				</Button>
			</div>
			{(() => {
				if (isPending) return null;
				const chatRooms = chatRoomsRes!;

				const withFormattedDate = chatRooms.map((room) => ({
					...room,
					formattedDate: formatDate(
						new Date(room.createdAt),
						"d 'tháng' M 'năm' yyyy",
						{ locale: vi }
					),
				}));

				const grouped = _.groupBy(withFormattedDate, 'formattedDate');

				const sortedDatesDesc = _.orderBy(
					Object.keys(grouped),
					(dateStr) => new Date(grouped[dateStr][0].createdAt),
					'desc'
				);

				const chatRoomGroups = sortedDatesDesc.map((date) => ({
					date,
					rooms: grouped[date],
				}));

				return (
					<div className='flex flex-col w-full gap-8'>
						{chatRoomGroups.length === 0 ? (
							<Empty description='Bạn chưa có cuộc trò chuyện nào' />
						) : (
							chatRoomGroups.map((chatRoomGroup) => {
								return (
									<div
										key={chatRoomGroup.date}
										className='flex flex-col gap-4'>
										<div className='flex gap-5 items-center'>
											<div className='text-xl font-medium'>
												{chatRoomGroup.date}
											</div>
											<Button
												icon={<BookOutlined />}
												type='primary'
												onClick={() =>
													onOpenModal(
														chatRoomGroup.rooms[0].createdAt.split('T')[0]
													)
												}>
												Xem nhật ký
											</Button>
										</div>
										<div className='flex flex-col gap-2'>
											{chatRoomGroup.rooms.map((chatRoom) => {
												return (
													<div
														key={chatRoom.id}
														className='border-[0.5px] border-gray-300 rounded-2xl p-5 hover:bg-white hover:shadow-xs cursor-pointer'
														onClick={() =>
															navigate(`/conversation/${chatRoom.id}`)
														}>
														<div className='text-xl truncate'>
															{chatRoom.name}
														</div>
													</div>
												);
											})}
										</div>
									</div>
								);
							})
						)}
					</div>
				);
			})()}
			<Modal
				centered
				styles={{ body: { height: 300 } }}
				open={modalOpen}
				onCancel={() => onCloseModal()}
				footer={() => {
					return (
						<div className='flex justify-center pt-3'>
							{currentSummarized && (
								<Button
									type='primary'
									icon={<RedoOutlined />}
									onClick={() => onSummarize()}
									loading={summarizing}>
									Tạo lại nhật ký mới
								</Button>
							)}
						</div>
					);
				}}>
				<div className='h-full pt-8 text-[16px] text-justify'>
					{(() => {
						if (currentSummarized) {
							return (
								<div className='h-full overflow-auto pr-2 ml-2'>
									<Markdown>{currentSummarized}</Markdown>
								</div>
							);
						}

						return (
							<div className='flex h-full justify-center items-center'>
								<Button
									type='primary'
									size='large'
									icon={<PlusOutlined />}
									loading={summarizing}
									onClick={() => onSummarize()}>
									Tạo nhật ký cho ngày
								</Button>
							</div>
						);
					})()}
				</div>
			</Modal>
		</div>
	);
}
