import React, { useState } from "react";
import {
  Box,
  Container,
  Typography,
  TextField,
  Button,
  Divider,
  FormControlLabel,
  Checkbox,
  Select,
  MenuItem,
  Modal,
  Grid,
} from "@mui/material";
import "../Study.css";

const Study = () => {
  const [subjects, setSubjects] = useState([]);
  const [newSubject, setNewSubject] = useState("");
  const [isModalOpen, setModalOpen] = useState(false);

  const addSubject = () => {
    if (newSubject.trim() === "") {
      alert("과목 이름을 입력하세요.");
      return;
    }
    if (subjects.includes(newSubject)) {
      alert("이미 추가된 과목입니다.");
      return;
    }
    setSubjects([...subjects, newSubject]);
    setNewSubject("");
    setModalOpen(false);
  };

  return (
    <Box className="login-background" minHeight="90vh">
      <Container maxWidth="md" sx={{ mt: 4 }}>
        {/* <Typography variant="h5" align="center" gutterBottom>
          SWM: Study With Me!
        </Typography> */}
        {/* <Divider sx={{ my: 2 }} /> */}

        {/* 학습 이름과 과목 추가 */}
        <Grid container spacing={2} justifyContent="center" alignItems="center">
          <Grid item xs={12} md={6}>
            <TextField label="학습 이름" variant="outlined" fullWidth />
          </Grid>
          <Grid item xs={12} md={4}>
            {/* <TextField label="과목" variant="outlined" fullWidth /> */}
          </Grid>
          <Grid item xs={12} md={2}>
            <Button
              variant="outlined"
              color="primary"
              fullWidth
              onClick={() => setModalOpen(true)}
            >
              과목 추가 +
            </Button>
          </Grid>
        </Grid>

        {/* 추가된 과목 리스트 */}
        <Box
          className={`button-container ${subjects.length > 0 ? "visible" : ""}`}
          sx={{ mt: 2 }}
        >
          {subjects.map((subject, index) => (
            <Box key={index} className="button-wrapper">
              <Button variant="contained">{subject}</Button>
              <Button
                className="close-button"
                onClick={() => {
                  const newSubjects = subjects.filter((s) => s !== subject);
                  setSubjects(newSubjects);
                }}
                sx={{
                  minWidth: "36px" /* 버튼의 최소 너비 */,
                  height: "36px" /* 다른 버튼과 동일한 높이 */,
                  padding: "0 8px",
                }}
              >
                &times;
              </Button>
            </Box>
          ))}
        </Box>

        <Divider sx={{ my: 2 }} />

        {/* 요일 선택 */}
        <Typography variant="subtitle1">요일 선택</Typography>
        <Grid container spacing={1}>
          {[
            "월요일",
            "화요일",
            "수요일",
            "목요일",
            "금요일",
            "토요일",
            "일요일",
          ].map((day) => (
            <Grid item key={day}>
              <FormControlLabel control={<Checkbox />} label={day} />
            </Grid>
          ))}
        </Grid>

        <Divider sx={{ my: 2 }} />

        {/* 성취도 단위 선택과 반복 횟수 선택 */}
        <Grid container spacing={2} justifyContent="space-between">
          <Grid item xs={6} md={4}>
            <Typography>성취도 단위</Typography>
            <Select defaultValue="" fullWidth>
              <MenuItem value="" disabled>
                선택
              </MenuItem>
              <MenuItem value="chap">챕터</MenuItem>
              <MenuItem value="page">장수</MenuItem>
              <MenuItem value="exam">문제</MenuItem>
              <MenuItem value="time">시간</MenuItem>
            </Select>
          </Grid>
          <Grid item xs={6} md={4}>
            <Typography>반복 횟수</Typography>
            <Select defaultValue="" fullWidth>
              <MenuItem value="" disabled>
                선택
              </MenuItem>
              <MenuItem value="1">1주</MenuItem>
              <MenuItem value="2">2주</MenuItem>
              <MenuItem value="3">3주</MenuItem>
              <MenuItem value="4">4주</MenuItem>
              <MenuItem value="5">5주</MenuItem>
              <MenuItem value="6">6주</MenuItem>
              <MenuItem value="7">7주</MenuItem>
              <MenuItem value="8">8주</MenuItem>
              <MenuItem value="9">9주</MenuItem>
            </Select>
          </Grid>
        </Grid>

        {/* 저장 / 취소 버튼 */}
        <Box display="flex" justifyContent="flex-end" mt={3}>
          <Button variant="outlined" color="primary" sx={{ mr: 2 }}>
            저장
          </Button>
          <Button variant="outlined" color="error">
            취소
          </Button>
        </Box>
      </Container>

      {/* Modal */}
      <Modal open={isModalOpen} onClose={() => setModalOpen(false)}>
        <Box
          sx={{
            p: 4,
            backgroundColor: "white",
            width: 400,
            mx: "auto",
            mt: "20vh",
            borderRadius: "10px",
          }}
        >
          <Typography variant="h6" mb={2}>
            과목 추가
          </Typography>
          <TextField
            label="과목 이름"
            value={newSubject}
            onChange={(e) => setNewSubject(e.target.value)}
            fullWidth
            sx={{ mb: 2 }}
          />
          <Button
            variant="contained"
            color="primary"
            fullWidth
            onClick={addSubject}
          >
            추가
          </Button>
        </Box>
      </Modal>
    </Box>
  );
};

export default Study;
