import Button from "commons/components/Button";
import React from "react";
import styled, { css } from "styled-components";
import { DataGrid } from "@mui/x-data-grid";
import Flexbox from "commons/components/Flexbox";

const STATUS = {
  VALID: "VALID",
  INVALID: "INVALID",
  UNVERIFIED: "UNVERIFIED",
  IN_PROGRESS: "IN_PROGRESS",
  PROCESSING_ERROR: "PROCESSING_ERROR",
};

const statusNameMap = {
  [STATUS.VALID]: "Valid",
  [STATUS.INVALID]: "Invalid",
  [STATUS.UNVERIFIED]: "Unverified",
  [STATUS.IN_PROGRESS]: "Processing...",
  [STATUS.PROCESSING_ERROR]: "Processing error",
};

const StyledDataGrid = styled(DataGrid)`
  &.MuiDataGrid-root .MuiDataGrid-columnHeader:focus,
  &.MuiDataGrid-root .MuiDataGrid-cell:focus {
    outline: none;
  }
`;

const ActionFlexbox = styled(Flexbox)`
  width: 100%;
`;

const StatusBadge = styled.div`
  border-radius: var(--border-radius-1);
  background-color: var(--neutral-190);
  color: var(--neutral-120);
  padding: 4px 8px;
  text-transform: uppercase;
  font-size: 12px;
  font-weight: 800;

  ${({ status }) =>
    status === STATUS.VALID &&
    css`
      background-color: var(--green-190);
      color: var(--green-100);
    `}

  ${({ status }) =>
    (status === STATUS.INVALID || status === STATUS.PROCESSING_ERROR) &&
    css`
      background-color: var(--red-190);
      color: var(--red-100);
    `}

  ${({ status }) =>
    status === STATUS.UNVERIFIED &&
    css`
      background-color: var(--neutral-190);
      color: var(--neutral-140);
    `}

  ${({ status }) =>
    status === STATUS.IN_PROGRESS &&
    css`
      background-color: var(--blue-190);
      color: var(--blue-140);
    `}
`;

const columns = [
  { field: "id", headerName: "ID", width: 64 },
  { field: "fileName", headerName: "File name", flex: 1, minWidth: 150 },
  {
    field: "addedDate",
    headerName: "Date added",
    width: 200,
    renderCell: (params) => (
      <>
        {new Date(params.value).toLocaleDateString("en-gb", {
          year: "numeric",
          month: "short",
          day: "2-digit",
          hour: "numeric",
          minute: "numeric",
        })}
      </>
    ),
  },
  {
    field: "status",
    headerName: "Status",
    width: 120,
    renderCell: (params) => (
      <>
        <StatusBadge status={params.value}>
          {statusNameMap[params.value]}
        </StatusBadge>
      </>
    ),
  },
  {
    field: "actions",
    headerName: "Actions",
    sortable: false,
    width: 120,
    renderCell: (params) => [
      <ActionFlexbox key={params.id} gap={8} justifyContent="flex-end">
        {params.row.status === STATUS.UNVERIFIED && <>-</>}
        {params.row.status !== STATUS.UNVERIFIED && (
          <Button
            icon="assignment"
            size="small"
            variant="tertiary"
            link={`/report/${params.id}`}
          >
            See report
          </Button>
        )}
      </ActionFlexbox>,
    ],
  },
];

function DocumentTable({ documents }) {
  return (
    <StyledDataGrid
      rows={documents}
      columns={columns}
      pageSize={25}
      rowsPerPageOptions={[25]}
      autoHeight
    />
  );
}

export default DocumentTable;
