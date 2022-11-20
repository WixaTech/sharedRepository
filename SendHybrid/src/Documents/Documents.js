import Button from "commons/components/Button";
import Typography from "commons/components/Typography";
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Flexbox from "commons/components/Flexbox";
import FileUpload from "./FileUpload";
import callApi from "commons/util/callApi";
import Loader from "commons/components/Loader";
import DocumentTable from "./DocumentTable";

const Container = styled.div`
  max-width: 960px;
  margin: 0 auto;
  padding: 40px 40px 80px;
`;

const EmptyState = styled(Flexbox)`
  text-align: center;
  height: 800px;
`;

const MAX_ITERATIONS = 5;

function DocumentsEmptyState() {
  return (
    <EmptyState justifyContent="center" alignItems="center">
      <Typography variant="h3" color="neutral-160">
        No documents added yet
      </Typography>
    </EmptyState>
  );
}

function Documents() {
  const [open, setOpen] = useState(false);
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);

  function intervalLoad() {
    let iterations = 0;

    const interval = setInterval(() => {
      iterations++;
      console.log(iterations);

      loadDocuments();

      if (iterations >= MAX_ITERATIONS) {
        clearInterval(interval);
      }
    }, 2000);

    return () => clearInterval(interval);
  }

  async function loadDocuments() {
    const docs = await callApi("documents");
    setDocuments(docs);
    setLoading(false);
  }

  useEffect(() => {
    loadDocuments();
  }, []);

  function onFilesSent() {
    setTimeout(() => {
      setLoading(true);
      loadDocuments();
      setOpen(false);
      intervalLoad();
    }, 500);
  }

  if (loading) {
    return <Loader />;
  }

  return (
    <Container>
      <Flexbox
        justifyContent="space-between"
        alignItems="center"
        margin="0 0 32px 0"
      >
        <Typography variant="h1">Documents</Typography>
        <Button icon="add" onClick={() => setOpen(true)}>
          Add document(s)
        </Button>
      </Flexbox>
      {documents.length ? (
        <DocumentTable documents={documents} />
      ) : (
        <DocumentsEmptyState />
      )}

      <FileUpload
        open={open}
        onClose={() => setOpen(false)}
        onFilesSent={onFilesSent}
      />
    </Container>
  );
}

export default Documents;
