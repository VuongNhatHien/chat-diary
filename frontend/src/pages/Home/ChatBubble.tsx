import Markdown from 'react-markdown';

interface Props {
	text: string;
	owner?: boolean;
}

export default function ChatBubble({ text, owner = true }: Props) {
	return (
		<div className={`flex ${owner ? 'justify-end' : ''}`}>
			<div
				className={`${
					owner ? 'bg-background-4 border border-gray-200' : 'bg-background-3'
				}  rounded-3xl max-w-3/4 p-4 overflow-x-auto`}>
				<Markdown>{text}</Markdown>
			</div>
		</div>
	);
}
