import React from "react";
import { Grid, Typography, Box } from "@mui/material";

const ScoreDisplay = () => {
  return (
    <Grid item xs={12} md={4} textAlign="center">
      <Typography variant="h5">총점: 100 점</Typography>
      <Box my={2}>
        <Typography sx={{ fontSize: "700%" }}>😍</Typography>
      </Box>
      <Box mt={5}>
        <Typography variant="h6">1주차 : 100 점</Typography>
        <Typography variant="h6">2주차 : 100 점</Typography>
        <Typography variant="h6">3주차 : 100 점</Typography>
      </Box>
    </Grid>
  );
};

export default ScoreDisplay;
