import React from "react";
import { Link } from "react-router-dom";
import styled, { css } from "styled-components";
import T from "prop-types";
import Icon from "commons/components/Icon";

const buttonStyle = css`
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: var(--border-radius-1);
  width: fit-content;
  font-weight: 500;
  font-family: "Roboto", sans-serif;
  white-space: nowrap;
  cursor: pointer;

  &:disabled {
    pointer-events: none;
  }

  ${({ $fullWidth }) =>
    $fullWidth &&
    css`
      width: 100%;
    `}

  ${({ variant }) =>
    variant === "primary" &&
    css`
      color: var(--neutral-200);
      background-color: var(--primary-100);
      &:hover,
      &:focus-visible {
        background-color: var(--primary-90);
      }
      &:active {
        background-color: var(--primary-70);
      }
      &:disabled {
        background-color: var(--primary-140);
      }
    `}

  ${({ variant }) =>
    variant === "secondary" &&
    css`
      color: var(--primary-100);
      background-color: var(--primary-190);
      &:hover,
      &:focus-visible {
        background-color: var(--primary-180);
      }
      &:active {
        background-color: var(--primary-170);
      }
      &:disabled {
        background-color: var(--neutral-190);
        color: var(--neutral-160);
        svg {
          fill: var(--neutral-160);
        }
      }
    `}

  ${({ variant }) =>
    variant === "tertiary" &&
    css`
      color: var(--neutral-120);
      font-weight: 400;
      background-color: var(--neutral-200);
      border: 1px solid var(--neutral-180);
      &:hover,
      &:focus-visible {
        background-color: var(--neutral-190);
      }
      &:active {
        background-color: var(--neutral-180);
      }
      &:disabled {
        background-color: var(--neutral-190);
        color: var(--neutral-170);
        svg {
          fill: var(--neutral-170);
        }
      }
    `}

  ${({ size }) =>
    size === "small" &&
    css`
      padding: 8px;
      font-size: 14px;
      line-height: 16px;
    `}

  ${({ size }) =>
    size === "medium" &&
    css`
      padding: 10px;
      font-size: 16px;
      line-height: 20px;
    `}

  ${({ size }) =>
    size === "large" &&
    css`
      padding: 14px;
      font-size: 16px;
      line-height: 20px;
    `}
`;

const Box = styled.button`
  ${buttonStyle};
`;

const LinkBox = styled(Link)`
  ${buttonStyle};
`;

const Label = styled.div`
  ${({ size }) =>
    size === "small" &&
    css`
      padding: 0 4px;
    `}

  ${({ size }) =>
    size === "medium" &&
    css`
      padding: 0 6px;
    `}

  ${({ size }) =>
    size === "large" &&
    css`
      padding: 0 8px;
    `}
`;

function Button({
  className,
  children,
  icon,
  onClick,
  link,
  size = "medium",
  variant = "primary",
  fullWidth,
  ariaLabel,
  disabled,
  target,
  as,
  href,
}) {
  const commonProps = {
    className,
    size,
    variant,
    $fullWidth: fullWidth,
    disabled,
    $hasLabel: !!children,
    as,
    target,
  };
  const iconSize = commonProps.$hasLabel ? 16 : 20;

  const content = (
    <>
      {icon && <Icon name={icon} size={iconSize} />}
      {children && <Label size={size}>{children}</Label>}
    </>
  );

  return (
    <>
      {link ? (
        <LinkBox to={link} {...commonProps} aria-label={ariaLabel}>
          {content}
        </LinkBox>
      ) : (
        <Box
          href={href}
          onClick={onClick}
          {...commonProps}
          aria-label={ariaLabel}
        >
          {content}
        </Box>
      )}
    </>
  );
}

Button.propTypes = {
  className: T.string,
  children: T.oneOfType([T.object, T.string]),
  icon: T.string,
  onClick: T.func,
  link: T.string,
  size: T.oneOf(["small", "medium", "large"]),
  variant: T.oneOf(["primary", "secondary", "tertiary"]),
  fullWidth: T.bool,
  ariaLabel: T.string,
  disabled: T.bool,
  target: T.string,
  href: T.string,
  as: T.string,
};

export default Button;
