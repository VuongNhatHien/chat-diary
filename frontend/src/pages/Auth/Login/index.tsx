import { useUpdate } from '@/hooks/useUpdate';
import {
	GithubOutlined,
	GoogleOutlined,
	LockOutlined,
	UserOutlined,
} from '@ant-design/icons';
import { ProForm, ProFormText } from '@ant-design/pro-components';
import { Button, Divider, Typography } from 'antd';
import { useForm } from 'antd/es/form/Form';

interface FormType {
	email: string;
	password: string;
}

export default function Login() {
	const [form] = useForm<FormType>();
	const { mutateAsync } = useUpdate<FormType>('/auth/login');

	const onFinish = async (values: FormType) => {
		await mutateAsync(values);
		window.location.reload();
	};

	return (
		<div className='flex h-full justify-center items-center'>
			<div
				className='flex flex-col gap-3 text-center'
				style={{ width: 300 }}>
				<Typography.Text className='!text-4xl font-bold'>
					Đăng nhập vào
					<br /> Chat Diary
				</Typography.Text>
				<ProForm
					onKeyDown={(e) => {
						if (e.key === 'Enter') {
							form.submit();
						}
					}}
					form={form}
					onFinish={onFinish}
					submitter={{
						render() {
							return [
								<Button
									className='w-full'
									type='primary'
									onClick={() => form.submit()}>
									Đăng nhập
								</Button>,
							];
						},
					}}>
					<ProFormText
						label='Email'
						name={'email'}
						placeholder={'Email'}
						fieldProps={{ prefix: <UserOutlined /> }}
					/>
					<ProFormText
						label='Mật khẩu'
						name={'password'}
						placeholder={'Mật khẩu'}
						fieldProps={{ type: 'password', prefix: <LockOutlined /> }}
					/>
				</ProForm>

				<Divider plain>hoặc</Divider>
				<div className='flex flex-col gap-3'>
					<Button
						onClick={() =>
							window.location.replace(
								`https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=${
									window.location.origin
								}/auth/oauth/google/callback&response_type=code&client_id=${
									import.meta.env.VITE_GOOGLE_CLIENT_ID
								}&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline&prompt=select_account`
							)
						}>
						<div className='flex h-full items-center gap-3'>
							<GoogleOutlined className='text-xl' />
							<div>Đăng nhập bằng Google</div>
						</div>
					</Button>
					<Button disabled>
						<div className='flex h-full items-center gap-3'>
							<GithubOutlined className='text-xl' />
							<div>Đăng nhập bằng Github</div>
						</div>
					</Button>
				</div>
			</div>
		</div>
	);
}
