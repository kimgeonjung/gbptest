import axios from "axios";
import { tokenManager } from "./TokenManager";

export const UserApi = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// 모든 요청에 현재 토큰 추가
UserApi.interceptors.request.use((config) => {
  const authHeader = tokenManager.getAuthorizationHeader();
  if (authHeader) {
    config.headers.Authorization = authHeader;
  }
  const refreshToken = tokenManager.getRefreshToken();
  if (refreshToken) {
    config.headers.REFRESH_TOKEN = refreshToken;
  }
  return config;
});

// 토큰 갱신
const refreshAccessToken = async () => {
  const response = await UserApi.get(`/api/v1/auth/reissue`);
  const newAccessToken = response.data;
  tokenManager.setTokens(
    tokenManager.tokenType,
    newAccessToken,
    tokenManager.refreshToken
  );
  return newAccessToken;
};

// 토큰 유효성 검사
UserApi.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 403 && !originalRequest._retry) {
      originalRequest._retry = true;
      const newAccessToken = await refreshAccessToken();
      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
      return UserApi(originalRequest);
    }
    return Promise.reject(error);
  }
);

export const fetchUser = async () => {
  const response = await UserApi.get("/api/v1/user");
  return response.data;
};

export const updateUser = async (userData) => {
  const response = await UserApi.put("/api/v1/user", userData);
  return response.data;
};

export const deleteUser = async () => {
  await UserApi.delete("/api/v1/user");
};
