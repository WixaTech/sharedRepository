import React from "react";
import T from "prop-types";
import styled, { css } from "styled-components";
import Icon from "../Icon";

const Box = styled.div``;

const HiddenInput = styled.input`
  position: absolute;
  visibility: hidden;
`;

const VisibleInput = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 10px 8px;
  gap: 12px;
  background: var(--primary-190);
  border-radius: var(--border-radius-1);

  ${({ size }) =>
    size === "small" &&
    css`
      padding: 2px 4px;
      gap: 4px;
    `}
`;

const StepperButton = styled.button`
  background-color: var(--neutral-200);
  border-radius: 50%;
  padding: 0;
  color: var(--primary-100);

  &:hover,
  &:focus-visible {
    box-shadow: var(--shadow-1);
  }
`;

const Value = styled.div`
  min-width: 32px;
  font-weight: 500;
  font-size: 16px;
  line-height: 20px;
  text-align: center;
  color: var(--primary-100);
`;

function NumberStepper({
  className,
  min = 1,
  max = 100,
  step = 1,
  value = 1,
  onChange,
  size = "medium",
}) {
  function stepUp() {
    if (value + step > max) return;
    onChange(value + step);
  }

  function stepDown() {
    if (value - step < min) return;
    onChange(value - step);
  }

  return (
    <Box className={className}>
      <HiddenInput
        type="number"
        min={min}
        max={max}
        step={step}
        value={value}
        onChange={onChange}
      />
      <VisibleInput size={size}>
        <StepperButton type="button" onClick={stepDown}>
          <Icon name="remove" size={size === "medium" ? 20 : 16} />
        </StepperButton>
        <Value>{value}</Value>
        <StepperButton type="button" onClick={stepUp}>
          <Icon name="add" size={size === "medium" ? 20 : 16} />
        </StepperButton>
      </VisibleInput>
    </Box>
  );
}

NumberStepper.propTypes = {
  className: T.string,
  min: T.number,
  max: T.number,
  step: T.number,
  value: T.number,
  onChange: T.func.isRequired,
  size: T.oneOf(["medium", "small"]),
};

export default NumberStepper;
