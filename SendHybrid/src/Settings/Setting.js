import React, { useState } from "react";
import T from "prop-types";
import styled from "styled-components";
import Flexbox from "commons/components/Flexbox";
import Typography from "commons/components/Typography";
import Button from "commons/components/Button";
import RadioButtonWrap from "commons/components/RadioButtonWrap";
import NumberStepper from "commons/components/NumberStepper";
import TextInput from "commons/components/TextInput";

const StringTextInput = styled(TextInput)`
  margin: 0 24px 0 -4px;
`;

const StringValue = styled(Typography)`
  line-height: 44px;
`;

const SelectFlexbox = styled(Flexbox)`
  gap: 12px;
`;

function Setting({
  type = "string",
  label,
  value,
  options = [],
  min = 1,
  max = 100,
  step = 1,
  onChange,
}) {
  const [isInputActive, setIsInputActive] = useState(false);
  const [stringValue, setStringValue] = useState(
    type === "string" ? value : ""
  );

  return (
    <>
      {type === "string" && (
        <Flexbox flexDirection="column" padding="16px" gap={10}>
          <Typography variant="h3">{label}</Typography>
          <Flexbox justifyContent="space-between" alignItems="center">
            {isInputActive ? (
              <>
                <StringTextInput
                  value={stringValue}
                  onChange={setStringValue}
                />
                <Button
                  variant="primary"
                  size="small"
                  onClick={() => {
                    setIsInputActive(false);
                    onChange(stringValue);
                  }}
                >
                  save
                </Button>
              </>
            ) : (
              <>
                <StringValue variant="body1">
                  {value.replaceAll("_", " ")}
                </StringValue>
                <Button
                  variant="secondary"
                  size="small"
                  onClick={() => setIsInputActive(true)}
                >
                  Change
                </Button>
              </>
            )}
          </Flexbox>
        </Flexbox>
      )}

      {type === "select" && (
        <Flexbox flexDirection="column" padding="16px" gap={10}>
          <Typography variant="h3">{label}</Typography>
          <SelectFlexbox gap={12} alignItems="flex-start" flexWrap="wrap">
            {options.map((option) => (
              <RadioButtonWrap
                key={option}
                label={option}
                isActive={value.includes(option)}
                onClick={() => {
                  if (value.includes(option)) {
                    onChange(value.filter((o) => o !== option));
                  } else {
                    onChange([...value, option]);
                  }
                }}
              />
            ))}
          </SelectFlexbox>
        </Flexbox>
      )}

      {type === "bool" && (
        <Flexbox flexDirection="column" padding="16px" gap={10}>
          <Typography variant="h3">{label}</Typography>
          <Flexbox gap={12} alignItems="flex-start">
            <RadioButtonWrap
              label="Yes"
              isActive={!!value}
              onClick={() => onChange(true)}
            />
            <RadioButtonWrap
              label="No"
              isActive={!value}
              onClick={() => onChange(false)}
            />
          </Flexbox>
        </Flexbox>
      )}

      {type === "number" && (
        <Flexbox flexDirection="column" padding="16px" gap={10}>
          <Typography variant="h3">{label}</Typography>
          <Flexbox gap={12} alignItems="flex-start">
            <NumberStepper
              min={min}
              max={max}
              step={step}
              value={value}
              onChange={onChange}
            />
          </Flexbox>
        </Flexbox>
      )}
    </>
  );
}

Setting.propTypes = {
  type: T.oneOf(["string", "select", "bool", "number"]),
  label: T.string.isRequired,
  value: T.any,
  options: T.array,
  min: T.number,
  max: T.number,
  step: T.number,
  onChange: T.func,
  children: T.oneOfType([T.object, T.string, T.node]),
};

export default Setting;
