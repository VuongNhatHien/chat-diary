import useGet from '@/hooks/useGet';
import { useUpdate } from '@/hooks/useUpdate';
import { ChatMessage } from '@/types/Chat';
import { ArrowUpOutlined } from '@ant-design/icons';
import { Button, Spin } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router';
import ScrollToBottom, { useScrollToBottom } from 'react-scroll-to-bottom';
import ChatBubble from './ChatBubble';

export default function Conversation() {
	const { chatRoomId } = useParams();
	const [chatInput, setChatInput] = useState('');
	const scrollToBottomRef = useRef<() => void | undefined>(undefined);

	const createMessageEndpoint = `/chat-rooms/${chatRoomId}/messages`;

	const { data: messagesOpt, isPending: messagesPending } = useGet<
		ChatMessage[]
	>(`/chat-rooms/${chatRoomId}/messages`);

	const { mutateAsync: createMessage } = useUpdate<{ text: string }>(
		createMessageEndpoint,
		{
			invalidateKeys: [createMessageEndpoint, `/me/chat-rooms`],
		}
	);

	const { mutateAsync: createResponse, isPending: creatingResponse } =
		useUpdate(`/chat-rooms/${chatRoomId}/responses`, {
			invalidateKeys: [createMessageEndpoint],
		});

	const onSendMessage = async () => {
		setTimeout(() => setChatInput(''), 0);
		scrollToBottomRef.current?.();
		await createMessage({ text: chatInput });
		await createResponse({});
	};

	const MessageArea = () => {
		const scrollToBottom = useScrollToBottom();

		useEffect(() => {
			scrollToBottomRef.current = scrollToBottom;
		}, [scrollToBottom]);

		if (messagesPending) {
			return (
				<div className='flex h-full justify-center items-center'>
					<Spin />
				</div>
			);
		}

		const messages = messagesOpt!;

		return (
			<div className='flex flex-col gap-5 px-28 pb-5 text-[16px]'>
				{messages.map((message) => (
					<ChatBubble
						key={message.id}
						text={message.text}
						owner={message.userId !== '0'}
					/>
				))}
				{creatingResponse && <Spin />}
			</div>
		);
	};

	return (
		<div className='flex flex-col h-full'>
			<div className='flex-1 overflow-y-hidden'>
				<ScrollToBottom className='h-full scroll-to-bottom-button'>
					<MessageArea />
				</ScrollToBottom>
			</div>
			<div className='flex-shrink-0 mb-3 mx-12'>
				<div className='flex justify-center'>
					<div className='w-11/12 px-5 pt-2 pb-4 bg-white rounded-3xl shadow border border-gray'>
						<div className='flex flex-col'>
							<TextArea
								size='large'
								placeholder='Chat something...'
								autoSize={{ minRows: 1, maxRows: 5 }}
								variant='borderless'
								value={chatInput}
								onChange={(e) => setChatInput(e.target.value)}
								onKeyDown={(e) => {
									if (e.key === 'Enter') {
										onSendMessage();
									}
								}}
							/>
							<div className='flex justify-end'>
								<Button
									disabled={chatInput === ''}
									loading={creatingResponse}
									icon={<ArrowUpOutlined />}
									type='primary'
									onClick={() => onSendMessage()}
								/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
