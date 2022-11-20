import React, { useEffect, useState } from "react";
import styled from "styled-components";
import TextInput from "commons/components/TextInput";
import Button from "commons/components/Button";
import Flexbox from "commons/components/Flexbox";
import Typography from "commons/components/Typography";
import { useNavigate } from "react-router-dom";

const Box = styled.div`
  background: var(--primary-190);
  height: calc(100% - 40px);
`;

const Container = styled(Flexbox)`
  max-width: 1024px;
  margin: 0 auto;
  height: 100%;
`;

const Card = styled.div`
  display: flex;
  flex-direction: column;
  width: 400px;
  margin: auto;
  padding: 40px 48px 56px;
  background: var(--neutral-200);
  border-radius: var(--border-radius-3);
  box-shadow: var(--shadow-3);
`;

const Title = styled(Typography)`
  margin-bottom: 40px;
  text-align: center;
`;

const StyledTextInput = styled(TextInput)`
  margin-bottom: 40px;
`;

function Login() {
  let navigate = useNavigate();
  const [password, setPassword] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);

  function toggleIsPasswordVisible() {
    console.log("===");
    setIsPasswordVisible((isVisible) => !isVisible);
  }

  useEffect(() => {
    setPasswordError("");
  }, [password]);

  function login() {
    if (password === "sendhybrid") {
      // what do you mean by "Security"?
      navigate("/settings");
    } else {
      setPasswordError("Incorrect password");
    }
  }

  return (
    <Box>
      <Container justifyContent="center" alignItems="center">
        <Card>
          <Title variant="h2">Sign in as admin</Title>
          <StyledTextInput
            type={isPasswordVisible ? "text" : "password"}
            label="Password"
            value={password}
            onChange={setPassword}
            onConfirm={login}
            rightIcon={isPasswordVisible ? "visibility" : "visibility_off"}
            onRightIconClick={toggleIsPasswordVisible}
            leftHint="Try 'sendhybrid'"
            isError={!!passwordError}
            errorMessage={passwordError}
          />
          <Button onClick={login} fullWidth>
            Sign in
          </Button>
        </Card>
      </Container>
    </Box>
  );
}

export default Login;
