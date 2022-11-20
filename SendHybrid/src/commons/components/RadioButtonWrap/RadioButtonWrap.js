import React from "react";
import T from "prop-types";
import styled, { css } from "styled-components";
import RadioButton from "commons/components/RadioButton";
import Flexbox from "commons/components/Flexbox";

const Box = styled(Flexbox)`
  min-width: 140px;
  padding: 10px 12px;
  border-radius: var(--border-radius-2);
  cursor: pointer;
  text-transform: capitalize;

  &:hover {
    background-color: var(--neutral-190);
  }

  ${({ isActive }) =>
    isActive &&
    css`
      background-color: var(--primary-190);

      &:hover {
        background-color: var(--primary-180);
      }
    `}
`;

const Content = styled.div`
  height: 96px;
  border: 1px solid var(--neutral-180);
  border-radius: var(--border-radius-2);
  background-color: var(--neutral-200);
`;

function RadioButtonWrap({
  className,
  label,
  isActive = false,
  children,
  onClick,
}) {
  return (
    <Box
      className={className}
      flexDirection="column"
      gap={12}
      isActive={isActive}
      isBordered
      onClick={onClick}
    >
      <RadioButton label={label} isActive={isActive} />
      {children && <Content>{children}</Content>}
    </Box>
  );
}

RadioButtonWrap.propTypes = {
  className: T.string,
  label: T.string.isRequired,
  isActive: T.bool,
  children: T.oneOfType([T.object, T.string, T.node]),
  onClick: T.func,
};

export default RadioButtonWrap;
