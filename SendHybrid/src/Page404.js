import React from "react";
import styled from "styled-components";
import Button from "commons/components/Button";
import Typography from "commons/components/Typography";
import { Navigate } from "react-router-dom";

const Canvas = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 80px;
`;

const Title = styled(Typography)`
  max-width: 420px;
  text-align: center;
  margin-top: 40px;
  margin-bottom: 16px;
`;

const Subtitle = styled(Typography)`
  max-width: 400px;
  text-align: center;
  font-size: 16px;
  color: var(--neutral-140);
  margin-bottom: 32px;
`;

function Page404() {
  return (
    <>
      <Navigate replace to="/" />
    </>
  );
  // return (
  //   <Canvas>
  //     <Title variant="h1">Looks like you're lost</Title>
  //     <Subtitle variant="body1">
  //       The page you are looking for does not exist.
  //     </Subtitle>
  //     <Button link="/">Go to homepage</Button>
  //   </Canvas>
  // );
}

export default Page404;
