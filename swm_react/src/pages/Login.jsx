import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useUserStore from "../store/userStore";
import {
  Box,
  Container,
  Typography,
  TextField,
  Checkbox,
  Button,
  FormControlLabel,
  Divider,
} from "@mui/material";
import { login } from "../api/AuthApi";
import { fetchUser } from "../api/UserApi";

const Login = () => {
  const [form, setForm] = useState({
    loginId: "",
    password: "",
    rememberId: false,
  });

  const navigate = useNavigate();

  useEffect(() => {
    const savedId = localStorage.getItem("savedId");
    if (savedId) {
      setForm((prev) => ({
        ...prev,
        loginId: savedId,
        rememberId: true,
      }));
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const { setUser } = useUserStore();

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (form.rememberId) {
      localStorage.setItem("savedId", form.loginId);
    } else {
      localStorage.removeItem("savedId");
    }
    try {
      const response = await login(form.loginId, form.password);
      localStorage.removeItem("tokenType");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      localStorage.setItem("tokenType", response.tokenType);
      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);

      const user = await fetchUser();
      setUser(user);
      console.log(user);
      alert("로그인 성공!");
      navigate("/");
    } catch (err) {
      console.log(err);
      alert("로그인 실패: " + err.response?.data || err.message);
    }
  };

  const goToSignUp = (e) => {
    navigate("/signup");
  };

  return (
    <Box
      minHeight="90vh"
      display="flex"
      alignItems="center"
      justifyContent="center"
    >
      <Container maxWidth="xs" sx={{ margin: "auto", mt: 15 }}>
        {/* 타이틀 */}
        <Box display="flex" justifyContent="center" alignItems="center" mb={2}>
          <Typography variant="h5" className="eng">
            SWM: Study With Me!
          </Typography>
        </Box>

        <Divider />

        {/* 로그인 폼 */}
        <Box component="form" mt={2} onSubmit={handleSubmit}>
          <TextField
            label="ID"
            type="text"
            name="loginId"
            value={form.loginId}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <TextField
            label="Password"
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <FormControlLabel
            control={
              <Checkbox
                name="rememberId"
                checked={form.rememberId}
                onChange={(e) =>
                  setForm({ ...form, rememberId: e.target.checked })
                }
              />
            }
            label="아이디 저장"
          />
          <Button variant="outlined" fullWidth color="info" type="submit">
            로그인
          </Button>
        </Box>

        {/* 회원가입 링크 */}
        <Divider />
        <Box mt={2}>
          <Button
            variant="contained"
            color="primary"
            fullWidth
            onClick={goToSignUp}
          >
            회원가입
          </Button>
        </Box>
      </Container>
    </Box>
  );
};

export default Login;
