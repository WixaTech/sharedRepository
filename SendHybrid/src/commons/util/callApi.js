import axios from "axios";

const callApi = async (url, method = "get", data = {}, headers) => {
  try {
    const config = {
      url: `https://hack-yeah-backend-wixa-tech.herokuapp.com/${url}`,
      method,
      data,
      headers,
    };
    const response = await axios(config);

    return response.data;
  } catch (error) {
    console.log(error);
  }
};

export default callApi;
