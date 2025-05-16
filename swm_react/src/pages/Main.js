import { Box, Button, Container, Grid, Typography } from "@mui/material";
import { React, useState } from "react";
import ScoreDialog from "../components/ScoreDialog";
import LineChart from "../components/LineChart";
import ScoreDisplay from "../components/ScoreDisplay";

function Main(props) {
  const [open, setOpen] = useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = (subject, score) => {
    // 점수 제출 로직
    console.log(`Subject: ${subject}, Score: ${score}`);
  };
  return (
    <Container sx={{ mt: 4 }}>
      <Grid container spacing={4}>
        {/* 설정한 학습 이름 및 Modal 버튼 */}
        <Grid item xs={12} sm={6}>
          <Typography variant="h4">설정한 학습 이름</Typography>
        </Grid>
        <Grid item xs={12} sm={6} textAlign="right">
          <Button variant="outlined" color="primary" onClick={handleClickOpen}>
            학습 진행률 입력
          </Button>
        </Grid>

        {/* Modal for 입력 */}
        <ScoreDialog
          open={open}
          onClose={handleClose}
          onSubmit={handleSubmit}
        />

        {/* Graph Section */}
        <Grid item xs={12} md={8}>
          <Typography variant="h6" gutterBottom>
            과목별 학습률
          </Typography>
          <Box className="bg-secondary" p={2}>
            <LineChart />
          </Box>
        </Grid>

        {/* 점수 표기 부분 */}
        <ScoreDisplay />
      </Grid>
    </Container>
  );
}

export default Main;
