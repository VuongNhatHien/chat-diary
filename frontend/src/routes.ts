import { createBrowserRouter } from 'react-router';
import App from './App';
import Auth from './pages/Auth';
import Login from './pages/Auth/Login';
import GoogleOAuthCallback from './pages/Auth/OAuthCallback/GoogleOAuthCallback';
import Authenticated from './pages/Authenticated';
import Home from './pages/Home';
import Conversation from './pages/Home/Conversation';
import History from './pages/Home/History';
import RouteErrorBoundary from './shared/components/ErrorBoundary';

export const router = createBrowserRouter([
	{
		path: '/',
		Component: App,
		ErrorBoundary: RouteErrorBoundary,
		children: [
			{
				path: 'auth',
				Component: Auth,
				children: [
					{
						path: 'login',
						Component: Login,
					},
					{
						path: 'oauth/google/callback',
						Component: GoogleOAuthCallback,
					},
				],
			},
			{
				path: '',
				Component: Authenticated,
				children: [
					{
						path: '',
						Component: Home,
						children: [
							{
								path: '',
								Component: History,
							},
							{
								path: 'conversation/:chatRoomId',
								Component: Conversation,
							},
						],
					},
				],
			},
		],
	},
]);
