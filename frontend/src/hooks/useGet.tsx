import { QueryKey, useQuery, UseQueryOptions } from '@tanstack/react-query';
import api from '../config/api';
import { ApiErrorResponse } from '../types/ApiErrorResponse';
import type { ApiResponse } from '../types/ApiResponse';

export default function useGet<TResponse = unknown>(
	url: string,
	options?: {
		queryConfig?: Omit<
			UseQueryOptions<
				ApiResponse<TResponse>,
				ApiErrorResponse,
				ApiResponse<TResponse>,
				QueryKey
			>,
			'queryKey' | 'queryFn'
		>;
	}
) {
	const { queryConfig } = options ?? {};

	const result = useQuery<ApiResponse<TResponse>, ApiErrorResponse>({
		queryKey: [url],
		queryFn: () => {
			return api.get(url);
		},
    retry: false,
		...queryConfig,
	});

	return { ...result, data: result.data?.data };
}
