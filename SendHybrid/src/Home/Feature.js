import React from "react";
import T from "prop-types";
import styled, { css } from "styled-components";
import Flexbox from "commons/components/Flexbox";
import Icon from "commons/components/Icon";
import Typography from "commons/components/Typography";

const Box = styled(Flexbox)`
  padding: 16px;
  width: 296px;
  height: 184px;
  border-radius: var(--border-radius-3);

  ${({ isActive }) =>
    isActive &&
    css`
      border: 1px solid var(--primary-170);
      box-shadow: 0px 0px 3px rgba(255, 118, 134, 0.08), 0px 3px 6px 1px rgba(255, 118, 134, 0.12);
    `}
`;

const FeatureIconWrap = styled.div`
  padding: 6px;
  color: var(--primary-100);
  background-color: var(--primary-190);
  border-radius: var(--border-radius-2);
`;

function Feature({ className, icon, title, description, isActive = false }) {
  return (
    <Box className={className} flexDirection="column" alignItems="flex-start" gap={12} isActive={isActive}>
      <FeatureIconWrap>
        <Icon name={icon} size={24} />
      </FeatureIconWrap>
      <Typography variant="h3">{title}</Typography>
      <Typography variant="body1">{description}</Typography>
    </Box>
  );
}

Feature.propTypes = {
  className: T.string,
  icon: T.string,
  title: T.string,
  description: T.string,
  isActive: T.bool,
};

export default Feature;
