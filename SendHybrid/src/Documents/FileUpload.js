import React, { useState } from "react";
import T from "prop-types";
import styled from "styled-components";
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import Button from "commons/components/Button";
import Flexbox from "commons/components/Flexbox";
import callApi from "commons/util/callApi";
import Typography from "commons/components/Typography";

const HiddenFileInput = styled.input`
  position: absolute;
  visibility: hidden;
`;

const BrowseWrap = styled(Flexbox)`
  height: 140px;
  width: 400px;
  padding: 40px;
`;

const FileList = styled.ul``;

const File = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 8px 8px 16px;
  margin: 8px 0;
  background-color: var(--primary-190);
  font-weight: 500;
  border-radius: var(--border-radius-1);
`;

function FileUpload({ onClose, open, onFilesSent }) {
  const [uploadedFiles, setUploadedFiles] = useState([]);

  function removeFile(file) {
    const uploaded = [...uploadedFiles].filter((f) => f.name !== file.name);
    setUploadedFiles(uploaded);
  }

  function handleUploadFiles(files) {
    const uploaded = [...uploadedFiles];
    files.some((file) => {
      if (uploaded.findIndex((f) => f.name === file.name) === -1) {
        uploaded.push(file);
      }
    });
    setUploadedFiles(uploaded);
  }

  function handleFileEvent(e) {
    const chosenFiles = Array.prototype.slice.call(e.target.files);
    handleUploadFiles(chosenFiles);
  }

  async function sendFile(file) {
    const formData = new FormData();
    formData.append("file", file);

    await callApi("upload", "post", formData, {
      "Content-Type": "multipart/form-data",
    });
  }

  function sendFiles() {
    uploadedFiles.forEach((file) => {
      sendFile(file);
    });
    onFilesSent();
  }

  return (
    <Dialog onClose={onClose} open={open}>
      <DialogTitle>Upload documents</DialogTitle>
      <DialogContent>
        <label htmlFor="fileUpload">
          <BrowseWrap justifyContent="center" alignItems="center">
            <Button as="a" variant="tertiary">
              Browse
            </Button>
          </BrowseWrap>
        </label>
        <HiddenFileInput
          type="file"
          id="fileUpload"
          multiple
          onChange={handleFileEvent}
        />
        {!!uploadedFiles.length && (
          <Typography variant="h3">Documents to upload:</Typography>
        )}
        <FileList>
          {uploadedFiles.map((file) => (
            <File key={file.name}>
              {file.name}
              <Button
                icon="delete"
                size="small"
                variant="tertiary"
                onClick={() => removeFile(file)}
              />
            </File>
          ))}
        </FileList>
      </DialogContent>
      <DialogActions>
        <Button variant="secondary" onClick={onClose}>
          Cancel
        </Button>
        <Button onClick={() => sendFiles()}>Upload</Button>
      </DialogActions>
    </Dialog>
  );
}

FileUpload.propTypes = {
  onClose: T.func.isRequired,
  onFilesSent: T.func.isRequired,
  open: T.bool.isRequired,
};

export default FileUpload;
