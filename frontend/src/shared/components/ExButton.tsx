import { ReactNode } from 'react';
import { useLocation, useNavigate } from 'react-router';

interface Props {
	children: ReactNode;
	to?: string;
	onClick?: () => void;
}

export default function ExButton({ children, to, onClick }: Props) {
	const navigate = useNavigate();
	const location = useLocation();
	const isActive = to ? location.pathname === to : false;

	return (
		<div
			className={`cursor-pointer py-2 px-3 rounded-xl hover:bg-hover ${
				isActive ? 'bg-hover' : ''
			}`}
			onClick={() => {
				if (to) {
					navigate(to);
				} else {
					onClick?.();
				}
			}}>
			{children}
		</div>
	);
}
