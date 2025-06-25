import { useUpdate } from '@/hooks/useUpdate';
import { ProForm, ProFormText } from '@ant-design/pro-components';
import { Button, Typography } from 'antd';
import useApp from 'antd/es/app/useApp';
import { useForm } from 'antd/es/form/Form';
import { useNavigate } from 'react-router';

interface FormType {
	email: string;
	password: string;
	passwordConfirm: string;
	firstName: string;
	lastName: string;
}

export default function Register() {
	const navigate = useNavigate();
	const [form] = useForm<FormType>();
	const { mutateAsync } = useUpdate<FormType>('/auth/register');
	const { message } = useApp();

	const onFinish = async (values: FormType) => {
		if (values.password !== values.passwordConfirm) {
			message.error('Mật khẩu nhập lại không trùng khớp');
			return;
		}

		await mutateAsync(values);
		message.info('Đăng ký thành công');
		navigate('/auth/login');
	};

	return (
		<div className='flex h-full justify-center items-center'>
			<div
				className='flex flex-col gap-3 text-center'
				style={{ width: 300 }}>
				<Typography.Text className='!text-4xl font-bold'>
					Đăng ký tài khoản mới
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
								<div className='flex flex-col gap-2'>
									<Button
										className='w-full'
										type='primary'
										onClick={() => form.submit()}>
										Đăng ký
									</Button>
									<div className='flex justify-center'>
										<Button
											onClick={() => navigate('/auth/login')}
											type='link'
											style={{ padding: 0 }}>
											Quay về đăng nhập
										</Button>
									</div>
								</div>,
							];
						},
					}}>
					<ProFormText
						label='Email'
						name={'email'}
						placeholder={'Email'}
					/>
					<ProFormText
						label='Mật khẩu'
						name={'password'}
						placeholder={'Mật khẩu'}
						fieldProps={{ type: 'password' }}
					/>
					<ProFormText
						label='Xác nhận mật khẩu'
						name={'passwordConfirm'}
						placeholder={'Nhập lại mật khẩu'}
						fieldProps={{ type: 'password' }}
					/>
					<ProFormText
						label='Họ'
						name={'lastName'}
						placeholder={'Nhập họ của bạn'}
					/>
					<ProFormText
						label='Tên'
						name={'firstName'}
						placeholder={'Nhập tên của bạn'}
					/>
				</ProForm>
			</div>
		</div>
	);
}
