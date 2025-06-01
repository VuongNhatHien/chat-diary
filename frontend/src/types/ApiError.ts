import { ErrorCode } from './ErrorCode';

export class ApiError extends Error {
	data?: unknown;
	status?: number;
	errorCode?: ErrorCode;

	constructor(data?: unknown, status?: number, errorCode?: ErrorCode) {
		super();
		(this.data = data), (this.status = status), (this.errorCode = errorCode);
	}
}
