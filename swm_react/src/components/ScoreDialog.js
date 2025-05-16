// src/components/ScoreDialog.js
import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Button,
} from "@mui/material";

const ScoreDialog = ({ open, onClose, onSubmit }) => {
  const [subject, setSubject] = useState("");
  const [score, setScore] = useState(0);

  const handleSubmit = () => {
    onSubmit(subject, score);
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>점수 입력</DialogTitle>
      <DialogContent>
        <FormControl fullWidth margin="normal">
          <InputLabel>과목 선택</InputLabel>
          <Select value={subject} onChange={(e) => setSubject(e.target.value)}>
            <MenuItem value="spring">Spring</MenuItem>
            <MenuItem value="react">React</MenuItem>
            <MenuItem value="javascript">JavaScript</MenuItem>
          </Select>
        </FormControl>
        <TextField
          fullWidth
          margin="normal"
          label="점수 입력"
          type="number"
          value={score}
          onChange={(e) => setScore(e.target.value)}
          inputProps={{ min: 0, max: 100 }}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="secondary">
          취소
        </Button>
        <Button onClick={handleSubmit} color="primary">
          제출
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ScoreDialog;
