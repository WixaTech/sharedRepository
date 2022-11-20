import React from "react";
import styled from "styled-components";
import { Outlet } from "react-router-dom";
import Button from "commons/components/Button";

const Box = styled.div`
  height: 100%;
`;

const Navigation = styled.nav`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  height: 40px;
  background-color: var(--blue-190);
`;

function App() {
  return (
    <Box>
      <Navigation>
        <Button variant="tertiary" size="small" icon="home" link="/" />
        <Button variant="tertiary" size="small" icon="settings" link="/login" />
      </Navigation>
      <Outlet />
    </Box>
  );
}

export default App;
