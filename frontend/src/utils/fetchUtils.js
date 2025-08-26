export const httpRequest = async (method, path, data = null, headers = {}) => {
   const options = {
      method: method.toUpperCase(),
      headers: {
      ...headers,
   },
};

   if (data) {
      if (data instanceof FormData) {
      // FormData 
      options.body = data;
   } else {
      // JSON
      options.headers['Content-Type'] = 'application/json';
      options.body = JSON.stringify(data);
   }
}

try {
   const response = await fetch(`${import.meta.env.VITE_BASE_URL}/${path}`, options);

   if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP error ${response.status}: ${errorText}`);
   }

   if (response.status === 204) {
      return { status: response.status, data: null };
   }

   return {
      status: response.status,
      data: await response.json(),
   };
} catch (error) {
   console.error('HTTP Request Failed', error);
   throw error;
}
};
