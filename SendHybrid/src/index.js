import React from "react";
import { render } from "react-dom";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";
import Documents from "./Documents/Documents";
import Report from "./Report/Report";
import Settings from "./Settings/Settings";
import Login from "./Login/Login";
import Page404 from "./Page404";
import "./index.css";

const rootElement = document.getElementById("root");
render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />}>
          <Route index element={<Documents />} />
          <Route path="report/:id" element={<Report />} />
          <Route path="settings" element={<Settings />} />
          <Route path="login" element={<Login />} />
          <Route path="download/:id" element={<Report />} />

          <Route path="*" element={<Page404 />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
  rootElement
);
