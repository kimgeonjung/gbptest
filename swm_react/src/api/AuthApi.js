import axios from "axios";
import { tokenManager } from "./TokenManager";

// 기본 API 인스턴스 (인증 불필요)
export const AuthApi = axios.create({
  baseURL: "http://localhost:8080/",
  headers: {
    "Content-Type": "application/json",
  },
});

// 인증이 필요한 요청을 위한 인터셉터 추가
AuthApi.interceptors.request.use((config) => {
  if (
    !config.url.includes("/auth/signup") &&
    !config.url.includes("/auth/login")
  ) {
    const authHeader = tokenManager.getAuthorizationHeader();
    if (authHeader) {
      config.headers.Authorization = authHeader;
    }
  }
  return config;
});

export const login = async (loginId, password) => {
  const data = {
    loginId: loginId,
    password: password,
  };
  const response = await AuthApi.post("/api/v1/auth/login", data);
  const { tokenType, accessToken, refreshToken } = response.data;
  tokenManager.setTokens(tokenType, accessToken, refreshToken);
  return response.data;
};

export const signup = async (email, loginId, name, password) => {
  const data = {
    email: email,
    loginId: loginId,
    name: name,
    password: password,
  };
  try {
    const response = await AuthApi.post("/api/v1/auth/signup", data);
    return response;
  } catch (error) {
    if (error.response) {
      throw new Error(
        error.response.data.message || "회원가입 처리 중 오류가 발생했습니다."
      );
    }
    throw error;
  }
};

export const logout = () => {
  tokenManager.clearTokens();
};
