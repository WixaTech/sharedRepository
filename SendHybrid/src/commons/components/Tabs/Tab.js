import React from "react";
import T from "prop-types";
import styled, { css } from "styled-components";
import Icon from "commons/components/Icon";

const Box = styled.div`
  padding: 6px 12px;
  margin: 4px;
  font-size: 14px;
  line-height: 20px;
  font-weight: 500;
  border-radius: var(--border-radius-1);
  cursor: pointer;

  padding: ${({ hasIcon }) => (hasIcon ? "8px" : "6px 12px")};

  ${({ isActive }) =>
    isActive &&
    css`
      color: var(--primary-100);
      background-color: var(--neutral-200);
      pointer-events: none;
    `}

  ${({ isActive }) =>
    !isActive &&
    css`
      color: var(--neutral-140);

      &:hover,
      &:focus-visible {
        color: var(--neutral-120);
      }
    `}
`;

function Tab({ className, isActive = false, label, icon, onClick }) {
  return (
    <Box
      className={className}
      isActive={isActive}
      onClick={onClick}
      hasIcon={!!icon}
      tabIndex="0"
    >
      {label && !icon && label}
      {icon && <Icon name={icon} size={16} />}
    </Box>
  );
}

Tab.propTypes = {
  className: T.string,
  isActive: T.bool,
  label: T.string.isRequired,
  icon: T.string,
  onClick: T.func,
};

export default Tab;
