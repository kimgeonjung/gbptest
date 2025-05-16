import React, { useEffect, useState } from "react";
import { AppBar, Toolbar, Typography, Button, styled } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
// import useUserStore from "../store/userStore";
import { fetchUser } from "../api/UserApi";

const Header = () => {
  const [user, setUser] = useState({});
  const navigate = useNavigate();
  const ACCESS_TOKEN = localStorage.getItem("accessToken");

  useEffect(() => {
    if (ACCESS_TOKEN) {
      fetchUser()
        .then((response) => {
          setUser(response);
          console.log(response.data);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, [ACCESS_TOKEN]);

  const handleLogout = async () => {
    localStorage.clear();
    navigate("/");
  };

  return (
    <AppBar
      position="static"
      sx={{ backgroundColor: "#c8ffed", color: "#121212" }}
    >
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          <RouterList
            to="/"
            sx={{
              fontSize: "inherit",
              textDecoration: "none",
              color: "black",
            }}
            className="eng"
          >
            SWM: Study With Me!
          </RouterList>
        </Typography>
        <Button color="inherit">
          <RouterList to={"/study"}>새로운 학습 시작</RouterList>
        </Button>
        {ACCESS_TOKEN ? (
          <>
            <Typography variant="body1" sx={{ mr: 2 }}>
              {user.name}님 안녕하세요!
            </Typography>
            <Button color="inherit" onClick={handleLogout}>
              로그아웃
            </Button>
          </>
        ) : (
          <>
            <Button color="inherit">
              <RouterList to={"/login"} className="eng">
                Login
              </RouterList>
            </Button>
            <Button color="inherit">
              <RouterList to={"/signup"} className="eng">
                Sign Up
              </RouterList>
            </Button>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
};

const RouterList = styled(Link)`
  color: ${(props) => (props.isActive ? "blue" : "black")};
  text-decoration: none;
`;
export default Header;
