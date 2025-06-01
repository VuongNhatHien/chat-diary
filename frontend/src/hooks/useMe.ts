import { User } from '../types/User.types';
import useGet from './useGet';

export default function useMe() {
	return useGet<User>('/me');
}
