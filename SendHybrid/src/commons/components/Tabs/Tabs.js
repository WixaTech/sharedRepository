import React from "react";
import T from "prop-types";
import styled from "styled-components";
import Tab from "./Tab.js";

const Box = styled.div`
  display: inline-flex;
  background-color: var(--neutral-190);
  border-radius: var(--border-radius-1);

  flex-direction: ${({ direction }) => direction};
`;

function Tabs({
  className,
  tabs,
  activeTabId,
  setActiveTab,
  direction = "row",
}) {
  return (
    <Box className={className} direction={direction}>
      {tabs.map((tab) => (
        <Tab
          key={tab.id}
          isActive={activeTabId === tab.id}
          {...tab}
          onClick={() => setActiveTab(tab)}
        />
      ))}
    </Box>
  );
}

Tabs.propTypes = {
  className: T.string,
  tabs: T.arrayOf(
    T.shape({
      isActive: T.bool,
      label: T.string.isRequired,
      badge: T.oneOfType([T.string, T.number]),
      icon: T.string,
      onClick: T.func,
    })
  ),
  direction: T.oneOf(["row", "column"]),
  activeTabId: T.oneOfType([T.string, T.number]),
  setActiveTab: T.func,
};

export default Tabs;
