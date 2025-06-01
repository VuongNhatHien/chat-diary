import { useMutation } from '@tanstack/react-query';
import api from '../config/api';

interface UseUpdateOptions {
	method?: 'post' | 'put' | 'patch' | 'delete';
}

export function useUpdate<TBody = unknown, TResponse = unknown>(
	url: string,
	options: UseUpdateOptions = {}
) {
	const { method = 'post' } = options;

	return useMutation<TResponse, string, TBody>({
		mutationFn: (data) => {
			if (method === 'put') {
				return api.put(url, data);
			} else if (method === 'patch') {
				return api.patch(url, data);
			} else if (method === 'delete') {
				return api.delete(url, { data });
			} else {
				return api.post(url, data);
			}
		},
	});
}
