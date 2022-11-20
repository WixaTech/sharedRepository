import React from "react";
import T from "prop-types";
import styled, { css } from "styled-components";

const variantsMapping = {
  h1: "h1",
  h2: "h2",
  h3: "h3",
  body1: "p",
  body2: "p",
  label: "span",
  timer: "span",
};

const StyledComponent = styled.div`
  color: ${({ color }) => (color ? `var(--${color})` : "inherit")};
  margin: ${({ margin }) => margin};

  ${({ variant }) =>
    variant === "h1" &&
    css`
      font-family: "Avenir Next", sans-serif;
      font-size: 32px;
      line-height: 48px;
      font-weight: 800;
    `};

  ${({ variant }) =>
    variant === "h2" &&
    css`
      font-family: "Avenir Next", sans-serif;
      font-size: 24px;
      line-height: 32px;
      font-weight: 700;
    `};

  ${({ variant }) =>
    variant === "h3" &&
    css`
      font-family: "Avenir Next", sans-serif;
      /* font-size: 18px; */
      /* line-height: 20px; */
      font-size: 16px;
      line-height: 24px;
      font-weight: 700;
    `};

  ${({ variant }) =>
    variant === "label" &&
    css`
      font-size: 14px;
      line-height: 20px;
    `};

  ${({ variant }) =>
    variant === "body1" &&
    css`
      font-size: 16px;
      line-height: 24px;
    `};

  ${({ variant }) =>
    variant === "body2" &&
    css`
      font-size: 16px;
      line-height: 20px;
    `};

  ${({ variant }) =>
    variant === "timer" &&
    css`
      font-size: 18px;
      line-height: 20px;
    `};
`;

function Typography({
  className,
  variant,
  color,
  children,
  as,
  margin = "",
  ...props
}) {
  const component = as || variantsMapping[variant] || "p";

  return (
    <StyledComponent
      className={className}
      as={component}
      variant={variant}
      color={color}
      margin={margin}
      {...props}
    >
      {children}
    </StyledComponent>
  );
}

Typography.propTypes = {
  className: T.string,
  variant: T.oneOf(["h1", "h2", "h3", "label", "body1", "body2", "timer"])
    .isRequired,
  color: T.string,
  children: T.oneOfType([T.object, T.string, T.node]),
  as: T.string,
  margin: T.string,
};

export default Typography;
