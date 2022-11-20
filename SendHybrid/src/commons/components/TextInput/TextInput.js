import React, { useRef } from "react";
import styled, { css } from "styled-components";
import T from "prop-types";
import Icon from "commons/components/Icon";

const Wrap = styled.div`
  width: 100%;
`;

const Label = styled.label`
  font-size: 14px;
  line-height: 20px;
  margin-bottom: 4px;
  color: var(--neutral-120);
`;

const iconStyle = css`
  color: var(--neutral-160);

  ${({ onClick }) =>
    onClick &&
    css`
      cursor: pointer;

      &:hover,
      &:focus {
        path {
          fill: var(--neutral-140);
        }
      }
    `}
`;

const LeftIcon = styled(Icon)`
  ${iconStyle};
  margin-left: 16px;
`;

const RightIcon = styled(Icon)`
  ${iconStyle};
  margin-right: 16px;
`;

const ErrorIcon = styled(Icon)`
  margin-right: 16px;
  color: var(--neutral-200);
  background-color: var(--red-100);
  border-radius: 50%;
`;

const Box = styled.div`
  display: flex;
  align-items: center;
  background-color: var(--neutral-200);
  border: 1px solid var(--neutral-180);
  border-radius: var(--border-radius-1);
  width: 100%;

  &:hover {
    border: 1px solid var(--neutral-170);
  }

  &:focus-within {
    box-shadow: 0px 0px 0px 3px rgba(223, 237, 247, 0.5);
    border: 1px solid var(--primary-140);
  }

  ${({ isSuccess }) =>
    isSuccess &&
    css`
      border: 1px solid var(--green-100) !important;

      &:focus-within {
        box-shadow: 0px 0px 0px 3px var(--green-190);
      }
    `}

  ${({ isError }) =>
    isError &&
    css`
      border: 1px solid var(--red-100) !important;

      &:focus-within {
        box-shadow: 0px 0px 0px 3px var(--red-190);
      }
    `}
`;

const Field = styled.input`
  outline: none;
  width: 100%;
  border: none;
  background-color: transparent;
  padding: 11px 12px;
  font-family: "Roboto";
  font-style: normal;
  font-weight: 400;
  font-size: 16px;
  line-height: 20px;
  color: var(--neutral-120);

  ::placeholder {
    color: var(--neutral-160);
  }
`;

const helperTextStyles = css`
  font-size: 13px;
  line-height: 20px;
  margin-top: 4px;
`;

const Hint = styled.div`
  ${helperTextStyles};
  color: var(--neutral-160);
  text-align: ${({ alignment }) => alignment};
`;

const Error = styled.div`
  ${helperTextStyles};
  color: var(--red-100);
`;

function TextInput({
  className,
  type = "text",
  value = "",
  placeholder,
  label,
  leftIcon,
  rightIcon,
  leftHint,
  rightHint,
  onLeftIconClick,
  onRightIconClick,
  onChange,
  onConfirm,
  onFocus,
  onBlur,
  isError,
  errorMessage,
  isSuccess,
  autoFocus,
  disabled,
}) {
  const fieldRef = useRef();

  function onLabelClick() {
    fieldRef.current.focus();
  }

  function onChangeProxy(e) {
    if (onChange) onChange(e.target.value);
  }

  function onKeyPress(e) {
    if (e.key === "Enter" && onConfirm) onConfirm();
  }

  return (
    <Wrap className={className}>
      {label && <Label onClick={onLabelClick}>{label}</Label>}
      <Box isError={isError} isSuccess={isSuccess}>
        {leftIcon && <LeftIcon onClick={onLeftIconClick} name={leftIcon} />}
        <Field
          type={type}
          onChange={onChangeProxy}
          onKeyPress={onKeyPress}
          value={value}
          placeholder={placeholder}
          autoFocus={autoFocus}
          onFocus={onFocus}
          onBlur={onBlur}
          ref={fieldRef}
          disabled={disabled}
        />
        {isError && <ErrorIcon name="priority_high" />}
        {rightIcon && <RightIcon onClick={onRightIconClick} name={rightIcon} />}
      </Box>
      {!isError && leftHint && <Hint alignment="left">{leftHint}</Hint>}
      {!isError && rightHint && <Hint alignment="right">{rightHint}</Hint>}
      {errorMessage && <Error>{errorMessage}</Error>}
    </Wrap>
  );
}

TextInput.propTypes = {
  className: T.string,
  type: T.string,
  value: T.string,
  placeholder: T.string,
  label: T.string,
  leftIcon: T.string,
  rightIcon: T.string,
  leftHint: T.string,
  rightHint: T.string,
  onLeftIconClick: T.func,
  onRightIconClick: T.func,
  onChange: T.func,
  onConfirm: T.func,
  onFocus: T.func,
  onBlur: T.func,
  isError: T.bool,
  errorMessage: T.string,
  isSuccess: T.bool,
  autoFocus: T.bool,
  disabled: T.bool,
};

export default TextInput;
