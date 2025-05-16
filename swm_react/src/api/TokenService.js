import { AuthApi } from "./AuthApi";

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

// 토큰 갱신 인터셉터
AuthApi.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 토큰 만료 에러이고, 재시도하지 않은 요청인 경우
    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        // 이미 토큰 갱신 중이면 큐에 추가
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers["Authorization"] = "Bearer " + token;
            return AuthApi(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const refreshToken = localStorage.getItem("refreshToken");
        const response = await AuthApi.get("/api/v1/auth/reissue", {
          headers: {
            REFRESH_TOKEN: refreshToken,
          },
        });

        const { accessToken } = response.data;
        localStorage.setItem("accessToken", accessToken);

        // 큐에 있는 요청들 처리
        processQueue(null, accessToken);

        // 원래 요청 재시도
        originalRequest.headers["Authorization"] = "Bearer " + accessToken;
        return AuthApi(originalRequest);
      } catch (err) {
        processQueue(err, null);
        // 리프레시 토큰도 만료된 경우 로그아웃 처리
        localStorage.removeItem("tokenType");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "/login";
        return Promise.reject(err);
      } finally {
        isRefreshing = false;
      }
    }
    return Promise.reject(error);
  }
);
