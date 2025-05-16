import React, { useState } from "react";
import {
  Box,
  Button,
  Container,
  Divider,
  TextField,
  Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { signup } from "../api/AuthApi";

const SignUp = () => {
  const [form, setForm] = useState({
    email: "",
    loginId: "",
    name: "",
    password: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    signup(form.email, form.loginId, form.name, form.password)
      .then((response) => {
        alert("회원가입 성공: " + response.data.loginId);
        navigate("/login");
      })
      .catch((err) => {
        alert(
          "회원가입 실패: " +
            (err?.response?.data?.message || "알 수 없는 오류")
        );
      });
  };

  return (
    <Box
      minHeight="90vh"
      display="flex"
      alignItems="center"
      justifyContent="center"
    >
      <Container maxWidth="xs">
        {/* 타이틀 */}
        <Box display="flex" justifyContent="center" alignItems="center" mb={2}>
          <Typography variant="h5" className="eng">
            SWM: Study With Me!
          </Typography>
        </Box>

        <Divider />

        {/* 회원가입 폼 */}
        <Box component="form" my={2} onSubmit={handleSubmit}>
          <TextField
            label="이메일"
            name="email"
            type="text"
            value={form.email}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <TextField
            label="ID"
            name="loginId"
            type="text"
            value={form.loginId}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <TextField
            label="Password"
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <TextField
            label="이름"
            name="name"
            value={form.name}
            onChange={handleChange}
            variant="outlined"
            fullWidth
            margin="normal"
          />

          {/* 회원가입 버튼 */}
          <Box my={2}>
            <Button type="submit" variant="contained" color="primary" fullWidth>
              회원가입
            </Button>
          </Box>
        </Box>
      </Container>
    </Box>
  );
};

export default SignUp;
