// 토큰 관리를 위한 클래스
class TokenManager {
  constructor() {
    this.tokenType = localStorage.getItem("tokenType");
    this.accessToken = localStorage.getItem("accessToken");
    this.refreshToken = localStorage.getItem("refreshToken");
  }

  setTokens(tokenType, accessToken, refreshToken) {
    this.tokenType = tokenType;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;

    localStorage.setItem("tokenType", tokenType);
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
  }

  clearTokens() {
    this.tokenType = null;
    this.accessToken = null;
    this.refreshToken = null;

    localStorage.removeItem("tokenType");
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  }

  getAuthorizationHeader() {
    return this.tokenType && this.accessToken
      ? `${this.tokenType} ${this.accessToken}`
      : null;
  }

  getRefreshToken() {
    return this.refreshToken;
  }
}

export const tokenManager = new TokenManager();
