
export const API_BASE_URL = 'https://walkerholic-backend.o-r.kr';
export const ACCESS_TOKEN = 'accessToken';

export const OAUTH2_REDIRECT_URI = 'https://walkerholic.o-r.kr/oauth2/redirect';

export const GOOGLE_AUTH_URL = API_BASE_URL + '/oauth2/authorization/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = API_BASE_URL + '/oauth2/authorization/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const NAVER_AUTH_URL = API_BASE_URL + '/oauth2/authorization/naver?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const KAKAO_AUTH_URL = API_BASE_URL + '/oauth2/authorization/kakao?redirect_uri=' + OAUTH2_REDIRECT_URI;

// "http://localhost:8080/oauth2/authorization/google