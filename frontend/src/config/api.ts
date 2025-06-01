import axios from 'axios';
import { ApiError } from '../types/ApiError';

const api = axios.create({
	baseURL: import.meta.env.VITE_BACKEND_URL,
	validateStatus: () => true,
});

api.defaults.withCredentials = true;

api.interceptors.response.use(
	(response) => {
		if (response.status >= 400) {
			throw new ApiError(
				response.data?.data,
				response.data?.status,
				response.data?.errorCode
			);
		}
		return response;
	},
	(error) => {
		throw error;
	}
);

export default api;
