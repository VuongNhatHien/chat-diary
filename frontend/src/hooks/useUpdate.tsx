import { ApiErrorResponse } from '@/types/ApiErrorResponse';
import { ApiResponse } from '@/types/ApiResponse';
import {
	useMutation,
	UseMutationOptions,
	useQueryClient,
} from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import api from '../config/api';

interface UseUpdateOptions<TBody = unknown, TResponse = unknown> {
	method?: 'post' | 'put' | 'patch' | 'delete';
	invalidateKeys?: unknown[];
	mutationConfig?: UseMutationOptions<TResponse, ApiErrorResponse, TBody>;
}

export function useUpdate<TBody = unknown, TResponse = unknown>(
	url: string,
	options: UseUpdateOptions<TBody, TResponse> = {}
) {
	const {
		method = 'post',
		invalidateKeys = [],
		mutationConfig,
	} = options || {};
	const queryClient = useQueryClient();

	return useMutation<TResponse, ApiErrorResponse, TBody>({
		mutationFn: async (data) => {
			let result: AxiosResponse<ApiResponse<TResponse>, any>;
			if (method === 'put') {
				result = await api.put<ApiResponse<TResponse>>(url, data);
			} else if (method === 'patch') {
				result = await api.patch<ApiResponse<TResponse>>(url, data);
			} else if (method === 'delete') {
				result = await api.delete<ApiResponse<TResponse>>(url, { data });
			} else {
				result = await api.post<ApiResponse<TResponse>>(url, data);
			}

			return result.data.data;
		},
		...mutationConfig,
		onSuccess(data, variables, context) {
			mutationConfig?.onSuccess?.(data, variables, context);

			invalidateKeys.map((value) => {
				queryClient.invalidateQueries({
					queryKey: [value],
					refetchType: 'active',
				});
			});
		},
	});
}
