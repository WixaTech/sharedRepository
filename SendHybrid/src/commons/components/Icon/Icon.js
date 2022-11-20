import React from "react";
import T from "prop-types";
import styled from "styled-components";

const Span = styled.span`
  display: block;
  font-size: ${({ size }) => (size ? `${size}px` : "inherit")};
  color: ${({ color }) => color && `var(--${color})`};
  flex-shrink: 0;
`;

function Icon({ className, name, size, color, onClick }) {
  return (
    <Span
      size={size}
      color={color}
      onClick={onClick}
      className={"material-symbols-rounded " + className}
    >
      {name}
    </Span>
  );
}

Icon.propTypes = {
  className: T.string,
  name: T.string,
  size: T.number,
  color: T.string,
  onClick: T.func,
};

export default Icon;
