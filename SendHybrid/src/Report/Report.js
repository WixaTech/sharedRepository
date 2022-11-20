import Button from "commons/components/Button";
import Flexbox from "commons/components/Flexbox";
import Loader from "commons/components/Loader";
import Typography from "commons/components/Typography";
import callApi from "commons/util/callApi";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import styled from "styled-components";

const Container = styled.div`
  position: relative;
  max-width: 768px;
  margin: 0 auto;
  padding: 40px 40px 80px;
`;

const BackButton = styled(Button)`
  position: absolute;
  left: -120px;
`;

const errorGroupNameMap = {
  fileFormat: "File format",
  fileContent: "File content",
  file_params: "File params",
  file_name: "File name",
  signature: "Signature",
};

const metadataNameMap = {
  "e-mail: ": "E-mail",
  recipient_surname: "Recipient's surname",
  unp: "UNP",
  recipient_post_code: "Recipient's postal code",
  recipient_name: "Recipient's name",
  case_number: "Case number",
  recipient_city: "Recipient's city",
  recipient_street: "Recipient's street",
  ePUAP: "ePUAP",
};

function Report() {
  const [report, setReport] = useState({});
  const [loading, setLoading] = useState(true);
  const { id } = useParams();

  async function fetchData() {
    setLoading(true);
    const report = await callApi(`documents/${id}/recentReport`);
    setReport(report);
    setLoading(false);
  }

  useEffect(() => {
    fetchData();
  }, [id]);

  async function repairFile() {
    await callApi(`documents/${id}/recover`, "post").then(() => {
      setTimeout(() => {
        fetchData();
      }, 500);
    });
  }

  if (loading) {
    return <Loader />;
  }

  if (!report) {
    return (
      <Flexbox
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
      >
        <Typography variant="h3" margin="40px 0 24px">
          Report not found
        </Typography>
        <Button variant="tertiary" icon="arrow_back" link="/">
          Go back
        </Button>
      </Flexbox>
    );
  }

  return (
    <Container>
      <BackButton variant="tertiary" icon="arrow_back" link="/">
        Back
      </BackButton>
      <Typography variant="h1">Validation report (file ID: {id})</Typography>
      {report.errorGroups.length ? (
        <>
          {report.errorGroups.map((group) => (
            <div key={group.groupName}>
              <Typography variant="h3" margin={"32px 0 0 0"}>
                {errorGroupNameMap[group.groupName] || group.groupName}
              </Typography>
              {group.messages.map((message) => (
                <Typography variant="body1" key={message} margin={"0 0 8px 0"}>
                  {message}
                </Typography>
              ))}
              {group.recoverable && (
                <Button
                  icon="build"
                  size="small"
                  variant="tertiary"
                  onClick={repairFile}
                >
                  Repair file automatically
                </Button>
              )}
            </div>
          ))}
        </>
      ) : (
        <>
          <Typography variant="h3" margin={"24px 0"} color="green-100">
            No errors, the document is valid and can be sent to Poczta Polska.
          </Typography>
          <Button
            icon="download"
            size="small"
            variant="tertiary"
            href={`https://hack-yeah-backend-wixa-tech.herokuapp.com/download/${id}`}
            target="_blank"
            as="a"
          >
            Download file
          </Button>
        </>
      )}
      {Object.keys(report.metadata).length !== 0 && (
        <>
          <Typography variant="h1" margin="64px 0 0 0">
            Metadata
          </Typography>
          {Object.entries(report.metadata).map(([key, value]) => (
            <div key={key}>
              <Typography variant="h3" margin={"32px 0 0 0"}>
                {metadataNameMap[key] || key}
              </Typography>
              <Typography variant="body1" margin={"0 0 8px 0"}>
                {value}
              </Typography>
            </div>
          ))}
        </>
      )}
    </Container>
  );
}

export default Report;
