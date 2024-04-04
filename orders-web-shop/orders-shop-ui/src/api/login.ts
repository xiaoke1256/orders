import axiosInst from '@/axios';

import { UserInfo } from '@/types/commons';

export const login: (loginName: string, password: string) => Promise<{ token: string; refreshToken: string; user: UserInfo }> = (loginName, password) => {
    return axiosInst.post('/api/login/login', { loginName, password }).then((resp) => resp.data);
};

export const logout: () => void = () => {

};

export const getSessionId: () => Promise<string> = () => {
    return axiosInst.get('/api//login/sessionId').then((resp) => resp.data);
}

export const getloginPublicKey: (sessionId: string) => Promise<string> = (sessionId) => {
    return axiosInst.get(`/api//login/loginSecret/${sessionId}`).then((resp) => resp.data);
}