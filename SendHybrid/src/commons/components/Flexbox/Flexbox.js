import React from "react";
import T from "prop-types";
import styled, { css } from "styled-components";

const Box = styled.div`
  display: flex;
  border-radius: var(--border-radius-2);

  align-items: ${({ alignItems }) => alignItems};
  justify-content: ${({ justifyContent }) => justifyContent};
  flex-direction: ${({ flexDirection }) => flexDirection};
  flex-wrap: ${({ flexWrap }) => flexWrap};
  gap: ${({ gap }) => gap};
  flex-grow: ${({ flexGrow }) => flexGrow};
  flex-shrink: ${({ flexShrink }) => flexShrink};
  flex-basis: ${({ flexBasis }) => flexBasis};

  margin: ${({ margin }) => margin};
  padding: ${({ padding }) => padding};

  ${({ isBordered }) =>
    isBordered &&
    css`
      border: 1px solid var(--neutral-180);
    `};
`;

function Flexbox({
  className,
  alignItems = "stretch",
  justifyContent = "flex-start",
  flexDirection = "row",
  flexWrap = "nowrap",
  gap = 0,
  //
  flexGrow = 0,
  flexShrink = 1,
  flexBasis = "auto",
  //
  margin = "0",
  padding = "0",
  isBordered,
  //
  children,
  as,
  onClick,
}) {
  return (
    <Box
      as={as}
      className={className}
      alignItems={alignItems}
      justifyContent={justifyContent}
      flexDirection={flexDirection}
      flexWrap={flexWrap}
      gap={flexDirection === "row" ? `0 ${gap}px` : `${gap}px 0`}
      flexGrow={flexGrow}
      flexShrink={flexShrink}
      flexBasis={flexBasis}
      margin={margin}
      padding={padding}
      isBordered={isBordered}
      onClick={onClick}
    >
      {children}
    </Box>
  );
}

Flexbox.propTypes = {
  className: T.string,
  alignItems: T.string,
  justifyContent: T.string,
  flexDirection: T.string,
  flexWrap: T.string,
  gap: T.number,
  flexGrow: T.number,
  flexShrink: T.number,
  flexBasis: T.string,
  margin: T.string,
  padding: T.string,
  isBordered: T.bool,
  children: T.oneOfType([T.object, T.string, T.node]),
  as: T.string,
  onClick: T.func,
};

export default Flexbox;
