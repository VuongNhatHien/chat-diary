import { createBrowserRouter } from 'react-router';
import App from './App';
import Auth from './pages/Auth';
import Login from './pages/Auth/Login';
import Authenticated from './pages/Authenticated';
import Home from './pages/Home';
import RouteErrorBoundary from './shared/components/ErrorBoundary';
import GoogleOAuthCallback from './pages/Auth/OAuthCallback/GoogleOAuthCallback';

export const router = createBrowserRouter([
	{
		path: '/',
		Component: App,
		ErrorBoundary: RouteErrorBoundary,
		children: [
			{
				path: 'oauth/google/callback',
				Component: GoogleOAuthCallback,
			},
			{
				path: 'auth',
				Component: Auth,
				children: [
					{
						path: 'login',
						Component: Login,
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
					},
				],
			},
		],
	},
]);
