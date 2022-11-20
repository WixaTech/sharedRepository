import React from "react";
import T from "prop-types";
import styled from "styled-components";
import Flexbox from "../Flexbox";

const Container = styled(Flexbox)`
  width: 100%;
  height: 800px;
`;

const Spinner = styled.div`
  width: 50px;
  height: 50px;
  border: 6px solid var(--primary-190);
  border-top: 6px solid var(--primary-100);
  border-radius: 50%;
  animation: spinner 1.5s linear infinite;

  @keyframes spinner {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
`;

function Loader({ className }) {
  return (
    <Container
      className={className}
      justifyContent="center"
      alignItems="center"
    >
      <Spinner></Spinner>
    </Container>
  );
}

Loader.propTypes = {
  className: T.string,
};

export default Loader;
