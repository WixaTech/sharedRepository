import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Flexbox from "commons/components/Flexbox";
import Typography from "commons/components/Typography";
import callApi from "commons/util/callApi";
import Tabs from "commons/components/Tabs";
import Setting from "./Setting";
import Loader from "commons/components/Loader";

const Box = styled.div`
  position: relative;
  width: 508px;
  margin: 0 auto;
  padding: 40px 0 80px;
`;

const AbsoluteTabs = styled(Tabs)`
  position: absolute;
  left: -154px;
`;

const defaultTabs = [
  {
    id: "file_name",
    label: "File name",
  },
  {
    id: "images",
    label: "Images",
  },
  {
    id: "form",
    label: "Form",
  },
  {
    id: "optimization",
    label: "Optimization",
  },
  {
    id: "file_params",
    label: "File parameters",
  },
  {
    id: "lines",
    label: "Lines",
  },
  {
    id: "tint",
    label: "Tint",
  },
  {
    id: "font",
    label: "Font",
  },
];

const configStructure = {
  file_name: [
    {
      id: "forbiden_chars",
      label: "Forbiden chars",
      type: "select",
      options: [
        "~",
        '"',
        "#",
        "%",
        "&",
        "*",
        ":",
        "<",
        ">",
        "?",
        "!",
        "/",
        "\\",
        "{",
        "|",
        "}",
        ".",
        ",",
        "-",
        "_",
        "@",
      ],
    },
    {
      id: "trim",
      label: "Trim",
      type: "select",
      options: ["Space before", "Space after"],
    },
    {
      id: "char_code",
      label: "Char code",
      type: "string",
    },
    {
      id: "full_filename_length",
      label: "Full file name length",
      type: "number",
      min: 5,
      max: 500,
      step: 5,
    },
  ],
  images: [
    {
      id: "modes",
      label: "Modes",
      type: "select",
      options: ["CMYK", "8-bit", "RGB"],
    },
    {
      id: "optimal_dpi_scale_1_to_1",
      label: "Optimal DPI scale 1 to 1",
      type: "number",
      min: 5,
      max: 600,
      step: 5,
    },
    {
      id: "min_dpi",
      label: "Minimum DPI",
      type: "number",
      min: 5,
      max: 300,
      step: 5,
    },
  ],
  form: [
    {
      id: "allowed",
      label: "Allowed",
      type: "bool",
    },
  ],
  optimization: [
    {
      id: "to_delete",
      label: "To delete",
      type: "select",
      options: ["tab", "hyperlink", "bad links", "inactive layers"],
    },
    {
      id: "generation_type",
      label: "Generation type",
      type: "string",
    },
  ],
  file_params: [
    {
      id: "margin_bottom",
      label: "Margin bottom",
      type: "number",
      min: 1,
      max: 40,
      step: 0.5,
    },
    {
      id: "orientation",
      label: "Orientation",
      type: "string",
    },
    {
      id: "margin_top",
      label: "Margin top",
      type: "number",
      min: 1,
      max: 40,
      step: 0.5,
    },
    {
      id: "format",
      label: "Format",
      type: "string",
    },
    {
      id: "type",
      label: "Type",
      type: "string",
    },
    {
      id: "pdf_version",
      label: "PDF version",
      type: "select",
      options: ["A-2", "A-4"],
    },
    {
      id: "margin_right",
      label: "Margin right",
      type: "number",
      min: 1,
      max: 40,
      step: 0.5,
    },
    {
      id: "forbiden_restrictions",
      label: "Forbiden restrictions",
      type: "select",
      options: [
        "password",
        "print",
        "edition",
        "copy",
        "edit",
        "other based on certification",
      ],
    },
    {
      id: "margin_left",
      label: "Margin left",
      type: "number",
      min: 1,
      max: 40,
      step: 0.5,
    },
  ],
  lines: [
    {
      id: "single_color_min_thickness",
      label: "Single color minimum thickness",
      type: "number",
      min: 0,
      max: 5,
      step: 0.1,
    },
    {
      id: "contra_or_multiple_color_min_thickness",
      label: "Contra or multiple color minimun thickness",
      type: "number",
      min: 0,
      max: 5,
      step: 0.1,
    },
  ],
  tint: [
    {
      id: "tint",
      label: "Tint",
      type: "string",
    },
  ],
  font: [
    {
      id: "multiple_color_one_element_font",
      label: "Multiple color one element font",
      type: "number",
    },
    {
      id: "single_color_two_element_font",
      label: "Single color two element font",
      type: "number",
    },
    {
      id: "rule",
      label: "Rule",
      type: "string",
    },
    {
      id: "single_color_one_element_font",
      label: "Single color one element font",
      type: "number",
    },
    {
      id: "multiple_color_two_element_font",
      label: "Multiple color two element font",
      type: "number",
    },
  ],
};

function Settings() {
  const [settings, setSettings] = useState({});
  const [loading, setLoading] = useState(true);

  const [activeTab, setActiveTab] = useState(defaultTabs[0]);

  useEffect(() => {
    async function fetchData() {
      const config = await callApi("config");
      setSettings(config);
      setLoading(false);
    }
    fetchData();
  }, []);

  if (loading) {
    return <Loader />;
  }

  return (
    <Box>
      <AbsoluteTabs
        direction="column"
        activeTabId={activeTab.id}
        setActiveTab={setActiveTab}
        tabs={defaultTabs}
      />
      <Typography variant="h2">{activeTab.label}</Typography>
      <Flexbox flexDirection="column" gap={16} margin="24px 0 0 0" isBordered>
        {configStructure[activeTab.id]?.map((el) => (
          <Setting
            key={el.id}
            type={el.type}
            label={el.label}
            value={settings[activeTab.id][el.id]}
            options={el.options}
            min={el.min}
            max={el.max}
            step={el.step}
            onChange={(value) => {
              setSettings((sett) => ({
                ...sett,
                [activeTab.id]: {
                  ...sett[activeTab.id],
                  [el.id]: value,
                },
              }));
            }}
          />
        ))}
      </Flexbox>
    </Box>
  );
}

export default Settings;
